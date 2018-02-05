package org.kulingkwi.learning.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;

public class Client {
    private Connection connection;
    private Channel channel;
    private String replyQueueName;
    private QueueingConsumer consumer;
    private boolean flag = true;
    public static final String ENCODING = "UTF-8";

    public Client init() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        connection = factory.newConnection();
        channel = connection.createChannel();
        return this;
    }

    public Client setupConsumer() throws Exception {
        replyQueueName = channel.queueDeclare().getQueue();
        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, false, consumer);
        return this;
    }

    public String call(String message) throws Exception{
        String response = null;
        channel.basicPublish("rpc", "ping", getRequestProperties(), message.getBytes());
        System.out.println("Sent 'ping' RPC call. Waiting for reply....");
        while (flag) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            response = new String(delivery.getBody(), ENCODING);
            break;
        }
        return response;
    }

    public void close() throws Exception {
        flag = false;
        connection.close();
    }

    public String createRequest() throws Exception {
        float epoch = System.currentTimeMillis() / 1000;
        JSONObject jsonObject = new JSONObject();
        return jsonObject.fluentPut("calient_name", "RPC Client").fluentPut("time", Float.toString(epoch)).toString();
    }

    private AMQP.BasicProperties getRequestProperties() {
        return new AMQP.BasicProperties().builder().replyTo(replyQueueName).build();
    }

    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client();
            client.init().setupConsumer();
            String response = client.call(client.createRequest());
            System.out.println("RPC replay..." + response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
