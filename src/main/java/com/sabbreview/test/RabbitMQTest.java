package com.sabbreview.test;

import com.rabbitmq.client.*;
import com.sabbreview.controller.EmailController;

public class RabbitMQTest {
    public static void main(String[] args) throws Exception {
        String uri = System.getenv(EmailController.loadFile("src/main/resources/static/RabbitMQ_URL.txt"));
        if (uri == null) uri = EmailController.loadFile("src/main/resources/static/RabbitMQ_URL.txt");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);

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
        String message = "loremIpsum/Kal/kaloianbch@gmail.com";

        String exchangeName = "";
        String routingKey = "Email Instructions";
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        channel.close();
        connection.close();

        EmailController.ReceiveFromQueue();
    }
}
