package com.sabbreview.queue;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Queue {
  private static final String ENV_AMQP_QUEUE = "CLOUDAMQP_URL";

  private static String queueURL;

  private static Connection CONNECTION;

  public static Connection getConnection() throws Exception {


    if (System.getenv(ENV_AMQP_QUEUE) != null) {
      queueURL = System.getenv(ENV_AMQP_QUEUE);
    } else {
      throw new Exception("No AMQP Queue URL Provided");
    }

    if(CONNECTION == null) {
      String uri = queueURL;
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUri(uri);

      //Recommended settings
      factory.setRequestedHeartbeat(30);
      factory.setConnectionTimeout(30000);
      try {
        CONNECTION = factory.newConnection();
      } catch (IOException | TimeoutException e) {
        e.printStackTrace();
      }
    }

    return CONNECTION;
  }
}
