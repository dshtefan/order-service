package com.example.order.broker;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Bean
    public Declarables exchanges() {
        DirectExchange statusQueue = new DirectExchange("StatusQueue", true, false);
        DirectExchange warehouseQueueReserveItems = new DirectExchange("WarehouseQueueReserveItems", true, false);
        DirectExchange warehouseQueueReserveItemsCancelled = new DirectExchange("WarehouseQueueReserveItemsCancelled", true, false);
        Queue paymentStatus = new Queue("paymentStatus", false);
        Queue warehouseItem = new Queue("warehouseItem", false);
        Queue warehouseCancelled = new Queue("warehouseCancelled", false);

        return new Declarables(
                paymentStatus,
                warehouseItem,
                warehouseCancelled,
                statusQueue,
                warehouseQueueReserveItems,
                warehouseQueueReserveItemsCancelled,
                BindingBuilder.bind(paymentStatus).to(statusQueue).with("pmKey"),
                BindingBuilder.bind(warehouseItem).to(warehouseQueueReserveItems).with("whKey"),
                BindingBuilder.bind(warehouseCancelled).to(warehouseQueueReserveItemsCancelled).with("whcKey")
        );
    }
}
