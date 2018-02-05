package com.urvin.standard.queue;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;

import javax.jms.*;

public class TextMessageQueue extends Queue implements MessageListener {

    public TextMessageQueue(String queueName, SQSConnectionFactory connectionFactory) {
        super(queueName,connectionFactory);
        receiveMessageAsync();
    }

    public void sendTextMessage(String textMessage) {
        try {
            SQSConnection connection = this.getConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Queue name is :"+this.getQueueName());
            ensureQueueExists(connection, this.getQueueName());
//            Queue queue = session.createQueue(this.getQueueName());
            MessageProducer producer = session.createProducer(session.createQueue(this.getQueueName()));
            TextMessage message = session.createTextMessage(textMessage);

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
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
