package com.og.CouponSystemJB.service;

import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.Customer;
import com.og.CouponSystemJB.entity.CustomerCoupon;
import com.og.CouponSystemJB.entity.User;
import com.og.CouponSystemJB.entity.exception.CouponException;
import com.og.CouponSystemJB.entity.exception.CustomerException;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.repository.CouponRepositorySql;
import com.og.CouponSystemJB.repository.CustomerCouponRepositorySql;
import com.og.CouponSystemJB.repository.CustomerRepositorySql;
import com.og.CouponSystemJB.repository.UserRepositorySql;
import com.og.CouponSystemJB.service.exception.CustomerServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerServiceImpl implements CustomerService {

    private Customer customer;

    private CustomerRepositorySql customerRepos;
    private CouponRepositorySql couponRepository;
    private CustomerCouponRepositorySql customerCouponRepository;
    private UserRepositorySql userRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepositorySql customerRepos, CouponRepositorySql couponRepository,
                               CustomerCouponRepositorySql customerCouponRepository, UserRepositorySql userRepository) {
        this.customerRepos = customerRepos;
        this.couponRepository = couponRepository;
        this.customerCouponRepository = customerCouponRepository;
        this.userRepository = userRepository;
    }


    private static void CheckValidCustomer(Customer customer) throws CustomerServiceException {
        if (null == customer) {
            throw new CustomerServiceException("Invalid Customer: Customer null ");
        }
        if (null == customer.getFirstName()) {
            throw new CustomerServiceException("Invalid Customer: first name null ");
        }
        if (null == customer.getLastName()) {
            throw new CustomerServiceException("Invalid Customer: last name null ");
        }
        if (null == customer.getEmail()) {
            throw new CustomerServiceException("Invalid Customer: email null ");
        }
        if (null == customer.getPassword()) {
            throw new CustomerServiceException("Invalid Customer: password null ");
        }
    }

    public void setCustomer(Customer customer) throws CustomerException {
        Collection<CustomerCoupon> customerCoupons = this.customerCouponRepository.findByCustomerId(customer.getId());
        if (null != customerCoupons && !customerCoupons.isEmpty()) {
            customer.setCoupons(customerCoupons);
        }
        this.customer = customer;
    }

    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    @Override
    public Collection<Coupon> findAllCoupons() {
        return this.couponRepository.findAll();
    }

    @Override
    public Collection<CustomerCoupon> findCustomerCoupons() {
        return this.customer.getCoupons();
    }

    private void checkUpdateCustomer(Customer customer) throws CustomerServiceException {
        if (!customer.getEmail().equals(this.customer.getEmail())) {
            throw new CustomerServiceException("Update Customer fail: Can not change email ");
        }
        if (null == customer.getFirstName()) {
            throw new CustomerServiceException("Update Customer fail: New first name is null ");
        }
        if (null == customer.getLastName()) {
            throw new CustomerServiceException("Update Customer fail: New last name is null ");
        }
        if (customer.getPassword().length() < Customer.MIN_CHAR) {
            throw new CustomerServiceException("Update Customer fail: New password is too short ");
        }
        if (customer.getFirstName().length() < Customer.MIN_CHAR_NAME) {
            throw new CustomerServiceException("Update Customer fail: New first name is too short ");
        }
        if (customer.getLastName().length() < Customer.MIN_CHAR_NAME) {
            throw new CustomerServiceException("Update Customer fail: New last name is too short ");
        }
        customer.setId(this.customer.getId());
    }

    @Override
    public Customer updateCustomer(Customer customer) throws CustomerServiceException, CustomerException, UserException {
        CheckValidCustomer(customer);
        this.checkUpdateCustomer(customer);
        customer.setCoupons(this.customer.getCoupons());
        this.customer = customer;
        Optional<User> user = this.userRepository.findByEmail(this.customer.getEmail());
        if (!user.isPresent()){
            throw new CustomerServiceException("Update Customer failed: Update User info failed. ");
        }
        user.get().setClient(this.customer);
        user.get().setEmail();
        user.get().setPassword();
        this.userRepository.save(user.get());
        return this.customerRepos.save(this.customer);
    }

    @Override
    public String purchaseCoupon(String couponTitle)
            throws CustomerServiceException, CustomerException, CouponException {
        Optional<Coupon> coupon = this.couponRepository.findByTitle(couponTitle);
        if (!coupon.isPresent()) {
            throw new CustomerServiceException("Purchase Coupon Failed: Coupon does not exist ");
        }
        Optional<CustomerCoupon> customerCoupon =
                this.customerCouponRepository.findByCustomerIdAndCouponId(this.customer.getId(), coupon.get().getId());
        if (customerCoupon.isPresent()) {
            customerCoupon.get().purchaseCoupon();
            this.customerCouponRepository.save(customerCoupon.get());
            this.customerRepos.save(this.customer);
            return "Coupon purchased successfully, you now own " + customerCoupon.get().getAmount();
        }
        CustomerCoupon newCustomerCoupon = new CustomerCoupon(this.customer, coupon.get());
        newCustomerCoupon.purchaseCoupon();
        this.customer.addCoupon(newCustomerCoupon);
        this.customerCouponRepository.save(newCustomerCoupon);
        this.customerRepos.save(this.customer);
        return "Coupon purchased successfully, you now own " + newCustomerCoupon.getAmount();
    }
}
