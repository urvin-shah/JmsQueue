package com.urvin.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Value("${sqs.connection}")
    private String connectionType;

    @Autowired
    private ApplicationContext context;

    @Bean(value = "elasticMQ")
    public SQSConnectionFactory getEQConnectionFactory() {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient(new BasicAWSCredentials("x","x"));
        String endPoint = "http://localhost:9324";
        amazonSQSClient.setEndpoint(endPoint);
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),amazonSQSClient);
        return connectionFactory;
    }

    @Bean(value = "sqsMQ")
    public SQSConnectionFactory getSQSConnectionFactory() {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient(new ProfileCredentialsProvider());
        amazonSQSClient.configureRegion(Regions.US_EAST_1);
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),amazonSQSClient);
        return connectionFactory;
    }

    @Bean(value = "jms")
    public SQSConnectionFactory getConnectionFactory() {
        System.out.println("Value of connection type :"+connectionType);
        return (SQSConnectionFactory) context.getBean(connectionType);
    }

    @Bean(value = "jms")
    public AmazonSQSClient getAmazonSQSClient() {
        System.out.println("Value of connection type :"+connectionType);
        return (AmazonSQSClient) context.getBean(connectionType);
    }

    @Bean(value = "elasticMQ")
    public AmazonSQSClient getEQAmazonSQSClient() {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient(new BasicAWSCredentials("x","x"));
        String endPoint = "http://localhost:9324";
        amazonSQSClient.setEndpoint(endPoint);
        return amazonSQSClient;
    }

    @Bean(value = "sqsMQ")
    public AmazonSQSClient getSQSAmazonClient() {
        AmazonSQSClient amazonSQSClient = new AmazonSQSClient(new ProfileCredentialsProvider());
        amazonSQSClient.configureRegion(Regions.US_EAST_1);
        return amazonSQSClient;
    }
}
