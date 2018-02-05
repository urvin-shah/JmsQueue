package com.urvin.fifo;

import com.urvin.fifo.queue.FifoTextMessageQueue;
import com.urvin.config.Config;

public class FifoQueueDemo {
    public static void main(String[] args) {
        FifoQueueDemo fifoQueue = new FifoQueueDemo();
        fifoQueue.elasticMQ();
//        fifoQueue.sqsMQ();
    }

    private void elasticMQ() {
        Config config = new Config();
        FifoTextMessageQueue queue = new FifoTextMessageQueue("TextMessageQueue",config.getEQConnectionFactory());
        queue.sendTextMessage("This is Fifo Elastic MQ test message");
        queue.sendTextMessage("This is Fifo Elastic MQ test message1");
        queue.sendTextMessage("This is Fifo Elastic MQ test message2");
        queue.sendTextMessage("This is Fifo Elastic MQ test message3");
        queue.sendTextMessage("This is Fifo Elastic MQ test message4");
        queue.sendTextMessage("This is Fifo Elastic MQ test message5");



    }

    private void sqsMQ() {
        Config config = new Config();
        FifoTextMessageQueue queue = new FifoTextMessageQueue("TextMessageQueue.fifo",config.getSQSConnectionFactory());
        queue.sendTextMessage("This is Fifo Queue Test message 1");
        queue.sendTextMessage("This is Fifo Queue Test message 2");
        queue.sendTextMessage("This is Fifo Queue Test message 3");
        queue.sendTextMessage("This is Fifo Queue Test message 4");
        queue.sendTextMessage("This is Fifo Queue Test message 5");
        queue.sendTextMessage("This is Fifo Queue Test message 6");
    }
}
