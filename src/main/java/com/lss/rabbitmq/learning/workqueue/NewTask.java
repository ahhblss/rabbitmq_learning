package com.lss.rabbitmq.learning.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Sean.liu on 2016/7/20.
 */
public class NewTask {
    public static final String QUEUE_NAME="hello_task";

    public static void main(String[] args) throws IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.12.113");
        factory.setUsername("user_admin");
        factory.setPassword("password_admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        boolean stopFlag = false;
        while (!stopFlag){
            String message =in.readLine();
            if (message.equals("quit")){
                stopFlag =true;
            }else {
                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println(" NewTask: '" + message + "'");
            }
        }
        channel.close();
        connection.close();
    }
}
