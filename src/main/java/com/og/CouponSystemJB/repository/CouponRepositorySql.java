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
import java.util.Set;

/*---------- sql ----------*/
import java.sql.Date;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.Coupon;

/**
 * Repository (DB DAO) for Coupon entity using JPA Repository. This is a Data Base Data Access Object.
 */
@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface CouponRepositorySql extends JpaRepository<Coupon, Long> {

    /**
     * Find a Coupon entity by title (title is unique).
     *
     * @param title String title of the desired Coupon.
     * @return Optional of Coupon.
     */
    @Query("FROM Coupon AS coup WHERE coup.title=:title")
    Optional<Coupon> findByTitle(String title);

    /**
     * Find all Coupons that are issued by a Certain Company.
     *
     * @param id long Id of the Company issuing the Coupons.
     * @return Collection of Company Coupons.
     */
    @Query("FROM Coupon AS coup WHERE coup.company.id=:id")
    Set<Coupon> findCompanyCoupons(long id);

    /**
     * Find a Coupon entity by title (title is unique) that belongs to a certain Company.
     *
     * @param title     String title of the desired Coupon.
     * @param companyId long Id of the Company issuing the Coupon.
     * @return Optional of Coupon.
     */
    @Query("FROM Coupon AS coup WHERE coup.title=:title AND coup.company.id=:companyId")
    Optional<Coupon> findCompanyCouponByTitle(String title, long companyId);

    /**
     * Delete all the Coupons a certain Company is issuing by the Company's Id.
     *
     * @param companyId long Id of the Company issuing the Coupons.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Coupon AS coup WHERE coup.company.id=:companyId")
    void deleteByCompanyId(long companyId);

    @Query("FROM Coupon AS coup WHERE coup.company.id=:id AND coup.category=:category")
    Set<Coupon> findCompanyCouponsByCategory(long id, int category);

    @Query("FROM Coupon AS coup WHERE coup.company.id=:id AND coup.price<=:price")
    Set<Coupon> findCompanyCouponsLessThan(long id, double price);

    @Query("FROM Coupon AS coup WHERE coup.company.id=:id AND coup.endDate<:date")
    Set<Coupon> findCompanyCouponsBeforeExpiredDate(long id, Date date);

    @Query("FROM Coupon AS coup WHERE coup.endDate < current_time()")
    Set<Coupon> findExpiredCoupons();

}
