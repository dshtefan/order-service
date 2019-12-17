package com.example.order.controllers;

import com.example.order.dto.ItemAdditionParametersDTO;
import com.example.order.dto.OrderDTO;
import com.example.order.entities.Order;
import com.example.order.entities.OrderItem;
import com.example.order.repos.OrderItemRepo;
import com.example.order.repos.OrderRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public OrderController(OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @GetMapping
    public List<OrderDTO> list() {
        List<OrderDTO> result = new LinkedList<>();
        orderRepo.findAll().forEach(order -> result.add(order.toDTO()));
        return result;
    }

    @GetMapping("/{id}")
    public OrderDTO orderById(@PathVariable Integer id) {
        return orderRepo.getOne(id).toDTO();
    }

    @PostMapping("/addition")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO create(
            @RequestParam(required = false) String id,
            @RequestBody ItemAdditionParametersDTO item,
            @RequestParam String username) {
        try {
            int idOrder = Integer.parseInt(id);
            return orderRepo.save(orderRepo
                    .getOne(idOrder)
                    .addItem(item.toOrderItem()))
                    .toDTO();

        } catch (NumberFormatException e) {
            System.out.println("dwqwdqwd");
            return orderRepo
                    .save(new Order(username, item.toOrderItem()))
                    .toDTO();

        }
    }



    @GetMapping("/name")
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
    }
}