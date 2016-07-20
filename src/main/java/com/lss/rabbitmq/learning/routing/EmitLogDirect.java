package com.lss.rabbitmq.learning.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Sean.liu on 2016/7/20.
 */
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv)
            throws java.io.IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.12.113");
        factory.setUsername("user_admin");
        factory.setPassword("password_admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        boolean stopFlag = false;
        while (!stopFlag) {
            String message = in.readLine();
            if (message.equals("quit")){
                stopFlag =true;
            }else {
                String[] payLoad = message.split(" ");
                String severity = payLoad[0];
                String messageContent = payLoad[1];
                channel.basicPublish(EXCHANGE_NAME, severity, null, messageContent.getBytes());
                System.out.println(" [x] Sent '" + severity + "':'" + messageContent + "'");
            }
        }
        channel.close();
        connection.close();
    }

}
