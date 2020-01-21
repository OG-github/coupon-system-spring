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

    /**
     * User JPA Repository.
     */
    private UserRepositorySql userRepository;

    /**
     * Admin JPA Repository.
     */
    private AdminRepositorySql adminRepository;

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
        Optional<User> user = this.userRepository.findByEmail("admin@csjb.com");
        if (user.isPresent()) { // found
            return;
        }
        else { // not found, making new user
            Admin adminClient = new Admin("admin", "admin@csjb.com",
                    "1234");

            User admin =
                    new User("admin@csjb.com",
                            "1234",
                            true, true, true, true, adminClient);

            // Set Ids to 0 so hibernate will save them.
            adminClient.setId(0);
            admin.setId(0);
            this.adminRepository.save(adminClient);
            this.userRepository.save(admin);
        }
    }

}
