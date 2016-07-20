package com.lss.rabbitmq.learning.workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Sean.liu on 2016/7/20.
 */
public class Worker {
    public static final String QUEUE_NAME="hello_task";

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.12.113");
        factory.setUsername("user_admin");
        factory.setPassword("password_admin");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    System.out.println(message+":start");
                    doWork(message);
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    System.out.println(message+":Done");
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

    private static void doWork(String task) throws InterruptedException {
        Thread.sleep(10000);
    }
}
