@file:JvmName("CoreUtils")

package com.tony.utils

/**
 * 核心工具类
 *
 * @author tangli
 * @since 2022/9/29 10:20
 */
import com.tony.ApiProperty
import com.tony.SpringContexts.Env
import com.tony.exception.BaseException
import com.tony.exception.BizException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.BigInteger
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

public fun <T> T?.println(): Unit = println(this)

@JvmSynthetic
public fun <T> T.getLogger(): Logger where T : Any =
    LoggerFactory.getLogger(this::class.java)

/**
 * 获取 logger.
 * @param name logger 名
 */
public fun getLogger(name: String?): Logger = LoggerFactory.getLogger(name)

/**
 * 将 [this] 自身 转为 [E] 类型
 *
 * @receiver [Any]?
 * @param E 转换后的类型
 */
@Suppress("UNCHECKED_CAST")
public fun <E> Any?.asTo(): E? where E : Any = this as E?

@Suppress("UNCHECKED_CAST")
public fun <E> Any.asToNotNull(): E where E : Any = this as E

public fun <E> Any?.asToNumber(numberType: Class<E>): E? {
    if (!numberType.isNumberTypes()) throw IllegalArgumentException("Only support number types, no $numberType")
    return when (this) {
        is Number -> toNumber(numberType)
        is CharSequence -> toNumber(numberType)
        else -> null
    }
}

public fun <T : Number, R : Number> T.toNumber(numberType: Class<in R>): R =
    when (numberType) {
        Long::class.javaObjectType, Long::class.javaPrimitiveType -> this.toLong()
        Int::class.javaObjectType, Int::class.javaPrimitiveType -> this.toInt()
        Double::class.javaObjectType, Double::class.javaPrimitiveType -> this.toDouble()
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType -> this.toByte()
        Short::class.javaObjectType, Short::class.javaPrimitiveType -> this.toShort()
        Float::class.javaObjectType, Float::class.javaPrimitiveType -> this.toFloat()
        BigInteger::class.java -> BigInteger.valueOf(this.toLong())
        BigDecimal::class.java -> BigDecimal(this.toString())
        else -> throw IllegalArgumentException("Not support input type: $numberType")
    }.asToNotNull()

public fun <T : Number> CharSequence.toNumber(numberType: Class<in T>): T =
    when (numberType) {
        Long::class.javaObjectType, Long::class.javaPrimitiveType -> toString().toLong()
        Int::class.javaObjectType, Int::class.javaPrimitiveType -> toString().toInt()
        Double::class.javaObjectType, Double::class.javaPrimitiveType -> toString().toDouble()
        Byte::class.javaObjectType, Byte::class.javaPrimitiveType -> toString().toByte()
        Short::class.javaObjectType, Short::class.javaPrimitiveType -> toString().toShort()
        Float::class.javaObjectType, Float::class.javaPrimitiveType -> toString().toFloat()
        BigInteger::class.java -> BigInteger.valueOf(toString().toLong())
        BigDecimal::class.java -> BigDecimal(toString())
        else -> throw IllegalArgumentException("Not support input type: $numberType")
    }.asToNotNull()

@JvmSynthetic
public inline fun Boolean.doIf(crossinline block: () -> Any) {
    if (this) block()
}

@JvmSynthetic
public inline fun <T> T.doIf(condition: Boolean, crossinline block: T.() -> Unit): T {
    if (condition) block()
    return this
}

/**
 * 当[condition]为真时,抛出[BizException]异常.
 *
 * 异常信息为[message], 异常代码为[code],默认为[ApiProperty.preconditionFailedCode]
 *
 * @param condition 异常条件
 * @param message 异常信息
 * @param code 异常代码
 */
@JvmOverloads
public fun throwIf(
    condition: Boolean,
    message: String,
    code: Int = ApiProperty.preconditionFailedCode,
    ex: (message: String, code: Int) -> BaseException = ::BizException,
) {
    if (condition) throw ex(message, code)
}

/**
 * 当 [this] [T]? 为 null 时, 抛出异常.
 *
 * 异常信息为 [message], 默认为 [ApiProperty.notFoundMessage]
 *
 * 异常代码为 [code], 默认为 [ApiProperty.notFoundCode]
 *
 * @receiver [T]? this
 * @param [T]
 * @param message 异常信息
 * @param code 异常代码
 * @return [T] this
 *
 * @see throwIf
 */
@JvmOverloads
public fun <T> T?.throwIfNull(message: String = ApiProperty.notFoundMessage, code: Int = ApiProperty.notFoundCode): T {
    throwIf(this == null, message, code)
    return this!!
}

@JvmSynthetic
public inline fun <R> throwIfAndReturn(condition: Boolean, message: String, crossinline block: () -> R): R {
    if (condition) throw BizException(message)
    return block()
}

@JvmSynthetic
public inline fun <T, R> T?.throwIfNullAndReturn(message: String, crossinline block: () -> R): R =
    throwIfAndReturn(this == null, message, block)

@JvmSynthetic
public inline fun <reified T> T?.returnIfNull(crossinline block: () -> T): T = this ?: block()

/**
 * 获取本地ip.
 *
 * 参考SpringCloud获取IP的代码.
 * @return ip
 */
public val localIp: String = NetworkInterface
    .getNetworkInterfaces()
    .asSequence()
    .filter { it.isUp && it.index != -1 && !it.isLoopback }
    .minByOrNull { it.index }
    ?.inetAddresses
    ?.toList()
    ?.firstOrNull { it is Inet4Address && !it.isLoopbackAddress }
    ?.hostAddress
    ?: try {
        InetAddress.getLocalHost().hostAddress
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        null
    } ?: "127.0.0.1"

/**
 * 判断地址是否是期望的域.
 */
@Suppress("UNCHECKED_CAST")
public fun isPreferredAddress(address: InetAddress): Boolean {
    val useOnlySiteLocalInterfaces =
        Env.getProperty("spring.cloud.inetutils.use-only-site-local-interfaces", Boolean::class.java, false)
    if (useOnlySiteLocalInterfaces) {
        return address.isSiteLocalAddress
    }

    val preferredNetworks =
        Env.getProperty(
            "spring.cloud.inetutils.preferred-networks",
            List::class.java,
            emptyList<String>(),
        ) as List<String>

    if (preferredNetworks.isEmpty()) {
        return true
    }
    return preferredNetworks.any {
        val hostAddress = address.hostAddress
        hostAddress.matches(Regex(it)) || hostAddress.startsWith(it)
    }
}
