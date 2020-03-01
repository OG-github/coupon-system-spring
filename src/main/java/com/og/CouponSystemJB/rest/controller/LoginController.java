package com.og.CouponSystemJB.rest.controller;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Map;
import java.util.UUID;

/*-------------------- javax --------------------*/

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CustomerException;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.service.LoginService;
import com.og.CouponSystemJB.service.exception.LoginServiceException;

/**
 * This Login RESTful Controller will intercept all login requests made by HTTP in order to validate and login those
 * HTTP requests into our system. This Controller will use the LoginService in order to validate the email and
 * password and get a new ClientSession. After receiving the ClientSession with the specific EntityService, this
 * Controller will generate a random token and bind it to the ClientSession in the tokensMap.
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /* The replacement String for the random UUID String that was generated. */
    private static final String UUID_REPLACEMENT = "";

    /* The String to replace for the random UUID String that was generated. */
    private static final String UUID_TO_REPLACE = "-";

    /* The starting position for the substring from the UUID. */
    private static final int TOKENS_START = 0;

    /* The length for the substring from the UUID. */
    private static final int TOKENS_LENGTH = 15;

    /* The maximum number of tries to generate a random token that isn't already taken */
    private static final int MAX_TRY = 100;

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /* Service for logging in HTTP requests. LoginService returns a ClientSession if login was valid. */
    private LoginService loginService;

    /* The map that binds the tokens to their relevant ClientSessions. */
    private Map<String, ClientSession> tokensMap;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Full Autowired constructor used automatically by Spring.
     *
     * @param loginService LoginService component to handle login logic.
     * @param tokensMap    HashMap for binding random generated tokens to their respective ClientSessions.
     */
    @Autowired
    public LoginController(LoginService loginService, @Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.loginService = loginService;
        this.tokensMap = tokensMap;
    }

    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /**
     * Helper function to generate and new String token
     *
     * @return
     */
    private String generateToken() {
        return UUID.randomUUID()
                .toString()
                .replaceAll(UUID_TO_REPLACE, UUID_REPLACEMENT)
                .substring(TOKENS_START, TOKENS_LENGTH);
    }

    /**
     * @param email
     * @param password
     * @param response
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password,
                                        HttpServletResponse response) {
        try {
            ClientSession session = this.loginService.login(email, password);
            String token = this.generateToken(); // get new token
            // check random token doesn't already exists in tokens map
            int index = 0; // create index to avoid infinite loop
            while (index < MAX_TRY && null != this.tokensMap.get(token)) {
                token = this.generateToken(); // get new token
                ++index;
            }
            this.tokensMap.put(token, session); // bind token to session
            // add cookie to response
            response.addCookie(new Cookie("randToken", token));
            return ResponseEntity.ok(token);
        }
        catch (LoginServiceException | CompanyException | CustomerException e) {
            return ResponseEntity.ok("Error" + e.getMessage());
        }
    }

}
