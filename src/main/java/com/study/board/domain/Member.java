package com.study.board.domain;

import com.study.board.domain.base.BaseTimeEntity;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Set;

@Audited @AuditOverride(forClass = BaseTimeEntity.class)
@Entity @Builder @Getter
@ToString(of = {"id", "username"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SequenceGenerator(sequenceName = "MEMBER_SEQ_GENERATOR", name = "MEMBER_SEQ", initialValue = 1, allocationSize = 50)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, length = 20)
    private String username;

    @NotAudited
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles;
    
}
