package com.example.order.entities;

import javax.persistence.*;

@Entity
@Table(name="order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idOrderItem;
    @Column(name = "id_item")
    private int idItem;
    @Column(name = "amount")
    private int amount;

    public OrderItem() {
    }

    public OrderItem(int idItem, int amount) {
        this.idItem = idItem;
        this.amount = amount;
    }

    public Integer getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(Integer idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    public OrderItem(Order order, int idItem, int amount) {
        this.idItem = idItem;
        this.amount = amount;
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

    public void addOne() { this.amount++; }

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
