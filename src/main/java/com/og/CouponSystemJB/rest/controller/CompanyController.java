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
     * must have a unique title that is not taken by another Coupon in our database. In addition parameters can not be
     * too short and null and the amount and price must be above 0. This method will return a ResponseEntity with the
     * new Coupon from the database. In order for the request to be valid a token who is mapped to a ClientSession
     * with the correct EntityService must be provided.
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
     * HTTP get request to retrieve the Company of the current Company User from the database. The Company that will
     * be returned will only be the Company of the current User, and not any other Company. This method will return a
     * ResponseEntity with the relevant data. In order for the request to be valid a token who is mapped to a
     * ClientSession with the correct EntityService must be provided.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with the Company from the database.
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
     * HTTP get request to retrieve all Coupons from the database. This method will return a ResponseEntity with the
     * relevant data. In order for the request to be valid a token who is mapped to a ClientSession with the correct
     * EntityService must be provided.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with the all the Coupons from the database.
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
     * HTTP get request to retrieve all Coupons issued by a specific Company from the database. This method will
     * return a ResponseEntity with the relevant data. In order for the request to be valid a token who is mapped to
     * a ClientSession with the correct EntityService must be provided.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with the all the Coupons of a Company from the database.
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
     * to the issuing Company and the JSON representing it can not have an ID, the Coupon will be identified by its
     * title (unique parameter) and the parameters can not be null or too short. In addition price and amount must be
     * above 0. This method will return a ResponseEntity with the relevant data. In order for the request to be valid
     * a token who is mapped to a ClientSession with the correct EntityService must be provided.
     *
     * @param coupon JSON (without id) of Coupon in the body of the request.
     * @param token  String token generated from the UUID class and given at login.
     * @return ResponseEntity with the updated Coupon.
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
     * HTTP put request to update that a Customer has used a Coupon they have purchased. The Coupon will be
     * identified by its title and the Customer by their email. The system will update the amount of the Coupon
     * available to use for the Customer. This method will return a ResponseEntity with the relevant data. In order
     * for the request to be valid a token who is mapped to a ClientSession with the correct EntityService must be
     * provided.
     *
     * @param couponTitle   String title of the Coupon (unique parameter) to be used by the Customer.
     * @param customerEmail String email of the Customer whom wants to use the Coupon.
     * @param token         String token generated from the UUID class and given at login.
     * @return ResponseEntity with message about the use.
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
     * HTTP put request to update a Company in the database. The JSON representing the Company must not have an ID,
     * the parameters can not be too short, if email is changed it must not be taken by another User in the system
     * and the JSON can not include the Company's Coupons. This method will return a ResponseEntity with the relevant
     * data. In order for the request to be valid a token who is mapped to a ClientSession with the correct
     * EntityService must be provided.
     *
     * @param company JSON of Company in the body of the request.
     * @param token   String token generated from the UUID class and given at login.
     * @return ResponseEntity with the updated Company.
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
