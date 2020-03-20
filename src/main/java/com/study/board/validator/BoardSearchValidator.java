package com.study.board.validator;

import com.study.board.search.BoardSearch;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BoardSearchValidator {

    private static final int MAX_SEARCH_PERIOD_YEARS = 1;

    public boolean validate(BoardSearch boardSearch, Errors errors) {
        // 시작일이 마지막일보다 클 경우
        if (boardSearch.getStartDate().isAfter(boardSearch.getEndDate())) {
            errors.rejectValue("startDate", "wrongValue", "startDate is not after then endDate.");
        }

        // 검색 기간이 1년을 넘을 경우
        if (boardSearch.getStartDate().isBefore(boardSearch.getEndDate().minusYears(MAX_SEARCH_PERIOD_YEARS))) {
            errors.reject("wrongValue", "Max search period is 1 years.");
        }

        return errors.hasErrors();
    }

}
