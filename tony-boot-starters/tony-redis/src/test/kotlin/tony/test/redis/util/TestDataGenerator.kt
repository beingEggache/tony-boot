/*
 * MIT License
 *
 * Copyright (c) 2023-present, tangli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tony.test.redis.util

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import tony.enums.IntEnumCreator
import tony.enums.IntEnumValue
import tony.enums.StringEnumCreator
import tony.enums.StringEnumValue
import tony.utils.uuid
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.UUID

/**
 * 测试数据生成器
 *
 * 提供各种类型的测试数据，用于Redis单元测试
 * 深度集成tony-core提供的工具类和枚举功能
 *
 * @author tony
 * @date 2025/07/01 17:00
 */
object TestDataGenerator {

    /**
     * 创建测试用户
     *
     * @param id 用户ID
     * @param name 用户名
     * @param age 年龄
     * @param email 邮箱
     * @param tags 标签列表
     * @return 测试用户对象 [TestUser]
     * @author tony
     * @date 2025-06-30 14:00
     */
    fun createTestUser(
        id: Long = 1L,
        name: String = "张三",
        age: Int = 25,
        email: String? = "zhangsan@example.com",
        tags: List<String> = listOf("VIP", "活跃用户")
    ): TestUser = TestUser(id, name, age, email, tags)

    /**
     * 创建测试订单
     *
     * @param orderId 订单ID
     * @param amount 订单金额
     * @param items 订单项列表
     * @param status 订单状态
     * @return 测试订单对象 [TestOrder]
     * @author tony
     * @date 2025-06-30 14:00
     */
    fun createTestOrder(
        orderId: String = "ORDER001",
        amount: BigDecimal = BigDecimal("99.99"),
        items: List<TestOrderItem> = listOf(createTestOrderItem()),
        status: OrderStatus = OrderStatus.PENDING
    ): TestOrder = TestOrder(orderId, amount, items, status)

    /**
     * 创建测试订单项
     *
     * @param itemId 商品ID
     * @param name 商品名称
     * @param price 商品价格
     * @param quantity 数量
     * @return 测试订单项对象 [TestOrderItem]
     * @author tony
     * @date 2025-06-30 14:00
     */
    fun createTestOrderItem(
        itemId: String = "ITEM001",
        name: String = "测试商品",
        price: BigDecimal = BigDecimal("49.99"),
        quantity: Int = 2
    ): TestOrderItem = TestOrderItem(itemId, name, price, quantity)

    /**
     * 创建简单对象
     *
     * @param name 名称
     * @param age 年龄
     * @return 简单对象 [SimpleObj]
     * @author tony
     * @date 2025-06-30 14:00
     */
    fun createSimpleObj(
        name: String = "测试对象",
        age: Int = 30
    ): SimpleObj = SimpleObj(name, age)

    /**
     * 创建包含数字类型的对象
     *
     * @return 包含多种数字类型的对象 [ObjWithNumberTypes]
     * @author tony
     * @date 2025-06-30 14:00
     */
    fun createObjWithNumberTypes(): ObjWithNumberTypes = ObjWithNumberTypes(
        byte = 127,
        short = 32767,
        int = 2147483647,
        long = 9223372036854775807L,
        bigInteger = BigInteger("9999999999999999999999999999999999999999"),
        float = 3.14f,
        double = 3.141592653589793,
        bigDecimal = BigDecimal("3.141592653589793238462643383279")
    )

    /**
     * 创建包含列表的对象
     *
     * @param name 名称
     * @param list 字符串列表
     * @return 包含列表的对象 [ObjWithList]
     * @author tony
     * @date 2025-06-30 14:00
     */
    fun createObjWithList(
        name: String = "列表对象",
        list: List<String> = listOf("item1", "item2", "item3")
    ): ObjWithList = ObjWithList(name, list)

