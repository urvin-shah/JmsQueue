package com.urvin.fifo.queue;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazon.sqs.javamessaging.message.SQSObjectMessage;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.urvin.domain.User;

import javax.jms.*;


public class FifoObjectMessageQueue extends FifoQueue implements MessageListener{
    public static int messageGroupID = 1;

    public FifoObjectMessageQueue(String queueName, AmazonSQSClient amazonSQSClient) {
        super(queueName,amazonSQSClient);
//        try {
//            ensureQueueExists(getConnection(), queueName);
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
        receiveMessageAsync();
    }

    public void sendMessage(com.urvin.domain.Message message) {
        try {
            SQSConnection connection = this.getConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Queue name is :"+this.getQueueName());
            MessageProducer producer = session.createProducer(session.createQueue(this.getQueueName()));
            SQSObjectMessage sqsMessage = new SQSObjectMessage(message);
            sqsMessage.setStringProperty("JMSXGroupID", "message"+(messageGroupID++));
            producer.send(sqsMessage);
            System.out.println("JMS Message " + sqsMessage.getJMSMessageID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessageAsync() {
        try {
            System.out.println("The Queue name is:-"+this.getQueueName());
            SQSConnection connection = this.getConnection();
            ensureQueueExists(getConnection(), this.getQueueName());
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
            User user =(User) ((SQSObjectMessage)message).getObject();
            System.out.println("Username :"+user.getUsername());
            System.out.println("Email :"+user.getEmail());
            System.out.println("Phone :"+user.getPhone());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
