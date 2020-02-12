package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.*;
import com.og.CouponSystemJB.entity.exception.CompanyException;
import com.og.CouponSystemJB.entity.exception.CouponException;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.repository.*;
import com.og.CouponSystemJB.service.exception.CompanyServiceException;


/**
 * This class handles the business logic and data manipulation for Users who are logged in as Company. This Service is
 * of scope prototype and will handle the logic for each Company with a different instance. Each Company will have its
 * own service component that is directly attached to it and responsible to handle the Repositories, this is done by
 * assigning each Company User to a Service and a token Cookie.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompanyServiceImpl implements CompanyService {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /* Represents no Coupons left for a Customer to use. */
    private static final int NO_COUPON_LEFT_AMOUNT = 0;

    /**
     * Return message if using a Coupon and updating in the DB was successful.
     */
    private static final String COUPON_USED = "Coupon used successfully";

    /**
     * Return message if deleting a Coupon from the DB was successful.
     */
    private static final String COUPON_DELETED = "Coupon deleted successfully";

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /* Company Entity model attached and associated to this Service */
    private Company company;

    /* SQL repository for User Entity */
    private UserRepositorySql userRepository;

    /* SQL repository for Company Entity */
    private CompanyRepositorySql companyRepository;

    /* SQL repository for Coupon Entity */
    private CouponRepositorySql couponRepository;

    /* SQL repository for Customer Entity */
    private CustomerRepositorySql customerRepository;

    /* SQL repository for CustomerCoupon Entity */
    private CustomerCouponRepositorySql customerCouponRepository;

    /**
     * Id to save to repositories.
     */
    private static final int ID_TO_SAVE = 0;


    /*----------------- Static Methods / Functions ----------------------------------------------------------------------*/

    /**
     * Static helper function to determine if Company is valid or not. The parameters of the Company will be checked
     * and this method will throw a CompanyServiceException with a relevant message if one of them is invalid.
     *
     * @param company Company Entity model to check.
     * @throws CompanyServiceException Thrown if parameters are null or invalid.
     */
    private static void CheckValidCompany(Company company) throws CompanyServiceException {
        if (null == company) { // null company
            throw new CompanyServiceException(CompanyServiceException.INVALID_COMP + CompanyServiceException.NULL_COMP);
        }
        if (null == company.getName()) { // null name
            throw new CompanyServiceException(CompanyServiceException.INVALID_COMP + CompanyServiceException.NULL_NAME);
        }
        if (null == company.getEmail()) { // null email
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COMP + CompanyServiceException.NULL_EMAIL);
        }
        if (null == company.getPassword()) { // null password
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COMP + CompanyServiceException.NULL_PASSWORD);
        }
    }

    /**
     * Static helper function to determine if Coupon is valid or not. The parameters of the Coupon will be checked
     * and this method will throw a CompanyServiceException with a relevant message if one of them is invalid.
     *
     * @param coupon Coupon Entity model to check.
     * @throws CompanyServiceException Thrown if parameters are null or invalid.
     */
    private static void CheckValidCoupon(Coupon coupon) throws CompanyServiceException {
        if (null == coupon) { // null coupon
            throw new CompanyServiceException(CompanyServiceException.INVALID_COUP + CompanyServiceException.NULL_COUP);
        }
        if (null == coupon.getTitle()) { // null title
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.NULL_TITLE);
        }
        if (null == coupon.getStartDate()) { // null startDate
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.NULL_STARTDATE);
        }
        if (null == coupon.getEndDate()) { // null endDate
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.NULL_ENDDATE);
        }
        // null or invalid category
        if (CouponCategory.valueOf(coupon.getCategory()).equals(CouponCategory.NOT_EXIST)) {
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.INVALID_CATEGORY);
        }
        if (null == coupon.getDescription()) { // null description
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.NULL_DESCRIPTION);
        }
        if (coupon.getTitle().length() < Coupon.MIN_CHAR) { // title too short
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.TITLE_SHORT);
        }
        if (coupon.getAmount() < Coupon.MIN_AMOUNT) { // amount too low
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.AMOUNT_INVALID);
        }
        if (coupon.getDescription().length() < Coupon.MIN_CHAR) { // description too short
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.DESCRIPTION_SHORT);
        }
        if (coupon.getPrice() < Coupon.MIN_PRICE) { // price too low
            throw new CompanyServiceException(
                    CompanyServiceException.INVALID_COUP + CompanyServiceException.PRICE_INVALID);
        }
    }

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Full Autowired constructor used automatically by Spring.
     *
     * @param couponRepository         JPA Repository for Coupon (Autowired).
     * @param companyRepository        JPA Repository for Company (Autowired).
     * @param customerCouponRepository JPA Repository for CustomerCoupon (Autowired).
     * @param customerRepository       JPA Repository for Customer (Autowired).
     * @param userRepository           JPA Repository for User (Autowired).
     */
    @Autowired
    public CompanyServiceImpl(CouponRepositorySql couponRepository, CompanyRepositorySql companyRepository,
                              CustomerCouponRepositorySql customerCouponRepository,
                              CustomerRepositorySql customerRepository, UserRepositorySql userRepository) {
        this.couponRepository = couponRepository;
        this.companyRepository = companyRepository;
        this.customerCouponRepository = customerCouponRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }


    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /**
     * (For LoginService) Setter for the Company Entity model this Service holds and is responsible for.
     *
     * @param company Company Entity model.
     * @throws CompanyException Thrown if setting CompanyCoupons failed.
     */
    public void setCompany(Company company) throws CompanyException {
        Set<Coupon> companyCoupons = this.couponRepository.findCompanyCoupons(company.getId());
        if (null != companyCoupons && !companyCoupons.isEmpty()) {
            company.setCoupons(companyCoupons);
        }
        this.company = company;
    }

    /**
     * Helper function to determine if a Coupon's title is already taken or not. Will throw CompanyServiceException if
     * title was found to another Coupon.
     *
     * @param title String title of the new Coupon
     * @throws CompanyServiceException Will be thrown if title already taken.
     */
    private void checkAvailableCouponTitle(String title) throws CompanyServiceException {
        Collection<Coupon> coupons = this.couponRepository.findAll();
        for (Coupon coupon : coupons) {
            if (coupon.getTitle().equals(title)) {
                throw new CompanyServiceException(CompanyServiceException.COUP_TITLE_TAKEN);
            }
        }
    }

    /**
     * Helper function to determine if a Coupon to be updated is found in the DB by its title (unique parameter). If
     * the Coupon is not found CompanyServiceException will be thrown.
     *
     * @param coupon Coupon entity of an updated Coupon this Company is issuing.
     * @throws CompanyServiceException Will be thrown if title is not found.
     * @throws CouponException         Will be thrown if failed to change entity Coupon.
     */
    private void checkUpdateCoupon(Coupon coupon) throws CompanyServiceException, CouponException {
        // if coupon exists in only this Company's Coupons
        Optional<Coupon> couponByTitle = this.couponRepository.findCompanyCouponByTitle(coupon.getTitle(),
                this.company.getId());
        if (!couponByTitle.isPresent()) {
            throw new CompanyServiceException(
                    CompanyServiceException.UPDATE_COUP + CompanyServiceException.COUP_NOT_FOUND);
        }
        // id is not sent in update must be set here
        coupon.setId(couponByTitle.get().getId());
        coupon.setCompany(this.company);
    }

    /**
     * Helper function to determine if a Company to be updated has valid parameters before entering the DB. This
     * function will check if the email has not been changed, if the new name is not too short or is already taken by
     * another Company and if the new password is not too short.
     *
     * @param company Company entity with the updated parameters.
     * @throws CompanyServiceException Will be thrown if one of the parameters is invalid.
     */
    private void checkUpdateCompany(Company company) throws CompanyServiceException {
        // check if email changed
        if (!company.getEmail().equals(this.company.getEmail())) {
            throw new CompanyServiceException(
                    CompanyServiceException.UPDATE_COMP + CompanyServiceException.EMAIL_CHANGE);

        }
        // check if new name is too short
        if (company.getName().length() < Company.MIN_CHAR) {
            throw new CompanyServiceException(CompanyServiceException.UPDATE_COMP + CompanyServiceException.NAME_SHORT);

        }
        // name changed so check if new name is already taken
        if (!company.getName().equals(this.company.getName())) {
            Optional<Company> companyRepo = this.companyRepository.findByName(company.getName());
            if (companyRepo.isPresent()) { // name already taken
                throw new CompanyServiceException(
                        CompanyServiceException.UPDATE_COMP + CompanyServiceException.COMP_NAME_TAKEN);
            }
        }
        // check if password too short
        if (company.getPassword().length() < Company.MIN_CHAR) {
            throw new CompanyServiceException(
                    CompanyServiceException.UPDATE_COMP + CompanyServiceException.PASSWORD_SHORT);

        }
        company.setId(this.company.getId());
    }

    /*----------------- Create / Insert -----------------------*/

    /**
     * Add a new Coupon into the DB from this Company. Coupon must have unique title and belong only to 1 Company.
     *
     * @param coupon Coupon entity of a new Coupon this Company is issuing.
     * @return Coupon entity of a new Coupon this Company is issuing.
     * @throws CompanyServiceException Will be thrown if adding to the DB failed.
     */
    @Override
    public Coupon addCoupon(Coupon coupon) throws CompanyServiceException {
        CheckValidCoupon(coupon);  // check valid Coupon
        this.checkAvailableCouponTitle(coupon.getTitle());  // check title available
        coupon.setId(ID_TO_SAVE); // set id to 0 to save in DB
        try {
            coupon.setCompany(this.company);
            this.company.add(coupon);
        }
        catch (CompanyException e) {
            throw new CompanyServiceException(CompanyServiceException.ADD_COUP + e.getMessage());
        }
        catch (CouponException e) {
            throw new CompanyServiceException(CompanyServiceException.ADD_COUP + e.getMessage());
        }
        return this.couponRepository.save(coupon);
    }
    /*----------------- Read / Get -----------------------*/

    /**
     * Getter for the Company entity that this Service is handling. The Company entity's parameters will represent
     * data from the SQL DB.
     *
     * @return Company entity for this Service.
     */
    @Override
    public Company getCompany() {
        return this.company;
    }

    /**
     * Find all Coupon Entities from the DB. Each Coupon hold data from the SQL DB in its field members.
     *
     * @return Collection of Coupon Entities from SQL DB.
     */
    @Override
    public Collection<Coupon> findAllCoupons() {
        return this.couponRepository.findAll();
    }

    /**
     * Find all Coupon Entities from the DB that are issued by this Company. Each Coupon hold data from the SQL DB in
     * its field members.
     *
     * @return Collection of Coupon Entities of this Company from SQL DB.
     */
    @Override
    public Collection<Coupon> findCompanyCoupons() {
        return this.couponRepository.findCompanyCoupons(this.company.getId());
    }

    /*---------------------- Update ---------------------------*/

    /**
     * Update a Coupon this Company is issuing in the DB. The Coupon's parameters must have its fields with the
     * updated values.
     *
     * @param coupon Coupon entity with the updated field values.
     * @return The updated Coupon entity with the new values.
     * @throws CompanyServiceException Thrown if Coupon or one of its values invalid.
     * @throws CouponException         Thrown if updating Coupon in Coupon DB failed.
     * @throws CompanyException        Thrown if updating Company in Company DB failed.
     */
    @Override
    public Coupon updateCoupon(Coupon coupon) throws CompanyServiceException, CouponException, CompanyException {
        CheckValidCoupon(coupon); // check valid Coupon
        this.checkUpdateCoupon(coupon); // check Coupon title belongs to this Company and exists
        Coupon savedCoupon = this.couponRepository.save(coupon);
        this.company.setCoupons(this.findCompanyCoupons());
        return savedCoupon;
    }


    /**
     * Let a Customer entity use a Coupon. Customer will be identified by email and Coupon by title (unique
     * parameters). Using the Coupon will reduce the amount of Coupons the Customer has by 1.
     *
     * @param couponTitle   String title of the Coupon the Customer wants to use.
     * @param customerEmail String email of the Customer using the Coupon.
     * @return String message of update status.
     * @throws CompanyServiceException Thrown if updating failed or Customer or Customer's email is invalid.
     * @throws CouponException         Thrown if Coupon title or Coupon is invalid.
     */
    @Override
    public String useCoupon(String couponTitle, String customerEmail) throws CompanyServiceException, CouponException {
        // check nulls
        if (null == couponTitle) { // coupon title null
            throw new CompanyServiceException(CompanyServiceException.USE_COUP + CompanyServiceException.NULL_TITLE);
        }
        if (null == customerEmail) { // customer email null
            throw new CompanyServiceException(
                    CompanyServiceException.USE_COUP + CompanyServiceException.CUST_EMAIL_NULL);
        }
        // find customer and find coupon
        Optional<Coupon> coupon = this.couponRepository.findByTitle(couponTitle);
        Optional<Customer> customer = this.customerRepository.findByEmail(customerEmail);
        // check if found
        if (!coupon.isPresent()) {
            throw new CompanyServiceException(
                    CompanyServiceException.USE_COUP + CompanyServiceException.COUP_NOT_FOUND);
        }
        if (!customer.isPresent()) {
            throw new CompanyServiceException(
                    CompanyServiceException.USE_COUP + CompanyServiceException.CUST_NOT_FOUND);
        }
        // check if customer owns this coupon
        Optional<CustomerCoupon> customerCoupon =
                this.customerCouponRepository.findByCustomerIdAndCouponId(customer.get().getId(), coupon.get().getId());
        if (!customerCoupon.isPresent()) {
            throw new CompanyServiceException(
                    CompanyServiceException.USE_COUP + CompanyServiceException.CUST_NOT_OWN_COUP);
        }
        // check if amount left to use by customer
        if (customerCoupon.get().getAmount() <= NO_COUPON_LEFT_AMOUNT) {
            this.customerCouponRepository.delete(customerCoupon.get());
            throw new CompanyServiceException(
                    CompanyServiceException.USE_COUP + CompanyServiceException.CUST_NO_COUP_LEFT);
        }
        customerCoupon.get().useCoupon(); // reduce by 1
        this.customerCouponRepository.save(customerCoupon.get());
        return COUPON_USED;
    }


    /**
     * Update the Company this service is handling in the DB. The Company will be updated by the Company argument and
     * its fields.
     *
     * @param company Company entity with the updated field values.
     * @return The updated Company entity with the new values.
     * @throws CompanyServiceException Thrown if updating failed or Company is invalid or with invalid fields.
     * @throws CompanyException        Thrown if updating the Company data base failed or Company is invalid or with
     *                                 invalid fields.
     * @throws UserException           Thrown if updating the User data base failed.
     */
    @Override
    public Company update(Company company) throws CompanyServiceException, CompanyException, UserException {
        CheckValidCompany(company); // check valid Company parameters
        checkUpdateCompany(company); // check email not changed, and parameters not too short
        company.setCoupons(this.company.getCoupons());
        // update local instance here in Service
        this.company = company;
        Optional<User> user = this.userRepository.findByEmail(this.company.getEmail());
        if (!user.isPresent()) { // if user is not found for this company
            throw new CompanyServiceException(
                    CompanyServiceException.UPDATE_COMP + CompanyServiceException.USER_NOT_FOUND);
        }
        // update the User in DB
        user.get().setClient(this.company);
        this.userRepository.save(user.get());
        // update the Company in DB
        return this.companyRepository.save(this.company);
    }

    /*----------------- Remove / Delete  -----------------------*/

    /**
     * Delete a Coupon in the DB this Company is issuing by title (unique parameter).
     *
     * @param title Title of the Coupon to delete.
     * @return String message of deletion status.
     * @throws CompanyServiceException Thrown if failed to delete in DB, title invalid or Coupon was not found .
     */
    @Override
    public String deleteCouponByTitle(String title) throws CompanyServiceException {
        Collection<Coupon> companyCoupons = this.company.getCoupons();
        for (Coupon coupon : companyCoupons) {
            if (coupon.getTitle().equals(title)) {
                this.company.getCoupons().remove(coupon);
                this.companyRepository.save(this.company);
                this.couponRepository.delete(coupon);
                this.customerCouponRepository.deleteAllByCouponId(coupon.getId());
                return COUPON_DELETED;
            }
        }
        throw new CompanyServiceException(CompanyServiceException.DLT + CompanyServiceException.COUP_NOT_FOUND);
    }
}

