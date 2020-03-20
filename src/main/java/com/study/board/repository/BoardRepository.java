package com.study.board.repository;


import com.study.board.domain.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"createdBy"})
    Optional<Board> findWithCreatedMemberById(Long aLong);

    @Modifying
    @Query(value = "UPDATE board SET view_count = view_count + 1 WHERE board_id = :id", nativeQuery = true)
    void updateViewCount(@Param("id") Long boardId);

}
