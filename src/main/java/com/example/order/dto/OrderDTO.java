package com.example.order.dto;

import com.example.order.entities.Order;
import com.example.order.entities.OrderItem;
import com.example.order.enums.OrderStatus;

import java.util.Set;

public class OrderDTO {
    private int idOrder;
    private OrderStatus status;
    private double totalCost;
    private int totalAmount;
    private String userName;
    private Set<OrderItem> orderItems;

    public OrderDTO(){
    }

    public OrderDTO(int idOrder){
        this.idOrder = idOrder;
    }

    public OrderDTO(int idOrder, OrderStatus status){
        this.idOrder = idOrder;
        this.status = status;
    }

    public OrderDTO(int idOrder, OrderStatus status, String userName, int totalAmount, double totalCost, Set<OrderItem> ordItem){
        this.idOrder = idOrder;
        this.status = status;
        this.userName = userName;
        this.totalAmount = totalAmount;
        this.totalCost = totalCost;
        this.orderItems = ordItem;
    }

    public OrderDTO(Order order){
        this(order.getIdOrder(), order.getStatus(), order.getUserName(), order.getTotalAmount(), order.getTotalCost(), order.getOrderItems());
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
