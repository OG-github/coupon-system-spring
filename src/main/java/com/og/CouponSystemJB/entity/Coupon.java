package com.og.CouponSystemJB.entity;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;

/*-------------------- javax --------------------*/

/*---------- persistence ----------*/
import javax.persistence.*;

/*-------------------- jackson --------------------*/
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.og.CouponSystemJB.entity.exception.CouponException;

/**
 * Entity model(Hibernate) for the table "coupon". Coupon represents a Coupon for purchase in our server available to
 * Customers. This class will hold all the parameters to characterize a Coupon in our system and DB. If the
 * parameters have not been initialized they will be by default null.
 */
@Entity
@Table(name = "coupon")
public class Coupon {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Minimum number of String characters for Coupon's parameters.
     */
    public static final int MIN_CHAR = 5;

    /**
     * Minimum number of field amount that can be left for sale.
     */
    public static final int MIN_AMOUNT = 0;

    /**
     * Minimum number of field price.
     */
    public static final int MIN_PRICE = 0;

    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function that will throw a CouponException if one of the parameters of Coupon is invalid.
     * (null or below 5 characters)
     *
     * @param param The parameter to check.
     * @param msg   Exception message of which parameter is being checked.
     * @throws CouponException Will be thrown with relevant message.
     */
    private static void CHECK_PARAM(String param, String msg) throws CouponException {
        if (null == param) {
            throw new CouponException(msg + CouponException.MSG_NULL_PARAM);
        }
        if (param.length() < MIN_CHAR) {
            throw new CouponException(msg + CouponException.MSG_MIN_PARAM);
        }
    }

    /*----------------- Fields --------------------------------------------------------------------------------------*/

