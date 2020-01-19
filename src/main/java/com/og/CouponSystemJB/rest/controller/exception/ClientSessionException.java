package com.og.CouponSystemJB.rest.controller.exception;

public class ClientSessionException extends Exception {

    private static final String MSG = "Can't find session: ";

    public ClientSessionException() {
        super(ClientSessionException.MSG);
    }

    public ClientSessionException(String message) {
        super(MSG + message);
    }
}
