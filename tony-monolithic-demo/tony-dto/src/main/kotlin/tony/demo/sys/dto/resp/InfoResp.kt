package tony.demo.sys.dto.resp

import io.swagger.v3.oas.annotations.media.Schema

/**
 * InfoResp is
 * @author tangli
 * @date 2024/07/02 13:33
 */
data class InfoResp(
    @param:Schema(description = "用户id")
    val employeeId: String,
    @param:Schema(description = "账号")
    val account: String,
    @param:Schema(description = "姓名")
    val realName: String,
    @param:Schema(description = "手机号")
    val employeeMobile: String,
) {
    @get:Schema(description = "路由列表")
    var routePaths: List<String> = emptyList()

    @get:Schema(description = "控件列表")
    var componentCodes: List<String> = emptyList()
}
