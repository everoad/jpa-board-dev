package com.study.board.service;

import com.study.board.domain.Board;
import com.study.board.domain.Comment;
import com.study.board.domain.Member;
import com.study.board.domain.MemberRole;
import com.study.board.dto.BoardDto;
import com.study.board.search.BoardSearch;
import com.study.board.dto.CommentDto;
import com.study.board.repository.BoardQueryRepository;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long saveBoard(BoardDto.Save boardDto) {
        Board board = boardDto.toEntity();
        boardRepository.save(board);
        return board.getId();
    }

    @Transactional
    public void updateBoard(Long boardId, Member member, BoardDto.Save boardDto) {
        Board board = getBoardWithValidation(boardId);
        checkAuthority(member, board.getCreatedBy());
        board.updateDetail(boardDto);
    }

    @Transactional
    public void deleteBoard(Long boardId, Member member) {
        Board board = getBoardWithValidation(boardId);
        checkAuthority(member, board.getCreatedBy());
        boardRepository.delete(board);
    }

    @Transactional
    public BoardDto.Info queryBoard(Long boardId) {
        //조회수 증가, lastModified 도 같이 수정되는 것을 막기 위해 native query 사용.
        //TODO 다른 방법 없는지 확인 필요.
        boardRepository.updateViewCount(boardId);
        Board board = getBoardWithValidation(boardId);
        return BoardDto.Info.create(board);
    }

    public Page<BoardDto.List> queryBoardList(BoardSearch boardSearch, Pageable pageable) {
        return boardQueryRepository.findAll(boardSearch, pageable);
    }

    @Transactional
    public Long saveComment(Long boardId, CommentDto.Save commentDto) {
        Board board = getBoardWithValidation(boardId);
        Comment comment = commentDto.toEntity(board);
        commentRepository.save(comment);
        return comment.getId();
    }

    public Page<CommentDto.List> queryCommentList(Long boardId, Pageable pageable) {
        Board board = getBoardWithValidation(boardId);
        Page<Comment> page = commentRepository.findAllByBoardId(board.getId(), pageable);
        return page.map(CommentDto.List::create);
    }

    private Board getBoardWithValidation(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findWithCreatedMemberById(boardId);
        if (optionalBoard.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No board by Id=" + boardId);
        }
        return optionalBoard.get();
    }

    private void checkAuthority(Member member, Member createdBy) {
        if (!member.getId().equals(createdBy.getId())
                && !member.getRoles().contains(MemberRole.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied by username=" + member.getUsername());
        }
    }



}
