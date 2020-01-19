package com.og.CouponSystemJB.entity;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- fasterxml --------------------*/

/*---------- jackson ----------*/

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.og.CouponSystemJB.entity.exception.ClientException;

/*-------------------- javax --------------------*/
import javax.persistence.*;

/**
 * Mapped Super Class for Hibernate. Client represents an abstract super class entity which all entities with roles
 * inherit from. This class will hold three fields that all entities with roles share which are id, email and password.
 */
@MappedSuperclass
@JsonIgnoreProperties("hibernateLazyInitializer")
public abstract class Client {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Minimum number of String characters for Client's parameters.
     */
    public static final int MIN_CHAR = 5;

    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function that will throw a ClientException if one of the parameters of Client is invalid.
     * (null or below 5 characters)
     *
     * @param param The parameter to check.
     * @param msg   Exception message of which parameter is being checked.
     * @throws ClientException Will be thrown with relevant message.
     */
    private static void CHECK_PARAM(String param, String msg) throws ClientException {
        if (null == param) {
            throw new ClientException(msg + ClientException.MSG_NULL_PARAM);
        }
        if (param.length() < MIN_CHAR) {
            throw new ClientException(msg + ClientException.MSG_MIN_PARAM);
        }
    }
    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /**
     * Id long of the Client in their Corresponding DB table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    protected long id;

    /**
     * The contact e-mail of the Client.(Must be at least MIN_CHAR characters).
     */
    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    /**
     * Password of the Client in the DB.(Must be at least MIN_CHAR characters).
     */
    @Column(name = "password", nullable = false)
    protected String password;

    /*----------------- Getters / Setters ---------------------------------------------------------------------------*/


    /*---------------------- Id ---------------------------*/

    /**
     * Getter for Client's Id.
     *
     * @return long id in the corresponding table.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for Client's Id.
     *
     * @param id long id in the corresponding table.
     */
    public void setId(long id) {
        this.id = id;
    }

    /*---------------------- Email ---------------------------*/

    /**
     * Getter for Client's email.
     *
     * @return String email of the Client.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Setter for Client's email. (MIN_CHAR).
     *
     * @param email String new email for the Client.
     * @throws ClientException Thrown if parameter is invalid.
     */
    public void setEmail(String email) throws ClientException {
        CHECK_PARAM(email, ClientException.MSG_EMAIL);
        this.email = email;
    }

    /*---------------------- Password ---------------------------*/

    /**
     * Getter for Client's password.
     *
     * @return String password of the Client.
     */
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    /**
     * Setter for Client's password. (MIN_CHAR).
     *
     * @param password String new password for the Client.
     * @throws ClientException Thrown if parameter is invalid.
     */
    @JsonProperty
    public void setPassword(String password) throws ClientException {
        CHECK_PARAM(password, ClientException.MSG_PASSWORD);
        this.password = password;
    }
}
