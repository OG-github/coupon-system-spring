package com.og.CouponSystemJB.entity;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- io ----------*/

import java.io.Serializable;

/*-------------------- javax --------------------*/

/*---------- persistence ----------*/
import javax.persistence.*;

/*-------------------- CouponSystemJB --------------------*/
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CouponException;
import com.og.CouponSystemJB.entity.exception.CustomerException;

/**
 * Entity model(Hibernate) for the table "customer_coupon". Represents a Coupon a Customer bought and more relevant
 * information about it. This entity will connect the relation mapping between Customers and Coupon.
 */
@Entity
@Table(name = "customer_coupon")
public class CustomerCoupon {


    /*----------------- Embedded Id ----------------------------------------------------------------------------------*/

    /**
     * This is a static nested class that will act as an embeddable Id in the CustomerCoupon entity. The Id will be
     * made by the foreign keys of the Customer who bought the Coupon and Id of the Coupon.
     */
    @Embeddable
    public static class CustomerCouponId implements Serializable {

        /*----------------- Foreign Keys -----------------------*/

        /**
         * Foreign key of the Customer who bought the Coupon.
         */
        @Column(name = "fk_customerId")
        protected Long customerId;

        /**
         * Foreign key of the Coupon.
         */
        @Column(name = "fk_couponId")
        protected Long couponId;

        /*----------------- Constructor -----------------------*/

        /**
         * Empty constructor for Hibernate.
         */
        public CustomerCouponId() {
            /* For Hibernate */
        }

        /**
         * Full constructor from foreign keys.
         *
         * @param customerId Customer Id.
         * @param couponId   Coupon Id.
         */
        CustomerCouponId(Long customerId, Long couponId) {
            this.customerId = customerId;
            this.couponId = couponId;
        }

        /*-------------------- Object overrides necessary for embeddable Id --------------------*/

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((this.couponId == null) ? 0 : this.couponId.hashCode());

            result = prime * result
                    + ((this.customerId == null) ? 0 : this.customerId.hashCode());

            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            CustomerCouponId other = (CustomerCouponId) obj;

            if (this.couponId == null) {
                if (other.couponId != null)
                    return false;
            }
            else if (!this.couponId.equals(other.couponId))
                return false;

            if (this.customerId == null) {
                if (other.customerId != null)
                    return false;
            }
            else if (!this.customerId.equals(other.customerId))
                return false;

            return true;
        }
    }

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /**
     * Embeddable Id made by the static nested class of this class.
     */
    @EmbeddedId
    private CustomerCouponId id;

    /**
     * Customer entity who bought this Coupon.
     */
    @ManyToOne
    @JoinColumn(name = "fk_customerId", insertable = false, updatable = false)
    private Customer customer;

    /**
     * The Coupon entity that was bought.
     */
    @ManyToOne
    @JoinColumn(name = "fk_couponId", insertable = false, updatable = false)
    private Coupon coupon;

    /**
     * Amount of the Coupon this Customer bought and own.
     */
    @Column(name = "amount")
    private int amount;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Empty constructor for Hibernate.
     */
    public CustomerCoupon() {
        /* For Hibernate */
    }

    /**
     * Full constructor with Customer entity and Coupon entity.
     *
     * @param customer Customer entity buying the Coupon.
     * @param coupon   Coupon entity that is bought.
     * @throws CustomerException Thrown if Customer is null.
     * @throws CouponException   Thrown if Coupon is null.
     */
    public CustomerCoupon(Customer customer, Coupon coupon) throws CustomerException, CouponException {
        if (null == customer) {
            throw new CustomerException(CustomerException.MSG_NULL_PARAM);
        }
        if (null == coupon) {
            throw new CouponException(CompanyException.MSG_NULL_PARAM);
        }
        this.id = new CustomerCouponId(customer.getId(), coupon.getId());
        this.customer = customer;
        this.coupon = coupon;
        this.amount = 0;

        this.customer.getCoupons().add(this);
        this.coupon.getCustomers().add(this);

    }

    /*----------------- Getters / Setters ----------------------------------------------------------------------------*/

    /**
     * Getter for the amount purchased.
     *
     * @return int amount owned by Customer of this Coupon.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Purchase another Coupon and add to the amount this Customer owns.
     */
    public void purchaseCoupon() {
        ++this.amount;
    }

    /**
     * Use the Coupon this Customer has bought and decrease the amount owned.
     *
     * @throws CouponException If amount is below or equal to 0.
     */
    public void useCoupon() throws CouponException {
        if (this.amount > 0) {
            --this.amount;
        }
        else {
            throw new CouponException(CouponException.MSG_CUSTOMER_COUPON);
        }
    }

    @JsonProperty
    public Coupon getCoupon() {
        return coupon;
    }

    @JsonIgnore
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}

