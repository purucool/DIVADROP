package com.SBProject.DivaDrop.Repository;

import com.SBProject.DivaDrop.Modal.Cart;
import com.SBProject.DivaDrop.Modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.categoryName = :name")
    public Boolean existByName(@Param("name") String name);

    @Query("SELECT c FROM Category c where c.isActive= true")
    List<Category> findAllActiveCategory();


}
