package com.sopt.smeem

import com.sopt.smeem.util.DateUtil
import org.junit.Test

class DateUtilsTest {

    @Test
    fun `1~9 월은 두자리로 치환된다`() {
        assert(DateUtil.month(1) == "01")
        assert(DateUtil.month(9) == "09")
    }

    @Test
    fun `10~12 월은 그대로 반환된다`() {
        assert(DateUtil.month(10) == "10")
        assert(DateUtil.month(12) == "12")
    }

    @Test
    fun `1~9 일은 두자리로 치환된다`() {
        assert(DateUtil.day(1) == "01")
        assert(DateUtil.day(9) == "09")
    }

    @Test
    fun `10~31일은 그대로 반환된다`() {
        assert(DateUtil.day(10) == "10")
        assert(DateUtil.day(31) == "31")
    }

}