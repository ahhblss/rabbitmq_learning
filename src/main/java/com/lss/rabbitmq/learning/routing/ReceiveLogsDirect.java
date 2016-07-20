package com.lss.rabbitmq.learning.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Sean.liu on 2016/7/20.
 */
public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.12.113");
        factory.setUsername("user_admin");
        factory.setPassword("password_admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        String[] severities = new String[]{"info","error","warn"};

        String severity = severities[new Random().nextInt(3)];
        channel.queueBind(queueName, EXCHANGE_NAME, severity);
        System.out.println(severity+": [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
