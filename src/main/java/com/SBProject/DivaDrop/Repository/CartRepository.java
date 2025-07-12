package com.SBProject.DivaDrop.Repository;

import com.SBProject.DivaDrop.Modal.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query("SELECT c from Cart c WHERE c.product.id = :pid and c.user.id = :uid ")
    public Cart findByProductIdAndUserId(@Param("pid") Integer pid, @Param("uid") Integer uid);

    @Query("SELECT COUNT(c) from Cart c WHERE c.user.id= :uid")
    Integer countByUserId(@Param("uid") Integer uid);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :uid")
    List<Cart> findByUserId(@Param("uid") Integer uid);

    @Query("SELECT c FROM Cart c WHERE c.id= :cid")
    Cart findByCartId(@Param("cid") Integer cid);

}
