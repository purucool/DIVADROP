package com.SBProject.DivaDrop.Modal;

import jakarta.persistence.*;

@Entity
@Table(name = "Product", uniqueConstraints = @UniqueConstraint(columnNames = "category_name"))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;

    @Column(name = "product_name", unique = true,length=500)
    private String productName;


    @Column(name = "product_desc", unique = true,length=5000)
    private String description;

    @Column(name = "product_price")
    private Double price;

    @Column(name = "product_discount")
    private int discount;

    @Column(name = "product_qty")
    private int quantity;

    @Column(name = "product_img_name")
    private String ImgName;
    private String category;

    @Column(name = "is_active")
    private  Boolean isActive;

//    @OneToOne
//    @JoinColumn(name = "id")//foriegn key
//    private Category category;



    public Product() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImgName() {
        return ImgName;
    }

    public void setImgName(String imgName) {
        ImgName = imgName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        this.isActive = active;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", quantity=" + quantity +
                ", ImgName='" + ImgName + '\'' +
                ", category='" + category + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
