package com.study.board.domain;

import com.study.board.domain.base.BaseEntity;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import javax.persistence.*;


@Audited @AuditOverride(forClass = BaseEntity.class)
@Entity @Builder @Getter
@ToString(of = {"id", "description"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SequenceGenerator(sequenceName = "COMMENT_SEQ_GENERATOR", name = "COMMENT_SEQ", initialValue = 1, allocationSize = 50)
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column(name = "comment_id")
    private Long id;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

}