    /**
     * ID number of the Coupon.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private long id;

    /**
     * The Company (Entity model) that is issuing this Coupon.
     */
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "company_id")
    private Company company;

    /**
     * The title (name) of this Coupon. (Must be at least MIN_CHAR characters).
     */
    @Column(name = "title", unique = true, nullable = false)
    private String title;

    /**
     * The starting date for this Coupon. (java.sql.Date)
     */
    @Column(name = "startDate", nullable = false)
    private Date startDate;

    /**
     * The ending (expiration) date for this Coupon. (java.sql.Date)
     */
    @Column(name = "endDate", nullable = false)
    private Date endDate;

    /**
     * The shopping or services category for this Coupon.
     */
    @Column(name = "category", nullable = false)
    private CouponCategory category;

    /**
     * Amount left to issue to Customers. Can't be a negative number.
     */
    @Column(name = "amount", nullable = false)
    private int amount;

    /**
     * A textual description of the Coupon to Customers. (Must be at least MIN_CHAR characters).
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Price of the Coupon (USD). Must be a positive number.
     */
    @Column(name = "price", nullable = false)
    private double price;

    /**
     * A URL to an image of the Coupon.
     */
    @Column(name = "image")
    private String image;

    /**
     * Collection of CustomerCoupon representing all the Customer Entities that have bought this Coupon.
     */
    @Column(name = "customers")
    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private Collection<CustomerCoupon> customers;


    /**
     * A generic empty constructor.
     */
    public Coupon() {
        /* For Hibernate */
        this.customers = new HashSet<>();
    }


    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /*-------------------- ID --------------------*/

    /**
     * Getter for the Id of this Coupon.
     *
     * @return long Id in corresponding table.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the Id of this Coupon.
     *
     * @param id long Id in corresponding table.
     */
    public void setId(long id) {
        this.id = id;
    }

    /*-------------------- Company --------------------*/

    /**
     * Getter for the Company issuing this Coupon.
     *
     * @return Company as parent Company of this Coupon.
     */
    @JsonIgnore
    public Company getCompany() {
        return company;
    }

    /**
     * Setter for the Company issuing this Coupon.
     *
     * @param company Company as parent Company for this Coupon.
     * @throws CouponException Thrown if Company is invalid.
     */
    @JsonIgnore
    public void setCompany(Company company) throws CouponException {
        if (null == company) {
            throw new CouponException(CouponException.MSG_COMPANY + CouponException.MSG_NULL_PARAM);
        }
        this.company = company;
    }

    /*-------------------- Title --------------------*/

    /**
     * Getter for the title of this Coupon.
     *
     * @return String title of the Coupon.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title of this Coupon.
     *
     * @param title String title for the Coupon.
     * @throws CouponException Thrown if parameter is invalid.
     */
    public void setTitle(String title) throws CouponException {
        CHECK_PARAM(title, CouponException.MSG_TITLE);
        this.title = title;
    }

    /*-------------------- Start Date --------------------*/

    /**
     * Getter for the Start Date of this Coupon.
     *
     * @return java.sql.Date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Setter for the Start Date of this Coupon.
     *
     * @param startDate java.sql.Date
     * @throws CouponException Thrown if startDate is invalid.
     */
    public void setStartDate(Date startDate) throws CouponException {
        if (null == startDate) {
            throw new CouponException(CouponException.MSG_STARTDATE + CouponException.MSG_NULL_PARAM);
        }
        this.startDate = startDate;
    }

    /*-------------------- End Date --------------------*/

    /**
     * Getter for the End Date of this Coupon.
     *
     * @return java.sql.Date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Setter for the End Date of this Coupon.
     *
     * @param endDate java.sql.Date
     * @throws CouponException Thrown if endDate is invalid.
     */
    public void setEndDate(Date endDate) throws CouponException {
        if (null == startDate) {
            throw new CouponException(CouponException.MSG_ENDDATE + CouponException.MSG_NULL_PARAM);
        }
        this.endDate = endDate;
    }

    /*-------------------- Category --------------------*/

    /**
     * Getter for the category of this Coupon as the value of CouponCategory Enum.
     *
     * @return int representing the value of CouponCategory Enum.
     */
    public int getCategory() {
        return category.categoryValue();
    }

    /**
     * Getter for the category of this Coupon as CouponCategory Enum.
     *
     * @return CouponCategory Enum.
     */
    public CouponCategory getCategoryEnum() {
        return category;
    }

    /**
     * Setter for the category of this Coupon as CouponCategory Enum. An Exception will be thrown if the int is not one
     * of the values of the CouponCategory Enum.
     *
     * @param category int representing the value of CouponCategory Enum.
     * @throws CouponException thrown if int is not one of the values of the CouponCategory Enum.
     */
    public void setCategory(int category) throws CouponException {
        // check if category as int exists as Enum
        if (null == CouponCategory.valueOf(category) ||
                CouponCategory.NOT_EXIST.equals(CouponCategory.valueOf(category))) {
            throw new CouponException(CouponException.MSG_CATEGORY);
        }
        this.category = CouponCategory.valueOf(category);
    }

    /*-------------------- Amount --------------------*/

    /**
     * Getter of the amount left to sale to Customers for this Coupon.
     *
     * @return int of the amount left for sale.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Setter for the amount left to sale to Customers for this Coupon. Can not be set to a number below 0.
     *
     * @param amount int of the amount left for sale. Can not be under 0.
     */
    public void setAmount(int amount) throws CouponException {
        if (amount < MIN_AMOUNT) {
            throw new CouponException(CouponException.MSG_AMOUNT);
        }
        this.amount = amount;
    }

    /*-------------------- Description --------------------*/

    /**
     * Getter of the description for this Coupon. The description is a textual representation that will be presented on
     * the server or website which will rely information about buying this Coupon.
     *
     * @return String of the textual description for the server or website.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of this Coupon. The description is a textual representation that will be presented
     * on the server or website which will rely information about buying this Coupon. Must be at least MIN_CHAR long.
     *
     * @param description String of the textual description for the server or website. Must be at least MIN_CHAR long.
     * @throws CouponException Thrown if description is below MIN_CHAR.
     */
    public void setDescription(String description) throws CouponException {
        CHECK_PARAM(description, CouponException.MSG_DESCRIPTION);
        this.description = description;
    }

    /*-------------------- Price --------------------*/

    /**
     * Getter of the price for this Coupon.
     *
     * @return double price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter for the description of this Coupon.
     *
     * @param price double price.
     * @throws CouponException Thrown if price is invalid.
     */
    public void setPrice(double price) throws CouponException {
        if (price < MIN_PRICE) {
            throw new CouponException(CouponException.MSG_PRICE);
        }
        this.price = price;
    }

    /*-------------------- Image URL --------------------*/

    /**
     * Getter of the image URL for this Coupon.
     *
     * @return String image URL source.
     */
    public String getImage() {
        return image;
    }

    /**
     * Setter for the image URL of this Coupon.
     *
     * @param image String image URL source.
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Getter of the Customers Collection of this Coupon.
     *
     * @return Collection<CustomerCoupon> the customers who bought this Coupon.
     */
    public Collection<CustomerCoupon> getCustomers() {
        return this.customers;
    }

    /**
     * Setter for the Customers Collection of this Coupon.
     *
     * @param customers Collection<CustomerCoupon> the customers who bought this Coupon.
     * @throws CouponException Thrown if Collection is invalid.
     */
    public void setCustomers(Collection<CustomerCoupon> customers) throws CouponException {
        if (null == customers) {
            throw new CouponException(CouponException.MSG_CUSTOMERS);
        }
        this.customers = customers;
    }
}


