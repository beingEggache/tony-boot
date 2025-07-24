package tony.knife4j.customizers

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.customizers.GlobalOperationCustomizer
import org.springframework.core.PriorityOrdered
import org.springframework.web.method.HandlerMethod

/**
 * Use200ResponseOperationCustomizer is
 * @author tangli
 * @date 2025/07/24 13:46
 */
internal class UnifyResponseOperationCustomizer(
    private val defaultResponseName: String = "200",
) : GlobalOperationCustomizer,
    PriorityOrdered {
    override fun customize(
        operation: Operation,
        handlerMethod: HandlerMethod,
    ): Operation {
        operation.responses =
            ApiResponses().addApiResponse(defaultResponseName, operation.responses[defaultResponseName])
        return operation
    }

    override fun getOrder(): Int =
        PriorityOrdered.HIGHEST_PRECEDENCE
}
