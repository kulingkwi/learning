package org.kulingkwi.learning.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;

public class Server {

    private Connection connection;
    private Channel channel;
    private QueueingConsumer consumer;
    private boolean flag = true;
    private static final String ENCODING = "UTF-8";

    public Server server() {
        return this;
    }

    public Server init() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        connection = factory.newConnection();
        channel.exchangeDeclare("rpc", "direct");
        channel.queueDeclare("ping", false, false, false, null);
        channel.queueBind("ping", "rpc", "ping");

        consumer = new QueueingConsumer(channel);
        channel.basicConsume("ping", false, "ping", consumer);

        System.out.println(
                "Waiting for RPC calls"
        );

        return this;
    }

    public void closeConnectin() {
        flag = false;
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }
    }

    public void handle() {
        while (flag) {
            try{
               QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                BasicProperties props = delivery.getProperties();
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                System.out.println("Received API call...replying...");
                channel.basicPublish("", props.getReplyTo(), null, getResponse(delivery).getBytes(ENCODING));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getResponse(QueueingConsumer.Delivery delivery) {
        String response = null;
        try{
            String message = new String(delivery.getBody(), ENCODING);
            JSONObject jsonObject = JSON.parseObject(message);
            response = "Pong!" + jsonObject.getString("time");
        } catch (Exception e) {
            e.printStackTrace();
            response = "";
        }
        return response;

    }

    public static void main(String[] args) {
        Server server = null;
        try {
            server = new Server();
            server.init().server();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null ){
                server.closeConnectin();
            }
        }
    }

}
