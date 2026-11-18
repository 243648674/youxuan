package com.xiangmu.youxuan.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMqConfig {
    public static final String VOUCHER_RECEIVE_QUEUE = "voucher.receive.queue";

    @Bean
    public Queue voucherReceiveQueue() {
        return new Queue(VOUCHER_RECEIVE_QUEUE, true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter("com.xiangmu.youxuan.mq");
    }
}