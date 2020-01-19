package com.og.CouponSystemJB.service;

import com.og.CouponSystemJB.entity.Company;
import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CouponException;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.service.exception.CompanyServiceException;

import java.util.Collection;

public interface CompanyService extends EntityService {

    Company getCompany();

    Collection<Coupon> findAllCoupons();

    Collection<Coupon> findCompanyCoupons();

    Coupon addCoupon(Coupon coupon) throws CompanyServiceException;

    String deleteCouponByTitle(String title) throws CompanyServiceException;

    Coupon updateCoupon(Coupon coupon) throws CompanyServiceException, CouponException, CompanyException;

    String useCoupon(String couponTitle, String customerEmail) throws CompanyServiceException, CouponException;

    Company update(Company company) throws CompanyServiceException, CompanyException, UserException;

}

