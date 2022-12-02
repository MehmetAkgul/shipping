package com.backend.shipping;

import com.backend.shipping.domain.Role;
import com.backend.shipping.domain.User;
import com.backend.shipping.repository.RoleRepository;
import com.backend.shipping.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class ShippingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShippingApplication.class, args);
    }

   /* @Bean
    CommandLineRunner createInitialUsers(RoleRepository roleRepository) {
        return (args) -> {
            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            roleRepository.save(admin);

            Role staff = new Role();
            staff.setName("ROLE_STAFF");
            roleRepository.save(staff);

            Role dispatcher = new Role();
            dispatcher.setName("ROLE_DISPATCHER");
            roleRepository.save(dispatcher);
        };
    }*/

}
