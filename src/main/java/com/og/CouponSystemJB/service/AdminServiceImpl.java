package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.Optional;

/*-------------------- springframework --------------------*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.*;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.repository.*;
import com.og.CouponSystemJB.service.exception.AdminServiceException;

/**
 * This class handles the business logic and data manipulation for Users who are logged in as Admin. This Service is
 * of scope prototype and will handle the logic for each Admin with a different instance. Each Admin will have its
 * own service component that is directly attached to it and responsible to handle the Repositories, this is done by
 * assigning each Admin User to a Service and a token Cookie.
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AdminServiceImpl implements AdminService {

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /* Admin Entity model attached and associated to this Service */
    private Admin admin;

    /* Repository for User Entity */
    private UserRepositorySql userRepository;

    /* Repository for Company Entity */
    private CompanyRepositorySql companyRepository;

    /* Repository for Customer Entity */
    private CustomerRepositorySql customerRepository;

    /* Repository for Coupon Entity */
    private CouponRepositorySql couponRepository;

    /* Repository for CustomerCoupon Entity */
    private CustomerCouponRepositorySql customerCouponRepository;

    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function to determine if Company is valid or not. The parameters of the Company will be checked
     * and this method will throw an AdminServiceException with a relevant message if one of them is invalid.
     *
     * @param company Company Entity model to check.
     * @throws AdminServiceException Thrown if parameters are null or invalid.
     */
    public static void CheckValidCompany(Company company) throws AdminServiceException {
        if (null == company) {
            throw new AdminServiceException(AdminServiceException.INVALID_COMP + "null Company ");
        }
        if (null == company.getEmail()) {
            throw new AdminServiceException(AdminServiceException.INVALID_COMP + "null email ");
        }
        if (null == company.getPassword()) {
            throw new AdminServiceException(AdminServiceException.INVALID_COMP + "null password ");
        }
        if (null == company.getName()) {
            throw new AdminServiceException(AdminServiceException.INVALID_COMP + "null name ");
        }
        if (company.getEmail().length() < Company.MIN_CHAR) {
            throw new AdminServiceException(AdminServiceException.INVALID_COMP + "email too short ");
        }
        if (company.getPassword().length() < Company.MIN_CHAR) {
            throw new AdminServiceException(AdminServiceException.INVALID_COMP + "password too short ");
        }
        if (company.getName().length() < Company.MIN_CHAR) {
            throw new AdminServiceException(AdminServiceException.INVALID_COMP + "Company name too short ");
        }
    }

    /**
     * Static helper function to determine if Customer is valid or not. The parameters of the Customer will be checked
     * and this method will throw an AdminServiceException with a relevant message if one of them is invalid.
     *
     * @param customer Customer Entity model to check.
     * @throws AdminServiceException Thrown if parameters are null or invalid.
     */
    public static void CheckValidCustomer(Customer customer) throws AdminServiceException {
        if (null == customer) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "null Customer ");
        }
        if (null == customer.getEmail()) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "null email ");
        }
        if (null == customer.getPassword()) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "null password ");
        }
        if (null == customer.getFirstName()) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "null first name ");
        }
        if (null == customer.getLastName()) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "null last name ");
        }
        if (customer.getEmail().length() < Customer.MIN_CHAR) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "email too short ");
        }
        if (customer.getPassword().length() < Customer.MIN_CHAR) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "password too short ");
        }
        if (customer.getFirstName().length() < Customer.MIN_CHAR_NAME) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "first name too short ");
        }
        if (customer.getLastName().length() < Customer.MIN_CHAR_NAME) {
            throw new AdminServiceException(AdminServiceException.INVALID_CUST + "last name too short ");
        }
    }

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Full Autowired constructor used automatically by Spring.
     *
     * @param userRepository           JPA Repository for User (Autowired).
     * @param companyRepository        JPA Repository for Company (Autowired).
     * @param customerRepository       JPA Repository for Customer (Autowired).
     * @param couponRepository         JPA Repository for Coupon (Autowired).
     * @param customerCouponRepository JPA Repository for CustomerCoupon (Autowired).
     */
    @Autowired
    public AdminServiceImpl(UserRepositorySql userRepository, CompanyRepositorySql companyRepository,
                            CustomerRepositorySql customerRepository, CouponRepositorySql couponRepository,
                            CustomerCouponRepositorySql customerCouponRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
        this.customerCouponRepository = customerCouponRepository;

    }


    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /**
     * Setter for the Admin Entity model this Service holds and is responsible for.
     *
     * @param admin Admin Entity model.
     */
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    /**
     * Helper function that determines if an email is available and does not belong to another User or Client. Will
     * throw AdminServiceException if email was found to another User.
     *
     * @param email String email to check if already taken.
     * @throws AdminServiceException if email already taken.
     */
    private void checkEmailAvailabe(String email) throws AdminServiceException {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new AdminServiceException("Invalid: email already taken ");
        }

    }

    /**
     * Helper function to determine if a Company's name is already taken or not. Will throw AdminServiceException if
     * name was found to another Company.
     *
     * @param name String name to check for a Company.
     * @throws AdminServiceException if name already taken.
     */
    private void checkCompanyName(String name) throws AdminServiceException {
        Optional<Company> company = this.companyRepository.findByName(name);
        if (company.isPresent()) {
            throw new AdminServiceException("Invalid Company: name already taken ");
        }
    }

    /*----------------- Create / Insert -----------------------*/

    /**
     * Add a new Company into the DB. Company must have unique email and unique Company name, and must be also be
     * registered in the DB as a User with role Company.
     *
     * @param company new Company Entity model to add.
     * @return Company Entity model that was added.
     * @throws AdminServiceException Thrown if failed to insert to DB or Company invalid.
     * @throws UserException         Thrown if failed to insert to DB or User invalid.
     */
    @Override
    public Company addCompany(Company company) throws AdminServiceException, UserException {
        CheckValidCompany(company); // check valid company
        checkEmailAvailabe(company.getEmail()); // check email available
        checkCompanyName(company.getName()); // check name available
        company.setId(0);
        Company save = this.companyRepository.save(company);
        User user = new User(company.getEmail(), company.getPassword(), true, true,
                true, true, save);
        user.setId(0);
        this.userRepository.save(user);
        return this.companyRepository.findById(save.getId()).get();
    }

    /**
     * Add a new Customer into the DB. Customer must have unique email, and must be also be registered in the DB as a
     * User with role Customer.
     *
     * @param customer new Customer Entity model to add.
     * @return new Customer Entity model to add.
     * @throws AdminServiceException Thrown if failed to insert to DB or Customer invalid.
     * @throws UserException         Thrown if failed to insert to DB or User invalid.
     */
    @Override
    public Customer addCustomer(Customer customer) throws AdminServiceException, UserException {
        CheckValidCustomer(customer);
        checkEmailAvailabe(customer.getEmail());
        customer.setId(0);
        Customer save = this.customerRepository.save(customer);
        User user = new User(customer.getEmail(), customer.getPassword(), true, true,
                true, true, save);
        user.setId(0);
        this.userRepository.save(user);
        return this.customerRepository.findById(save.getId()).get();
    }

    /*----------------- Read / Get -----------------------*/

    /**
     * Find all Company Entities from the DB.
     *
     * @return Collection of Company Entities model.
     */
    @Override
    public Collection<Company> findAllCompanies() {
        return this.companyRepository.findAll();
    }

    /**
     * Find all Customer Entities from the DB.
     *
     * @return Collection of Customer Entities model.
     */
    @Override
    public Collection<Customer> findAllCustomers() {
        return this.customerRepository.findAll();
    }

    /**
     * Find all Coupon Entities from the DB.
     *
     * @return Collection of Coupon Entities model.
     */
    @Override
    public Collection<Coupon> findAllCoupons() {
        return this.couponRepository.findAll();
    }

    /**
     * Find Company by email (unique parameter).
     *
     * @param email String unique email of the desired Company.
     * @return Optional of Company model.
     */
    @Override
    public Optional<Company> findCompanyByEmail(String email) {
        return this.companyRepository.findByEmail(email);
    }

    /**
     * Find Customer by email (unique parameter).
     *
     * @param email String unique email of the desired Customer.
     * @return Optional of Customer model.
     */
    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        return this.customerRepository.findByEmail(email);
    }

    /*----------------- Remove / Delete  -----------------------*/

    /**
     * Delete Company by email (unique parameter).
     *
     * @param email String unique email of the Company for deletion.
     * @return String message if successful.
     * @throws AdminServiceException Thrown if failed to delete to DB or Company invalid.
     */
    @Override
    public String deleteCompanyByEmail(String email) throws AdminServiceException {
        Optional<Company> company = this.companyRepository.findByEmail(email);
        if (company.isPresent()) {
            // get company coupons
            Collection<Coupon> companyCoupons = this.couponRepository.findCompanyCoupons(company.get().getId());
            // remove company coupons from customer coupons
            this.couponRepository.deleteByCompanyId(company.get().getId());
            for (Coupon coupon : companyCoupons) { // delete all Company Coupons
                this.customerCouponRepository.deleteAllByCouponId(coupon.getId());
            }
            this.userRepository.deleteByEmail(email); // remove from user
            this.companyRepository.deleteByEmail(email); // remove from company
            return "Company removed successfully";
        }
        throw new AdminServiceException("Company not found");
    }

    /**
     * Delete Customer by email (unique parameter).
     *
     * @param email String unique email of the Customer for deletion.
     * @return String message if successful.
     * @throws AdminServiceException Thrown if failed to delete to DB or Customer invalid.
     */
    @Override
    public String deleteCustomerByEmail(String email) throws AdminServiceException {
        Optional<Customer> customer = this.customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            this.customerCouponRepository.deleteAllByCustomerId(customer.get().getId()); // remove customercoupons
            this.userRepository.deleteByEmail(email); // remove from user
            this.customerRepository.deleteByEmail(email); // remove from customer
            return "Customer removed successfully";
        }
        throw new AdminServiceException("Customer not found");
    }
}

