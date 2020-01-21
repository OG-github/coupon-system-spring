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
 * manipulation. Must extend EntityService so can be used by LoginService and be assigned to tokens.
 */
public interface CompanyService extends EntityService {

    /**
     *
     * @return
     */
    Company getCompany();

    Collection<Coupon> findAllCoupons();

    Collection<Coupon> findCompanyCoupons();

    Coupon addCoupon(Coupon coupon) throws CompanyServiceException;

    String deleteCouponByTitle(String title) throws CompanyServiceException;

    Coupon updateCoupon(Coupon coupon) throws CompanyServiceException, CouponException, CompanyException;

    String useCoupon(String couponTitle, String customerEmail) throws CompanyServiceException, CouponException;

    Company update(Company company) throws CompanyServiceException, CompanyException, UserException;

}

