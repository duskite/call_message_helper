package com.dus.back.team;

import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import com.dus.back.exception.DuplicateException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
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
}
