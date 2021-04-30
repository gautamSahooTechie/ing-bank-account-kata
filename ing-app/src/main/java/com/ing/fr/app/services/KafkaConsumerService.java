package com.ing.fr.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger =
            LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "topic1", topicPartitions = @TopicPartition(
            topic = "topic1", partitions = {"0"}
            ), groupId = "GROUP_ID")
    public void getTopicP1(String message) {
        logger.info(String.format("Message received getTopicP1-> %s", message));

        System.out.println(message);
    }

    @KafkaListener(topics = "topic1", topicPartitions = @TopicPartition(
            topic = "topic1", partitions = {"1"}
    ), groupId = "GROUP_ID")
    public void getTopicP2(String message) {
        logger.info(String.format("Message getTopicP2 -> %s", message));

        System.out.println(message);
    }

//    @KafkaListener(topics = "topic2" , groupId = "GROUP_ID")
//    public void getMessageTwo(String message) {
//        logger.info(String.format("Message getMessageTwo -> %s", message));
//
//        System.out.println(message);
//    }
}