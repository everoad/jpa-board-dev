package com.study.board;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CommonTest {

    @Test
    public void timeTest() {
        String str1 = "asd";
        String str2 = null;
        if (!ObjectUtils.allNotNull(str1, str2)) {
            System.out.println("not null");
        } else {
            System.out.println("null");
        }

    }
}
