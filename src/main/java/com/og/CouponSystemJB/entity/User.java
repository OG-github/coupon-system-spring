package com.og.CouponSystemJB.entity;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- hibernate --------------------*/

import com.og.CouponSystemJB.entity.exception.UserException;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

/*-------------------- javax --------------------*/

/*---------- persistence ----------*/
import javax.persistence.*;

/**
 * Entity model(Hibernate) for the table "user". User will represent all our Clients in the system that are
 * registered to different roles and are attempting to login to our system. User will login details like email and
 * password but also the role of each Client in the system.
 */
@Entity
@Table(name = "user")
public class User {

    /*----------------- Roles ----------------------------------------------------------------------------------------*/

    /**
     * String for Company Role.
     */
    public static final String ROLE_COMPANY = "ROLE_COMPANY";

    /**
     * String for Customer Role.
     */
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

    /**
     * String for Admin Role.
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Minimum number of String characters for User's parameters.
     */
    public static final int MIN_CHAR = 5;

    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function that will throw a UserException if one of the parameters of User is invalid.
     * (null or below 5 characters)
     *
     * @param param The parameter to check.
     * @param msg   Exception message of which parameter is being checked.
     * @throws UserException Will be thrown with relevant message.
     */
    private static void CHECK_PARAM(String param, String msg) throws UserException {
        if (null == param) {
            throw new UserException(msg + UserException.MSG_NULL_PARAM);
        }
        if (param.length() < MIN_CHAR) {
            throw new UserException(msg + UserException.MSG_MIN_PARAM);
        }
    }

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /**
     * Id of the User.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * Email of the User. (Email is unique).
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Login password for the User.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * User account not expired.
     */
    @Column(name = "notExpired", nullable = false)
    private boolean notExpired;

    /**
     * User account not locked.
     */
    @Column(name = "notLocked", nullable = false)
    private boolean notLocked;

    /**
     * User account with not expired credentials.
     */
    @Column(name = "credentialsNotExpired", nullable = false)
    private boolean credentialsNotExpired;

    /**
     * User account enabled.
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    /**
     * The Client this User entity represents. Hibernate will go to the correct corresponding table according to the
     * Roles defined in the Meta Definition and its Meta Values.
     */
    @Any(metaColumn = @Column(name = "role"))
    @AnyMetaDef(idType = "long", metaType = "string",
            metaValues = {
                    @MetaValue(value = ROLE_COMPANY, targetEntity = Company.class),
                    @MetaValue(value = ROLE_CUSTOMER, targetEntity = Customer.class),
                    @MetaValue(value = ROLE_ADMIN, targetEntity = Admin.class)
            })
    @JoinColumn(name = "client_id")
    private Client client;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Empty constructor for Hibernate.
     */
    public User() {
        /* For Hibernate */
    }

    /**
     * Full constructor to initialize a new User and all its fields.
     *
     * @param email                 String email (unique) of the User.
     * @param password              String password of the User.
     * @param notExpired            Boolean is account not-expired.
     * @param notLocked             Boolean is account not-locked.
     * @param credentialsNotExpired Boolean is account's credentials not-expired.
     * @param enabled               Boolean is account enabled.
     * @param client                Client this User represents in our System.
     */
    public User(String email, String password, boolean notExpired, boolean notLocked, boolean credentialsNotExpired,
                boolean enabled, Client client) throws UserException {
        CHECK_PARAM(email, UserException.MSG_EMAIL);
        this.email = email;
        CHECK_PARAM(email, UserException.MSG_PASSWORD);
        this.password = password;
        this.notExpired = notExpired;
        this.notLocked = notLocked;
        this.credentialsNotExpired = credentialsNotExpired;
        this.enabled = enabled;
        this.client = client;
    }

    /*----------------- Getters / Setters ----------------------------------------------------------------------------*/

    /*-------------------- Id --------------------*/

    /**
     * Getter for User's Id.
     *
     * @return long id in the user table.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for User's Id.
     *
     * @param id long id in the user table.
     */
    public void setId(long id) {
        this.id = id;
    }

    /*-------------------- Email --------------------*/

    /**
     * Getter for User's email.
     *
     * @return String email of the User.
     */
    public String getEmail() {
        this.email = this.client.getEmail();
        return this.email;
    }

    /**
     * Setter for Client's email. (MIN_CHAR).
     */
    public void setEmail() {
        this.email = this.client.getEmail();
    }

    /*-------------------- Password --------------------*/

    /**
     * Getter for User's password.
     *
     * @return String password of the User.
     */
    public String getPassword() {
        this.password = this.client.getPassword();
        return this.password;
    }

    /**
     * Setter for User's password. (MIN_CHAR).
     *
     */
    public void setPassword()  {
        this.password = this.client.getPassword();
    }

    /*-------------------- Client --------------------*/

    /**
     * Getter for User's Client.
     *
     * @return Client this User represents from one of the corresponding tables.
     */
    public Client getClient() {
        return client;
    }

    /**
     * Setter for User's Client.
     *
     * @param client Client this User represents from one of the corresponding tables.
     * @throws UserException Thrown if parameter is invalid.
     */
    public void setClient(Client client) throws UserException {
        if (null == client) {
            throw new UserException(UserException.MSG_Client + UserException.MSG_NULL_PARAM);
        }
        this.client = client;
    }

    /*-------------------- Not Expired --------------------*/

    /**
     * Getter for is User account not expired.
     *
     * @return boolean false if expired.
     */
    public boolean isNotExpired() {
        return notExpired;
    }

    /**
     * Setter for is User account not expired.
     *
     * @param notExpired boolean false if expired.
     */
    public void setNotExpired(boolean notExpired) {
        this.notExpired = notExpired;
    }

    /*-------------------- Not Locked --------------------*/

    /**
     * Getter for is User account not locked.
     *
     * @return boolean false if User account locked.
     */
    public boolean isNotLocked() {
        return notLocked;
    }

    /**
     * Setter for is User account not locked.
     *
     * @param notLocked boolean false if User account locked.
     */
    public void setNotLocked(boolean notLocked) {
        this.notLocked = notLocked;
    }

    /*-------------------- Credentials not Expired --------------------*/

    /**
     * Getter for is User account credentials not expired.
     *
     * @return boolean false if User account credentials expired.
     */
    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    /**
     * Setter for is User account credentials not expired.
     *
     * @param credentialsNotExpired boolean false if User account credentials expired.
     */
    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }

    /*-------------------- Enabled --------------------*/

    /**
     * Getter for is User account enabled.
     *
     * @return boolean true if User account enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Setter for is User account enabled.
     * @param enabled boolean true if User account enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

