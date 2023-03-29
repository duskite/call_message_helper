package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoilerplateServiceImpl implements BoilerplateService {

    private final BoilerplateRepository boilerplateRepository;

    public BoilerplateServiceImpl(BoilerplateRepository boilerplateRepository) {
        this.boilerplateRepository = boilerplateRepository;
    }

    @Override
    public Long addBoilerplate(Boilerplate boilerplate) {
        return boilerplateRepository.save(boilerplate);
    }

    @Override
    public Long modifyBoilerplate(Boilerplate boilerplate) {
        return null;
    }

    @Override
    public boolean removeBoilerplate(Boilerplate boilerplate) {
        Optional<Boilerplate> optionalBoilerplate = boilerplateRepository
                .findBySubjectAndAuthorUserId(boilerplate.getSubject(), boilerplate.getAuthorUserId());
        if(optionalBoilerplate.isPresent()){
            Boilerplate findBoilerplate = optionalBoilerplate.get();
            boilerplateRepository.remove(findBoilerplate);

            return true;
        }
        return false;
    }

    @Override
    public List<Boilerplate> findAllBoilerplate(String authorUserId) {
        return boilerplateRepository.findAllByAuthorUserId(authorUserId);
    }
}
