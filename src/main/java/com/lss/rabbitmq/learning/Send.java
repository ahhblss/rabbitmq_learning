package com.lss.rabbitmq.learning;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by Sean.liu on 2016/7/19.
 */
public class Send {

    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.12.113");
        factory.setUsername("user_admin");
        factory.setPassword("password_admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        * @param queue the name of the queue
//        * @param durable true if we are declaring a durable queue (the queue will survive a server restart)
//        * @param exclusive true if we are declaring an exclusive queue (restricted to this connection)
//        * @param autoDelete true if we are declaring an autodelete queue (server will delete it when no longer in use)
//        * @param Map<String, Object>  arguments other properties (construction arguments) for the queue
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World only one!";

//        * @param exchange the exchange to publish the message to
//        * @param routingKey the routing key
//        * @param props other properties for the message - routing headers etc
//        * @param body the message body

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
