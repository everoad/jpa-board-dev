package com.study.board.service;

import com.study.board.common.MemberAdapter;
import com.study.board.domain.Member;
import com.study.board.domain.MemberRole;
import com.study.board.dto.MemberDto;
import com.study.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        Member member = optionalMember.get();
        return new MemberAdapter(member);
    }

    @Transactional
    public Long saveMember(MemberDto.Save memberDto) {
        Optional<Member> optionalMember = memberRepository.findByUsername(memberDto.getUsername());
        if (optionalMember.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "exists username=" + memberDto.getUsername());
        }

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberDto.setRoles(Set.of(MemberRole.USER));
        Member member = memberDto.toEntity();
        memberRepository.save(member);
        return member.getId();
    }

    public Page<MemberDto.List> queryMemberList(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page.map(MemberDto.List::create);
    }

}
