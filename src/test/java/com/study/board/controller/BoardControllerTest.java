package com.study.board.controller;

import com.study.board.common.BaseControllerTest;
import com.study.board.domain.Board;
import com.study.board.dto.BoardDto;
import com.study.board.dto.CommentDto;
import com.study.board.repository.BoardQueryRepository;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import com.study.board.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends BaseControllerTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardQueryRepository boardQueryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CommentRepository commentRepository;

    @Test
    public void 게시글_생성() throws Exception {
        generateBoard(1);
    }

    @AfterEach
    public void setDown() throws Exception {
        commentRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("게시글_생성_입력값_누락_파라미터")
    public void 게시글_생성_입력값_누락(String title, String description) throws Exception {
        // Given
        Map<String, String> boardMap = new HashMap<>();
        boardMap.put("title", title);
        boardMap.put("description", description);

        // When
        ResultActions actions = postRequest("/boards", boardMap);

        // Then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> 게시글_생성_입력값_누락_파라미터() {
        return Stream.of(
                Arguments.of("Hello title", null),
                Arguments.of(null, "Hello Description")
        );
    }

    @Test
    public void 게시글_단건_조회() throws Exception {
        // Given
        Long boardId = generateBoard(1);
        Thread.sleep(1000);
        // When
        ResultActions actions = getRequest("/boards/" + boardId, null);

        // Then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value(boardId))
                .andExpect(jsonPath("data.viewCount").value(1))
                .andExpect(jsonPath("data.createdBy.username").value(appProperties.getTestUserName()));

        Optional<Board> optionalBoard = boardRepository.findWithCreatedMemberById(boardId);

        Board board = optionalBoard.get();
        assertThat(board.getCreatedDate().isEqual(board.getLastModifiedDate()));
    }


    @ParameterizedTest
    @MethodSource("게시글_리스트_검색_조회_파라미터")
    public void 게시글_리스트_검색_조회(String keyword, String startDate, String endDate, int hasSize) throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateBoard);

        Map<String, String> params = new HashMap<>();
        String size = "15";
        String page = "0";
        params.put("size", size);
        params.put("page", page);
        params.put("keyword", keyword);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("sort", "createdBy.username,DESC");

        // When
        ResultActions actions = getRequest("/boards", params);

        // Then
        actions
                .andDo(print())
                .andExpect(status().isOk());
                //.andExpect(jsonPath("data.content", hasSize(hasSize)));
    }


    private static Stream<Arguments> 게시글_리스트_검색_조회_파라미터() {
        return Stream.of(
                Arguments.of("Title29", LocalDate.now().minusDays(1).toString(), LocalDate.now().toString(), 1),
                Arguments.of("Description29", LocalDate.now().toString(), LocalDate.now().toString(), 1),
                Arguments.of(null, LocalDate.now().toString(), LocalDate.now().toString(), 15)
        );
    }


    @ParameterizedTest
    @MethodSource("게시글_리스트_검색_조회_기간_누락_파라미터")
    public void 게시글_리스트_검색_조회_기간_누락(String keyword, String startDate, String endDate, int hasSize) throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateBoard);

        Map<String, String> params = new HashMap<>();
        String size = "15";
        String page = "0";
        params.put("size", size);
        params.put("page", page);
        params.put("keyword", keyword);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        // When
        ResultActions actions = getRequest("/boards", params);

        // Then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest());

    }


    private static Stream<Arguments> 게시글_리스트_검색_조회_기간_누락_파라미터() {
        return Stream.of(
                Arguments.of("Title29", LocalDate.now().minusDays(1).toString(), null, 1),
                Arguments.of("Description29", null, LocalDate.now().toString(), 1)
        );
    }



    @Test
    public void 게시글_수정() throws Exception {
        // Given
        Long boardId = generateBoard(1);

        Thread.sleep(200);
        BoardDto.Save boardDto = BoardDto.Save.builder()
                .title("Update Title")
                .description("Update Description")
                .build();

        // When
        ResultActions actions = putRequest("/boards/" + boardId, boardDto);

        // Then
        actions
                .andDo(print())
                .andExpect(status().isOk());

        Board findBoard = boardRepository.findWithCreatedMemberById(boardId).get();

        assertThat(findBoard.getTitle()).isEqualTo(boardDto.getTitle());
        assertThat(findBoard.getDescription()).isEqualTo(boardDto.getDescription());
        assertThat(findBoard.getCreatedDate()).isNotEqualTo(findBoard.getLastModifiedDate());
    }

    @Test
    public void 게시글_삭제() throws Exception {
        // Given
        Long boardId = generateBoard(1);

        // When
        ResultActions actions = deleteRequest("/boards/" + boardId, null);

        // Then
        actions
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Board> optionalBoard = boardRepository.findById(boardId);

        assertThat(optionalBoard.isEmpty()).isTrue();
    }

    @Test
    public void 댓글_작성() throws Exception {
        // Given
        Long boardId = generateBoard(1);
        CommentDto.Save commentDto = CommentDto.Save.builder()
                .description("Hello Comment Description")
                .build();

        // When
        ResultActions actions = postRequest("/boards/" + boardId + "/comments", commentDto);

        // Then
        actions
                .andDo(print())
                .andExpect(status().isCreated());
    }



    private Long generateBoard(int i) {
        // Given
        String title = "Hello Title" + i;
        String description = "Hello Description" + i;
        BoardDto.Save boardDto = BoardDto.Save.builder()
                .title(title)
                .description(description)
                .build();

        // When
        ResultActions actions = null;
        try {
            actions = postRequest("/boards", boardDto);

            // Then
            actions
                .andExpect(status().isCreated());

        } catch (Exception e) {
            e.printStackTrace();
        }

        String redirectedUrl = actions.andReturn().getResponse().getRedirectedUrl();
        Long id = Long.parseLong(redirectedUrl.substring(redirectedUrl.lastIndexOf("/")+1));
        return id;
    }

}