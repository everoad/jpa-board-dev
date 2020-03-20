package com.study.board.domain;

import com.study.board.domain.base.BaseEntity;
import com.study.board.dto.BoardDto;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

/**
 *  Hibernate-envers : Entity 변경 이력을 자동으로 관리해주는 라이브러리.
 *  => @Audited : 해당 Entity 이력을 관리함. (등록,수정,삭제)
 *  => @AuditOverride : Entity 상위 클래스도 같이 관리함.
 *  => @NotAudited : 해당 Field 는 관리에서 제외됨.
 *
 *  @SequenceGenerator : 시퀀스를 사용할 수 있는 DB의 경우, Sequence 정보를 작성.
 *  => allocationSize : 설정된 값만큼 Sequence 를 가져와서 메모리에 저장 후 insert 시 사용한다.
 *                      Insert 마다 DB에 매번 Sequence 값을 가져오는 것을 방지.
 */
@Audited @AuditOverride(forClass = BaseEntity.class)
@Entity @Getter @Builder
@ToString(of = {"id", "title", "description"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SequenceGenerator(sequenceName = "BOARD_SEQ_GENERATOR", name = "BOARD_SEQ", initialValue = 1, allocationSize = 50)
public class Board extends BaseEntity {

    /**
     * @GeneratedValue
     *  => IDENTITY : AUTO_INCREMENT 사용. persist 시 ID 값을 가져오기 위해 바로 insert 해야 하는 단점 존재.
     *  => SEQUENCE : SEQUENCE 사용. generator 의 allocationSize 로 IDENTITY 단점 보완.
     *  => TABLE : MySql 같이 Sequence 가 없는 Database 일 경우 테이블로 Sequence 흉내 내서 키를 관리.
     *  => AUTO : Database 종류에 따라 Jpa 가 위의 셋 중에 하나를 선택함.
     */
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @NotAudited
    private int viewCount;

    //==비즈니스 로직==//

    /**
     * title, description 업데이트.
     * @param boardDto
     */
    public void updateDetail(BoardDto.Save boardDto) {
        this.title = boardDto.getTitle();
        this.description = boardDto.getDescription();
    }

}
