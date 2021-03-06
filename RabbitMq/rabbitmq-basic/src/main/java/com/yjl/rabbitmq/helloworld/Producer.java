package com.yjl.rabbitmq.helloworld;

import com.yjl.rabbitmq.utils.RabbitConstant;
import com.yjl.rabbitmq.utils.RabbitUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author yjl
 * <p>
 * rabbit简单模式
 *
 * producer  -》  queue  -》 consumer
 * 连接rabbit并发送消息
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        //获取TCP长连接
        Connection conn = RabbitUtils.getConnection();
        //创建通信“通道”，相当于TCP中的虚拟连接
        Channel channel = conn.createChannel();
        /**
         * 创建队列,声明并创建一个队列，如果队列已存在，则使用这个队列
         * 第一个参数：队列名称ID
         * 第二个参数：是否持久化，false对应不持久化数据，MQ停掉数据就会丢失
         * 第三个参数：是否队列私有化，false则代表所有消费者都可以访问，true代表只有第一次拥有它的消费者才能一直使用，其他消费者不让访问
         * 第四个：是否自动删除,false代表连接停掉后不自动删除掉这个队列
         * 其他额外的参数, null
         */
        channel.queueDeclare(RabbitConstant.QUEUE_HELLOWORLD, false, false, false, null);

        String message = "hello ranbbit 666";
        /**
         *  exchange 交换机
         *   queueName 队列名称
         *  Properties 额外的设置属性
         *  message[] 最后一个参数是要传递的消息字节数组
         */
        channel.basicPublish("", RabbitConstant.QUEUE_HELLOWORLD, null, message.getBytes());
        //关闭channel通道
        channel.close();
        //关闭TCP长连接
        conn.close();
        System.out.println("===发送成功===");

    }
}
