package com.study.board.dto;

import com.mysema.commons.lang.Assert;
import com.querydsl.core.annotations.QueryProjection;
import com.study.board.domain.Board;
import com.study.board.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class BoardDto {

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Save {

        @NotEmpty
        @Length(min = 1, max = 100)
        private String title;

        @NotEmpty
        private String description;

        @Builder
        public Save(String title, String description) {
            Assert.hasText(title, "Title is not empty");
            Assert.hasText(description, "Description is not empty");

            this.title = title;
            this.description = description;
        }

        public Board toEntity() {
            return Board.builder()
                    .title(title)
                    .description(description)
                    .build();
        }
    }


    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class List {

        private Long id;
        private String title;
        private int viewCount;
        private MemberDto.Info author;
        private LocalDateTime createdDate;

        @Builder
        @QueryProjection
        public List(Long id, String title, int viewCount, Member author, LocalDateTime createdDate) {
            Assert.isTrue(id > 0L, "Id is not zero");
            Assert.hasText(title, "Title is not empty");
            Assert.notNull(author, "Author is not null");
            Assert.notNull(createdDate, "CreatedDate is not null");

            this.id = id;
            this.title = title;
            this.viewCount = viewCount;
            this.author = MemberDto.Info.create(author);
            this.createdDate = createdDate;
        }

        public static BoardDto.List create(Board board) {
            return List.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .viewCount(board.getViewCount())
                    .author(board.getCreatedBy())
                    .createdDate(board.getCreatedDate())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Info {

        private Long id;
        private String title;
        private String description;
        private int viewCount;
        private MemberDto.Info author;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;


        @Builder
        public Info(Long id, String title, String description, int viewCount, MemberDto.Info author, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
            Assert.isTrue(id > 0L, "Id is not zero");
            Assert.hasText(title, "Title is not empty");
            Assert.hasText(description, "Description is not empty");
            Assert.notNull(author, "Author is not null");
            Assert.notNull(createdDate, "CreatedDate is not null");
            Assert.notNull(lastModifiedDate, "LastModifiedDate is not null");

            this.id = id;
            this.title = title;
            this.description = description;
            this.viewCount = viewCount;
            this.author = author;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
        }

        public static BoardDto.Info create(Board board) {
            return Info.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .description(board.getDescription())
                    .viewCount(board.getViewCount())
                    .author(MemberDto.Info.create(board.getCreatedBy()))
                    .createdDate(board.getCreatedDate())
                    .lastModifiedDate(board.getLastModifiedDate())
                    .build();
        }
    }


}
