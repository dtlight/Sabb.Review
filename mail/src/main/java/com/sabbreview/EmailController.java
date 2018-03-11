package com.sabbreview;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sabbreview.queue.EmailQueueInstance;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;

import java.io.IOException;

public class EmailController {
  private static final String ENV_DOMAIN = "MAILGUN_DOMAIN";
  private static final String ENV_MAILGUN_API_KEY = "MAILGUN_API_KEY";
  private static final String ENV_MAILGUN_FROM_EMAIL = "MAILGUN_FROM_EMAIL";
  private static final String ENV_MAILGUN_FROM_NAME = "MAILGUN_FROM_NAME";
  private static final String ENV_AMQP_QUEUE = "CLOUDAMQP_URL";
  private static final String queueName = "email";
  private static String domain;
  private static String mailgunApiKey;
  private static String mailgunFromEmail;
  private static String mailgunFromName;
  public static void main(String[] args) throws Exception {

    if (System.getenv() != null) {
      domain = System.getenv(ENV_DOMAIN);
    } else {
      throw new Exception("No Domain Provided");
    }

    if (System.getenv(ENV_MAILGUN_API_KEY) != null) {
      mailgunApiKey = System.getenv(ENV_MAILGUN_API_KEY);
    } else {
      throw new Exception("No MailGun Key Provided");
    }

    if (System.getenv(ENV_MAILGUN_FROM_NAME) != null) {
      mailgunFromName = System.getenv(ENV_MAILGUN_FROM_NAME);
    } else {
      throw new Exception("No MailGun From Name Provided");
    }

    if (System.getenv(ENV_MAILGUN_FROM_EMAIL) != null) {
      mailgunFromEmail = System.getenv(ENV_MAILGUN_FROM_EMAIL);
    } else {
      throw new Exception("No MailGun From Email Provided");
    }
    EmailQueueInstance emailQueueInstance = new EmailQueueInstance();
    emailQueueInstance.addConsumer(new DefaultConsumer(emailQueueInstance.getChannel()) {
      @Override public void handleDelivery(String consumerTag, Envelope envelope,
          AMQP.BasicProperties properties, byte[] body) throws IOException {
        Email currentEmail;
        String[] message = new String(body, "UTF-8").split("/");
        if (message.length < 3) {
          //TODO
        } else {
          currentEmail = Email.emailNameToEnum(message[0]);
          Send(currentEmail.getTitle(), currentEmail.generateHTML(message[1]), message[2]);
        }
      }
    });
  }

  /**
   * Sends an email using a MailGun SMTP server and sargue's mailgun library
   *
   * @param subject   Subject name of the email being sent.
   * @param content   Content of the email.
   * @param recipient Recipient's email address.
   */
  private static void Send(String subject, String content, String recipient) {
    Configuration configuration = null;     //Sender details
    configuration =
        new Configuration().domain(domain).apiKey(mailgunApiKey)  //MailGun API key read from file
            .from(mailgunFromName, mailgunFromEmail);

    Mail.using(configuration).to(recipient).subject(subject).html(content).build().send();
  }
}
