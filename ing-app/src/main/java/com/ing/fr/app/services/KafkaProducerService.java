package com.ing.fr.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger =
            LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message)
    {
        logger.info(String.format("Message sent -> %s", message));
//        this.kafkaTemplate.send("topic1", message);
//        this.kafkaTemplate.send("topic2", message);
        this.kafkaTemplate.send("topic1", 2, "1", message);
//        this.kafkaTemplate.send("topic1", 1, "X", message);
//        this.kafkaTemplate.send("topic1", 0, "X", message);

        // this.kafkaTemplate.send("topic2", 2, "Y", message);

    }
}
