package com.backend.shipping.service;

import com.backend.shipping.domain.Role;
import com.backend.shipping.domain.User;
import com.backend.shipping.domain.enumeration.UserRole;
import com.backend.shipping.dto.UserDtoForAdmin;
import com.backend.shipping.dto.UserDtoForGet;
import com.backend.shipping.exception.AuthException;
import com.backend.shipping.exception.BadRequestException;
import com.backend.shipping.exception.ConflictException;
import com.backend.shipping.exception.ResourceNotFoundException;
import com.backend.shipping.repository.RoleRepository;
 import com.backend.shipping.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
 import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
   // private final UserDtoRepository userDtoRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final static String INVALID_CREDENTIALS = "Invalid credentials";
    private final static String USER_NOT_FOUND_MSG = "Error: User with id %d not found";
    private final static String EMAIL_NOT_FOUND_MSG = "Error: Email %s not found";
    private final static String EMAIL_IS_ALREADY_IN_USE_MSG = "Error: Email is already in use!";
    private final static String YOU_DONT_HAVE_PERMISSION_TO_UPDATE_USER_INFO = "You dont have permission to update user info!";
     private final static String ROLE_NOT_FOUND_MSG = "Error: Role is not found.";


    public void create(UserDtoForAdmin userDto) throws BadRequestException {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException(EMAIL_IS_ALREADY_IN_USE_MSG);
        }

        if (userDto.getBuiltIn() == null)
            userDto.setBuiltIn(false);
        if (checkHasRole("ROLE_ADMIN"))
            throw new ResourceNotFoundException(ROLE_NOT_FOUND_MSG);

        Set<Role> roles = new HashSet<>();
        Role member = roleRepository.findByName(userDto.getRoles().toString())
                .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND_MSG));
        roles.add(member);

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = new User(userDto,roles);
        userRepository.save(user);
    }

    public Set<Role> addRoles(Set<String> userRoles) {
        Set<Role> roles = new HashSet<>();

        if (userRoles == null) {
            Role userRole = roleRepository.findByName(UserRole.ROLE_DISPATCHER.name())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            userRoles.forEach(role -> {
                System.out.println("role" + role);
                switch (role) {
                    case "Administrator":
                        Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "Employee":
                        Role staffRole = roleRepository.findByName(UserRole.ROLE_STAFF.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(staffRole);

                        break;
                    case "Dispatcher":
                        Role memberRole = roleRepository.findByName(UserRole.ROLE_DISPATCHER.name())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(memberRole);
                        break;
                    default:
                        throw new RuntimeException("Error: Role is not found.");
                }
            });
        }
        return roles;
    }
    public void login(String email, String password) throws AuthException {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException(String.format(EMAIL_NOT_FOUND_MSG)));

            if (!BCrypt.checkpw(password, user.getPassword()))
                throw new AuthException(String.format(INVALID_CREDENTIALS));
        } catch (Exception e) {
            throw new AuthException(String.format(INVALID_CREDENTIALS));
        }
    }

    public UserDtoForGet findById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        return new UserDtoForGet(user);
    }

    public void updatePassword(Long id, String newPassword, String oldPassword) throws BadRequestException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));

        if (user.getBuiltIn()) {
            throw new ResourceNotFoundException("You dont have permission to update password!");
        }

        if (!(BCrypt.hashpw(oldPassword, user.getPassword()).equals(user.getPassword())))
            throw new BadRequestException("password does not match");

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }


    public void updateUser(Long id, UserDtoForAdmin userDtoForAdmin) throws BadRequestException {

        if (checkHasRole("ADMIN"))
            throw new ResourceNotFoundException(YOU_DONT_HAVE_PERMISSION_TO_UPDATE_USER_INFO);

        User userDetails = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id))
        );

        boolean emailExists = userRepository.existsByEmail(userDtoForAdmin.getEmail());

        if (emailExists && !userDtoForAdmin.getEmail().equals(userDetails.getEmail()))
            throw new ConflictException(EMAIL_IS_ALREADY_IN_USE_MSG);


        if (userDtoForAdmin.getFirstName() != null) userDetails.setFirstName(userDtoForAdmin.getFirstName());
        if (userDtoForAdmin.getLastName() != null) userDetails.setLastName(userDtoForAdmin.getLastName());
        if (userDtoForAdmin.getPassword() != null)
            userDetails.setPassword(passwordEncoder.encode(userDtoForAdmin.getPassword()));
        if (userDtoForAdmin.getPhoneNumber() != null) userDetails.setPhoneNumber(userDtoForAdmin.getPhoneNumber());
        if (userDtoForAdmin.getEmail() != null) userDetails.setEmail(userDtoForAdmin.getEmail());
        if (userDtoForAdmin.getBuiltIn() != null) userDetails.setBuiltIn(userDtoForAdmin.getBuiltIn());
        if (userDtoForAdmin.getRoles() != null) userDetails.setRoles(addRoles(userDtoForAdmin.getRoles()));

        userRepository.save(userDetails);
    }

    public void deleteUserByAdminById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        if (user.getBuiltIn())
            throw new BadRequestException("You dont have permission to delete user");
        userRepository.deleteById(id);
    }

    public boolean checkHasRole(String roleName) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }

    public Page<UserDtoForGet> findAll(Pageable pageable) throws ResourceNotFoundException  {
        return userRepository.findAllForDto(pageable);
    }
}
