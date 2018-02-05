package com.urvin;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.urvin.standard.queue.TextMessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JmsQueueApplication {

	@Autowired
	@Qualifier("elasticMQ")
	static SQSConnectionFactory eqConnectionFactory;

	public static void main(String[] args) {
		SpringApplication.run(JmsQueueApplication.class, args);
//		standardQueueDemo();
	}

	public static void standardQueueDemo() {
		TextMessageQueue textMessageQueue = new TextMessageQueue("StandardTextMessageQueue",eqConnectionFactory);
		textMessageQueue.sendTextMessage("This is standard Queue Text Message !!!");
	}
}
