package com.ing.fr.app.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greet")
public class GreetingController {

    @GetMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> greeting(){
        long startTime = System.currentTimeMillis();
        return ResponseEntity.ok("hello baby");
    }
}
