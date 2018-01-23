package org.kulingkwi.learning.rocketmq;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

import java.util.UUID;

public class Producer {

    private DefaultMQProducer sender;

    protected String nameServer;

    protected String groupName;

    protected String topic;

    public Producer(String nameServer, String groupName, String topic) {
        this.nameServer = nameServer;
        this.groupName = groupName;
        this.topic = topic;
    }

    public void init() {
        sender = new DefaultMQProducer(groupName);
        sender.setNamesrvAddr(nameServer);
        sender.setInstanceName(UUID.randomUUID().toString());
        try {
            sender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        message.setTopic(topic);
        try {
            SendResult result = sender.send(message);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        this.sender.shutdown();
    }

    public static void main(String[] args) {
        String nameServer = "127.0.0.1:9876";
        String topic = "test";
        String groupName = "producer";
        Producer producer = new Producer(nameServer, groupName, topic);
        producer.init();

        for (int i = 0; i < 100; i ++) {
            Message message = new Message();
            String m = "test message " + i;
            message.setBody(m.getBytes());
            producer.send(message);
        }
        producer.destroy();
    }


}
