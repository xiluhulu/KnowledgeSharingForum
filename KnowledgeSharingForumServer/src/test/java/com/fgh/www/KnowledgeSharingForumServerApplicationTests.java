package com.fgh.www;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KnowledgeSharingForumServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test2(){
        DateTime dateTime = DateUtil.nextMonth();
        System.out.println(dateTime);
        String dateStr = DateUtil.format(dateTime, "yyyy-MM-dd");
        System.out.println(dateStr);
    }

}
