package com.study.board.dto;

import com.mysema.commons.lang.Assert;
import com.study.board.domain.Member;
import com.study.board.domain.MemberRole;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

public class MemberDto {

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Save {

        @NotEmpty
        @Length(min = 5, max = 15)
        private String username;

        @NotEmpty
        private String password;

        private Set<MemberRole> roles;

        @Builder
        public Save(String username, String password, Set<MemberRole> roles) {
            Assert.hasText(username, "Username is not empty");
            Assert.hasText(password, "Password is not empty");
            Assert.notEmpty(roles, "Password is not empty");

            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        public Member toEntity() {
            return Member.builder()
                    .username(username)
                    .password(password)
                    .roles(roles)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Info {

        private Long id;
        private String username;

        @Builder
        public Info(Long id, String username) {
            Assert.isTrue(id > 0L, "ID is not zero.");
            Assert.hasText(username, "Username is not empty");

            this.id = id;
            this.username = username;
        }

        public static MemberDto.Info create(Member member) {
            return MemberDto.Info.builder()
                    .id(member.getId())
                    .username(member.getUsername())
                    .build();
        }
    }


    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class List {

        private Long id;
        private String username;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        @Builder
        public List(Long id, String username, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
            Assert.isTrue(id > 0L, "ID is not zero.");
            Assert.hasText(username, "Username is not empty");
            Assert.notNull(createdDate, "CreatedDate is not empty");

            this.id = id;
            this.username = username;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
        }

        public static MemberDto.List create(Member member) {
            return MemberDto.List.builder()
                    .username(member.getUsername())
                    .createdDate(member.getCreatedDate())
                    .lastModifiedDate(member.getLastModifiedDate())
                    .build();
        }

    }


}
