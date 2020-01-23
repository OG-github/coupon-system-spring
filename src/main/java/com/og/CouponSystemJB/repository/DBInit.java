package com.og.CouponSystemJB.repository;

/*----------------- IMPORTS -----------------------------------------------------------------------------------------*/

/*-------------------- springframework --------------------*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/*-------------------- java --------------------*/

/*---------- util ----------*/
import java.util.Optional;

/*-------------------- CouponSystemJB --------------------*/
import com.og.CouponSystemJB.entity.Admin;
import com.og.CouponSystemJB.entity.User;

/**
 * This class will initialize our SQL DB with data members and will execute as a command line after the program has
 * been compiled and started to run. This class is used so we can have at least one Admin in the SQL DB when we
 * initialize this project, after that the Admin can add Companies.
 */
@Service
public class DBInit implements CommandLineRunner {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Name for initialized admin client name.
     */
    private static final String ADMIN_CLIENT_NAME = "admin";

    /**
     * Email for initialized admin user and client.
     */
    private static final String ADMIN_EMAIL = "admin@csjb.com";

    /**
     * Password for initialized admin user and client.
     */
    private static final String ADMIN_PASSWORD = "1234";

    /**
     * User is expired or not.
     */
    private static final boolean NOT_EXPIRED = true;

    /**
     * User is locked or not.
     */
    private static final boolean NOT_LOCKED = true;

    /**
     * User credentials are expired or not.
     */
    private static final boolean CREDENTIALS_NOT_EXPIRED = true;

    /**
     * User is enabled or not.
     */
    private static final boolean ENABLED = true;

    /**
     * Id to save to repositories.
     */
    private static final int ID_TO_SAVE = 0;

    /*----------------- Fields ---------------------------------------------------------------------------------------*/

    /**
     * User JPA Repository.
     */
    private UserRepositorySql userRepository;

    /**
     * Admin JPA Repository.
     */
    private AdminRepositorySql adminRepository;

    /*----------------- Constructors ---------------------------------------------------------------------------------*/

    /**
     * Full Autowired constructor used automatically by Spring.
     *
     * @param userRepository  User JPA Repository. (Autowired).
     * @param adminRepository Admin JPA Repository. (Autowired).
     */
    @Autowired
    public DBInit(UserRepositorySql userRepository,
                  AdminRepositorySql adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    /*----------------- Methods / Functions -----------------------------------------------------------------------------*/

    /**
     * This will execute in the command line after the program has compiled and started. It will add new members to
     * the SQL DB.
     *
     * @param args Run arguments.
     * @throws Exception Thrown if Entities or Repositories failed.
     */
    @Override
    public void run(String... args) throws Exception {
        // check if user already exists with optional
        Optional<User> user = this.userRepository.findByEmail(ADMIN_EMAIL);
        if (user.isPresent()) { // found
            return;
        }
        else { // not found, making new user
            Admin adminClient = new Admin(ADMIN_CLIENT_NAME, ADMIN_EMAIL,
                    ADMIN_PASSWORD);

            User admin =
                    new User(ADMIN_EMAIL,
                            ADMIN_PASSWORD,
                            NOT_EXPIRED, NOT_LOCKED, CREDENTIALS_NOT_EXPIRED, ENABLED, adminClient);

            // Set Ids to 0 so hibernate will save them.
            adminClient.setId(ID_TO_SAVE);
            admin.setId(ID_TO_SAVE);
            this.adminRepository.save(adminClient);
            this.userRepository.save(admin);
        }
    }

}
