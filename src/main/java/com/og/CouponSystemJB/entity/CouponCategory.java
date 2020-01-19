package com.og.CouponSystemJB.entity;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the type of shopping category a Coupon is attributed to.
 * The enums here work with values of int, since the Coupon class uses int in
 * setting category parameter.
 *
 * @author Alex Jones
 */
public enum CouponCategory {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /* This Enum category will give value to the different categories. */
    NOT_EXIST(-1),
    TRAVELING(1),
    FOOD(2),
    ELECTRICITY(3),
    HEALTH(4),
    SPORTS(5),
    CAMPING(6),
    FASHION(7),
    STUDIES(8);

    /*----------------- Parameters --------------------------------------------------------------------------------------*/

    /* Represents the value of the category in ints */
    private final int category;

    /* Holds the int values of the Enum Types */
    private static Map<Integer, CouponCategory> map = new HashMap<>();

    /*----------------- Constructors ------------------------------------------------------------------------------------*/

    private CouponCategory(int category) {
        this.category = category;
    }

    // Initialize the Map holding the int as keys
    static {
        for (CouponCategory couponCategory : CouponCategory.values()) {
            map.put(couponCategory.category, couponCategory);
        }
    }

    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /**
     * Static function to get a CouponCategory from an int. If int doesen't match
     * NOT_EXIST will be returned.
     *
     * @param category the int representing the CouponCategory.
     * @return CouponCategory based on int or NOT_EXIST
     */
    public static CouponCategory valueOf(int category) {
        CouponCategory couponCategory = map.get(category); // will be null if not found
        if (null == couponCategory || NOT_EXIST.categoryValue() == category) {
            return NOT_EXIST;
        }
        return couponCategory;
    }

    /**
     * Retrieves the value of the category enum in int.
     *
     * @return int representing the corresponding category
     */
    public int categoryValue() {
        return category;
    }
}

