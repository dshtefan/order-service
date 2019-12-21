package com.example.order.controllers;

import com.example.order.dto.ItemAdditionParametersDTO;
import com.example.order.dto.OrderDTO;
import com.example.order.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService service) {
        this.service = service;
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