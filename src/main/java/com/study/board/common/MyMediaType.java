package com.study.board.common;

import org.springframework.http.MediaType;

public class MyMediaType {

    private final static String UTF_8 = ";charset=utf-8";

    public final static String APPLICATION_JSON_VALUE = MediaType.APPLICATION_JSON_VALUE + UTF_8;

}
