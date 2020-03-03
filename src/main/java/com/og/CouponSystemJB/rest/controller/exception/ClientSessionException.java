package com.og.CouponSystemJB.rest.controller.exception;

/**
 * This will represent Exceptions that are related to the ClientSession.
 */
public class ClientSessionException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /*---------------------- General ---------------------------*/

    private static final String MSG = "Can't find session: ";

    public ClientSessionException() {
        super(ClientSessionException.MSG);
    }

    public ClientSessionException(String message) {
        super(MSG + message);
    }
}
