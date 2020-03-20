package com.study.board.controller;

import com.study.board.common.MyMediaType;
import com.study.board.dto.MemberDto;
import com.study.board.service.MemberService;
import com.study.board.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/members", produces = MyMediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;
    private final MemberValidator memberValidator;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody @Valid MemberDto.Save memberDto, Errors errors) {
        if (errors.hasErrors() || memberValidator.validate(memberDto, errors)) {
            return ResponseEntity.badRequest().body(errors);
        }
        Long savedId = memberService.saveMember(memberDto);
        URI created = linkTo(MemberController.class).slash(savedId).toUri();
        return ResponseEntity.created(created).build();
    }

    @GetMapping
    public ResponseEntity<?> getMemberList(Pageable pageable) {
        Page<MemberDto.List> page = memberService.queryMemberList(pageable);
        return ResponseEntity.ok(page);
    }

}
