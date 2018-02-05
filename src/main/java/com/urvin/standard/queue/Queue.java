package com.urvin.standard.queue;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import javax.jms.JMSException;

public class Queue {
    private SQSConnectionFactory connectionFactory;
    private String queueName;

    public Queue(String queueName, SQSConnectionFactory connectionFactory) {
        this.queueName = queueName;
        this.connectionFactory = connectionFactory;
    }

    private void initConnectionFactory() {
         this.connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1)
                        .withCredentials(new ProfileCredentialsProvider()));
    }

    public SQSConnection getConnection() throws JMSException {
        return connectionFactory.createConnection();
    }

    public static void ensureQueueExists(SQSConnection connection, String queueName) throws JMSException {
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        if( !client.queueExists(queueName) ) {
            client.createQueue( queueName );
        }
    }


    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return this.queueName;
    }

}
