package com.og.CouponSystemJB.rest.controller;

import com.og.CouponSystemJB.entity.Company;
import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.rest.controller.exception.ClientSessionException;
import com.og.CouponSystemJB.service.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private Map<String, ClientSession> tokensMap;

    @Autowired
    public CompanyController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }

    private ClientSession getSession(String token) {
        return tokensMap.get(token);
    }

    @GetMapping("/get")
    public ResponseEntity getCompany(@CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CompanyServiceImpl companyService = (CompanyServiceImpl) session.getService();
            return ResponseEntity.ok(companyService.getCompany());
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/getAllCoupons")
    public ResponseEntity getAllCoupons(@CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CompanyServiceImpl companyService = (CompanyServiceImpl) session.getService();
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

    @GetMapping("/getCompanyCoupons")
    public ResponseEntity getCompanyCoupons(@CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CompanyServiceImpl companyService = (CompanyServiceImpl) session.getService();
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

    @PostMapping("/addCoupon")
    public ResponseEntity addCoupon(@RequestBody Coupon coupon, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CompanyServiceImpl companyService = (CompanyServiceImpl) session.getService();
            Coupon newCoupon = companyService.addCoupon(coupon);
            return ResponseEntity.ok(newCoupon);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteCouponByTitle")
    public ResponseEntity deleteCouponByTitle(@RequestParam String title, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CompanyServiceImpl companyService = (CompanyServiceImpl) session.getService();
            return ResponseEntity.ok(companyService.deleteCouponByTitle(title));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/updateCoupon")
    public ResponseEntity updateCoupon(@RequestBody Coupon coupon, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CompanyServiceImpl companyService = (CompanyServiceImpl) session.getService();
            return ResponseEntity.ok(companyService.updateCoupon(coupon));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/useCoupon")
    public ResponseEntity useCoupon(@RequestParam String couponTitle, @RequestParam String customerEmail,
                                    @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CompanyServiceImpl companyService = (CompanyServiceImpl) session.getService();
            return ResponseEntity.ok(companyService.useCoupon(couponTitle, customerEmail));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody Company company, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CompanyServiceImpl companyService = (CompanyServiceImpl) session.getService();
            return ResponseEntity.ok(companyService.update(company));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }


}
