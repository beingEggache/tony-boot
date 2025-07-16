package tony.test.core.jackson

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import tony.core.jackson.MaskConverter
import tony.core.jackson.MobileMaskFun
import tony.core.jackson.NameMaskFun
import tony.core.jackson.MaskSerializer
import tony.core.jackson.NullValueBeanSerializerModifier
import tony.core.jackson.InjectableValuesBySupplier
import tony.core.jackson.InjectableValueSupplier
import kotlin.jvm.java

/**
 * jackson 扩展点测试
 *
 * @author tangli
 * @date 2025/06/27 17:00
 */
@DisplayName("tony-core jackson 扩展点测试")
class JacksonTest {

    data class MaskedUser(
        @MaskConverter(NameMaskFun::class)
        val name: String,
        @MaskConverter(MobileMaskFun::class)
        val mobile: String
    )
    @Nested
    @DisplayName("字段遮蔽序列化")
    inner class MaskConverterTest {

        @Test
        @DisplayName("姓名与手机号遮蔽序列化")
        fun testMaskSerialize() {
            val user = MaskedUser("张三丰", "13812345678")
            val om = ObjectMapper()
            val module = SimpleModule().addSerializer(MaskSerializer::class.java, MaskSerializer())
            om.registerModule(module)
            val json = om.writeValueAsString(user)
            // 姓名遮蔽：只保留首字，后面全部变为**
            assertTrue(json.contains("\"name\":\"张**\""))
            // 手机号遮蔽：前2后4保留，中间****
            assertTrue(json.contains("\"mobile\":\"13****5678\""))
        }
    }
    data class NullFields(
        val str: String? = null,
        val arr: List<String>? = null,
        val obj: Map<String, Any>? = null
    )
    @Nested
    @DisplayName("Null值序列化定制")
    inner class NullValueModifierTest {

        @Test
        @DisplayName("null字符串/集合/对象序列化")
        fun testNullValueSerialize() {
            val om = ObjectMapper()
            om.setSerializerFactory(
                om.serializerFactory.withSerializerModifier(NullValueBeanSerializerModifier())
            )
            val bean = NullFields()
            val json = om.writeValueAsString(bean)
            // null字符串应输出空串
            assertTrue(json.contains("\"str\":\"\""))
            // null集合应输出 []
            assertTrue(json.contains("\"arr\":[]"))
            // null对象应输出 {}
            assertTrue(json.contains("\"obj\":{}"))
        }
    }
    data class InjectedBean(
        @field:JacksonInject("injected")
        var injected: String? = null
    )
    class SimpleSupplier : InjectableValueSupplier {
        override val name: String = "injected"
        override fun value(property: com.fasterxml.jackson.databind.BeanProperty?, instance: Any?): Any = "注入值"
    }

    @Nested
    @DisplayName("InjectableValues 注入机制")
    inner class InjectableValuesTest {

        @Test
        @DisplayName("自定义InjectableValues注入")
        fun testInjectableValues() {
            val om = ObjectMapper()
            om.setInjectableValues(InjectableValuesBySupplier(mapOf("injected" to SimpleSupplier())))
            val bean = om.readValue("{}", InjectedBean::class.java)
            assertEquals("注入值", bean.injected)
        }
    }
}
