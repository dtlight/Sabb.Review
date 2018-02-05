package com.sabbreview.controller;

import net.sargue.mailgun.*;
import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmailController {

    public static String ReadFile(String url) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(url));
            return bf.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static Configuration configuration = new Configuration()
            .domain("sabb.review")
            .apiKey(ReadFile("src/main/resources/static/API-Key.txt"))
            .from("sabbbot", "postmaster@sabb.review");

    public static void Send(String subject, String content, String receiver) {
        Mail.using(configuration)
                .to(receiver)
                .subject(subject)
                .text(content)
                .build()
                .send();
    }

    public static void RecieveFromQueue() {
        String uri = System.getenv(ReadFile("src/main/resources/static/RabbitMQ_URL.txt"));
        if (uri == null) uri = ReadFile("src/main/resources/static/RabbitMQ_URL.txt");

        ConnectionFactory factory = new ConnectionFactory();

        try {
            factory.setUri(uri);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Recommended settings
        factory.setRequestedHeartbeat(30);
        factory.setConnectionTimeout(30000);

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String queue = "Email Instructions";     //queue name

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    if (message.equals("send test")) {
                        Send("TEST","\uD83D\uDE4B\uD83C\uDFFC\u200D","kaloianbch@gmail.com");
                    }
                }
            };
            channel.basicConsume(queue, true, consumer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
