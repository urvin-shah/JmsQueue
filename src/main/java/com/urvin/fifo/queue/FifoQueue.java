package com.urvin.fifo.queue;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

public class FifoQueue {
    private SQSConnectionFactory connectionFactory;
    private String queueName;
    private String queueUrl;
    private AmazonSQSClient amazonSQSClient;

    public FifoQueue(String queueName,AmazonSQSClient amazonSQSClient) {
        this.queueName = queueName;
        this.amazonSQSClient = amazonSQSClient;
        this.connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),amazonSQSClient);
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

    public AmazonSQSClient getAmazonSQSClient() {
        return this.amazonSQSClient;
    }
}
