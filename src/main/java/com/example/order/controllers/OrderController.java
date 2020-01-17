package com.example.order.controllers;

import com.example.order.dto.ItemAdditionParametersDTO;
import com.example.order.dto.OrderDTO;
import com.example.order.services.OrderService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService service, RabbitTemplate rabbitTemplate) {
        this.service = service;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> list() {
        logger.info("---- got list request");
        return ResponseEntity.ok().body(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> orderById(@PathVariable Integer id) {
        logger.info("---- got orderById request; id = {}", id);
        try {
            return ResponseEntity.ok().body(service.orderById(id));
        } catch (InvalidParameterException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/add")
    public void add() {
        logger.info("---- got add request");
        JSONObject json = new JSONObject(); // { orderID, username, Item: {amount, price, id, name} }
        JSONObject item = new JSONObject();
        json.put("orderID", "3");
        json.put("username", "denis");
        item.put("amount", 1);
        item.put("price", 2.0);
        item.put("id", "213d");
        item.put("name", "item");
        json.put("Item", item);
        rabbitTemplate.convertAndSend("WarehouseReserveItemsExchange", "whKey", json.toString());
    }

    @PostMapping("/addition")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderDTO> addItem(@RequestParam(required = false) String id, @RequestParam String username, @RequestBody ItemAdditionParametersDTO item) {
        logger.info("---- got addItem request; id = {} username = {} item = {}", id, username, item);
        try {
            return ResponseEntity.ok().body(service.addItem(id, username, item));
        } catch (InvalidParameterException e) {
            logger.error("InvalidParameterException in addItem()");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{orderID}/status/{status}")
    public ResponseEntity<OrderDTO> changeStatus(@PathVariable Integer orderID, @PathVariable String status) {
        logger.info("---- got changeStatus request; orderID = {} status = {}", orderID, status);
        try {
            return ResponseEntity.ok().body(service.changeStatus(orderID, status));
        } catch (InvalidParameterException e){
            logger.error("InvalidParameterException in changeStatus()");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Exception in changeStatus()");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}