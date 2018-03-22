package com.sabbreview.queue;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Creates and maintains the connection to the cloudAMQP server.
 */
public class QueueManager {
  private static final String ENV_AMQP_QUEUE = "CLOUDAMQP_URL";

  private static String queueURL;

  private static Connection CONNECTION;

  /**
   * Retrieves connection to a cloudAMQP server.
   * Creates a new connection if one does not exist in the QueueManager object.
   * @return RabbitMQ connection
   * @throws Exception Throws I/O and Time out exceptions for the connection
   * and a generic exception if there is no CLOUDAMQP_URL in the environment variables.
   */
  public static Connection getConnection() throws Exception {


    if (System.getenv(ENV_AMQP_QUEUE) != null) {
      queueURL = System.getenv(ENV_AMQP_QUEUE);
    } else {
      throw new Exception("No AMQP QueueManager URL Provided");
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
