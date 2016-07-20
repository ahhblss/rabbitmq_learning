package com.lss.rabbitmq.learning.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Sean.liu on 2016/7/20.
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv)
            throws java.io.IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.12.113");
        factory.setUsername("user_admin");
        factory.setPassword("password_admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        boolean stopFlag = false;
        while (!stopFlag) {
            String message = in.readLine();
            if (message.equals("quit")){
                stopFlag =true;
            }else {
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
                System.out.println("Sent '" + message + "'");
            }

        }
        channel.close();
        connection.close();
    }
}
