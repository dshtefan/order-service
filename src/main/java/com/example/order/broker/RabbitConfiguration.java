package com.example.order.broker;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Bean
    public Declarables exchanges() {
//        DirectExchange statusQueue = new DirectExchange("StatusQueue");
        DirectExchange warehouseQueueReserveItems = new DirectExchange("WarehouseQueueReserveItemsExchange");
        DirectExchange warehouseQueueReserveItemsCancelled = new DirectExchange("WarehouseReserveItemsExchange2", true, false);
        Queue paymentStatus = new Queue("paymentStatus", false);
        Queue warehouseItem = new Queue("WarehouseQueueReserveItems", false);
        Queue warehouseCancelled = new Queue("WarehouseQueueReserveItemsCancelled", false);

        return new Declarables(
                paymentStatus,
                warehouseItem,
                warehouseCancelled,
//                statusQueue,
                warehouseQueueReserveItems,
                warehouseQueueReserveItemsCancelled,
//                BindingBuilder.bind(paymentStatus).to(statusQueue).with("pmKey"),
                BindingBuilder.bind(warehouseItem).to(warehouseQueueReserveItems).with("whKey"),
                BindingBuilder.bind(warehouseCancelled).to(warehouseQueueReserveItemsCancelled).with("whcKey")
        );
    }
}
