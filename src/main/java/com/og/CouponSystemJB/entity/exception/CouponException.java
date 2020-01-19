package com.og.CouponSystemJB.entity.exception;

/**
 * This will represent Exceptions that are related to the Coupon entity.
 */
public class CouponException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Exception message if a parameter is null.
     */
    public static String MSG_NULL_PARAM = "Null. ";

    /**
     * Exception message if a parameter is below minimun characters (MIN_CHAR).
     */
    public static String MSG_MIN_PARAM = "Below minimum characters. ";

    /**
     * Exception message if Company is invalid.
     */
    public static String MSG_COMPANY = "Invalid Company: ";

    /**
     * Exception message if Company is invalid.
     */
    public static String MSG_TITLE = "Invalid title: ";

    /**
     * Exception message if StartDate is invalid.
     */
    public static String MSG_STARTDATE = "Invalid StartDate: ";

    /**
     * Exception message if EndDate is invalid.
     */
    public static String MSG_ENDDATE = "Invalid EndDate: ";

    /**
     * Exception message if Category is invalid.
     */
    public static String MSG_CATEGORY = "Invalid Category: ";

    /**
     * Exception message if Category is invalid.
     */
    public static String MSG_AMOUNT = "Invalid Amount: ";

    /**
     * Exception message if Description is invalid.
     */
    public static String MSG_DESCRIPTION = "Invalid Description: ";

    /**
     * Exception message if Price is invalid.
     */
    public static String MSG_PRICE = "Invalid Price: ";

    /**
     * Exception message if Customers Collection is invalid.
     */
    public static String MSG_CUSTOMERS = "Invalid Customers Collection: ";

    /**
     * Exception message if useCoupon in Customer Coupon has failed.
     */
    public static String MSG_CUSTOMER_COUPON = "Amount below 0 CustomerCoupon: ";


    public CouponException(String message) {
        super("Coupon Exception " + message);
    }
}
