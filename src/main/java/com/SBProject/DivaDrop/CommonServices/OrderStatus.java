package com.SBProject.DivaDrop.CommonServices;

import jakarta.persistence.criteria.CriteriaBuilder;

public enum OrderStatus {
    IN_PROGRSS("In Progress",1),
    ORDER_RECIVED("Order Received",2),
    ORDER_PACKED("Product Packed",3),
    OUT_FOR_DELIVERY("Out for Delivery",4),
    ORDER_DELIVER("Order Delivered",5) ,
    ORDER_CANCELED("Order Canceled",6) ,
    ORDER_SUCCESS("Order placed Successfully",7) ;
    private int id ;
    private String name;

    OrderStatus(String name, Integer id) {
        this.name = name;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer stirng) {
        id = stirng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
