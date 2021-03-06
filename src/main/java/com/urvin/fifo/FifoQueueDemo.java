package com.urvin.fifo;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.urvin.domain.User;
import com.urvin.fifo.queue.FifoObjectMessageQueue;
import com.urvin.fifo.queue.FifoTextMessageQueue;
import com.urvin.config.Config;

public class FifoQueueDemo {
    public static void main(String[] args) {
        FifoQueueDemo fifoQueue = new FifoQueueDemo();
        Config config = new Config();
//        fifoQueue.textMessage(config.getEQConnectionFactory());
//        fifoQueue.objectMessage(config.getEQConnectionFactory());
        fifoQueue.textMessage(config.getSQSAmazonClient());
        //fifoQueue.objectMessage(config.getSQSConnectionFactory());
    }

    private void textMessage(AmazonSQSClient amazonSQSClient) {
        FifoTextMessageQueue queue = new FifoTextMessageQueue("TextMessageQueue.fifo",amazonSQSClient);
        queue.sendTextMessage("This is Fifo Elastic MQ test message");
        queue.sendTextMessage("This is Fifo Elastic MQ test message1");
        queue.sendTextMessage("This is Fifo Elastic MQ test message2");
        queue.sendTextMessage("This is Fifo Elastic MQ test message3");
        queue.sendTextMessage("This is Fifo Elastic MQ test message4");
        queue.sendTextMessage("This is Fifo Elastic MQ test message5");
    }

    private void objectMessage(AmazonSQSClient amazonSQSClient) {
        FifoObjectMessageQueue queue = new FifoObjectMessageQueue("ObjectMessageQueue",amazonSQSClient);
        queue.sendMessage(new User("Urvin","urvin@gmail.com","9756568888"));
        queue.sendMessage(new User("Vivek","vivek@gmail.com","97689787897"));
        queue.sendMessage(new User("Riyal","riyal@gmail.com","94567807"));
        queue.sendMessage(new User("Harshal","harshal@gmail.com","990889088"));
    }
}
