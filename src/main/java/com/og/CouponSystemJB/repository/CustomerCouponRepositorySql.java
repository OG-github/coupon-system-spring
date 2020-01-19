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
import java.util.Collection;
import java.util.Optional;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.CustomerCoupon;

/**
 * Repository (DB DAO) for CustomerCoupon entity using JPA Repository. This is a Data Base Data Access Object.
 */
@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public interface CustomerCouponRepositorySql extends JpaRepository<CustomerCoupon, Long> {

    /**
     * Find CustomerCoupon based on Customer Id and Coupon Id.
     *
     * @param customerId Customer Id.
     * @param couponId   Coupon Id.
     * @return Optional of CustomerCoupon.
     */
    @Query("FROM CustomerCoupon AS custcoup WHERE custcoup.customer.id=:customerId AND custcoup.coupon.id=:couponId")
    Optional<CustomerCoupon> findByCustomerIdAndCouponId(long customerId,
                                                         long couponId);

    /**
     * Find all CustomerCoupons that belong to a Certain Customer based on Customer Id.
     *
     * @param customerId Id of the Customer owning the Coupons.
     * @return Collection of CustomerCoupon representing all the Coupons the Customer owns.
     */
    @Query("FROM CustomerCoupon AS custcoup WHERE custcoup.customer.id=:customerId")
    Collection<CustomerCoupon> findByCustomerId(long customerId);

    /**
     * Delete all CustomerCoupons that belong to a Certain Coupon based on Coupon Id.
     *
     * @param couponId Coupon Id.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerCoupon AS custcoup WHERE custcoup.coupon.id=:couponId")
    void deleteAllByCouponId(long couponId);

    /**
     * Delete all CustomerCoupons that belong to a Certain Customer based on Customer Id.
     *
     * @param customerId Customer Id.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerCoupon AS custcoup WHERE custcoup.customer.id=:customerId")
    void deleteAllByCustomerId(long customerId);
}
