package com.study.board.search.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class BaseDateTimeSearch {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDateTime;

    /**
     * LocalDateTime -> LocalDate 변경 = 년월일시분초 -> 년월일
     * @return 시작일
     */
    public LocalDate getStartLocalDate() {
        return LocalDate.from(startDateTime);
    }

    /**
     * LocalDateTime -> LocalDate 변경 = 년월일시분초 -> 년월일
     * @return 마지막일
     */
    public LocalDate getEndLocalDate() {
        return LocalDate.from(endDateTime);
    }

}
