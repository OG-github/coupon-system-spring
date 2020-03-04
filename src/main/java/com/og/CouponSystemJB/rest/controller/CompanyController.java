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

import com.og.CouponSystemJB.entity.Company;
import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.rest.controller.exception.ClientSessionException;
import com.og.CouponSystemJB.service.CompanyServiceImpl;


/**
 * This Company RESTful Controller will intercept all HTTP Company requests. In order for the requests to be valid they
 * must provide a Cookie with a token that validates their HTTP session. If the request is valid this controller will
 * get the relevant ClientSession by using the provided token and will delegate the logic and handling of the request to
 * the CompanyService within the ClientSession.
 */
@RestController
@RequestMapping("/api/company")
public class CompanyController {

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
    public CompanyController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
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
     * Helper function that will return the CompanyServiceImpl from the ClientSession it gets from the tokens Map, based
     * on the provided token.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return CompanyServiceImpl the specific service for a specific Company User.
     * @throws ClientSessionException Thrown if ClientSession was not located by using the provided token.
     */
    private CompanyServiceImpl getService(String token) throws ClientSessionException {
        ClientSession session = this.getSession(token);
        if (null == session) {
            throw new ClientSessionException();
        }
        return (CompanyServiceImpl) session.getService();
    }

    /*----------------- RESTful --------------------------------------------------------------------------------------*/

    /*----------------- Post mappings -----------------------*/

    /**
     * HTTP post request to add a new Coupon that is issued by a specific Company to the database. The new Coupon
     * must have a unique title that is not taken by another Coupon in our database and parameters that are not too
     * short. This method will return a ResponseEntity with the new Coupon from the database. In order for the
     * request to be valid a token who is mapped to a ClientSession with the correct EntityService must be provided.
     *
     * @param coupon JSON (without id) of Coupon in the body of the request.
     * @param token  String token generated from the UUID class and given at login.
     * @return ResponseEntity with the new Coupon from the database.
     */
    @PostMapping("/addCoupon")
    public ResponseEntity addCoupon(@RequestBody Coupon coupon, @CookieValue("randToken") String token) {
        try {
            CompanyServiceImpl companyService = this.getService(token);
            Coupon newCoupon = companyService.addCoupon(coupon);
            return ResponseEntity.ok(newCoupon);
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
    public ResponseEntity getCompany(@CookieValue("randToken") String token) {
        try {
            CompanyServiceImpl companyService = this.getService(token);
            return ResponseEntity.ok(companyService.getCompany());
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
            CompanyServiceImpl companyService = this.getService(token);
            Collection<Coupon> allCoupons = companyService.findAllCoupons();
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
    @GetMapping("/getCompanyCoupons")
    public ResponseEntity getCompanyCoupons(@CookieValue("randToken") String token) {
        try {
            CompanyServiceImpl companyService = this.getService(token);
            Collection<Coupon> companyCoupons = companyService.findCompanyCoupons();
            if (companyCoupons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(companyCoupons);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /*----------------- Put mappings -----------------------*/

    /**
     * HTTP put request to update an existing Coupon from a specific Company in the database. The Coupon must belong
     * to the issuing Company and the JSON representing it can not TODO check if can change title or anything else
     *
     * @param coupon
     * @param token  String token generated from the UUID class and given at login.
     * @return
     */
    @PutMapping("/updateCoupon")
    public ResponseEntity updateCoupon(@RequestBody Coupon coupon, @CookieValue("randToken") String token) {
        try {
            CompanyServiceImpl companyService = this.getService(token);
            return ResponseEntity.ok(companyService.updateCoupon(coupon));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * @param couponTitle
     * @param customerEmail
     * @param token         String token generated from the UUID class and given at login.
     * @return
     */
    @PutMapping("/useCoupon")
    public ResponseEntity useCoupon(@RequestParam String couponTitle, @RequestParam String customerEmail,
                                    @CookieValue("randToken") String token) {
        try {
            CompanyServiceImpl companyService = this.getService(token);
            return ResponseEntity.ok(companyService.useCoupon(couponTitle, customerEmail));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * @param company
     * @param token   String token generated from the UUID class and given at login.
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Company company, @CookieValue("randToken") String token) {
        try {
            CompanyServiceImpl companyService = this.getService(token);
            return ResponseEntity.ok(companyService.update(company));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /*----------------- Delete mappings -----------------------*/
    /**
     * @param title
     * @param token String token generated from the UUID class and given at login.
     * @return
     */
    @DeleteMapping("/deleteCouponByTitle")
    public ResponseEntity deleteCouponByTitle(@RequestParam String title, @CookieValue("randToken") String token) {
        try {
            CompanyServiceImpl companyService = this.getService(token);
            return ResponseEntity.ok(companyService.deleteCouponByTitle(title));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

}
