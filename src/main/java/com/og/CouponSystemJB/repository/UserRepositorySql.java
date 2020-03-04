package com.og.CouponSystemJB.repository;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- springframework --------------------*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*-------------------- java --------------------*/

/*---------- util ----------*/
import java.util.Optional;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.User;

/**
 * Repository (DB DAO) for User entity using JPA Repository. This is a database Data Access Object.
 */
@Repository
public interface UserRepositorySql extends JpaRepository<User, Long> {

    /*----------------- Queries String -------------------------------------------------------------------------------*/

    /**
     * Query String for Hibernate to delete a User by a User email.
     */
    String DLT_BY_EMAIL = "DELETE FROM User AS user WHERE user.email=:email";

    /*----------------- Queries --------------------------------------------------------------------------------------*/

    /*----------------- Read / Get ----------------------------*/

    /**
     * Find a User entity by email (email is unique).
     *
     * @param email String email of the desired User.
     * @return Optional of User.
     */
    public Optional<User> findByEmail(String email);

    /*----------------- Remove / Delete  -----------------------*/

    /**
     * Delete a User entity by email (email is unique).
     *
     * @param email String email of the desired User to delete.
     */
    @Transactional
    @Modifying
    @Query(DLT_BY_EMAIL)
    void deleteByEmail(String email);

}