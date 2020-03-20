package com.study.board.common;

import com.study.board.domain.Member;
import com.study.board.domain.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Security User class 를 상속 받음.
 * CurrentUser Annotation 으로 member field 값을 가져옴.
 */
public class MemberAdapter extends User {

    private final static String ROLE_PREFIX = "ROLE_";
    private Member member;

    public MemberAdapter(Member member) {
        super(member.getUsername(), member.getPassword(), authorities(member.getRoles()));
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    private static Collection<? extends GrantedAuthority> authorities(Set<MemberRole> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(ROLE_PREFIX + r.name())).collect(Collectors.toSet());
    }
}
