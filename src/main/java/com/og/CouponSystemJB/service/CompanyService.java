package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.Company;
import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CouponException;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.service.exception.CompanyServiceException;


/**
 * This interface will sign all the methods and functions Company Services have to implement on different Repositories.
 * The Services will use the Data Base Data Access Objects (DB DAO) and will handle the business logic and data
 * manipulation. Must extend EntityService so that the ClientSession class (representing a session with a Client
 * entity) can hold all sub-hierarchical Services regardless of Client or role; LoginService will set each
 * ClientSession with a respective Service and be assigned to tokens. Each Service must represent one Client.
 */
public interface CompanyService extends EntityService {

    /*----------------- Create / Insert -----------------------*/

    /**
     * Add a new Coupon that this Company is issuing.
     *
     * @param coupon Coupon entity of a new Coupon this Company is issuing.
     * @return The new Coupon this Company is issuing if successful.
     * @throws CompanyServiceException Thrown if failed to insert to data base or Coupon is invalid.
     */
    Coupon addCoupon(Coupon coupon) throws CompanyServiceException;

    /*----------------- Read / Get -----------------------*/

    /**
     * Get the Company entity this service is handling in the data base.
     *
     * @return Company class this service is handling.
     */
    Company getCompany();

    /**
     * Get all the Coupons that are in the data base.
     *
     * @return Collection of Coupons from all the Companies.
     */
    Collection<Coupon> findAllCoupons();

    /**
     * Get all the Coupons that belong to this Company from the data base.
     *
     * @return Collection of Coupons from this Company.
     */
    Collection<Coupon> findCompanyCoupons();

    /*---------------------- Update ---------------------------*/

    /**
     * Update a Coupon this Company is issuing. The Coupon's parameters must have its fields with the updated values.
     *
     * @param coupon Coupon entity with the updated field values.
     * @return The updated Coupon with the new values.
     * @throws CompanyServiceException Thrown if Coupon or one of its values invalid.
     * @throws CouponException         Thrown if updating Coupon in Coupon DB failed.
     * @throws CompanyException        Thrown if updating Company in Company DB failed.
     */
    Coupon updateCoupon(Coupon coupon) throws CompanyServiceException, CouponException, CompanyException;

    /**
     * Let a Customer entity use a Coupon. Customer will be identified by email and Coupon by title (unique
     * parameters). Using the Coupon will reduce the amount of Coupons the Customer has by 1.
     *
     * @param couponTitle   String title of the Coupon the Customer wants to use.
     * @param customerEmail String email of the Customer using the Coupon.
     * @return String message of update status.
     * @throws CompanyServiceException Thrown if updating failed or Customer or Customer's email is invalid.
     * @throws CouponException         Thrown if Coupon title or Coupon is invalid.
     */
    String useCoupon(String couponTitle, String customerEmail) throws CompanyServiceException, CouponException;

    /**
     * Update the Company this service is handling. The Company will be updated by the Company argument and its
     * fields.
     *
     * @param company Company entity with the updated field values.
     * @return The updated Company with the new values.
     * @throws CompanyServiceException Thrown if updating failed or Company is invalid or with invalid fields.
     * @throws CompanyException        Thrown if updating the Company data base failed or Company is invalid or with
     *                                 invalid fields.
     * @throws UserException           Thrown if updating the User data base failed.
     */
    Company update(Company company) throws CompanyServiceException, CompanyException, UserException;


    /*----------------- Remove / Delete  -----------------------*/

    /**
     * Delete a Coupon this Company is issuing by title (unique parameter).
     *
     * @param title Title of the Coupon to delete.
     * @return String message of deletion status.
     * @throws CompanyServiceException Thrown if failed to delete in DB or title invalid.
     */
    String deleteCouponByTitle(String title) throws CompanyServiceException;

}

