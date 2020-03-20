package com.study.board.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.yml 에 등록된 값들을 Java 단에서 사용 가능.
 * application.yml 에 새로운 값 추가시 build 해야 함.
 * prefix = my-app > my-app 으로 시작하는 값들을 가져옴.
 */
@Component
@Getter @Setter
@ConfigurationProperties(prefix = "my-app")
public class AppProperties {

    /**
     * Junit 테스트 용 계정 정보.
     * 테스트 시에만 생성됨.
     */
    private String testUserName;
    private String testUserPassword;

    /**
     * Client 정보.
     */
    private String clientId;
    private String clientSecret;

    /**
     * OAuth 인증 방식.
     */
    private String grantType;

}
