package com.ing.fr.app.controllers;

import com.ing.fr.app.services.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping(value = "/publish", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> publish(@RequestBody String message){
        kafkaProducerService.sendMessage(message);
        return ResponseEntity.ok("Done");
    }
}
