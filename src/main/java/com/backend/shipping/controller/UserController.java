package com.backend.library.controller;


import com.backend.library.domain.User;
import com.backend.library.dto.UserDtoForAdmin;
import com.backend.library.dto.LoanDtoForMember;
import com.backend.library.dto.UserDto;
import com.backend.library.dto.UserDtoForGet;
import com.backend.library.security.jwt.JwtUtils;
import com.backend.library.service.LoanService;
import com.backend.library.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping()
public class UserController {

    public UserService userService;
    public LoanService loanService;
    public AuthenticationManager authenticationManager;
    public JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> authenticateUser(
            @RequestBody
            Map<String, Object> userMap
    ) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        userService.login(email, password);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * It will return the created user object.
     * Default role is member.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> registerUser(
            @Valid @RequestBody
            User user
    ) {
        userService.register(user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    /**
     * It will return the authenticated user object
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('MEMBER') or hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<UserDtoForGet> getUserById(
            HttpServletRequest request
    ) {
        Long id = (Long) request.getAttribute("id");
        UserDtoForGet user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * It will return the authenticated  user object.
     * it should return the corresponding book object in response.
     * /user/loans?page=1&size=10&sort=createDate&type=desc
     */
    @GetMapping("/user/loans")
    @PreAuthorize("hasRole('MEMBER') or hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Page<LoanDtoForMember>> getUserLoansById(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type,
            HttpServletRequest request
    ) {
        Long id = (Long) request.getAttribute("id");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<LoanDtoForMember> loanPageByUser = loanService.findByUserIdWithLoans(id, pageable);
        return new ResponseEntity<>(loanPageByUser, HttpStatus.OK);
    }
//
//    //users
//    @GetMapping()
//    @PreAuthorize(" hasRole('STAFF') or hasRole('ADMIN')")
//    public ResponseEntity<UserDto> getUser(
//            HttpServletRequest request
//    ) {
//        Long id = (Long) request.getAttribute("id");
//        UserDto userDto = userService.findById(id);
//        return new ResponseEntity<>(userDto, HttpStatus.OK);
//    }

    //users/4
    @GetMapping("/user/{id}")
    @PreAuthorize("  hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<UserDtoForGet> getUserById(@PathVariable Long id) {
        UserDtoForGet user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * It will return the created user object.
     * An admin can create any type of user, while an employee can create only member-type users.
     */
    //users/4
    @PostMapping("/user")
    @PreAuthorize("  hasRole('STAFF') or hasRole('ADMIN') ")
    public ResponseEntity<Map<String, Boolean>> createNewUserByEmployeeOrAdmin(
            @Valid @RequestBody
            UserDto user
    ) {
        userService.create(user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    /**
     * It will return the updated user object.
     * An admin can update any type of user, while an employee can update only member-type users.
     */
    //users/4
    @PutMapping("/user/{id}")
    @PreAuthorize("  hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateUserByEmployeeOrAdmin(
            @PathVariable
            Long id,
         @RequestBody
         UserDtoForAdmin userDtoForAdmin
    ) {
        userService.updateUserByEmployeeOrAdmin(id, userDtoForAdmin);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //users/4
    @DeleteMapping("/user/{id}")
    @PreAuthorize("  hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteUserByAdmin(
            @PathVariable Long id
    ) {
        userService.deleteUserByAdminById(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
