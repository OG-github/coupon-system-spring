package com.og.CouponSystemJB.repository;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.Admin;

/**
 * Repository (DB DAO) for Admin entity using JPA Repository. This is a database Data Access Object.
 */
@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface AdminRepositorySql extends JpaRepository<Admin, Long> {

}
