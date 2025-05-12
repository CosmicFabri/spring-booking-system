package com.spring.spring_booking_system.configs;

import com.spring.spring_booking_system.entities.Role;
import com.spring.spring_booking_system.entities.Space;
import com.spring.spring_booking_system.entities.User;
import com.spring.spring_booking_system.repositories.BookingRepository;
import com.spring.spring_booking_system.repositories.RoleRepository;
import com.spring.spring_booking_system.repositories.SpaceRepository;
import com.spring.spring_booking_system.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class DataSeeding implements CommandLineRunner {
    RoleRepository roleRepository;
    UserRepository userRepository;
    SpaceRepository spaceRepository;
    PasswordEncoder passwordEncoder;
    BookingRepository bookingRepository;

    public DataSeeding(RoleRepository roleRepository, UserRepository userRepository, SpaceRepository spaceRepository, PasswordEncoder passwordEncoder, BookingRepository bookingRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedSpaces();
        seedUsers();
    }

    public void seedRoles() {
        if (roleRepository.count() == 0) {
            Role role1 = new Role();
            Role role2 = new Role();
            role1.setName("admin");
            role2.setName("user");
            roleRepository.save(role1);
            roleRepository.save(role2);
        }
    }

    public void seedSpaces() {
        if (spaceRepository.count() == 0) {
            List<Space> spaces = List.of(
                    createSpace("CIC 1", "Sala 1 del CIC", 20),
                    createSpace("CIC 2", "Sala 2 del CIC", 20),
                    createSpace("CIC 3", "Sala 3 del CIC", 20),
                    createSpace("CIC 4", "Sala 4 del CIC", 20),
                    createSpace("Sala de actos", "Sala de actos y ceremonias de la FDI", 80),
                    createSpace("Sala audiovisual", "Salón de estudio de lenguajes y proyecciones de películas", 50)
            );

            spaceRepository.saveAll(spaces);
        }
    }

    public void seedUsers() {
        if (userRepository.count() == 0) {
            Role role =  roleRepository.findById(1L).orElse(null); //"admin"
            User admin = new User();
            admin.setRole(role);
            admin.setFullName("Admin");
            admin.setEmail("admin@uacam.mx");
            admin.setPassword(passwordEncoder.encode("password"));
            userRepository.save(admin);

            List<User> users = List.of(
                    createUser("Alexis Perez", "al064601@uacam.mx"),
                    createUser("Luis Guzman", "al071274@uacam.mx"),
                    createUser("Miguel Alejandro", "al070690@uacam.mx")
            );
            userRepository.saveAll(users);
        }
    }

    private Space createSpace(String name, String description, int capacity) {
        Space space = new Space();
        space.setName(name);
        space.setDescription(description);
        space.setCapacity(capacity);
        space.setDisponibilityStart(LocalTime.of(7, 0));
        space.setDisponibilityEnd(LocalTime.of(17, 0));
        return space;
    }

    private User createUser(String fullName, String email) {
        Role role =  roleRepository.findById(2L).orElse(null); //"user"

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(role);

        return user;
    }
}
