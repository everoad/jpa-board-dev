package com.study.board.validator;

import com.study.board.dto.MemberDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class MemberValidator {

    private final static String PW_PATTERN = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{9,12}$";

    public boolean validate(MemberDto.Save memberDto, Errors errors) {

        if (memberDto.getPassword().matches(PW_PATTERN)) {
            errors.rejectValue("password", "wrongValue", "Password is not matching a pattern.");
        }

        return errors.hasErrors();
    }
}
