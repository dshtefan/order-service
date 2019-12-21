package com.example.order.services;

import com.example.order.dto.ItemAdditionParametersDTO;
import com.example.order.dto.OrderDTO;
import com.example.order.entities.Order;
import com.example.order.enums.OrderStatus;
import com.example.order.repos.OrderRepo;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.InvalidAttributeValueException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class OrderService {

    private final RabbitTemplate rabbitTemplate;
    private final OrderRepo orderRepo;

    public OrderService(OrderRepo orderRepo, RabbitTemplate rabbitTemplate) {
        this.orderRepo = orderRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = {"paymentStatus"})
    public void receiveMessageFromPayment(String message) throws InvalidAttributeValueException {
        System.out.println(message);
    }

    @RabbitListener(queues = {"WarehouseQueueReserveItemsCancelled"})
    public void receiveMessageFromWarehouse(String message) throws InvalidAttributeValueException {
        /*JSONObject json = new JSONObject(message);
        Map<String, Object> map = json.toMap();
        String ordstat = (String) map.get("status");
        int orderID = (Integer) map.get("orderID");
        changeStatus(orderID, ordstat);*/
        System.out.println(message);
    }

    public List<OrderDTO> list() {
        JSONObject jo = new JSONObject();
        JSONObject jo2 = new JSONObject();
        jo.put("id", "1");
        jo.put("username", "Denis");
        jo2.put("idItem", "1");
        jo2.put("name", "dhbwehdb");
        jo2.put("amount", 5);
        jo2.put("idItem", 1231.34);
        jo.put("item", jo2);
        rabbitTemplate.convertAndSend("WarehouseReserveItemsExchange2", "whKey", jo.toString());
        List<OrderDTO> result = new LinkedList<>();
        orderRepo.findAll().forEach(order -> result.add(order.toDTO()));
        return result;
    }

    public OrderDTO orderById(Integer id) throws InvalidParameterException {
        return orderRepo.findById(id).orElseThrow(InvalidParameterException::new).toDTO();
    }


    public OrderDTO addItem(String id, String username, ItemAdditionParametersDTO item) throws InvalidParameterException {
        try {
            int idOrder = Integer.parseInt(id);
            return orderRepo.save(
                    orderRepo.findById(idOrder).orElseThrow(InvalidParameterException::new).addItem(item.toOrderItem())
            ).toDTO();

        } catch (NumberFormatException e) {
            return orderRepo.save(new Order(username, item.toOrderItem())).toDTO();
        }
    }

    @Transactional
    public OrderDTO changeStatus (Integer orderID, String status) throws Exception {
        Order order = orderRepo.findById(orderID).orElseThrow(InvalidParameterException::new);
        if (order.getStatus().nextState().contains(OrderStatus.valueOf(status.toUpperCase())))
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        else
            throw new Exception();
        orderRepo.save(order);
        return order.toDTO();
    }
}
