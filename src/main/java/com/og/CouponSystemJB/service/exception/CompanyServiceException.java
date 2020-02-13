package com.og.CouponSystemJB.service.exception;

/**
 * This will represent Exceptions that are related to the CompanyService.
 */
public class CompanyServiceException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /*---------------------- User ---------------------------*/

    /**
     * Exception message if User was not found in DB.
     */
    public static final String USER_NOT_FOUND = "Update User info failed. User was not found in DB ";

    /*---------------------- General ---------------------------*/

    /**
     * Exception message if invalid Company.
     */
    public static final String INVALID_COMP = "Invalid Company: ";

    /**
     * Exception message for updating Company in DB failed.
     */
    public static final String UPDATE_COMP = "Update Company fail: ";

    /**
     * Exception message for deleting a Coupon for a Company in DB failed.
     */
    public static final String DLT = "Delete fail: ";

    /**
     * Exception message if null Company.
     */
    public static final String NULL_COMP = "null Company ";

    /**
     * Exception message for null name.
     */
    public static final String NULL_NAME = "null name ";

    /**
     * Exception message for null email.
     */
    public static final String NULL_EMAIL = "null email ";

    /**
     * Exception message for null password.
     */
    public static final String NULL_PASSWORD = "null password ";

    /**
     * Exception message if trying to change Email.
     */
    public static final String EMAIL_CHANGE = "Can not change email ";

    /**
     * Exception message if Name is too short.
     */
    public static final String NAME_SHORT = "Name too short ";

    /**
     * Exception message for Company name already taken by another Company.
     */
    public static final String COMP_NAME_TAKEN = "name already taken ";

    /**
     * Exception message if Password is too short.
     */
    public static final String PASSWORD_SHORT = "Password too short ";

    /*---------------------- Coupon ---------------------------*/

    /**
     * Exception message if invalid Coupon.
     */
    public static final String INVALID_COUP = "Invalid Coupon: ";

    /**
     * Exception message if null Coupon.
     */
    public static final String NULL_COUP = "null Coupon ";

    /**
     * Exception message if null Title.
     */
    public static final String NULL_TITLE = "null Title ";

    /**
     * Exception message if null startDate.
     */
    public static final String NULL_STARTDATE = "null startDate ";

    /**
     * Exception message if null endDate.
     */
    public static final String NULL_ENDDATE = "null endDate ";

    /**
     * Exception message if null or invalid Category.
     */
    public static final String INVALID_CATEGORY = "null or invalid Category ";

    /**
     * Exception message if null Description.
     */
    public static final String NULL_DESCRIPTION = "null Description ";

    /**
     * Exception message if Title too short.
     */
    public static final String TITLE_SHORT = "Title too short ";

    /**
     * Exception message if Amount too low.
     */
    public static final String AMOUNT_INVALID = "Coupon amount below zero ";

    /**
     * Exception message if Description too short.
     */
    public static final String DESCRIPTION_SHORT = "Description too short ";

    /**
     * Exception message if Price too low.
     */
    public static final String PRICE_INVALID = "Coupon price below zero ";

    /**
     * Exception message for Coupon title already taken by another Coupon.
     */
    public static final String COUP_TITLE_TAKEN = "Coupon title already taken ";

    /**
     * Exception message for Coupon update fail in the DB.
     */
    public static final String UPDATE_COUP = "Update Coupon failed: ";

    /**
     * Exception message for Coupon not found in the DB.
     */
    public static final String COUP_NOT_FOUND = "Coupon not found ";

    /**
     * Exception message if adding Coupon failed.
     */
    public static final String ADD_COUP = "Adding Coupon failed: ";

    /**
     * Exception message if using Coupon failed.
     */
    public static final String USE_COUP = "Use Coupon failed: ";

    /**
     * Exception message if Customer email is null.
     */
    public static final String CUST_EMAIL_NULL = "Customer email null ";

    /**
     * Exception message if Customer was not found in DB.
     */
    public static final String CUST_NOT_FOUND = "Customer not found ";

    /**
     * Exception message if Customer does not own a Coupon.
     */
    public static final String CUST_NOT_OWN_COUP = "Customer does not own this Coupon ";

    /**
     * Exception message if Customer does not have enough of a Coupon (Own previously but now amount is 0).
     */
    public static final String CUST_NO_COUP_LEFT = "Customer does not have enough Coupons ";

    public CompanyServiceException(String msg) {
        super(msg);
    }
}
