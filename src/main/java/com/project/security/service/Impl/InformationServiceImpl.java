package com.project.security.service.Impl;


import com.project.security.entity.Information;
import com.project.security.repository.InfromationRepository;
import com.project.security.service.InformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class InformationServiceImpl implements InformationService {
    public final InfromationRepository infromationRepository;

    public InformationServiceImpl(InfromationRepository infromationRepository) {
        this.infromationRepository = infromationRepository;
    }

    @Override
    public Information saveData(Information info) {
        log.info("Calling method");
        Information information = Information.builder()
                .name(info.getName())
                .age(info.getAge())
                .build();
        log.info("Saving information: {}", information);
        return infromationRepository.save(information);
    }

    @Override
    public List<Information> getAll() {
        log.info("Retrieving all information");
        return infromationRepository.findAll();
    }
}
