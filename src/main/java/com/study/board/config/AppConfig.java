package com.study.board.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.common.HtmlCharacterEscapes;
import com.study.board.common.MemberAdapter;
import com.study.board.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

import javax.persistence.EntityManager;
import java.util.Optional;


/**
 * Application Config.
 * 간단한 설정 및 Bean 등록.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final ObjectMapper objectMapper;

    private String ANONYMOUS_USER = "anonymousUser";

    /**
     * 비밀번호 인코딩에 사용할 Security 내장 Encoder 생성.
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /**
     * QueryDSL 사용하기 위한 JPAQueryFactory Bean 생성.
     * @param entityManager : JPA Entity Manager.
     * @return JPAQueryFactory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }


    /**
     * Auditing 을 적용한 Entity 에서 insert/update event 발생시 자동으로 로그인 정보를 가져와
     * 생성자, 수정자를 함께 입력해준다.
     * @return
     */
    @Bean
    public AuditorAware<Member> auditorProvider() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(MemberAdapter.class::cast)
                .map(MemberAdapter::getMember);
    }


    /**
     * request body 로 들어오는 xss 공격을 막기 위한 Converter.
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
