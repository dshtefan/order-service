package com.example.order.enums;

import java.util.EnumSet;

public enum OrderStatus {
    COLLECTING {
        @Override
        public EnumSet<OrderStatus> nextState(){
            return EnumSet.of(OrderStatus.PAYED, OrderStatus.FAILED, OrderStatus.CANCELLED);
        }
    },
    PAYED {
        @Override
        public EnumSet<OrderStatus> nextState(){
            return EnumSet.of(OrderStatus.SHIPPING, OrderStatus.CANCELLED);
        }
    },
    SHIPPING {
        @Override
        public EnumSet<OrderStatus> nextState(){
            return EnumSet.of(OrderStatus.COMPLETE);
        }
    },
    COMPLETE {
        @Override
        public EnumSet<OrderStatus> nextState() {
            return EnumSet.of(this);
        }
    },
    FAILED {
        @Override
        public EnumSet<OrderStatus> nextState() {
            return EnumSet.of(this);
        }
    },
    CANCELLED {
        @Override
        public EnumSet<OrderStatus> nextState() {
            return EnumSet.of(OrderStatus.PAYED);
        }
    };

    public abstract EnumSet<OrderStatus> nextState();
}
