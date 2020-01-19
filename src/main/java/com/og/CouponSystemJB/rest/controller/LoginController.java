package com.og.CouponSystemJB.rest.controller;

import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CustomerException;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.service.LoginService;
import com.og.CouponSystemJB.service.exception.LoginServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final String UUID_REPLACEMENT = "";

    private static final String UUID_TO_REPLACE = "-";

    private static final int TOKENS_START = 0;

    private static final int TOKENS_LENGTH = 15;

    private LoginService loginService;

    private Map<String, ClientSession> tokensMap;

    @Autowired
    public LoginController(LoginService loginService, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.loginService = loginService;
        this.tokensMap = tokensMap;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password,
                                        HttpServletResponse response) {
        try {
            ClientSession session = this.loginService.login(email, password);
            String token = this.generateToken();
            this.tokensMap.put(token, session);
            // add cookie to response
            response.addCookie(new Cookie("randToken" , token));
            return ResponseEntity.ok(token);
        }
        catch (LoginServiceException | CompanyException | CustomerException e) {
            return ResponseEntity.ok("Error" + e.getMessage());
        }
    }


    private String generateToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll(UUID_TO_REPLACE, UUID_REPLACEMENT)
                .substring(TOKENS_START, TOKENS_LENGTH);
    }
}
