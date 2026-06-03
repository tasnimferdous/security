package com.project.security.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("hasAnyAuthority('ALL', 'READ')")
    public String sayHello(){
        return "Hello!";
    }

}
