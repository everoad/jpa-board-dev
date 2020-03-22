package com.study.board.common;

import com.study.board.domain.MemberRole;
import com.study.board.dto.MemberDto;
import com.study.board.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@TestConfiguration
public class TestConfig {

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            MemberService memberService;
            @Autowired
            AppProperties appProperties;


            @Override
            public void run(ApplicationArguments args) throws Exception {

                MemberDto.Save memberDto = MemberDto.Save.builder()
                        .username(appProperties.getTestUserName())
                        .password(appProperties.getTestUserPassword())
                        .roles(Set.of(MemberRole.USER, MemberRole.ADMIN))
                        .build();

                memberService.saveMember(memberDto);
            }

        };
    }
}
