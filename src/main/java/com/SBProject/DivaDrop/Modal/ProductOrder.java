package com.SBProject.DivaDrop.Modal;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderId;
    private LocalDate orderDate;

    @ManyToOne
    private Product product;
    private Double price;
    private Integer quantity;
    @ManyToOne
    private User user;

    private String status;
    private String paymentType;

    @OneToOne(cascade = CascadeType.ALL)
    private  OrderAddress orderAddress;
    public ProductOrder(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public OrderAddress getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(OrderAddress orderAddress) {
        this.orderAddress = orderAddress;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", product=" + product +
                ", price=" + price +
                ", quantity=" + quantity +
                ", user=" + user +
                ", status='" + status + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", orderAddress=" + orderAddress +
                '}';
    }
}
