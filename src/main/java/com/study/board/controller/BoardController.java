package com.study.board.controller;

import com.study.board.common.CurrentUser;
import com.study.board.common.MyMediaType;
import com.study.board.domain.Member;
import com.study.board.dto.BoardDto;
import com.study.board.search.BoardSearch;
import com.study.board.dto.CommentDto;
import com.study.board.dto.ResponseData;
import com.study.board.service.BoardService;
import com.study.board.validator.BoardSearchValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/boards", produces = MyMediaType.APPLICATION_JSON_VALUE)
public class BoardController {

    private final BoardService boardService;
    private final BoardSearchValidator boardSearchValidator;

    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody @Valid BoardDto.Save boardDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        System.out.println("boardDto = " + boardDto);
        Long savedId = boardService.saveBoard(boardDto);
        URI created = linkTo(BoardController.class).slash(savedId).toUri();
        return ResponseEntity.created(created).build();
    }

    @GetMapping
    public ResponseEntity<?> getBoardList(@Valid BoardSearch boardSearch, Errors errors, Pageable pageable) {
        if (errors.hasErrors() || boardSearchValidator.validate(boardSearch, errors)) {
            return ResponseEntity.badRequest().body(errors);
        }
        Page<BoardDto.List> page = boardService.queryBoardList(boardSearch, pageable);
        return ResponseEntity.ok(new ResponseData<>(page));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable("boardId") Long boardId) {
        BoardDto.Info boardDto = boardService.queryBoard(boardId);
        return ResponseEntity.ok(new ResponseData<>(boardDto));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> editBoard(@PathVariable("boardId") Long boardId,
                                       @RequestBody @Valid BoardDto.Save boardDto, Errors errors,
                                       @CurrentUser Member member) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        boardService.updateBoard(boardId, member, boardDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> removeBoard(@PathVariable("boardId") Long boardId,
                                         @CurrentUser Member member) {
        boardService.deleteBoard(boardId, member);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<?> createComment(@PathVariable("boardId") Long boardId,
                                           @RequestBody @Valid CommentDto.Save commentDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Long savedId = boardService.saveComment(boardId, commentDto);
        URI created = linkTo(methodOn(BoardController.class).createComment(boardId, commentDto, errors)).slash(savedId).toUri();
        return ResponseEntity.created(created).build();
    }

    @GetMapping("/{boardId}/comments")
    public ResponseEntity<?> getCommentList(@PathVariable("boardId") Long boardId, Pageable pageable) {
        Page<CommentDto.List> page = boardService.queryCommentList(boardId, pageable);
        return ResponseEntity.ok(new ResponseData<>(page));
    }

}
