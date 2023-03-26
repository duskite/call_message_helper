package com.dus.back.security;

import com.dus.back.member.Member;
import com.dus.back.member.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    public UserDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        if(optionalMember.isEmpty()){
            throw new UsernameNotFoundException("존재하지 않는 userId");
        }

        return new SecurityUser(optionalMember.get());
    }
}
