package com.example.order.controllers;

import com.example.order.dto.ItemAdditionParametersDTO;
import com.example.order.dto.OrderDTO;
import com.example.order.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> list() {
        return ResponseEntity.ok().body(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> orderById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(service.orderById(id));
        } catch (InvalidParameterException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addition")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderDTO> create(@RequestParam(required = false) String id, @RequestParam String username, @RequestBody ItemAdditionParametersDTO item) {
        try {
            return ResponseEntity.ok().body(service.create(id, username, item));
        } catch (InvalidParameterException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cost/{id}")
    public ResponseEntity<OrderDTO> totalCost(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.totalCost(id));
    }

    @PutMapping("{orderID}/status/{status}")
    public ResponseEntity<OrderDTO> changeStatus (@PathVariable Integer orderID, @PathVariable String status) {
        try {
            return ResponseEntity.ok().body(service.changeStatus(orderID, status));
        } catch (InvalidParameterException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    //DEPRECATED МЕТОДЫ НИЖЕ
    /*@GetMapping("/name")
    public String name(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        return name;
    }

    @GetMapping("/print/order")
    public Iterable<Order> print() {
        return orderRepo.findAll();
    }

    @GetMapping("/print/oi")
    public Iterable<OrderItem> printOI() {
        return orderItemRepo.findAll();
    }

    @GetMapping("/add/order")
    public Iterable<Order> addOrder() {
        orderRepo.save(new Order(15.5, 5, "Ivan", new OrderItem(1, 2, 1, "1")));
        return orderRepo.findAll();
    }

    @GetMapping("/add/oi")
    public Iterable<OrderItem> addOI() {
        orderItemRepo.save(new OrderItem(0, 0, 1, "1"));
        return orderItemRepo.findAll();
    }

    @GetMapping("/delete")
    public String delete() {
        orderRepo.deleteAll();
        orderItemRepo.deleteAll();
        return "Repos was deleted";
    }

    @GetMapping("/add/item")
    public Iterable<Order> addItem() {
        OrderItem oi = new OrderItem(5, 4, 1, "1");
        orderRepo.save(orderRepo.findAll().get(1).addItem(oi));
        orderRepo.save(orderRepo.findAll().get(0).addItem(oi));
        return print();
    }*/
}