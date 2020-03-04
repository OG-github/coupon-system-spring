package com.og.CouponSystemJB.rest.controller;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.Map;

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.Customer;
import com.og.CouponSystemJB.entity.CustomerCoupon;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.rest.controller.exception.ClientSessionException;
import com.og.CouponSystemJB.service.CustomerServiceImpl;

/**
 * This Customer RESTful Controller will intercept all HTTP Customer requests. In order for the requests to be valid they
 * must provide a Cookie with a token that validates their HTTP session. If the request is valid this controller will
 * get the relevant ClientSession by using the provided token and will delegate the logic and handling of the request to
 * the CustomerService within the ClientSession.
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /* Component of tokens HashMap to provide the relevant ClientSession. */
    private Map<String, ClientSession> tokensMap;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Full Autowired constructor used automatically by Spring.
     *
     * @param tokensMap Component of tokens HashMap.
     */
    @Autowired
    public CustomerController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /**
     * Helper function that will return a ClientSession from the tokens HashMap by using the provided token. Each
     * ClientSession is unique with a unique EntityService, and is mapped to a unique token in the map.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return Relevant ClientSession with the correct EntityService.
     * @throws ClientSessionException Thrown if ClientSession was not located by using the provided token.
     */
    private ClientSession getSession(String token) throws ClientSessionException {
        ClientSession session = tokensMap.get(token);
        if (null != session) {
            return session;
        }
        throw new ClientSessionException();
    }

    /**
     * Helper function that will return the CustomerServiceImpl from the ClientSession it gets from the tokens Map, based
     * on the provided token.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return CustomerServiceImpl the specific service for a specific Customer User.
     * @throws ClientSessionException Thrown if ClientSession was not located by using the provided token.
     */
    private CustomerServiceImpl getService(String token) throws ClientSessionException {
        ClientSession session = this.getSession(token);
        if (null == session) {
            throw new ClientSessionException();
        }
        return (CustomerServiceImpl) session.getService();
    }

    /*----------------- RESTful --------------------------------------------------------------------------------------*/

    /*----------------- Post mappings -----------------------*/

    /**
     * @param title
     * @param token String token generated from the UUID class and given at login.
     * @return
     */
    @PostMapping("/purchase")
    public ResponseEntity purchase(@RequestParam String title, @CookieValue("randToken") String token) {
        try {
            CustomerServiceImpl service = this.getService(token);
            return ResponseEntity.ok(service.purchaseCoupon(title));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /*----------------- Get mappings -----------------------*/

    /**
     * @param token String token generated from the UUID class and given at login.
     * @return
     */
    @GetMapping("/get")
    public ResponseEntity getCustomer(@CookieValue("randToken") String token) {
        try {
            CustomerServiceImpl service = this.getService(token);
            return ResponseEntity.ok(service.getCustomer());
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * @param token String token generated from the UUID class and given at login.
     * @return
     */
    @GetMapping("/getAllCoupons")
    public ResponseEntity getAllCoupons(@CookieValue("randToken") String token) {
        try {
            CustomerServiceImpl service = this.getService(token);
            Collection<Coupon> allCoupons = service.findAllCoupons();
            if (allCoupons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(allCoupons);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * @param token String token generated from the UUID class and given at login.
     * @return
     */
    @GetMapping("/getCustomerCoupons")
    public ResponseEntity getCustomerCoupons(@CookieValue("randToken") String token) {
        try {
            CustomerServiceImpl service = this.getService(token);
            Collection<CustomerCoupon> customerCoupons = service.findCustomerCoupons();
            if (customerCoupons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(customerCoupons);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /*----------------- Put mappings -----------------------*/

    /**
     * @param customer
     * @param token    String token generated from the UUID class and given at login.
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Customer customer, @CookieValue("randToken") String token) {
        try {
            CustomerServiceImpl service = this.getService(token);
            return ResponseEntity.ok(service.updateCustomer(customer));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

}