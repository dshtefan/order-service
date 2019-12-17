package com.example.order.entities;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idOrderItem;
    @Column(name = "id_item")
    private int idItem;
    @Column(name = "amount")
    private int amount;
    @Column(name = "price")
    private double price;
    @Column(name = "name")
    private String name;

    public OrderItem() {
    }

    public OrderItem(int idItem, int amount, double price, String name) {
        this.idItem = idItem;
        this.amount = amount;
        this.price = price;
        this.name = name;
    }

    public Integer getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(Integer idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    public int getId() {
        return idOrderItem;
    }

    public void setId(int id) {
        this.idOrderItem = id;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addOne() {
        this.amount++;
    }

    public void add(int amount) {
        this.amount += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        return this.idItem == orderItem.idItem;
    }

    @Override
    public int hashCode() {
        return idItem;
    }
}
