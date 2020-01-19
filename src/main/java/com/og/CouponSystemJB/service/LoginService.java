package com.og.CouponSystemJB.service;

import com.og.CouponSystemJB.entity.*;
import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CustomerException;
import com.og.CouponSystemJB.repository.AdminRepositorySql;
import com.og.CouponSystemJB.repository.CompanyRepositorySql;
import com.og.CouponSystemJB.repository.CustomerRepositorySql;
import com.og.CouponSystemJB.repository.UserRepositorySql;
import com.og.CouponSystemJB.rest.ClientSession;
import com.og.CouponSystemJB.service.exception.LoginServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private ApplicationContext context;

    private UserRepositorySql userRepository;
    private AdminRepositorySql adminRepository;
    private CompanyRepositorySql companyRepository;
    private CustomerRepositorySql customerRepository;

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


    public ClientSession login(String email, String password)
            throws LoginServiceException, CompanyException, CustomerException {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new LoginServiceException("Email is not registered ");
        }
        if (!user.get().getPassword().equals(password)) {
            throw new LoginServiceException("Password is incorrect ");
        }
        Client client = user.get().getClient();
        ClientSession session = this.context.getBean(ClientSession.class);

        if (client instanceof Admin) {
            Optional<Admin> admin = this.adminRepository.findById(client.getId());
            if (!admin.isPresent()) {
                throw new LoginServiceException("Error finding corresponding Client ");
            }
            AdminServiceImpl adminService = this.context.getBean(AdminServiceImpl.class);
            adminService.setAdmin(admin.get());
            session.setService(adminService);
        }
        else if (client instanceof Company) {
            Optional<Company> company = this.companyRepository.findById(client.getId());
            if (!company.isPresent()) {
                throw new LoginServiceException("Error finding corresponding Client ");
            }
            CompanyServiceImpl companyService = this.context.getBean(CompanyServiceImpl.class);
            companyService.setCompany(company.get());
            session.setService(companyService);
        }
        else if (client instanceof Customer) {
            Optional<Customer> customer = this.customerRepository.findById(client.getId());
            if (!customer.isPresent()) {
                throw new LoginServiceException("Error finding corresponding Client ");
            }
            CustomerServiceImpl customerService = this.context.getBean(CustomerServiceImpl.class);
            customerService.setCustomer(customer.get());
            session.setService(customerService);
        }
        else {
            throw new LoginServiceException("Invalid client ");
        }
        session.accessed();
        return session;
    }

}
