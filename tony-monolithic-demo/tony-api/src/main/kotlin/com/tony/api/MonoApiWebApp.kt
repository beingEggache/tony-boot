@file:JvmName("App")

package com.tony.api

import com.tony.Env
import com.tony.annotation.EnableTonyBoot
import com.tony.api.permission.PermissionInterceptor
import com.tony.feign.exception.SignInvalidException
import com.tony.feign.genSign
import com.tony.feign.sortRequestBody
import com.tony.utils.doIf
import com.tony.utils.getFromRootAsString
import com.tony.utils.getLogger
import com.tony.utils.isBetween
import com.tony.utils.toLocalDateTime
import com.tony.web.ApiSession
import com.tony.web.WebApp
import com.tony.web.WebContext
import com.tony.web.filter.RepeatReadRequestWrapper.Companion.toRepeatRead
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.PriorityOrdered
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("HttpUrlsUsage")
fun main(args: Array<String>) {
    runApplication<MonoApiWebApp>(*args) {
        setHeadless(true)
    }
    val info =
        "${WebApp.appId} " +
            "http://${WebApp.ip}:${WebApp.port}${WebApp.contextPath.padStart(1, '/')} " +
            "started success. " +
            "profiles: ${Env.activeProfiles.joinToString()}. " +
            "He who has a why to live can bear almost any how."
    LoggerFactory.getLogger(MonoApiWebApp::class.java).info(info)
}

// @Profile(value = ["!prod"])
// @Component
/*open class InitApp(
    private val moduleService: ModuleService,
    private val userService: UserService
) : CommandLineRunner {

    @Lazy
    @Resource
    lateinit var requestMappingHandlerMapping: RequestMappingHandlerMapping

    private fun listApiModulesInProject() = requestMappingHandlerMapping.handlerMethods.filterValues {
        it.hasMethodAnnotation(ApiOperation::class.java) &&
            !it.hasMethodAnnotation(NoPermissionCheck::class.java)
    }.map { (requestMappingInfo, handlerMethod) ->

        val apiOperation = handlerMethod.getMethodAnnotation(ApiOperation::class.java)
        val moduleName = apiOperation?.value
        if (moduleName.isNullOrEmpty()) throw ApiException("${handlerMethod.shortLogMessage} apiOperation value null")
        val moduleId = requestMappingInfo.name ?: handlerMethod.shortLogMessage
        val nickname = apiOperation.nickname
        if (nickname.isBlank()) throw ApiException("${handlerMethod.shortLogMessage} apiOperation nickname null")
        val moduleGroup = nickname.split(",").sorted().joinToString(",")
        val moduleDescription = apiOperation.notes.defaultIfBlank()
        val moduleValue = "${requestMappingInfo.methodsCondition.methods.first()} " +
            requestMappingInfo.patternValues.first()
        Module().apply {
            this.moduleId = moduleId
            this.appId = WebApp.appId
            this.moduleName = moduleName
            this.moduleValue = moduleValue
            this.moduleType = ModuleType.API
            this.moduleGroup = moduleGroup
            this.moduleDescription = moduleDescription
        }
    }

    @Transactional
    override fun run(vararg args: String?) {
        moduleService.saveModules(
            this.listApiModulesInProject(),
            listOf(ModuleType.API),
            WebApp.appId
        )

        userService.initSuperAdmin(WebApp.appId)
    }
}*/

@EnableTonyBoot
@SpringBootApplication
class MonoApiWebApp(
    private val permissionInterceptor: PermissionInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(permissionInterceptor)
            .excludePathPatterns(*WebApp.whiteUrlPatterns(WebApp.contextPath).toTypedArray())
            .order(PriorityOrdered.HIGHEST_PRECEDENCE + 1)

        Env.activeProfiles.any { it != "dev" }.doIf {
            registry.addInterceptor(SignatureInterceptor())
                .order(PriorityOrdered.HIGHEST_PRECEDENCE)
        }
    }
}

class SignatureInterceptor : HandlerInterceptor {
    private val logger = getLogger()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val repeatReadRequestWrapper = request.toRepeatRead()
        val bodyStr = String(repeatReadRequestWrapper.contentAsByteArray)

        val timestampStrFromHeader = request.getHeader("x-timestamp")
        if (timestampStrFromHeader.isNullOrBlank()) {
            logger.warn("Check signature: timestamp from  is null or blank.")
            throw SignInvalidException("验签失败")
        }

        val timestampStrFromReq = bodyStr.getFromRootAsString("timestamp")
        if (timestampStrFromReq.isNullOrBlank()) {
            logger.warn("Check signature: timestamp from request body is null or blank.")
            throw SignInvalidException("验签失败")
        }

        if (timestampStrFromHeader != timestampStrFromReq) {
            logger.warn("Check signature: timestamp from request body != timestamp from header")
            throw SignInvalidException("验签失败")
        }

        val requestTime = timestampStrFromHeader.toLocalDateTime("yyyy-MM-dd HH:mm:ss")

        val now = LocalDateTime.now()
        if (!now.isBetween(requestTime.minusSeconds(3 * 60), requestTime.plusSeconds(3 * 60))) {
            throw SignInvalidException("签名已过期")
        }

        val appId = request.getHeader("x-app-id")
        if (appId.isNullOrBlank()) {
            logger.warn("Check signature: appId from header is null or blank.")
            throw SignInvalidException("验签失败")
        }

        // TODO
        val secret = "secret"

        val signatureRemote = request.getHeader("x-signature")
        val signatureLocal = bodyStr.sortRequestBody(timestampStrFromHeader).genSign(appId, secret)

        logger.debug("Check signature: request body is {}.", bodyStr)
        logger.debug("Check signature: appId is {}.", appId)
        logger.debug("Check signature: signatureRemote is {}.", signatureRemote)
        logger.debug("Check signature: signatureLocal is {}.", signatureLocal)

        if (signatureRemote != signatureLocal) {
            throw SignInvalidException("验签失败")
        }

        return true
    }
}

@Component
class StaticApiSession : ApiSession {
    override val userId: String
        get() = WebContext.request.getParameter("userId")

    override fun genTokenString(vararg params: Pair<String, String?>): String = "I'm token"

    override fun hasLogin(): Boolean = true
}
