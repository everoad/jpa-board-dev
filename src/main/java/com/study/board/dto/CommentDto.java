package com.study.board.dto;

import com.mysema.commons.lang.Assert;
import com.study.board.domain.Board;
import com.study.board.domain.Comment;
import com.study.board.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class CommentDto {

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Save {

        @NotEmpty
        private String description;

        @Builder
        public Save(String description) {
            Assert.hasText(description, "Description is not empty");

            this.description = description;
        }

        public Comment toEntity(Board board) {
            return Comment.builder()
                    .board(board)
                    .description(description)
                    .build();
        }

    }


    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class List {
        private Long id;
        private String description;
        private MemberDto.Info author;
        private LocalDateTime createdDate;

        @Builder
        public List(Long id, String description, Member author, LocalDateTime createdDate) {
            Assert.isTrue(id > 0L, "ID is not zero.");
            Assert.hasText(description, "Description is not empty");
            Assert.notNull(author, "Author is not null");
            Assert.notNull(createdDate, "CreatedDate is not null");

            this.id = id;
            this.description = description;
            this.author = MemberDto.Info.create(author);
            this.createdDate = createdDate;
        }

        public static CommentDto.List create(Comment comment) {
            return List.builder()
                    .id(comment.getId())
                    .description(comment.getDescription())
                    .author(comment.getCreatedBy())
                    .createdDate(comment.getCreatedDate())
                    .build();
        }
    }
}
