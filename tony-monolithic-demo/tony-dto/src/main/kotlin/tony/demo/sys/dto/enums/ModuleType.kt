package tony.demo.sys.dto.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import tony.enums.DEFAULT_INT_VALUE
import tony.enums.IntEnumCreator
import tony.enums.IntEnumValue

@Suppress("unused")
enum class ModuleType(
    override val value: Int,
) : IntEnumValue {
    NODE(0),
    ROUTE(1),
    COMPONENT(2),
    API(3),

    @JsonEnumDefaultValue
    UNUSED(DEFAULT_INT_VALUE),
    ;

    companion object : IntEnumCreator(ModuleType::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int) =
            super.create(value)
    }
}
