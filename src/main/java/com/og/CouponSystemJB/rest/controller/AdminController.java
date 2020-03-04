package com.og.CouponSystemJB.rest.controller;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.Company;
import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.Customer;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.rest.controller.exception.ClientSessionException;
import com.og.CouponSystemJB.service.AdminServiceImpl;

/**
 * This Admin RESTful Controller will intercept all HTTP Admin requests. In order for the requests to be valid they
 * must provide a Cookie with a token that validates their HTTP session. If the request is valid this controller will
 * get the relevant ClientSession by using the provided token and will delegate the logic and handling of the request to
 * the AdminService within the ClientSession.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /* Deletion successful message for deleting Company from the database. */
    private static final String DLT_COMP_MSG = "Company Removed Successfully";

    /* Deletion successful message for deleting Customer from the database. */
    private static final String DLT_CUST_MSG = "Customer Removed Successfully";

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
    public AdminController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
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
     * Helper function that will return the AdminServiceImpl from the ClientSession it gets from the tokens Map, based
     * on the provided token.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return AdminServiceImpl the specific service for a specific Admin User.
     * @throws ClientSessionException Thrown if ClientSession was not located by using the provided token.
     */
    private AdminServiceImpl getService(String token) throws ClientSessionException {
        ClientSession session = this.getSession(token);
        if (null == session) {
            throw new ClientSessionException();
        }
        return (AdminServiceImpl) session.getService();
    }

    /*----------------- RESTful --------------------------------------------------------------------------------------*/

    /*----------------- Post mappings -----------------------*/

    /**
     * HTTP post request to add a new Company to the database. The new Company must have an email that is not
     * registered in the database by any User and also its parameters can not be too short. This method will return a
     * ResponseEntity with the new Company from the database. In order or the request to be valid a token who is
     * mapped to a ClientSession with the correct EntityService must be provided.
     *
     * @param company JSON (without id and Coupons) of Company in the body of the request.
     * @param token   String token generated from the UUID class and given at login.
     * @return ResponseEntity with the new Company from the database.
     */
    @PostMapping("/companies/add")
    public ResponseEntity addCompany(@RequestBody Company company, @CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            Company newCompany = adminService.addCompany(company);
            return ResponseEntity.ok(newCompany);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * HTTP post request to add a new Customer to the database. The new Customer must have an email that is not
     * registered in the database by any User and also its parameters can not be too short. This method will return a
     * ResponseEntity with the new Customer from the database. In order for the request to be valid a token who is
     * mapped to a ClientSession with the correct EntityService must be provided.
     *
     * @param customer JSON (without id and CustomerCoupons) of Customer in the body of the request.
     * @param token    String token generated from the UUID class and given at login.
     * @return ResponseEntity with the new Customer from the database.
     */
    @PostMapping("/customers/add")
    public ResponseEntity addCustomer(@RequestBody Customer customer, @CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            Customer newCustomer = adminService.addCustomer(customer);
            return ResponseEntity.ok(newCustomer);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }

    }

    /*----------------- Get mappings -----------------------*/

    /**
     * HTTP get request to retrieve all Companies from the database. This method will return a ResponseEntity with the
     * relevant data. In order for the request to be valid a token who is mapped to a ClientSession with the correct
     * EntityService must be provided.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with the all the Companies from the database.
     */
    @GetMapping("/companies/findAll")
    public ResponseEntity findAllCompanies(@CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            Collection<Company> allCompanies = adminService.findAllCompanies();
            if (allCompanies.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(allCompanies);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * HTTP get request to retrieve all Customers from the database. This method will return a ResponseEntity with the
     * relevant data. In order for the request to be valid a token who is mapped to a ClientSession with the correct
     * EntityService must be provided.
     *
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with the all the Customers from the database.
     */
    @GetMapping("/customers/findAll")
    public ResponseEntity findAllCustomers(@CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            Collection<Customer> allCustomers = adminService.findAllCustomers();
            if (allCustomers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(allCustomers);
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
    @GetMapping("/coupons/findAll")
    public ResponseEntity findAllCoupons(@CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            Collection<Coupon> allCoupons = adminService.findAllCoupons();
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
     * HTTP get request to retrieve a Company by their User email from the database. This method will return a
     * ResponseEntity with the relevant data. In order for the request to be valid a token who is mapped to a
     * ClientSession with the correct EntityService must be provided.
     *
     * @param email String email of the Company from the database.
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with the Company from the database.
     */
    @GetMapping("/companies/findByEmail")
    public ResponseEntity findCompanyByEmail(@RequestParam String email, @CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            Optional<Company> company = adminService.findCompanyByEmail(email);
            if (!company.isPresent()) {
                return ResponseEntity.ok("Company Email not found ");
            }
            return ResponseEntity.ok(company.get());
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * HTTP get request to retrieve a Customer by their User email from the database. This method will return a
     * ResponseEntity with the relevant data. In order for the request to be valid a token who is mapped to a
     * ClientSession with the correct EntityService must be provided.
     *
     * @param email String email of the Customer from the database.
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with the Company from the database.
     */
    @GetMapping("/customers/findByEmail")
    public ResponseEntity findCustomerByEmail(@RequestParam String email, @CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            Optional<Customer> customer = adminService.findCustomerByEmail(email);
            if (!customer.isPresent()) {
                return ResponseEntity.ok("Customer Email not found ");
            }
            return ResponseEntity.ok(customer.get());
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /*----------------- Delete mappings -----------------------*/

    /**
     * HTTP delete request to delete a Company by their User email from the database. This method will return a
     * ResponseEntity with a deletion message. In order for the request to be valid a token who is mapped to a
     * ClientSession with the correct EntityService must be provided.
     *
     * @param email String email of the Company to delete from the database.
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with a deletion message.
     */
    @DeleteMapping("/companies/delete")
    public ResponseEntity deleteCompanyByEmail(@RequestParam String email, @CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            adminService.deleteCompanyByEmail(email);
            return ResponseEntity.ok(DLT_COMP_MSG);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    /**
     * HTTP delete request to delete a Customer by their User email from the database. This method will return a
     * ResponseEntity with a deletion message. In order for the request to be valid a token who is mapped to a
     * ClientSession with the correct EntityService must be provided.
     *
     * @param email String email of the Customer to delete from the database.
     * @param token String token generated from the UUID class and given at login.
     * @return ResponseEntity with a deletion message.
     */
    @DeleteMapping("/customers/delete")
    public ResponseEntity deleteCustomerByEmail(@RequestParam String email, @CookieValue("randToken") String token) {
        try {
            AdminServiceImpl adminService = this.getService(token);
            adminService.deleteCustomerByEmail(email);
            return ResponseEntity.ok(DLT_CUST_MSG);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }
}
