package com.backend.shipping.controller;

import com.backend.shipping.dto.UserDtoForAdmin;
import com.backend.shipping.dto.UserDtoForGet;
import com.backend.shipping.security.jwt.JwtUtils;
import com.backend.shipping.service.UserService;
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
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> createUser(
            @Valid @RequestBody
            UserDtoForAdmin user
    ) {
        userService.create(user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
    //users/4
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDtoForGet> getUserById(@PathVariable Long id) {
        UserDtoForGet user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * It will return the authenticated user object
     */
//    @GetMapping("/user")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER') or hasRole('DISPATCHER')")
//    public ResponseEntity<UserDtoForGet> getUserById(
//            HttpServletRequest request
//    ) {
//        Long id = (Long) request.getAttribute("id");
//        UserDtoForGet user = userService.findById(id);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }


    /**
     * page: active page number (optional, default: 0)
     * size: record count in a page (optional, default: 20)
     * sort : sort field name (optional, default: name)
     * type: sorting type (optional, default: asc)
     */
    //users?page=1&size=10&sort=name&type=asc
    @GetMapping("/user/all")
    public ResponseEntity<Page<UserDtoForGet>> all(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc"))
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        Page<UserDtoForGet> author = userService.findAll(pageable);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }


    /**
     * It will return the updated user object.
     * An admin can update any type of user, while an employee can update only member-type users.
     */
    //users/4
    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateUserByAdmin(
            @PathVariable
            Long id,
            @RequestBody
            UserDtoForAdmin userDtoForAdmin
    ) {
        userService.updateUser(id, userDtoForAdmin);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/change-password/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateUserPassword(
            @PathVariable
            Long id,
            String newPassword,
            String oldPassword
    ) {
        userService.updatePassword(id, newPassword,oldPassword);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //users/4
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<Map<String, Boolean>> deleteUserByAdmin(
            @PathVariable Long id
    ) {
        userService.deleteUserByAdminById(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
