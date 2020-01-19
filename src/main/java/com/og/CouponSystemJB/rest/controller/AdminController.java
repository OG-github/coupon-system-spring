package com.og.CouponSystemJB.rest.controller;


import com.og.CouponSystemJB.entity.Company;
import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.Customer;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.rest.controller.exception.ClientSessionException;
import com.og.CouponSystemJB.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    private Map<String, ClientSession> tokensMap;

    @Autowired
    public AdminController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    private ClientSession getSession(String token) {
        return tokensMap.get(token);
    }

    @GetMapping("/companies/findAll")
    public ResponseEntity findAllCompanies(@CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();
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

    @GetMapping("/customers/findAll")
    public ResponseEntity findAllCustomers(@CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();
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

    @GetMapping("/coupons/findAll")
    public ResponseEntity findAllCoupons(@CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();
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

    @GetMapping("/companies/findByEmail")
    public ResponseEntity findCompanyByEmail(@RequestParam String email, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();
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

    @GetMapping("/customers/findByEmail")
    public ResponseEntity findCustomerByEmail(@RequestParam String email, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();
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

    @PostMapping("/companies/add")
    public ResponseEntity addCompany(@RequestBody Company company, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();
            Company newCompany = adminService.addCompany(company);
            return ResponseEntity.ok(newCompany);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }


    @PostMapping("/customers/add")
    public ResponseEntity addCustomer(@RequestBody Customer customer, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();
            Customer newCustomer = adminService.addCustomer(customer);
            return ResponseEntity.ok(newCustomer);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }

    }

    @DeleteMapping("/companies/delete")
    public ResponseEntity deleteCompanyByEmail(@RequestParam String email, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();

            adminService.deleteCompanyByEmail(email);
            return ResponseEntity.ok("Company Removed Successfully");
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/customers/delete")
    public ResponseEntity deleteCustomerByEmail(@RequestParam String email, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            AdminServiceImpl adminService = (AdminServiceImpl) session.getService();

            adminService.deleteCustomerByEmail(email);
            return ResponseEntity.ok("Customer Removed Successfully");
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }
}
