package com.sabbreview.controller;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EmailController{

    /**
     * Sends an email using a MailGun SMTP server and sargue's mailgun library
     * @param subject Subject name of the email being sent.
     * @param content Content of the email.
     * @param recipient Recipient's email address.
     */
    public static void Send(String subject, String content, String recipient) {

        Configuration configuration = null;     //Sender details
        try{
            configuration = new Configuration()
                    .domain("sabb.review")
                    .apiKey(loadFile("API-Key.txt"))  //MailGun API key read from file
                    .from("sabbbot", "postmaster@sabb.review");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Mail.using(configuration)
                .to(recipient)
                .subject(subject)
                .html(content)
                .build()
                .send();
    }

    /**
     * Continuously attempts to pull messages from the RabbitMQ server. Upon receiving specific messages it will call the
     * Send() and pass it an email address received with the message, a template for the body of the email being sent and
     * an appropriate subject.<br><br>
     * Message format is <i>NotificationID/Name/Email</i>.<br>
     * <i>NotificationID</i> determines the contents of the email.<br>
     * <i>Name</i> is the name of the recipient.<br>
     * <i>Email</i> is the email address of the recipient.<br>
     */
    public static void ReceiveFromQueue() throws Exception{

        //Server URL read from a file
        //String uri = loadFile("RabbitMQ_URL.txt");
        String uri = "amqp://jprtmyha:Dd4MTFz_us4HC473xZypPXp4Aco86Zt0@llama.rmq.cloudamqp.com/jprtmyha";


        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);

        //Recommended settings
        factory.setRequestedHeartbeat(30);
        factory.setConnectionTimeout(30000);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queue = "Email Instructions";     //queue name

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String[] message = new String(body, "UTF-8").split("/");
                if (message.length != 3) {
                    //TODO
                } else if (message[0].equals("sendTest")) {
                    Send("TEST", "\uD83D\uDE4B\uD83C\uDFFC\u200D This is a test email. Feel free to " +
                            "delete it", message[2]);
                } else if (message[0].equals("applicationCreation")) {
                    Send("Application Created",generateEmailHTML(message[0],message[1]) ,message[2]);
                } else if (message[0].equals("loremIpsum")) {
                    Send("Lorem",generateEmailHTML(message[0],message[1]) ,message[2]);
                } else if (message[0].equals("sensitiveNotification")) {
                    Send("Notification",generateEmailHTML(message[0],message[1]) ,message[2]);
                }
            }
        };
        channel.basicConsume(queue, true, consumer);
    }

    /**
     * Generates a html for use in an email notifications.
     * @param notificationID Name of the text file in /emails/text/, *without* extension.
     * @param name Name to put at the top of the email. (e.g "Dear Alex,")
     */
    private static String generateEmailHTML(String notificationID, String name){
        try {

            String html = loadFile("emails\\notificationTemplate.html");

            //Adding message+name to email
            html = html.replaceFirst("\\{body}", loadFile("emails\\text\\" + notificationID + ".txt"));
            html = html.replaceFirst("\\{name}", name);

            return html;
        }
        catch( IOException e ){
            e.printStackTrace();
            return "IOException while generating email HTML!<br><br>" + e.toString() + "<br><br>";
        }
    }

    /**
     * Reads contents of a file stored in the 'static' folder.
     * @param filePath The path of the file to load relative to the 'static' folder, including the file extension
     * @return The contents of the file, including line breaks.
     * @throws IOException If something goes wrong reading the given file.
     */
    public static String loadFile(String filePath) throws IOException{

        //Removing leading slash if present
        if( filePath.charAt(0) == '/' || filePath.charAt(0) == '\\' ){
            filePath = filePath.substring( 1, filePath.length() - 1);
        }

        String envPath = new File("").getAbsolutePath() + "\\target\\classes\\static\\";

        BufferedReader bf = new BufferedReader(new FileReader(envPath + filePath));

        String line = "";
        StringBuilder text = new StringBuilder();

        while((line = bf.readLine())!= null){
            text.append(line);
            text.append("\n");
        }


        bf.close();

        //Removing final newline (which is added even if there isn't one in the file)
        return text.substring(0, text.length() - 1);
    }
}
