package com.study.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.board.dto.BoardDto;
import com.study.board.search.BoardSearch;
import com.study.board.dto.QBoardDto_List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.study.board.domain.QBoard.*;
import static com.study.board.domain.QMember.*;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {

    private final JPAQueryFactory query;

    public Page<BoardDto.List> findAll(BoardSearch boardSearch, Pageable pageable) {

        QueryResults<BoardDto.List> queryResults = query
                .select(
                        new QBoardDto_List(board.id, board.title, board.viewCount, board.createdBy, board.createdDate)
                )
                .from(board)
                .join(board.createdBy, member)
                .where(
                        keywordLikeAll(boardSearch.getKeyword()),
                        createdDateBetween(boardSearch.getStartDateTime(), boardSearch.getEndDateTime())
                )
                .offset(pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc()) //TODO order by 구현 필요.
                .fetchResults(); // fetchResults = 데이터 조회와 함께 total count 도 같이 조회한다.
                                 // 최적화 필요시 total count query 별도 구현.

        List<BoardDto.List> boardList = queryResults.getResults();
        long totalCount = queryResults.getTotal();

        return new PageImpl<>(boardList, pageable, totalCount);
    }

    private BooleanExpression createdDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ObjectUtils.allNotNull(startDate, endDate) ? board.createdDate.between(startDate, endDate) : null;
    }

    private BooleanExpression keywordLikeAll(String keyword) {
        return StringUtils.hasText(keyword) ? board.title.contains(keyword).or(board.description.contains(keyword)) : null;
    }

}
