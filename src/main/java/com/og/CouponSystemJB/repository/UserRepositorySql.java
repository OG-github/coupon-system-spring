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
 * Repository (DB DAO) for User entity using JPA Repository. This is a Data Base Data Access Object.
 */
@Repository
public interface UserRepositorySql extends JpaRepository<User, Long> {


    static final String QUERY_DLT_BY_EMAIL = "DELETE FROM User AS user WHERE user.email=:email";

    /**
     * Find a User entity by email (email is unique).
     *
     * @param email String email of the desired User.
     * @return Optional of User.
     */
    public Optional<User> findByEmail(String email);

    /**
     * Delete a User entity by email (email is unique).
     *
     * @param email String email of the desired User to delete.
     */
    @Transactional
    @Modifying
    @Query(QUERY_DLT_BY_EMAIL)
    void deleteByEmail(String email);

}