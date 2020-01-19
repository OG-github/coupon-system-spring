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
import com.og.CouponSystemJB.entity.Company;

/**
 * Repository (DB DAO) for Company entity using JPA Repository. This is a Data Base Data Access Object.
 */
@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface CompanyRepositorySql extends JpaRepository<Company, Long> {

    /**
     * Find a Company entity by email (email is unique).
     *
     * @param email String email of the desired Company.
     * @return Optional of Company.
     */
    Optional<Company> findByEmail(String email);

    /**
     * Find a Company entity by name (name is unique).
     *
     * @param name String name of the desired Company.
     * @return Optional of Company.
     */
    Optional<Company> findByName(String name);

    /**
     * Delete a Company entity by email (email is unique).
     *
     * @param email String email of the desired Company to delete.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Company AS comp WHERE comp.email=:email")
    void deleteByEmail(String email);
}
