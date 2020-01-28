package com.og.CouponSystemJB.repository;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*-------------------- java --------------------*/

/*---------- util ----------*/
import java.util.Optional;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.Customer;

/**
 * Repository (DB DAO) for Customer entity using JPA Repository. This is a Data Base Data Access Object.
 */
@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface CustomerRepositorySql extends JpaRepository<Customer, Long> {

    /*----------------- Queries String -------------------------------------------------------------------------------*/

    /**
     * Query String for Hibernate to delete a Customer by a Customer email.
     */
    String DLT_BY_EMAIL = "DELETE FROM Customer AS cust WHERE cust.email=:email";

    /*----------------- Queries --------------------------------------------------------------------------------------*/

    /*----------------- Read / Get ----------------------------*/

    /**
     * Find a Customer entity by email (email is unique).
     *
     * @param email String email of the desired Customer.
     * @return Optional of Customer.
     */
    Optional<Customer> findByEmail(String email);

    /*----------------- Remove / Delete  -----------------------*/

    /**
     * Delete a Customer entity by email (email is unique).
     *
     * @param email String email of the desired Customer to delete.
     */
    @Transactional
    @Modifying
    @Query(DLT_BY_EMAIL)
    void deleteByEmail(String email);
}
