package com.og.CouponSystemJB.entity;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.HashSet;

/*-------------------- javax --------------------*/

/*---------- persistence ----------*/
import javax.persistence.*;

/*-------------------- fasterxml --------------------*/

/*---------- jackson ----------*/
import com.fasterxml.jackson.annotation.JsonIgnore;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CouponException;


/**
 * Entity model(Hibernate) for the table "company". Company represents a Client with the role of company in our
 * system. This class will hold the email and name of the Company which are unique to each
 * Company, and a Collections of the Coupons this Company issues. There are two constructors, one constructor
 * which initializes the Collection of Coupons (in this case implemented via Java's Hashsets) but not the other
 * parameters (which will be null by default). Each parameter has it's own getter and setter, and can't be set to be
 * less than MIN_CHAR characters.
 */
@Entity
@Table(name = "company")
public class Company extends Client {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Minimum number of String characters for Company's parameters.
     */
    public static final int MIN_CHAR = 5;

    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function that will throw a CompanyException if one of the parameters of Company is invalid.
     * (null or below 5 characters)
     *
     * @param param The parameter to check.
     * @param msg   Exception message of which parameter is being checked.
     * @throws CompanyException Will be thrown with relevant message.
     */
    private static void CHECK_PARAM(String param, String msg) throws CompanyException {
        if (null == param) {
            throw new CompanyException(msg + CompanyException.MSG_NULL_PARAM);
        }
        if (param.length() < MIN_CHAR) {
            throw new CompanyException(msg + CompanyException.MSG_MIN_PARAM);
        }
    }
    /*----------------- Fields --------------------------------------------------------------------------------------*/

    /**
     * The name of the Company.(Must be at least MIN_CHAR characters).
     */
    @Column(name = "name", unique = true, nullable = false)
    private String name;


    /**
     * A collection of all the Coupons this Company is issuing.
     * (java.util.Collection.Set)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnore // will ignore both Set and Get Coupons.
    private Collection<Coupon> coupons;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * A generic constructor that will create a new HashSet to the Collection of
     * Coupons.
     */
    public Company() {
        coupons = new HashSet<>();
        // for Hibernate
    }

    /**
     * Constructor that will create a new Company with new name, email and password.
     * Names or emails are unique to each Company, and can not be shared with other Companies.
     * Parameters must be at least MIN_CHAR characters.
     *
     * @param name     new name that does not exist in the DB.
     * @param email    new email that does not exist in the DB.
     * @param password new password for users to access this Company.
     */
    public Company(String name, String email, String password) throws CompanyException {
        this();
        CHECK_PARAM(name, CompanyException.MSG_NAME);
        this.name = name;
        CHECK_PARAM(email, CompanyException.MSG_EMAIL);
        this.email = email;
        CHECK_PARAM(password, CompanyException.MSG_PASSWORD);
        this.password = password;
    }

    /*----------------- Getters / Setters ----------------------------------------------------------------------------*/

    /*-------------------- Name --------------------*/

    /**
     * Getter for Company's name.
     *
     * @return String name of the Company.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for Company's name. Has to be at least MIN_CHAR.
     *
     * @param name String name of the Company
     * @throws CompanyException Thrown if parameter is invalid.
     */
    public void setName(String name) throws CompanyException {
        CHECK_PARAM(name, CompanyException.MSG_NAME);
        this.name = name;
    }


    /*-------------------- Company Coupons --------------------*/

    /**
     * Add a new Coupon to Company's Coupons.
     *
     * @param coupon A new Coupon this Company is issuing.
     * @param coupon
     * @throws CompanyException Thrown if Coupon is invalid.
     * @throws CouponException  Thrown if this Coupon is invalid.
     */
    public void add(Coupon coupon) throws CompanyException, CouponException {
        if (null == coupon) {
            throw new CompanyException(CompanyException.MSG_COUPON + CompanyException.MSG_NULL_PARAM);
        }
        coupon.setCompany(this);
        this.coupons.add(coupon);
    }


    /**
     * Getter for Company's Coupons.
     *
     * @return a Set (HashSet) of this Company's Coupons.
     */
    public Collection<Coupon> getCoupons() {
        return this.coupons;
    }

    /**
     * Setter for Company's Coupons.
     *
     * @param coupons a Set (HashSet) for this Company's Coupons.
     * @throws CouponException Thrown if the Collection is invalid.
     */
    public void setCoupons(Collection<Coupon> coupons) throws CompanyException {
        if (null == coupons) {
            throw new CompanyException(CompanyException.MSG_COUPON);
        }
        this.coupons = coupons;
    }

    /*-------------------- Object overrides --------------------*/

    /**
     * Override of Object class.
     *
     * @return
     */
    @Override
    public String toString() {
        return "Company [id=" + this.getId() + ", name=" + name + ", email=" + email + ", password=" + password + "]\n";
    }

}
