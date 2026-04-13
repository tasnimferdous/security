package com.project.security.controller;


import com.project.security.entity.Information;
import com.project.security.service.InformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
@Slf4j
public class InfoController {

    public final InformationService informationService;

    @PostMapping("/store")
    public Information storeData(@RequestBody Information information){
        log.info("calling api..");
        return informationService.saveData(information);
    }

    @GetMapping("/all")
    public List<Information> getAllData(){
        log.info("Retrieving all data");
        return informationService.getAll();
    }
}
