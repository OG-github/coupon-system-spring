package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.*;
import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CouponException;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.repository.*;
import com.og.CouponSystemJB.service.exception.CompanyServiceException;


/**
 * This class handles the business logic and data manipulation for Users who are logged in as Company. This Service is
 * of scope prototype and will handle the logic for each Company with a different instance. Each Company will have its
 * own service component that is directly attached to it and responsible to handle the Repositories, this is done by
 * assigning each Company User to a Service and a token Cookie.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompanyServiceImpl implements CompanyService {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/



    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /* Company Entity model attached and associated to this Service */
    private Company company;

    /* SQL repository for User Entity */
    private UserRepositorySql userRepository;

    /* SQL repository for Company Entity */
    private CompanyRepositorySql companyRepository;

    /* SQL repository for Coupon Entity */
    private CouponRepositorySql couponRepository;

    /* SQL repository for Customer Entity */
    private CustomerRepositorySql customerRepository;

    /* SQL repository for CustomerCoupon Entity */
    private CustomerCouponRepositorySql customerCouponRepository;


    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     *
     * @param coupon
     * @throws CompanyServiceException
     */
    private static void CheckValidCoupon(Coupon coupon) throws CompanyServiceException {
        if (null == coupon) {
            throw new CompanyServiceException("Invalid Coupon: Coupon null ");
        }
        if (null == coupon.getTitle()) {
            throw new CompanyServiceException("Invalid Coupon: Coupon title null ");
        }
        if (null == coupon.getStartDate()) {
            throw new CompanyServiceException("Invalid Coupon: Coupon start date null ");
        }
        if (null == coupon.getEndDate()) {
            throw new CompanyServiceException("Invalid Coupon: Coupon end date null ");
        }
        if (CouponCategory.valueOf(coupon.getCategory()).equals(CouponCategory.NOT_EXIST)) {
            throw new CompanyServiceException("Invalid Coupon: Coupon Category invalid ");
        }
        if (null == coupon.getDescription()) {
            throw new CompanyServiceException("Invalid Coupon: Coupon description null ");
        }
        if (coupon.getTitle().length() < Coupon.MIN_CHAR) {
            throw new CompanyServiceException("Invalid Coupon: Coupon title too short ");
        }
        if (coupon.getAmount() < Coupon.MIN_AMOUNT) {
            throw new CompanyServiceException("Invalid Coupon: Coupon amount below zero ");
        }
        if (coupon.getDescription().length() < Coupon.MIN_CHAR) {
            throw new CompanyServiceException("Invalid Coupon: Coupon description too short ");
        }
        if (coupon.getPrice() < Coupon.MIN_PRICE) {
            throw new CompanyServiceException("Invalid Coupon: Coupon price below zero ");
        }
    }

    @Autowired
    public CompanyServiceImpl(CouponRepositorySql couponRepository, CompanyRepositorySql companyRepository,
                              CustomerCouponRepositorySql customerCouponRepository,
                              CustomerRepositorySql customerRepository, UserRepositorySql userRepository) {
        this.couponRepository = couponRepository;
        this.companyRepository = companyRepository;
        this.customerCouponRepository = customerCouponRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    private static void CheckValidCompany(Company company) throws CompanyServiceException {
        if (null == company) {
            throw new CompanyServiceException("Invalid Company: Company null ");
        }
        if (null == company.getName()) {
            throw new CompanyServiceException("Invalid Company: name null ");
        }
        if (null == company.getEmail()) {
            throw new CompanyServiceException("Invalid Company: email null ");
        }
        if (null == company.getPassword()) {
            throw new CompanyServiceException("Invalid Company: password null ");
        }
    }

    public void setCompany(Company company) throws CompanyException {
        Set<Coupon> companyCoupons = this.couponRepository.findCompanyCoupons(company.getId());
        if (null != companyCoupons && !companyCoupons.isEmpty()) {
            company.setCoupons(companyCoupons);
        }
        this.company = company;
    }


    @Override
    public Company getCompany() {
        return this.company;
    }

    @Override
    public Collection<Coupon> findAllCoupons() {
        return this.couponRepository.findAll();
    }

    @Override
    public Collection<Coupon> findCompanyCoupons() {
        return this.couponRepository.findCompanyCoupons(this.company.getId());
    }


    private void checkAvailableCouponTitle(String title) throws CompanyServiceException {
        Collection<Coupon> coupons = this.couponRepository.findAll();
        for (Coupon coupon : coupons) {
            if (coupon.getTitle().equals(title)) {
                throw new CompanyServiceException("Coupon title already taken ");
            }
        }
    }

    @Override
    public Coupon addCoupon(Coupon coupon) throws CompanyServiceException {
        CheckValidCoupon(coupon);
        this.checkAvailableCouponTitle(coupon.getTitle());
        coupon.setId(0);
        try {
            coupon.setCompany(this.company);
            this.company.add(coupon);
        }
        catch (CompanyException e) {
            throw new CompanyServiceException("Add Coupon failed: " + e.getMessage());
        }
        catch (CouponException e) {
            throw new CompanyServiceException("Add Coupon failed: " + e.getMessage());
        }
        return this.couponRepository.save(coupon);
    }

    @Override
    public String deleteCouponByTitle(String title) throws CompanyServiceException {
        Collection<Coupon> companyCoupons = this.company.getCoupons();
        for (Coupon coupon : companyCoupons) {
            if (coupon.getTitle().equals(title)) {
                this.company.getCoupons().remove(coupon);
                this.companyRepository.save(this.company);
                this.couponRepository.delete(coupon);
                this.customerCouponRepository.deleteAllByCouponId(coupon.getId());
                return "Coupon deleted successfully";
            }
        }
        throw new CompanyServiceException("Delete failed: Coupon not found ");
    }

    private void checkUpdateCoupon(Coupon coupon) throws CompanyServiceException, CouponException {
        // if coupon exists
        Optional<Coupon> couponByTitle = this.couponRepository.findCompanyCouponByTitle(coupon.getTitle(),
                this.company.getId());
        if (!couponByTitle.isPresent()) {
            throw new CompanyServiceException("Update Coupon failed: Coupon not found ");
        }
        coupon.setId(couponByTitle.get().getId());
        coupon.setCompany(this.company);
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) throws CompanyServiceException, CouponException, CompanyException {
        CheckValidCoupon(coupon);
        this.checkUpdateCoupon(coupon);
        Coupon savedCoupon = this.couponRepository.save(coupon);
        this.company.setCoupons(this.findCompanyCoupons());
        return savedCoupon;
    }


    @Override
    public String useCoupon(String couponTitle, String customerEmail) throws CompanyServiceException, CouponException {
        if (null == couponTitle) {
            throw new CompanyServiceException("Use Coupon failed: Coupon title null ");
        }
        if (null == customerEmail) {
            throw new CompanyServiceException("Use Coupon failed: Customer email null ");
        }
        Optional<Coupon> coupon = this.couponRepository.findByTitle(couponTitle);
        Optional<Customer> customer = this.customerRepository.findByEmail(customerEmail);
        if (!coupon.isPresent()) {
            throw new CompanyServiceException("Use Coupon failed: Coupon not found ");
        }
        if (!customer.isPresent()) {
            throw new CompanyServiceException("Use Coupon failed: Customer not found ");
        }
        Optional<CustomerCoupon> customerCoupon =
                this.customerCouponRepository.findByCustomerIdAndCouponId(customer.get().getId(), coupon.get().getId());
        if (!customerCoupon.isPresent()) {
            throw new CompanyServiceException("Use Coupon failed: Customer dose not own this Coupon ");
        }
        if (customerCoupon.get().getAmount() <= 0) {
            this.customerCouponRepository.delete(customerCoupon.get());
            throw new CompanyServiceException("Use Coupon failed: Customer dose not have enough Coupons ");
        }
        customerCoupon.get().useCoupon();
        this.customerCouponRepository.save(customerCoupon.get());
        return "Coupon used successfully";
    }

    private void checkUpdateCompany(Company company) throws CompanyServiceException {
        if (!company.getEmail().equals(this.company.getEmail())) {
            throw new CompanyServiceException("Update Company fail: Can not change email ");

        }
        if (company.getName().length() < Company.MIN_CHAR) {
            throw new CompanyServiceException("Update Company fail: New name is too short ");

        }
        if (!company.getName().equals(this.company.getName())) { // name changed
            Optional<Company> companyRepo = this.companyRepository.findByName(company.getName());
            if (companyRepo.isPresent()) { // name already taken
                throw new CompanyServiceException("Invalid Company: name already taken ");
            }
        }
        if (company.getPassword().length() < Company.MIN_CHAR) {
            throw new CompanyServiceException("Update Company fail: New password is too short ");

        }
        company.setId(this.company.getId());
    }

    @Override
    public Company update(Company company) throws CompanyServiceException, CompanyException, UserException {
        CheckValidCompany(company);
        checkUpdateCompany(company);
        company.setCoupons(this.company.getCoupons());
        this.company = company;
        Optional<User> user = this.userRepository.findByEmail(this.company.getEmail());
        if (!user.isPresent()) {
            throw new CompanyServiceException("Update Company failed: Update User info failed. ");
        }
        user.get().setClient(this.company);
        user.get().setEmail();
        user.get().setPassword();
        this.userRepository.save(user.get());
        return this.companyRepository.save(this.company);
    }
}

