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

    private void addItemCancelling(ItemAdditionParametersDTO item) {
        System.out.println("addItemCancelling");
        net.minidev.json.JSONObject json = new net.minidev.json.JSONObject();
        json.put("id", item.getIdItem());
        json.put("amount", item.getAmount());
        rabbitTemplate.convertAndSend("WarehouseReserveItemsExchange2", "whcKey", json);
    }

    @RabbitListener(queues = {"paymentStatus"})
    @Transactional
    public void receiveMessageFromPayment(String message) {
        try {
            System.out.println(message);
            JSONObject json = new JSONObject(message);
            Integer idOrder = json.getInt("idOrder");
            String status = json.getString("cartAuth");
            if (status.toUpperCase().equals("AUTHORIZED")) {
                System.out.println(idOrder + " " + "PAYED");
                changeStatus(idOrder, "PAYED");
            } else {
                System.out.println(idOrder + " " + "FAILED");
                changeStatus(idOrder, "FAILED");
                orderRepo
                        .findById(idOrder)
                        .orElseThrow(InvalidParameterException::new)
                        .getOrderItems()
                        .forEach(item -> {
                            addItemCancelling(new ItemAdditionParametersDTO(item.getIdItem(), item.getAmount()));
                        });
            }
        } catch (Exception e) {
        }
    }

    @RabbitListener(queues = {"WarehouseQueueReserveItems"})
    @Transactional
    public void receiveMessageFromWarehouse(String message) {
        try {
            System.out.println(message);
            JSONObject json = new JSONObject(message);
            Map<String, Object> map = json.toMap();
            JSONObject item = json.getJSONObject("Item");
            System.out.println((String) map.get("orderID"));
            String orderID = (String) map.get("orderID");
            String username = (String) map.get("username");
            int amount = item.getInt("amount");
            double price = item.getDouble("price");
            String id = item.getString("id");
            String name = item.getString("name");
            System.out.println("'"+orderID+"'");
            addItem(orderID, username, new ItemAdditionParametersDTO(id, name, amount, price));
        } catch (Throwable e) {
            JSONObject json = new JSONObject(message);
            JSONObject item = json.getJSONObject("Item");
            String id = item.getString("id");
            int amount = item.getInt("amount");
            addItemCancelling(new ItemAdditionParametersDTO(id, amount));
        }
    }

    public List<OrderDTO> list() {
        List<OrderDTO> result = new LinkedList<>();
        orderRepo.findAll().forEach(order -> result.add(order.toDTO()));
        return result;
    }

    public OrderDTO orderById(Integer id) throws InvalidParameterException {
        return orderRepo.findById(id).orElseThrow(InvalidParameterException::new).toDTO();
    }

    public OrderDTO addItem(String id, String username, ItemAdditionParametersDTO item) throws InvalidParameterException {
        try {
            System.out.println(item);
            int idOrder = Integer.parseInt(id);
            System.out.println(orderRepo.findById(1).orElse(new Order()));
            return orderRepo.save(
                    orderRepo.findById(idOrder).orElseThrow(InvalidParameterException::new).addItem(item.toOrderItem())).toDTO();
        } catch(NullPointerException | NumberFormatException e) {
            return orderRepo.save(new Order(username, item.toOrderItem())).toDTO();
        }
    }

    public OrderDTO changeStatus(Integer orderID, String status) throws Exception {
        Order order = orderRepo.findById(orderID).orElseThrow(InvalidParameterException::new);
        if (order.getStatus().nextState().contains(OrderStatus.valueOf(status.toUpperCase())))
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        else
            throw new Exception();
        orderRepo.save(order);
        return order.toDTO();
    }
}
