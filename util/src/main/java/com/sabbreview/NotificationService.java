package com.sabbreview;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sabbreview.model.NotificationID;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Creates connection to cloudAMQP server and sends message.
 * @deprecated
 */
public class NotificationService {

    /**
     * Sends instruction message to queue used by the email module.
     * Message format is <i>NotificationID/Recipient Name/Recipient Email</i>
     * @param notificationID
     * @param recipientName
     * @param recipientEmail
     * @see NotificationID
     */
    public void sendNotification(NotificationID notificationID, String recipientName, String recipientEmail){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(System.getenv("RABBITMQ_URI"));

            //Recommended settings
            factory.setRequestedHeartbeat(30);
            factory.setConnectionTimeout(30000);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String queue = "Email Instructions";     //queue name
            boolean durable = false;    //durable - RabbitMQ will never lose the queue if a crash occurs
            boolean exclusive = false;  //exclusive - if queue only will be used by one connection
            boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes

            channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
            String message = notificationID.toString() + "/" + recipientName + "/" + recipientEmail;
            String exchangeName = "";
            String routingKey = "Email Instructions";
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
            //System.out.println("Message sent");
            channel.close();
            connection.close();
        }catch (IOException ex){//fix me later
            ex.printStackTrace();
        }catch (TimeoutException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
