package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.Optional;

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/*-------------------- CouponSystemJB --------------------*/

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

/**
 * This class handles the business logic and data manipulation for Users who are logged in as Customer. This Service is
 * of scope prototype and will handle the logic for each Customer with a different instance. Each Customer will have its
 * own service component that is directly attached to it and responsible to handle the Repositories, this is done by
 * assigning each Company User to a Service and a token Cookie.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerServiceImpl implements CustomerService {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/


    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /* Customer Entity model attached and associated to this Service */
    private Customer customer;

    /* SQL repository for User Entity */
    private UserRepositorySql userRepository;

    /* SQL repository for Customer Entity */
    private CustomerRepositorySql customerRepos;

    /* SQL repository for Coupon Entity */
    private CouponRepositorySql couponRepository;

    /* SQL repository for CustomerCoupon Entity */
    private CustomerCouponRepositorySql customerCouponRepository;

    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function to determine if Customer is valid or not. The parameters of the Customer will be checked
     * and this method will throw a CustomerServiceException with a relevant message if one of them is invalid.
     *
     * @param customer Customer Entity model to check.
     * @throws CustomerServiceException Thrown if parameters are null or invalid.
     */
    private static void CheckValidCustomer(Customer customer) throws CustomerServiceException {
        if (null == customer) { // null customer
            throw new CustomerServiceException(
                    CustomerServiceException.INVALID_CUST + CustomerServiceException.NULL_CUST);
        }
        if (null == customer.getFirstName()) { // null first name
            throw new CustomerServiceException(
                    CustomerServiceException.INVALID_CUST + CustomerServiceException.NULL_FIRST_NAME);
        }
        if (null == customer.getLastName()) { // null last name
            throw new CustomerServiceException(
                    CustomerServiceException.INVALID_CUST + CustomerServiceException.NULL_LAST_NAME);
        }
        if (null == customer.getEmail()) { // null email
            throw new CustomerServiceException(
                    CustomerServiceException.INVALID_CUST + CustomerServiceException.NULL_EMAIL);
        }
        if (null == customer.getPassword()) { // null password
            throw new CustomerServiceException(
                    CustomerServiceException.INVALID_CUST + CustomerServiceException.NULL_PASSWORD);
        }
    }


    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Full Autowired constructor used automatically by Spring.
     *
     * @param customerRepos            JPA Repository for Customer (Autowired).
     * @param couponRepository         JPA Repository for Coupon (Autowired).
     * @param customerCouponRepository JPA Repository for CustomerCoupon (Autowired).
     * @param userRepository           JPA Repository for User (Autowired).
     */
    @Autowired
    public CustomerServiceImpl(CustomerRepositorySql customerRepos, CouponRepositorySql couponRepository,
                               CustomerCouponRepositorySql customerCouponRepository, UserRepositorySql userRepository) {
        this.customerRepos = customerRepos;
        this.couponRepository = couponRepository;
        this.customerCouponRepository = customerCouponRepository;
        this.userRepository = userRepository;
    }

    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /**
     * (For LoginService) Setter for the Customer Entity model this Service holds and is responsible for.
     *
     * @param customer Customer Entity model.
     * @throws CustomerException Thrown if setting CustomerCoupons failed.
     */
    public void setCustomer(Customer customer) throws CustomerException {
        Collection<CustomerCoupon> customerCoupons = this.customerCouponRepository.findByCustomerId(customer.getId());
        if (null != customerCoupons && !customerCoupons.isEmpty()) {
            customer.setCoupons(customerCoupons);
        }
        this.customer = customer;
    }

    /**
     * Helper function to determine if a Customer to be updated has valid parameters before entering the DB. This
     * function will check if the parameters are null or email has not been changed, if the new names are not too
     * short or password is not too short.
     *
     * @param customer Customer entity with the updated parameters.
     * @throws CustomerServiceException Will be thrown if one of the parameters is invalid.
     */
    private void checkUpdateCustomer(Customer customer) throws CustomerServiceException {
        if (!customer.getEmail().equals(this.customer.getEmail())) { // email changed
            throw new CustomerServiceException(
                    CustomerServiceException.UPDATE_CUST + CustomerServiceException.EMAIL_CHANGE);
        }
        if (null == customer.getFirstName()) { // null first name
            throw new CustomerServiceException(
                    CustomerServiceException.UPDATE_CUST + CustomerServiceException.NULL_FIRST_NAME);
        }
        if (null == customer.getLastName()) { // null last name
            throw new CustomerServiceException(
                    CustomerServiceException.UPDATE_CUST + CustomerServiceException.NULL_LAST_NAME);
        }
        if (customer.getPassword().length() < Customer.MIN_CHAR) { // password too short
            throw new CustomerServiceException(
                    CustomerServiceException.UPDATE_CUST + CustomerServiceException.PASSWORD_SHORT);
        }
        if (customer.getFirstName().length() < Customer.MIN_CHAR_NAME) { // first name too short
            throw new CustomerServiceException(
                    CustomerServiceException.UPDATE_CUST + CustomerServiceException.FIRST_NAME_SHORT);
        }
        if (customer.getLastName().length() < Customer.MIN_CHAR_NAME) { // last name too short
            throw new CustomerServiceException(
                    CustomerServiceException.UPDATE_CUST + CustomerServiceException.LAST_NAME_SHORT);
        }
        customer.setId(this.customer.getId());
    }

    /*----------------- Read / Get -----------------------*/

    /**
     * Getter for the Customer entity that this Service is handling. The Customer entity's parameters will represent
     * data from the SQL DB.
     *
     * @return Customer entity for this Service.
     */
    @Override
    public Customer getCustomer() {
        return this.customer;
    }

    /**
     * Find all Coupon Entities from the DB. Each Coupon holds data from the SQL DB in its field members.
     *
     * @return Collection of Coupon Entities from SQL DB.
     */
    @Override
    public Collection<Coupon> findAllCoupons() {
        return this.couponRepository.findAll();
    }

    /**
     * Find all CustomerCoupon Entities from the DB. Each CustomerCoupon holds data from the SQL DB in its field
     * members.
     *
     * @return Collection of CustomerCoupon Entities from SQL DB.
     */
    @Override
    public Collection<CustomerCoupon> findCustomerCoupons() {
        return this.customer.getCoupons();
    }

    /*---------------------- Update ---------------------------*/

    /**
     * Update the Customer this service is handling in the DB. The Customer will be updated by the Customer argument and
     * its fields.
     *
     * @param customer Customer entity with the updated field values.
     * @return The updated Customer entity with the new values.
     * @throws CustomerServiceException Thrown if updating failed or Customer is invalid or with invalid fields.
     * @throws CustomerException        Thrown if updating the Customer data base failed or Customer is invalid or with
     *                                  invalid fields.
     * @throws UserException            Thrown if updating the User data base failed.
     */
    @Override
    public Customer updateCustomer(Customer customer)
            throws CustomerServiceException, CustomerException, UserException {
        CheckValidCustomer(customer); // check valid
        this.checkUpdateCustomer(customer); // update in DB
        // update local instance
        customer.setCoupons(this.customer.getCoupons());
        this.customer = customer;
        Optional<User> user = this.userRepository.findByEmail(this.customer.getEmail());
        if (!user.isPresent()) {
            throw new CustomerServiceException(CustomerServiceException.UPDATE_CUST + "Update User info failed. ");
        }
        // update User DB
        user.get().setClient(this.customer);
        user.get().setEmail();
        user.get().setPassword();
        this.userRepository.save(user.get());
        return this.customerRepos.save(this.customer);
    }

    /**
     * Purchase a Coupon for this Customer. The amount of this Coupon available for sale will decrease by 1, and the
     * Coupon will be associated in CustomerCoupons with this Customer. The Coupon will be identified by the
     * CouponTitle which is a unique parameter in the data base.
     *
     * @param couponTitle Title of the Coupon (unique parameter) to purchase.
     * @return
     * @throws CustomerServiceException Thrown if unable to find Coupon or updating failed.
     * @throws CustomerException        Thrown if updating Customer in DB failed.
     * @throws CouponException          Thrown if updating Coupon in DB failed.
     */
    @Override
    public String purchaseCoupon(String couponTitle)
            throws CustomerServiceException, CustomerException, CouponException {
        // find Coupon by title
        Optional<Coupon> coupon = this.couponRepository.findByTitle(couponTitle);
        if (!coupon.isPresent()) {
            throw new CustomerServiceException("Purchase Coupon Failed: Coupon does not exist ");
        }
        // find CustomerCoupon by Coupon's title and the local instance of Customer
        Optional<CustomerCoupon> customerCoupon =
                this.customerCouponRepository.findByCustomerIdAndCouponId(this.customer.getId(), coupon.get().getId());
        // check if CustomerCoupon exists (purchased before)
        if (customerCoupon.isPresent()) {
            customerCoupon.get().purchaseCoupon(); // purchase
            this.customerCouponRepository.save(customerCoupon.get()); // save to CustomerCoupon DB
            this.customerRepos.save(this.customer); // save to Customer DB
            return "Coupon purchased successfully, you now own " + customerCoupon.get().getAmount();
        }
        // first purchase, make new CustomerCoupon
        CustomerCoupon newCustomerCoupon = new CustomerCoupon(this.customer, coupon.get());
        newCustomerCoupon.purchaseCoupon();
        this.customer.addCoupon(newCustomerCoupon); // add Coupon to Customer
        this.customerCouponRepository.save(newCustomerCoupon); // save to CustomerCoupon DB
        this.customerRepos.save(this.customer); // save to Customer DB
        return "Coupon purchased successfully, you now own " + newCustomerCoupon.getAmount();
    }
}
