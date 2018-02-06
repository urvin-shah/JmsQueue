package com.urvin.controller;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.urvin.domain.User;
import com.urvin.fifo.queue.FifoObjectMessageQueue;
import com.urvin.fifo.queue.FifoTextMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@RequestMapping("/elastic")
public class ElasticQueueController {

    @Value("${elastic.queue.text}")
    private String textQueueName;

    @Value("${elastic.queue.object}")
    private String objectQueueName;

    @Autowired
    @Qualifier("elasticMQ")
    SQSConnectionFactory connectionFactory;

    private FifoObjectMessageQueue objectMessageQueue;
    private FifoTextMessageQueue textMessageQueue;

//    @PostConstruct
//    public void init() {
//        System.out.println("Text Queue name:"+textQueueName);
//        System.out.println("Object Queue name:"+objectQueueName);
//        textMessageQueue = new FifoTextMessageQueue(textQueueName,connectionFactory);
//        objectMessageQueue = new FifoObjectMessageQueue(objectQueueName,connectionFactory);
//    }

    @PostMapping("/objectMessage")
    public boolean sendMessage(@RequestBody User user){
        System.out.println("User details :"+user);
        objectMessageQueue.sendMessage(user);
        return true;
    }

    @PostMapping("/textMessage")
    public boolean sendObjectMessage(@RequestParam String message) {
        System.out.println("Received Message : "+message);
        textMessageQueue.sendTextMessage(message);
        return true;
    }
}
