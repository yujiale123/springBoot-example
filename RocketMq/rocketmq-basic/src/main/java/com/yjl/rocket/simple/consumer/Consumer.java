package com.yjl.rocket.simple.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Set;

/**
 * @program: springboot-example
 * @author: yjl
 * @created: 2021/12/31
 * <p>
 * 消费要点：
 * 1、消费方式（拉取和推送）
 * 2、消费的模式（集群和广播）
 * 3、流量控制
 * 4、并发线程的设置
 * 5、消息的过滤（Tag、key）
 */
public class Consumer {
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        // 消息的拉取
        DefaultMQPullConsumer pullConsumer = new DefaultMQPullConsumer();

        // 消费的模式：集群
        pullConsumer.setMessageModel(MessageModel.CLUSTERING);
        // 消费的模式：广播
        pullConsumer.setMessageModel(MessageModel.BROADCASTING);


        final Set<MessageQueue> messageQueues = pullConsumer.fetchSubscribeMessageQueues("tp_demo_05");

        for (MessageQueue messageQueue : messageQueues) {
            // 指定消息队列，指定标签过滤的表达式，消息偏移量和单次最大拉取的消息个数
            pullConsumer.pull(messageQueue, "TagA||TagB", 0L, 100);
        }


        // 消息的推送
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer();

        pushConsumer.setMessageModel(MessageModel.BROADCASTING);
        pushConsumer.setMessageModel(MessageModel.CLUSTERING);

        // 设置消费者的线程数
        pushConsumer.setConsumeThreadMin(1);
        pushConsumer.setConsumeThreadMax(10);

        // subExpression表示对标签的过滤：
        // TagA||TagB|| TagC    *表示不对消息进行标签过滤
        pushConsumer.subscribe("tp_demo_05", "*");

        // 设置消息批处理的一个批次中消息的最大个数
        pushConsumer.setConsumeMessageBatchMaxSize(10);

        // 在设置完之后调用start方法初始化并运行推送消息的消费者
        pushConsumer.start();
    }
}
