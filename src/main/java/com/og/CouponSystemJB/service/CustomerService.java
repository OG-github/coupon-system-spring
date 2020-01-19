package com.og.CouponSystemJB.service;

import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.Customer;
import com.og.CouponSystemJB.entity.CustomerCoupon;
import com.og.CouponSystemJB.entity.exception.CouponException;
import com.og.CouponSystemJB.entity.exception.CustomerException;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.service.exception.CustomerServiceException;

import java.util.Collection;
public interface CustomerService extends EntityService {

    Customer getCustomer();

    Collection<Coupon> findAllCoupons();

    Collection<CustomerCoupon> findCustomerCoupons();

    Customer updateCustomer(Customer customer) throws CustomerServiceException, CustomerException, UserException;

    String purchaseCoupon(String couponTitle) throws CustomerServiceException, CustomerException, CouponException;

}

