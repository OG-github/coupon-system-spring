package com.og.CouponSystemJB.entity;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- javax --------------------*/
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.exception.AdminException;

/**
 * Entity model(Hibernate) for the table "admin". Admin represents a Client with the role of administrator in our
 * system. The only field Admin has is name which is unique in the table. There are two constructors, one is empty
 * for Hibernate and the other a full one to initialize a new Admin with all the fields. Fields must be at least
 * MIN_CHAR otherwise this class will throw an AdminException.
 */
@Entity
@Table(name = "admin")
public class Admin extends Client {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Minimum number of String characters for Admin's parameters.
     */
    public static final int MIN_CHAR = 5;

    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function that will throw a AdminException if one of the parameters of Admin is invalid.
     * (null or below 5 characters)
     *
     * @param param The parameter to check.
     * @param msg   Exception message of which parameter is being checked.
     * @throws AdminException Will be thrown with relevant message.
     */
    private static void CHECK_PARAM(String param, String msg) throws AdminException {
        if (null == param) {
            throw new AdminException(msg + AdminException.MSG_NULL_PARAM);
        }
        if (param.length() < MIN_CHAR) {
            throw new AdminException(msg + AdminException.MSG_MIN_PARAM);
        }
    }

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /**
     * The name representing this Admin. Must be unique and is not nullable.
     */
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Empty constructor for Hibernate.
     */
    public Admin() {
        /* For Hibernate */
    }

    /**
     * Full constructor for Admin. Will initiate name for Admin and also the fields of the super class Client email
     * and password.
     *
     * @param name     String name of the Admin. (unique, non null, MIN_CHAR).
     * @param email    String email of the Admin. (unique, non null, MIN_CHAR).
     * @param password String password of the Admin. (non null, MIN_CHAR).
     * @throws AdminException Thrown if parameter is invalid.
     */
    public Admin(String name, String email, String password) throws AdminException {
        CHECK_PARAM(name, AdminException.MSG_NAME);
        this.name = name;
        CHECK_PARAM(email, AdminException.MSG_EMAIL);
        this.email = email;
        CHECK_PARAM(password, AdminException.MSG_PASSWORD);
        this.password = password;
    }

    /*----------------- Getters / Setters ---------------------------------------------------------------------------*/


    /*---------------------- Name ---------------------------*/

    /**
     * Getter for Admin's name.
     *
     * @return String name of the Admin.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for Admin's name. (MIN_CHAR).
     *
     * @param name String name of the Company
     * @throws AdminException Thrown if parameter is invalid.
     */
    public void setName(String name) throws AdminException {
        CHECK_PARAM(name, AdminException.MSG_NAME);
        this.name = name;
    }
}
