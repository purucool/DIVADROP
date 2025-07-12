package com.SBProject.DivaDrop.Repository;

import com.SBProject.DivaDrop.Modal.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.productName = :pname")
    public Boolean existByName(@Param("pname") String name);

    @Query("SELECT p FROM Product p WHERE TRIM(LOWER(p.category)) = LOWER(TRIM(:category)) and p.isActive=true")
    public List<Product> findByCategory(@Param("category") String Category);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByProductOrCategory(@Param("keyword") String keyword);
     @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByProductOrCategoryPagging(@Param("keyword") String keyword,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    Page<Product> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE TRIM(LOWER(p.category)) = LOWER(TRIM(:category)) and p.isActive=true")
    public Page<Product> findByCategory(@Param("category") String Category,Pageable pageable);

}
