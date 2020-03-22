package com.study.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.domain.Board;
import com.study.board.domain.QBoard;
import com.study.board.dto.BoardDto;
import com.study.board.repository.support.Querydsl4RepositorySupport;
import com.study.board.search.BoardSearch;
import com.study.board.dto.QBoardDto_List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.study.board.domain.QBoard.*;
import static com.study.board.domain.QBoard.board;
import static com.study.board.domain.QMember.*;

@Repository
public class BoardQueryRepository extends Querydsl4RepositorySupport<Board> {

    public BoardQueryRepository() {
        super(Board.class);
    }

    public Page<BoardDto.List> findAll(BoardSearch boardSearch, Pageable pageable) {
        return applyPagination(pageable, query -> query
                .select(
                        new QBoardDto_List(
                                board.id, board.title, board.viewCount, board.createdBy, board.createdDate
                        )
                )
                .from(board)
                .join(board.createdBy, member)
                .where(
                        keywordLikeAll(boardSearch.getKeyword()),
                        createdDateBetween(boardSearch.getStartDateTime(), boardSearch.getEndDateTime())
                )
        );
    }

    private BooleanExpression createdDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ObjectUtils.allNotNull(startDate, endDate) ? board.createdDate.between(startDate, endDate) : null;
    }

    private BooleanExpression keywordLikeAll(String keyword) {
        return StringUtils.hasText(keyword) ? board.title.contains(keyword).or(board.description.contains(keyword)) : null;
    }

}
