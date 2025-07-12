package com.SBProject.DivaDrop.Modal;


import jakarta.persistence.*;

@Entity
@Table(name = "category", uniqueConstraints = @UniqueConstraint(columnNames = "category_name"))
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_name", unique = true)
    private String categoryName;
    private String imgName;

    private Boolean isActive;

    public Category(){
//        default
    }
    public Category(int id, String categoryName, String imgName) {
        this.id = id;
        this.categoryName = categoryName;
        this.imgName = imgName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", imgName='" + imgName + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
