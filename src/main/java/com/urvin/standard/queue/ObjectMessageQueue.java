package com.urvin.standard.queue;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazon.sqs.javamessaging.message.SQSObjectMessage;
import com.urvin.domain.User;

import javax.jms.*;

public class ObjectMessageQueue extends Queue implements MessageListener {

    private MessageConsumer consumer;

    public ObjectMessageQueue(String queueName, SQSConnectionFactory connectionFactory) {
        super(queueName,connectionFactory);
        try {
            ensureQueueExists(getConnection(), this.getQueueName());
        }catch(Exception e) {
            e.printStackTrace();
        }
        receiveMessageAsync();
    }

    public void sendTextMessage(com.urvin.domain.Message message) {
        try {
            SQSConnection connection = this.getConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Queue name is :"+this.getQueueName());
            MessageProducer producer = session.createProducer(session.createQueue(this.getQueueName()));
            SQSObjectMessage sqsMessage = new SQSObjectMessage(message);
            producer.send(sqsMessage);
            System.out.println("JMS Message " + sqsMessage.getJMSMessageID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessageAsync() {
        try {
            SQSConnection connection = this.getConnection();
            // Create the session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            consumer = session.createConsumer( session.createQueue( this.getQueueName() ) );

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
            consumer.receive();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
