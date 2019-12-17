package com.example.order.controllers;

import com.example.order.entities.Order;
import com.example.order.entities.OrderItem;
import com.example.order.repos.OrderItemRepo;
import com.example.order.repos.OrderRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public OrderController(OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @GetMapping("/name")
    public String name(@RequestParam(name="name", required=false, defaultValue="World") String name) {
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
        orderRepo.save(new Order(15.5, 5, "Ivan", new OrderItem(1, 2)));
        return orderRepo.findAll();
    }

    @GetMapping("/add/oi")
    public Iterable<OrderItem> addOI() {
        orderItemRepo.save(new OrderItem(0, 0));
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
        orderRepo.save(orderRepo.findAll().get(1).addItem(new OrderItem(5, 4)));
        return print();
    }
}