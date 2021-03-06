package com.urvin.fifo.queue;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazon.sqs.javamessaging.message.SQSMessage;
import com.amazonaws.services.sqs.AmazonSQSClient;

import javax.jms.*;

public class FifoTextMessageQueue extends FifoQueue implements MessageListener {

    public static int messageGroupID = 1;
    public FifoTextMessageQueue(String queueName, AmazonSQSClient amazonSQSClient) {
        super(queueName,amazonSQSClient);
        try {
            ensureQueueExists(getConnection(), queueName);
        }catch(Exception e) {
            e.printStackTrace();
        }
        receiveMessageAsync();
    }

    public void sendTextMessage(String textMessage) {
        try {
            SQSConnection connection = this.getConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Queue name is :"+this.getQueueName());
//            ensureQueueExists(connection, this.getQueueName());
//            Queue queue = session.createQueue(this.getQueueName());
            MessageProducer producer = session.createProducer(session.createQueue(this.getQueueName()));
            TextMessage message = session.createTextMessage(textMessage);
            message.setStringProperty("JMSXGroupID", "message"+(messageGroupID++));
            producer.send(message);
            System.out.println("JMS Message " + message.getJMSMessageID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessageAsync() {
        try {
            SQSConnection connection = this.getConnection();
            // Create the session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer( session.createQueue( this.getQueueName() ) );

            consumer.setMessageListener( this );

            // No messages are processed until this is called
            connection.start();
            System.out.println("Consumer is ready to receive the message >>>>>>>>>>");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            // Cast the received message as TextMessage and print the text to screen.
            System.out.println("Received: " + ((TextMessage) message).getText());
            System.out.println("Message Queue Url:"+((SQSMessage)message).getQueueUrl());
            System.out.println("Recepient handle:"+((SQSMessage)message).getReceiptHandle());
            this.getAmazonSQSClient().deleteMessage(((SQSMessage)message).getQueueUrl(),((SQSMessage)message).getReceiptHandle());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
