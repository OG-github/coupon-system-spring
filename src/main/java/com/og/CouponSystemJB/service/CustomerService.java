package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.Customer;
import com.og.CouponSystemJB.entity.CustomerCoupon;
import com.og.CouponSystemJB.entity.exception.CouponException;
import com.og.CouponSystemJB.entity.exception.CustomerException;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.service.exception.CustomerServiceException;

/**
 * This interface will sign all the methods and functions Customer Services have to implement on different Repositories.
 * The Services will use the Data Base Data Access Objects (DB DAO) and will handle the business logic and data
 * manipulation. Must extend EntityService so can be used by LoginService and be assigned to tokens. Each Service
 * must represent one Client.
 */
public interface CustomerService extends EntityService {

    /*----------------- Read / Get -----------------------*/

    /**
     * Get the Customer entity this service is handling in the data base.
     *
     * @return Customer class this service is handling.
     */
    Customer getCustomer();

    /**
     * Get all the Coupons that are in the data base.
     *
     * @return Collection of Coupons from all the Companies.
     */
    Collection<Coupon> findAllCoupons();

    /**
     * Get all the CustomerCoupon that this Customer has purchased from the data base.
     *
     * @return Collection of CustomerCoupon of this Customer.
     */
    Collection<CustomerCoupon> findCustomerCoupons();

    /*---------------------- Update ---------------------------*/

    /**
     * Update the Customer this service is handling. The Customer will be updated by the Customer argument and its
     * fields.
     *
     * @param customer Customer entity with the updated field values.
     * @return The updated Customer with the new values.
     * @throws CustomerServiceException Thrown if updating failed or Customer is invalid or with invalid fields.
     * @throws CustomerException        Thrown if updating the Customer data base failed or Customer is invalid or with
     *                                  invalid fields.
     * @throws UserException            Thrown if updating the User data base failed.
     */
    Customer updateCustomer(Customer customer) throws CustomerServiceException, CustomerException, UserException;

    /**
     * Purchase a Coupon for this Customer. The amount of this Coupon available for sale will decrease by 1, and the
     * Coupon will be associated in CustomerCoupons with this Customer. The Coupon will be identified by the
     * CouponTitle which is a unique parameter in the data base.
     *
     * @param couponTitle Title of the Coupon (unique parameter) to purchase.
     * @return String message of update status.
     * @throws CustomerServiceException Thrown if updating failed or couponTitle invalid.
     * @throws CustomerException        Thrown if updating failed or couponTitle invalid.
     * @throws CouponException          Thrown if updating failed or couponTitle invalid.
     */
    String purchaseCoupon(String couponTitle) throws CustomerServiceException, CustomerException, CouponException;

}

