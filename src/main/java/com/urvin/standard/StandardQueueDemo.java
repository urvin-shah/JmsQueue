package com.urvin.standard;

import com.urvin.config.Config;
import com.urvin.standard.queue.TextMessageQueue;

public class StandardQueueDemo {

    public static void main(String[] args) {
        StandardQueueDemo standardQueue = new StandardQueueDemo();
        standardQueue.elasticMQ();
    }

    private void elasticMQ() {
        Config config = new Config();
        TextMessageQueue queue = new TextMessageQueue("TextMessageQueue",config.getEQConnectionFactory());
        queue.sendTextMessage("This is Elastic MQ test message");
    }
}
