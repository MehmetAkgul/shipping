package com.backend.library.service;

import com.backend.library.domain.Role;
import com.backend.library.domain.User;
import com.backend.library.domain.enumeration.UserRole;
import com.backend.library.dto.UserDtoForAdmin;
import com.backend.library.dto.UserDto;
import com.backend.library.dto.UserDtoForGet;
import com.backend.library.exception.AuthException;
import com.backend.library.exception.BadRequestException;
import com.backend.library.exception.ConflictException;
import com.backend.library.exception.ResourceNotFoundException;
import com.backend.library.repository.RoleRepository;
import com.backend.library.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final static String USER_NOT_FOUND_MSG = "Error: User with id %d not found";
    private final static String EMAIL_NOT_FOUND_MSG = "Error: Email %s not found";
    private final static String ROLE_NOT_FOUND_MSG = "Error: Role is not found.";
    private final static String EMAIL_IS_ALREADY_IN_USE_MSG = "Error: Email is already in use!";
    private final static String YOU_DONT_HAVE_PERMISSION_TO_UPDATE_USER_INFO = "You dont have permission to update user info!";

    public UserDtoForGet findById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        UserDtoForGet userDto = new UserDtoForGet();
        userDto.setRoles(user.getRole());
        return new UserDtoForGet(user);
    }

    public void register(User user) throws BadRequestException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException(EMAIL_IS_ALREADY_IN_USE_MSG);
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        if (user.getBuiltIn() == null)
            user.setBuiltIn(false);

        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName(UserRole.ROLE_MEMBER)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
        roles.add(customerRole);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public void create(UserDto userDto) throws BadRequestException {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException(EMAIL_IS_ALREADY_IN_USE_MSG);
        }

        if (userDto.getBuiltIn() == null)
            userDto.setBuiltIn(false);

        Set<Role> roles = defineRole(userDto.getRoles());
        String password = passwordEncoder.encode(userDto.getPassword());
        User user = new User(userDto, password, roles);
        userRepository.save(user);
    }

    public Set<Role> defineRole(Set<String> userRole) {

        if (checkHasRole("ROLE_ADMIN")) {
            return addRoles(userRole);
        } else {
            Set<Role> roles = new HashSet<>();
            Role member = roleRepository.findByName(UserRole.ROLE_MEMBER)
                    .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND_MSG));
            roles.add(member);
            return roles;
        }
    }

    public void login(String email, String password) throws AuthException {
        try {
            Optional<User> user = userRepository.findByEmail(email);

            if (!BCrypt.checkpw(password, user.get().getPassword()))
                throw new AuthException("invalid credentials");
        } catch (Exception e) {
            throw new AuthException("invalid credentials");
        }
    }

    public Page<UserDto> fetchAllUserWithPage(Pageable pageable) {
        return userRepository.findAllUserWithPage(pageable);
    }


    public void updateUser(Long id, UserDto userDao) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(userDao.getEmail());
        Optional<User> userDetails = userRepository.findById(id);

        if (userDetails.get().getBuiltIn()) {
            throw new ResourceNotFoundException(YOU_DONT_HAVE_PERMISSION_TO_UPDATE_USER_INFO);
        }

        if (emailExists && !userDao.getEmail().equals(userDetails.get().getEmail())) {
            throw new ConflictException(EMAIL_IS_ALREADY_IN_USE_MSG);
        }

        userRepository.update(id, userDao.getFirstName(), userDao.getLastName(), userDao.getPhoneNumber(),
                userDao.getEmail(), userDao.getAddress(), userDao.getZipCode());
    }


    public void updatePassword(Long id, String newPassword, String oldPassword) throws BadRequestException {
        Optional<User> user = userRepository.findById(id);

        if (user.get().getBuiltIn()) {
            throw new ResourceNotFoundException("You dont have permission to update password!");
        }

        if (!(BCrypt.hashpw(oldPassword, user.get().getPassword()).equals(user.get().getPassword())))
            throw new BadRequestException("password does not match");

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.get().setPassword(hashedPassword);
        userRepository.save(user.get());
    }


    public void updateUserByEmployeeOrAdmin(Long id, UserDtoForAdmin userDtoForAdmin) throws BadRequestException {

        if (checkHasRole("ROLE_STAFF") && !userDtoForAdmin.getRoles().equals("Member"))
            throw new ResourceNotFoundException(YOU_DONT_HAVE_PERMISSION_TO_UPDATE_USER_INFO);

        boolean emailExists = userRepository.existsByEmail(userDtoForAdmin.getEmail());

        User userDetails = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id))
        );

        if (emailExists && !userDtoForAdmin.getEmail().equals(userDetails.getEmail()))
            throw new ConflictException(EMAIL_IS_ALREADY_IN_USE_MSG);

        Set<Role> roles = new HashSet<>();
        if (checkHasRole("ROLE_ADMIN")) {
            Set<String> userRoles = userDtoForAdmin.getRoles();
            roles = addRoles(userRoles);
        } else {
            Role customerRole = roleRepository.findByName(UserRole.ROLE_MEMBER)
                    .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND_MSG));
            roles.add(customerRole);
            if (userDetails.getBuiltIn()) {
                throw new ResourceNotFoundException(YOU_DONT_HAVE_PERMISSION_TO_UPDATE_USER_INFO);
            }
        }

        if (userDtoForAdmin.getFirstName() != null) userDetails.setFirstName(userDtoForAdmin.getFirstName());
        if (userDtoForAdmin.getLastName() != null) userDetails.setLastName(userDtoForAdmin.getLastName());
        if (userDtoForAdmin.getPassword() != null) userDetails.setPassword(passwordEncoder.encode(userDtoForAdmin.getPassword()));
        if (userDtoForAdmin.getPhoneNumber() != null) userDetails.setPhoneNumber(userDtoForAdmin.getPhoneNumber());
        if (userDtoForAdmin.getEmail() != null) userDetails.setEmail(userDtoForAdmin.getEmail());
        if (userDtoForAdmin.getBirthDate() != null) userDetails.setBirthDate(userDtoForAdmin.getBirthDate());
        if (userDtoForAdmin.getAddress() != null) userDetails.setAddress(userDtoForAdmin.getAddress());
        if (userDtoForAdmin.getZipCode() != null) userDetails.setZipCode(userDtoForAdmin.getZipCode());
        if (userDtoForAdmin.getScore() != null) userDetails.setScore(userDtoForAdmin.getScore());
        if (userDtoForAdmin.getBuiltIn() != null) userDetails.setBuiltIn(userDtoForAdmin.getBuiltIn());
        if (userDtoForAdmin.getRoles() != null) userDetails.setRoles(roles);

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

    public Set<Role> addRoles(Set<String> userRoles) {
        Set<Role> roles = new HashSet<>();

        if (userRoles == null) {
            Role userRole = roleRepository.findByName(UserRole.ROLE_MEMBER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            userRoles.forEach(role -> {
                System.out.println("role" + role);
                switch (role) {
                    case "Administrator":
                        Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "Employee":
                        Role staffRole = roleRepository.findByName(UserRole.ROLE_STAFF)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(staffRole);

                        break;
                    case "Member":
                        Role memberRole = roleRepository.findByName(UserRole.ROLE_MEMBER)
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


}
