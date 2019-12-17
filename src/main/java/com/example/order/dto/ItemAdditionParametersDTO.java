package com.example.order.dto;

import com.example.order.entities.OrderItem;

public class ItemAdditionParametersDTO {
    private int idItem;
    private String name;
    private int amount;
    private double price;

    public ItemAdditionParametersDTO(int idItem, String name, int amount, double price) {
        this.idItem = idItem;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public OrderItem toOrderItem() {
        return new OrderItem(idItem, amount, price, name);
    }

    @Override
    public String toString() {
        return "ItemAdditionParametersDTO{" +
                "idItem=" + idItem +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
