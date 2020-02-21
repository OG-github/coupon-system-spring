package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Optional;

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.og.CouponSystemJB.entity.*;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CustomerException;
import com.og.CouponSystemJB.repository.AdminRepositorySql;
import com.og.CouponSystemJB.repository.CompanyRepositorySql;
import com.og.CouponSystemJB.repository.CustomerRepositorySql;
import com.og.CouponSystemJB.repository.UserRepositorySql;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.service.exception.LoginServiceException;

/**
 * TODO
 */
@Service
public class LoginService {

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /* TODO */
    private ApplicationContext context;

    /* SQL repository for User Entity */
    private UserRepositorySql userRepository;

    /* SQL repository for Admin Entity */
    private AdminRepositorySql adminRepository;

    /* SQL repository for Company Entity */
    private CompanyRepositorySql companyRepository;

    /* SQL repository for Customer Entity */
    private CustomerRepositorySql customerRepository;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Full Autowired constructor used automatically by Spring.
     *
     * @param context            TODO
     * @param userRepository     JPA Repository for User (Autowired).
     * @param adminRepository    JPA Repository for Admin (Autowired).
     * @param companyRepository  JPA Repository for Company (Autowired).
     * @param customerRepository JPA Repository for Customer (Autowired).
     */
    @Autowired
    public LoginService(ApplicationContext context, UserRepositorySql userRepository,
                        AdminRepositorySql adminRepository,
                        CompanyRepositorySql companyRepository,
                        CustomerRepositorySql customerRepository) {
        this.context = context;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
    }

    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /**
     * TODO
     *
     * @param email String email (unique to each User entity) to look up the User by.
     * @param password String password for the User to gain access to a ClientSession.
     * @return
     * @throws LoginServiceException
     * @throws CompanyException
     * @throws CustomerException
     */
    public ClientSession login(String email, String password)
            throws LoginServiceException, CompanyException, CustomerException {
        Optional<User> user = this.userRepository.findByEmail(email); // find user by email
        if (!user.isPresent()) { // user by email not found in DB
            throw new LoginServiceException(LoginServiceException.EMAIL_NOT_FOUND);
        }
        if (!user.get().getPassword().equals(password)) { // email found password doesn't match
            throw new LoginServiceException(LoginServiceException.PASS_NOT_CORRECT);
        }
        Client client = user.get().getClient(); // get the Client from the User
        ClientSession session = this.context.getBean(ClientSession.class); // get new session
        // login admin
        if (client instanceof Admin) {
            Optional<Admin> admin = this.adminRepository.findById(client.getId());
            if (!admin.isPresent()) {
                throw new LoginServiceException(LoginServiceException.CLIENT_NOT_FOUND);
            }
            // get a service from appcontext
            AdminServiceImpl adminService = this.context.getBean(AdminServiceImpl.class);
            adminService.setAdmin(admin.get()); // set specific Client for the service
            session.setService(adminService); // set the service for this session
        }
        // login company
        else if (client instanceof Company) {
            Optional<Company> company = this.companyRepository.findById(client.getId());
            if (!company.isPresent()) {
                throw new LoginServiceException(LoginServiceException.CLIENT_NOT_FOUND);
            }
            // get a service from appcontext
            CompanyServiceImpl companyService = this.context.getBean(CompanyServiceImpl.class);
            companyService.setCompany(company.get()); // set specific Client for the service
            session.setService(companyService); // set the service for this session
        }
        // login customer
        else if (client instanceof Customer) {
            Optional<Customer> customer = this.customerRepository.findById(client.getId());
            if (!customer.isPresent()) {
                throw new LoginServiceException(LoginServiceException.CLIENT_NOT_FOUND);
            }
            // get a service from appcontext
            CustomerServiceImpl customerService = this.context.getBean(CustomerServiceImpl.class);
            customerService.setCustomer(customer.get()); // set specific Client for the service
            session.setService(customerService);  // set the service for this session
        }
        else { // invalid type of client
            throw new LoginServiceException(LoginServiceException.INVALID_CLIENT);
        }
        session.accessed(); // reset last time session was accessed
        return session;
    }

}
