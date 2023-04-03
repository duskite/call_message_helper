package com.dus.back.team;

import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamServiceImplTest {

    @Autowired TeamService teamService;
    @Autowired
    MemberService memberService;

    @BeforeEach
    void setMember(){
        Member member = Member.builder()
                .userId("ysy")
                .build();
        memberService.addMember(member);
    }

    @Test
    @DisplayName("admin이 없는 팀은 존재할 수 없음")
    void needAdminInTeam() {

        Team team = Team.builder()
                .teamName("test_team")
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            teamService.addTeam(team);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("팀 생성")
    void createTeam() {

        Team team = Team.builder()
                .teamName("test_team")
                .adminUserId("ysy")
                .build();

        teamService.addTeam(team);
    }

    @Test
    @DisplayName("팀 이름이 동일할 경우 생성 실패함")
    void createTeamSameTeamName() {

        Team team = Team.builder()
                .teamName("test_team")
                .adminUserId("ysy")
                .build();
        Team team2 = Team.builder()
                .teamName("test_team")
                .adminUserId("ysy")
                .build();

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            teamService.addTeam(team);
            teamService.addTeam(team2);
        }).isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("팀 생성 후 멤버 초대")
    void inviteMember() {
        Team team = Team.builder()
                .teamName("test_team")
                .adminUserId("ysy")
                .build();
        teamService.addTeam(team);

        Member member = Member.builder()
                .userId("new_member")
                .build();
        team.setMember(member);

        Team findTeam = teamService.findByTeamName(team.getTeamName());
        List<Member> members = findTeam.getMembers();
        members.contains(member);
    }


}