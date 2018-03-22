package com.sabbreview.queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import java.io.IOException;

public class EmailQueueInstance {
  private static final String queueName = "email";
  private static Channel channel;
  public EmailQueueInstance() throws Exception {
    if(channel == null) {
      channel = com.sabbreview.Queue.getConnection().createChannel();
      channel.queueDeclare(queueName, true, false, false, null);
    }
  }

  public void addConsumer(Consumer consumer) throws IOException {
    channel.basicConsume(queueName, true, consumer);
  }

  public void publish(String message) throws IOException {
    channel.basicPublish("", queueName, null, message.getBytes());
  }

  public Channel getChannel() {
    return channel;
  }
}
