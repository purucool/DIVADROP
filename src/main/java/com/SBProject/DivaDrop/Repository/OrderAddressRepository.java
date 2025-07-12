package com.SBProject.DivaDrop.Repository;

import com.SBProject.DivaDrop.Modal.OrderAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderAddressRepository extends JpaRepository<OrderAddress, Integer> {
}
