package com.tony.core.test

import com.tony.core.utils.formatToPercent
import com.tony.core.utils.println
import org.junit.jupiter.api.Test

/**
 *
 * @author tangli
 * @since 2021-05-21 12:48
 */
class Test {

    @Test
    fun testIfNullOrEmpty() {
        500.123f.formatToPercent().println()
    }


}
