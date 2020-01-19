package com.og.CouponSystemJB.entity;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.HashSet;

/*-------------------- fasterxml --------------------*/

/*---------- jackson ----------*/
import com.fasterxml.jackson.annotation.JsonIgnore;

/*-------------------- javax --------------------*/

/*---------- persistence ----------*/
import javax.persistence.*;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.exception.CustomerException;

/**
 * Entity model(Hibernate) for the table "customer". Customer represents a Client with the role of customer in our
 * system. This class will have a unique Client email, password, first and last name and a Collection of the Coupons
 * this Customer has bought. There are two constructors, one constructor which initializes the Collection of
 * CustomerCoupons and is used by hibernate and one full constructor.
 */
@Entity
@Table(name = "customer")
public class Customer extends Client {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Minimum number of String characters for Customer's parameters.
     */
    public static final int MIN_CHAR = 5;

    /**
     * Minimum number of String characters for Customer's name parameters.
     */
    public static final int MIN_CHAR_NAME = 3;

    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function that will throw a CustomerException if one of the parameters of Customer is invalid.
     * (null or below 5 characters)
     *
     * @param param The parameter to check.
     * @param msg   Exception message of which parameter is being checked.
     * @throws CustomerException Will be thrown with relevant message.
     */
    private static void CHECK_PARAM(String param, String msg) throws CustomerException {
        if (null == param) {
            throw new CustomerException(msg + CustomerException.MSG_NULL_PARAM);
        }
        if (param.length() < MIN_CHAR) {
            throw new CustomerException(msg + CustomerException.MSG_MIN_PARAM);
        }
    }

    /**
     * Static helper function that will throw a CustomerException if one of the name parameters of Customer is invalid.
     * (null or below 5 characters)
     *
     * @param param The name parameter to check.
     * @param msg   Exception message of which name parameter is being checked.
     * @throws CustomerException Will be thrown with relevant message.
     */
    private static void CHECK_PARAM_NAME(String param, String msg) throws CustomerException {
        if (null == param) {
            throw new CustomerException(msg + CustomerException.MSG_NULL_PARAM);
        }
        if (param.length() < MIN_CHAR_NAME) {
            throw new CustomerException(msg + CustomerException.MSG_MIN_PARAM);
        }
    }

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /**
     * First name of the Customer.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Last name of the Customer.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Collection of CustomerCoupons representing the Coupons this Customer purchased.
     */
    @Column(name = "coupons")
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore
    private Collection<CustomerCoupon> coupons;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * A generic empty constructor, used by Hibernate, that will create a new HashSet to the Collection of
     * CustomerCoupons.
     */
    public Customer() {
        /* For Hibernate */
        this.coupons = new HashSet<>();
    }

    /**
     * Full constructor that will initialize all the parameters. Must be MIN_CHAR.
     *
     * @param firstName String first name of the Customer.
     * @param lastName  String last name of the Customer.
     * @param email     String email of the Customer. (Unique to Client).
     * @param password  String password of the Customer.
     * @throws CustomerException Will be thrown if parameters are invalid.
     */
    public Customer(String firstName, String lastName, String email, String password) throws CustomerException {
        this();
        CHECK_PARAM_NAME(firstName, CustomerException.MSG_FIRST_NAME);
        this.firstName = firstName;
        CHECK_PARAM_NAME(lastName, CustomerException.MSG_LAST_NAME);
        this.lastName = lastName;
        CHECK_PARAM(email, CustomerException.MSG_EMAIL);
        this.email = email;
        CHECK_PARAM(password, CustomerException.MSG_PASSWORD);
        this.password = password;
    }

    /*----------------- Getters / Setters ----------------------------------------------------------------------------*/

    /*-------------------- First Name --------------------*/

    /**
     * Getter to Customer's First Name.
     *
     * @return String first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for Customer's First Name.
     *
     * @param firstName String first name.
     * @throws CustomerException Will be thrown if parameter are invalid.
     */
    public void setFirstName(String firstName) throws CustomerException {
        CHECK_PARAM_NAME(firstName, CustomerException.MSG_FIRST_NAME);
        this.firstName = firstName;
    }

    /*-------------------- Last Name --------------------*/

    /**
     * Getter to Customer's Last Name.
     *
     * @return String last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for Customer's Last Name.
     *
     * @param lastName String last name.
     * @throws CustomerException Will be thrown if parameter are invalid.
     */
    public void setLastName(String lastName) throws CustomerException {
        CHECK_PARAM_NAME(lastName, CustomerException.MSG_LAST_NAME);
        this.lastName = lastName;
    }

    /*-------------------- Coupons --------------------*/

    /**
     * Getter to Customer's Coupons that were purchased as CustomerCoupon.
     *
     * @return Collection of CustomerCoupon.
     */
    public Collection<CustomerCoupon> getCoupons() {
        return this.coupons;
    }

    /**
     * Setter for Customer's Coupons that were purchased as CustomerCoupon
     *
     * @param coupons Collection of CustomerCoupon.
     * @throws CustomerException Will be thrown if parameter are invalid.
     */
    public void setCoupons(Collection<CustomerCoupon> coupons) throws CustomerException {
        if (null == coupons) {
            throw new CustomerException(CustomerException.MSG_COUPONS);
        }
        this.coupons = coupons;
    }

    /**
     * Add a Customer Coupon to Coupons.
     *
     * @param customerCoupon CustomerCoupon that represents a Coupon purchased by this Customer.
     */
    public void addCoupon(CustomerCoupon customerCoupon) throws CustomerException {
        if (null == customerCoupon) {
            throw new CustomerException(CustomerException.MSG_COUPONS);
        }
        this.coupons.add(customerCoupon);
    }

    /*-------------------- Object overrides --------------------*/

    @Override
    public String toString() {
        return "Customer [id="
                + this.getId() + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email +
                ", password="
                + password + "]\n";
    }

}

