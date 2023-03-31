package com.dus.back.team;

import com.dus.back.domain.Invite;
import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class TeamServiceImpl implements TeamService{

    private final MemberService memberService;

    private final TeamRepository teamRepository;
    private final InviteRepository inviteRepository;


    public TeamServiceImpl(MemberService memberService, TeamRepository teamRepository, InviteRepository inviteRepository) {
        this.memberService = memberService;
        this.teamRepository = teamRepository;
        this.inviteRepository = inviteRepository;
    }

    @Override
    public Long addTeam(Team team) {
        duplicateCheck(team);

        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Team team) {

        Optional<Team> optionalTeam = teamRepository.findByTeamName(team.getTeamName());
        if(optionalTeam.isPresent()){
            teamRepository.remove(optionalTeam.get());
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
    public void createInvite(Invite invite) {
        inviteRepository.save(invite);
    }

    @Override
    public void rejectInvite(Invite invite) {
        inviteRepository.remove(invite);
    }

    @Override
    public void acceptInvite(Invite invite) {
        Optional<Team> optionalTeam = teamRepository.findByTeamName(invite.getTeamName());
        if (optionalTeam.isPresent()) {
            Team findTeam = optionalTeam.get();
            Member findMember = memberService.findByUserId(invite.getInviteeUserId());
            findTeam.setMember(findMember);
        }else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Invite findInviteByInviteeUserId(String inviteeUserId) {
        Optional<Invite> optionalInvite = inviteRepository.findByInviteeUserId(inviteeUserId);
        if(optionalInvite.isPresent()){
            return optionalInvite.get();
        }else {
            throw new NoSuchElementException("userId로 조회되는 초대 내역이 없음");
        }
    }

    @Override
    public Invite findInviteByAdminUserId(String adminUserId) {
        Optional<Invite> optionalInvite = inviteRepository.findByInviteeUserId(adminUserId);
        if(optionalInvite.isPresent()){
            return optionalInvite.get();
        }else {
            throw new NoSuchElementException("adminUserId로 조회되는 초대 내역이 없음");
        }
    }
}
