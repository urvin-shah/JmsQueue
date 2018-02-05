package com.urvin.fifo.queue;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

public class FifoQueue {
    private SQSConnectionFactory connectionFactory;
    private String queueName;
    private String queueUrl;


    public FifoQueue(String queueName,SQSConnectionFactory connectionFactory) {
        this.queueName = queueName;
        this.connectionFactory = connectionFactory;
    }

    public SQSConnection getConnection() throws JMSException {
        return connectionFactory.createConnection();
    }

    public static void ensureQueueExists(SQSConnection connection, String queueName) throws JMSException {
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        if( !client.queueExists(queueName) ) {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("FifoQueue", "true");
            attributes.put("ContentBasedDeduplication", "true");
            CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName).withAttributes(attributes);
            client.createQueue( createQueueRequest );
        }
    }
    public String getQueueName() {
        return this.queueName;
    }
}
