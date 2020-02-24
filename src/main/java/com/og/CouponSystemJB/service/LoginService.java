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
 * This provides a Service for the Controllers in order to log in Clients into the system. This service has only one
 * main function login that requires an email and a password. If the email matches the password this service will
 * return a ClientSession which represents a Session in our system a certain Client has. If the login failed the
 * service will throw LoginService Exception.
 */
@Service
public class LoginService {

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /* Spring framework ApplicationContext used here as BeanFactory mostly */
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
     * @param context            Spring framework ApplicationContext(Autowired).
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
     * The main function for this Service. This function will receive a String representing a Client's email
     * (unique parameter in DB) and a String for password. The method will find a User by that email and compare the
     * password. If the email and password match, this function will return an instance of ClientSession with the
     * relevant specific service for it. The returned ClientSession will hold a ServiceImpl which itself holds an
     * instance of the type of Client with the updated information inside it.
     *
     * @param email    String email (unique to each User entity) to look up the User by.
     * @param password String password for the User to gain access to a ClientSession.
     * @return ClientSession representing a Session a Client has with our system.
     * @throws LoginServiceException Thrown if login failed because of invalid parameters.
     * @throws CompanyException      Thrown if setting a Company for the ClientService's CompanyService failed.
     * @throws CustomerException     Thrown if setting a Customer for the ClientService's CustomerService failed.
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
