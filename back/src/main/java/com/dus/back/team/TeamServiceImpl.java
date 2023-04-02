package com.dus.back.team;

import com.dus.back.domain.Invitation;
import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
@Slf4j
public class TeamServiceImpl implements TeamService{

    private final MemberService memberService;

    private final TeamRepository teamRepository;
    private final InvitationRepository invitationRepository;


    public TeamServiceImpl(MemberService memberService, TeamRepository teamRepository, InvitationRepository invitationRepository) {
        this.memberService = memberService;
        this.teamRepository = teamRepository;
        this.invitationRepository = invitationRepository;
    }

    @Override
    public Long addTeam(Team team) {
        duplicateCheck(team);
        Member adminMember = memberService.findByUserId(team.getAdminUserId());
        team.setMember(adminMember);

        return teamRepository.save(team);
    }

    @Override
    public boolean deleteTeam(Team team) {

        Optional<Team> optionalTeam = teamRepository.findByTeamName(team.getTeamName());
        if(optionalTeam.isPresent()){
            teamRepository.remove(optionalTeam.get());
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void duplicateCheck(Team team) {
        Optional<Team> optionalTeam = teamRepository.findByTeamName(team.getTeamName());
        if(optionalTeam.isPresent()){
            throw new DuplicateException("중복 팀 이름");
        }
    }

    @Override
    public Team findByTeamName(String teamName) {
        Optional<Team> optionalTeam = teamRepository.findByTeamName(teamName);
        if(optionalTeam.isPresent()){
            return optionalTeam.get();
        }else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Team findByAdminUserId(String adminUserId) {
        Optional<Team> optionalTeam = teamRepository.findByAdminUserId(adminUserId);
        if(optionalTeam.isPresent()){
            return optionalTeam.get();
        }else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Team> findAllByAdminUserId(String adminUserId) {
        List<Team> findTeamList = teamRepository.findAllByAdminUserId(adminUserId);
        if(!findTeamList.isEmpty()){
            return findTeamList;
        }else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validName = error.getField();
            validatorResult.put(validName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Override
    public void deleteTeamMember(String teamName, String userID) {
        Optional<Team> optionalTeam = teamRepository.findByTeamName(teamName);
        if (optionalTeam.isPresent()) {
            Team findTeam = optionalTeam.get();
            Stream<Member> memberStream = findTeam.getMembers().stream().filter(member ->
                    member.getUserId().equals(userID)
            );
            Optional<Member> optionalMember = memberStream.findAny();
            Member findMember = optionalMember.get();

            log.info("팀원 삭제 로직. 삭제를 위해 찾은 멤버 {}", findMember.getUserId());
            findTeam.getMembers().remove(findMember);

        }else {
            throw new NoSuchElementException("팀원 삭제를 위해, 팀이름으로 팀을 조회했으나 존재하지 않음");
        }
    }


    @Override
    public boolean createInvite(Invitation invitation) {

        invitationRepository.save(invitation);
        return true;
    }

    @Override
    public void rejectInvite(Invitation invitation) {
        invitationRepository.remove(invitation);
    }

    @Override
    public void acceptInvite(Invitation invitation) {
        Optional<Team> optionalTeam = teamRepository.findByTeamName(invitation.getTeamName());
        if (optionalTeam.isPresent()) {
            Team findTeam = optionalTeam.get();
            Member findMember = memberService.findByUserId(invitation.getInviteeUserId());
            findTeam.setMember(findMember);

            //초대 수락하고 나서 초대장 지우기
            invitationRepository.remove(invitation);
        }else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Invitation> findAllInviteByInviteeUserId(String inviteeUserId) {
        return invitationRepository.findAllByInviteeUserId(inviteeUserId);
    }

}
