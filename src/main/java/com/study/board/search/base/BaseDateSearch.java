package com.study.board.search.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter @Setter
public class BaseDateSearch {

    /**
     * @DateTimeFormat : 해당 패턴 넘어올 경우 LocalDate로 파싱 가능. 다른 패턴일 경우 에러 발생.
     */
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * LocalDate -> LocalDateTime 변경 = 년월일 -> 년월일시분초 ( yyyy-MM-ddT00:00:00 )
     * @return 시작일
     */
    public LocalDateTime getStartDateTime() {
        return startDate.atStartOfDay();
    }

    /**
     * LocalDate -> LocalDateTime 변경 = 년월일 -> 년월일시분초 ( yyyy-MM-ddT00:00:00 )
     * 마지막일을 포함시키기 위해 하루를 더한다.
     * @return 마지막일
     */
    public LocalDateTime getEndDateTime() {
        return endDate.plusDays(1).atStartOfDay();
    }

}
