package org.kulingkwi.learning.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListener;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.UUID;

public class Consumer {

    private DefaultMQPushConsumer consumer;

    private MessageListenerConcurrently listener;

    private String nameServer;

    private String groupName;

    private String topic;

    public Consumer(String nameServer, String groupName, String topic) {
        this.nameServer = nameServer;
        this.groupName = groupName;
        this.topic = topic;
    }

    public void init() {
        consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameServer);
        try {
            consumer.subscribe(topic, "*");
        }catch (Exception e) {
            e.printStackTrace();
        }
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                if (msgs != null) {
                    for (MessageExt m : msgs) {
                        System.out.println(m);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        try {
            consumer.start();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void destroy() {
        this.consumer.shutdown();
    }

    public static void main(String[] args) {
        String nameServer = "127.0.0.1:9876";
        String topic = "test";
        String groupName = "consumer";
        Consumer consumer = new Consumer(nameServer, groupName, topic);
        consumer.init();

        consumer.destroy();
    }

}
