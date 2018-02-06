package com.urvin.controller;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.urvin.domain.User;
import com.urvin.fifo.queue.FifoObjectMessageQueue;
import com.urvin.fifo.queue.FifoTextMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@RequestMapping("/sqs")
public class SQSQueueController {

    @Autowired
    @Qualifier("jms")
    SQSConnectionFactory connectionFactory;

    @Value("${sqs.queue.text}")
    private String textQueueName;

    @Value("${sqs.queue.object}")
    private String objectQueueName;

    private FifoTextMessageQueue textMessageQueue;
    private FifoObjectMessageQueue objectMessageQueue;

    @PostConstruct
    public void init(){
        System.out.println("Text Message queue:"+textQueueName);
        System.out.println("Object message Queue name :"+objectQueueName);
        textMessageQueue = new FifoTextMessageQueue(textQueueName,connectionFactory);
        objectMessageQueue = new FifoObjectMessageQueue(objectQueueName,connectionFactory);
    }

    @PostMapping("/textMessage")
    public boolean sendTextMessage(@RequestParam String message) {
        System.out.println("Received message from rest api is :"+message);
        textMessageQueue.sendTextMessage(message);
        return true;
    }

    @PostMapping("/objectMessage")
    public boolean sendObjectMessage(@RequestBody User user) {
        System.out.println("Received User details:"+user);
        objectMessageQueue.sendMessage(user);
        return true;
    }
}
