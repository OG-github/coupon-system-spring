package com.og.CouponSystemJB.rest.controller;

import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.Customer;
import com.og.CouponSystemJB.entity.CustomerCoupon;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.rest.controller.exception.ClientSessionException;
import com.og.CouponSystemJB.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private Map<String, ClientSession> tokensMap;

    @Autowired
    public CustomerController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }


    private ClientSession getSession(String token) {
        return tokensMap.get(token);
    }

    @GetMapping("/get")
    public ResponseEntity getCustomer(@CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CustomerServiceImpl service = (CustomerServiceImpl) session.getService();
            return ResponseEntity.ok(service.getCustomer());
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
            CustomerServiceImpl service = (CustomerServiceImpl) session.getService();
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

    @GetMapping("/getCustomerCoupons")
    public ResponseEntity getCustomerCoupons(@CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CustomerServiceImpl service = (CustomerServiceImpl) session.getService();
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

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody Customer customer, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CustomerServiceImpl service = (CustomerServiceImpl) session.getService();
            return ResponseEntity.ok(service.updateCustomer(customer));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity purchase(@RequestParam String title, @CookieValue("randToken") String token) {
        try {
            ClientSession session = this.getSession(token);
            if (null == session) {
                throw new ClientSessionException();
            }
            CustomerServiceImpl service = (CustomerServiceImpl) session.getService();
            return ResponseEntity.ok(service.purchaseCoupon(title));
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }
}