    /**
     * 创建包含Map的对象
     *
     * @param name 名称
     * @param map Map对象
     * @return 包含Map的对象 [ObjWithMap]
     * @author tony
     * @date 2025-06-30 14:00
     */
    fun createObjWithMap(
        name: String = "Map对象",
        map: Map<String, SimpleObj> = mapOf(
            "key1" to createSimpleObj("对象1", 20),
            "key2" to createSimpleObj("对象2", 30)
        )
    ): ObjWithMap = ObjWithMap(name, map)

    /**
     * 创建大对象（用于性能测试）
     *
     * @return 大对象 [LargeTestObject]
     * @author tony
     * @date 2025-06-30 14:00
     */
    fun createLargeObject(): LargeTestObject = LargeTestObject(
        id = uuid(),
        name = "大对象测试",
        description = "这是一个用于测试的大对象，包含大量数据",
        timestamp = LocalDateTime.now(),
        data = ByteArray(1024) { it.toByte() }, // 1KB数据
        metadata = mapOf(
            "version" to "1.0.0",
            "created" to System.currentTimeMillis(),
            "tags" to listOf("large", "test", "performance")
        )
    )
}

/**
 * 测试用户
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class TestUser(
    val id: Long,
    val name: String,
    val age: Int,
    val email: String?,
    val tags: List<String>
)

/**
 * 测试订单
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class TestOrder(
    val orderId: String,
    val amount: BigDecimal,
    val items: List<TestOrderItem>,
    val status: OrderStatus
)

/**
 * 测试订单项
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class TestOrderItem(
    val itemId: String,
    val name: String,
    val price: BigDecimal,
    val quantity: Int
)

/**
 * 订单状态枚举
 * @author tangli
 * @date 2025/06/30 14:00
 */
enum class OrderStatus(override val value: String) : StringEnumValue {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),
    CANCELLED("cancelled"),
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    companion object : StringEnumCreator(OrderStatus::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): StringEnumValue? {
            return super.create(value.lowercase())
        }
    }
}

/**
 * 简单对象
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class SimpleObj(
    val name: String,
    val age: Int
)

/**
 * 包含数字类型的对象
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class ObjWithNumberTypes(
    val byte: Byte,
    val short: Short,
    val int: Int,
    val long: Long,
    val bigInteger: BigInteger,
    val float: Float,
    val double: Double,
    val bigDecimal: BigDecimal
)

/**
 * 包含列表的对象
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class ObjWithList(
    val name: String,
    val list: List<String>
)

/**
 * 包含Map的对象
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class ObjWithMap(
    val name: String,
    val map: Map<String, SimpleObj>
)

/**
 * 包含对象列表的对象
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class ObjWithObjList(
    val name: String,
    val list: List<SimpleObj>
)

/**
 * 大测试对象
 * @author tangli
 * @date 2025/06/30 14:00
 */
data class LargeTestObject(
    val id: String,
    val name: String,
    val description: String,
    val timestamp: LocalDateTime,
    val data: ByteArray,
    val metadata: Map<String, Any>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LargeTestObject

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (timestamp.equals(other.timestamp)) return false
        if (!data.contentEquals(other.data)) return false
        if (metadata != other.metadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + metadata.hashCode()
        return result
    }
}

/**
 * 测试字符串枚举
 * @author tangli
 * @date 2025/06/30 14:01
 */
enum class TestStringEnum(override val value: String) : StringEnumValue {
    YES("yes"),
    NO("no"),
    @JsonEnumDefaultValue
    UNKNOWN("unknown");

    companion object : StringEnumCreator(TestStringEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: String): StringEnumValue? {
            return super.create(value.lowercase())
        }
    }
}

/**
 * 测试整数枚举
 * @author tangli
 * @date 2025/06/30 14:01
 */
enum class TestIntEnum(override val value: Int) : IntEnumValue {
    ACTIVE(1),
    INACTIVE(0),
    PENDING(2);

    companion object : IntEnumCreator(TestIntEnum::class.java) {
        @JsonCreator
        @JvmStatic
        override fun create(value: Int): IntEnumValue? {
            return super.create(value)
        }
    }
}
