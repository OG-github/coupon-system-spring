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

    /*----------------- Queries String -------------------------------------------------------------------------------*/

    /**
     * Query String for Hibernate to find a Coupon by title.
     */
    String FIND_COUP_BY_TITLE = "FROM Coupon AS coup WHERE coup.title=:title";

    /**
     * Query String for Hibernate to find a Company's Coupons by Company ID.
     */
    String FIND_COMPANYCOUPONS_BY_COMP_ID = "FROM Coupon AS coup WHERE coup.company.id=:id";

    /**
     * Query String for Hibernate to find a Company's Coupons by Coupon's title. Company is identified by ID.
     */
    String FIND_COMPANYCOUPON_BY_COUP_TITLE =
            "FROM Coupon AS coup WHERE coup.title=:title AND coup.company.id=:companyId";

    /*----------------- Queries --------------------------------------------------------------------------------------*/

    /*----------------- Read / Get ----------------------------*/

    /**
     * Find a Coupon entity by title (title is unique).
     *
     * @param title String title of the desired Coupon.
     * @return Optional of Coupon.
     */
    @Query(FIND_COUP_BY_TITLE)
    Optional<Coupon> findByTitle(String title);

    /**
     * Find all Coupons that are issued by a Certain Company.
     *
     * @param id long Id of the Company issuing the Coupons.
     * @return Collection of Company Coupons.
     */
    @Query(FIND_COMPANYCOUPONS_BY_COMP_ID)
    Set<Coupon> findCompanyCoupons(long id);

    /**
     * Find a Coupon entity by title (title is unique) that belongs to a certain Company.
     *
     * @param title     String title of the desired Coupon.
     * @param companyId long Id of the Company issuing the Coupon.
     * @return Optional of Coupon.
     */
    @Query(FIND_COMPANYCOUPON_BY_COUP_TITLE)
    Optional<Coupon> findCompanyCouponByTitle(String title, long companyId);

    /**
     * Find all the Coupons in a certain shopping category that belong to a certain Company.
     *
     * @param id       Id of the Company issuing the Coupons.
     * @param category Shopping category for the Coupons.
     * @return Set of Coupons of a certain Company that belong to a certain shopping category.
     */
    @Query("FROM Coupon AS coup WHERE coup.company.id=:id AND coup.category=:category")
    Set<Coupon> findCompanyCouponsByCategory(long id, int category);

    /**
     * Find all the Coupons that their price is below or equal to a giving price and belong to a certain Company. The
     * price ceiling is inclusive (less or equal than).
     *
     * @param id    Id of the Company issuing the Coupons.
     * @param price Price ceiling (inclusive), will return all of the Company's Coupons below this price.
     * @return Set of Coupons of a certain Company that are below a certain price.
     */
    @Query("FROM Coupon AS coup WHERE coup.company.id=:id AND coup.price<=:price")
    Set<Coupon> findCompanyCouponsLessThan(long id, double price);

    /**
     * Find all the Coupons that their endDate is before a certain date and they belong to a certain Company. I.e find
     * all Coupons of a Company that their expiration date is before a giving date.
     *
     * @param id   Id of the Company issuing the Coupons.
     * @param date Date to compare with endDate of a Company's Coupons.
     * @return Set of Coupons of all the Coupons that belong to a Company that endDate is before date.
     */
    @Query("FROM Coupon AS coup WHERE coup.company.id=:id AND coup.endDate<:date")
    Set<Coupon> findCompanyCouponsBeforeExpiredDate(long id, Date date);

    /**
     * Find all the Coupons that their endDate is before today. I.e their expiration date is past due.
     *
     * @return Set of Coupons of all expired Coupons.
     */
    @Query("FROM Coupon AS coup WHERE coup.endDate < current_time()")
    Set<Coupon> findExpiredCoupons();

    /*----------------- Remove / Delete  -----------------------*/

    /**
     * Delete all the Coupons a certain Company is issuing by the Company's Id.
     *
     * @param companyId long Id of the Company issuing the Coupons.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM Coupon AS coup WHERE coup.company.id=:companyId")
    void deleteByCompanyId(long companyId);

}
