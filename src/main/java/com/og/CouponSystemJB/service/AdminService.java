package com.og.CouponSystemJB.service;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- java --------------------*/

/*---------- util ----------*/

import java.util.Collection;
import java.util.Optional;

/*-------------------- CouponSystemJB --------------------*/

import com.og.CouponSystemJB.entity.Company;
import com.og.CouponSystemJB.entity.Coupon;
import com.og.CouponSystemJB.entity.Customer;
import com.og.CouponSystemJB.entity.exception.UserException;
import com.og.CouponSystemJB.service.exception.AdminServiceException;

/**
 * This interface will sign all the methods and functions Admin Services have to implement on different Repositories.
 * The Services will use the database Data Access Objects (DB DAO) and will handle the business logic and data
 * manipulation. Must extend EntityService so that the ClientSession class (representing a session with a Client
 * entity) can hold all sub-hierarchical Services regardless of Client or role; LoginService will set each
 * ClientSession with a respective Service and be assigned to tokens. Each Service must represent one Client.
 */
public interface AdminService extends EntityService {

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
    Company addCompany(Company company) throws AdminServiceException, UserException;

    /**
     * Add a new Customer into the DB. Customer must have unique email, and must be also be registered in the DB as a
     * User with role Customer.
     *
     * @param customer new Customer Entity model to add.
     * @return Customer Entity model that was added.
     * @throws AdminServiceException Thrown if failed to insert to DB or Customer invalid.
     * @throws UserException         Thrown if failed to insert to DB or User invalid.
     */
    Customer addCustomer(Customer customer) throws AdminServiceException, UserException;

    /*----------------- Read / Get -----------------------*/

    /**
     * Find all Company Entities from the DB.
     *
     * @return Collection of Company Entities model.
     */
    Collection<Company> findAllCompanies();

    /**
     * Find all Customer Entities from the DB.
     *
     * @return Collection of Customer Entities model.
     */
    Collection<Customer> findAllCustomers();

    /**
     * Find all Coupon Entities from the DB.
     *
     * @return Collection of Coupon Entities model.
     */
    Collection<Coupon> findAllCoupons();

    /**
     * Find Company by email (unique parameter).
     *
     * @param email String unique email of the desired Company.
     * @return Optional of Company model.
     */
    Optional<Company> findCompanyByEmail(String email);

    /**
     * Find Customer by email (unique parameter).
     *
     * @param email String unique email of the desired Customer.
     * @return Optional of Customer model.
     */
    Optional<Customer> findCustomerByEmail(String email);

    /*----------------- Remove / Delete  -----------------------*/

    /**
     * Delete Company by email (unique parameter).
     *
     * @param email String unique email of the Company for deletion.
     * @return String message if successful.
     * @throws AdminServiceException Thrown if failed to delete to DB or Company invalid.
     */
    String deleteCompanyByEmail(String email) throws AdminServiceException;

    /**
     * Delete Customer by email (unique parameter).
     *
     * @param email String unique email of the Customer for deletion.
     * @return String message if successful.
     * @throws AdminServiceException Thrown if failed to delete in DB or Customer invalid.
     */
    String deleteCustomerByEmail(String email) throws AdminServiceException;
}
