package com.project.security.controller;


import com.project.security.entity.Information;
import com.project.security.service.InformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/info")
@Slf4j
public class InfoController {

    public final InformationService informationService;

    public InfoController(InformationService informationService) {
        this.informationService = informationService;
    }

    @PostMapping("/store")
    @PreAuthorize(("hasAuthority('WRITE')"))
    public Information storeData(@RequestBody Information information){
        log.info("calling api..");
        return informationService.saveData(information);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('READ')")
    public List<Information> getAllData(){
        log.info("Retrieving all data");
        return informationService.getAll();
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public String removeInfo(@PathVariable Long id){
        log.info("Removing information with id: {}", id);
        return informationService.deleteInfo(id);
    }
}
