package com.SBProject.DivaDrop.Repository;

import com.SBProject.DivaDrop.Modal.ProductOrder;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
    @Query("SELECT po from ProductOrder po WHERE po.user.id= :uid")
    List<ProductOrder> findByUserId(@Param("uid") Integer uid);

    @Query("SELECT o FROM ProductOrder o WHERE LOWER(o.orderId) LIKE LOWER(CONCAT('%', :orderId, '%'))")
    List<ProductOrder> searchByOrderId(@Param("orderId") String orderId);

    @Query("SELECT o FROM ProductOrder o WHERE LOWER(o.orderId) LIKE LOWER(CONCAT('%', :orderId, '%'))")
    Page<ProductOrder> searchByOrderIdPaging(@Param("orderId") String orderId, Pageable pageable);

    @Query("SELECT o from ProductOrder o")
    Page<ProductOrder> getAllOrdersPaging(Pageable pageable);
}
