package com.SBProject.DivaDrop.Repository;

import com.SBProject.DivaDrop.Modal.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END from User u where u.emailAddress= :name")
    Boolean findAllByUserName(@Param("name") String email);

    @Query("SELECT u from User u where u.emailAddress= :email")
    User findByEmail(@Param("email")String email);

    @Query("SELECT u from User u where u.role= :role")
    List<User> findByRole(@Param("role") String role);

    @Query("SELECT u from User u where u.role= :role")
    Page<User> findByRole(@Param("role") String role, Pageable pageable);

    @Query("SELECT u from User u where u.reset_token= :token")
    User findByToken(@Param("token") String token);
}
