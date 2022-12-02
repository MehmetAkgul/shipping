package com.backend.shipping.domain;

import com.backend.shipping.domain.enumeration.UserRole;
import com.backend.shipping.dto.UserDtoForAdmin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Size(min = 2, max = 30)
    @NotNull(message = "Please enter your first name. Min 2 and max 30 char")
    @Column(nullable = false, length = 30)
    private String firstName;

    @Size(min = 2, max = 30)
    @NotNull(message = "Please enter your last name. Min 2 and max 30 char")
    @Column(nullable = false, length = 30)
    private String lastName;

    @Size(min = 4, max = 60, message = "Please enter min 4 characters")
    @NotNull(message = "Please enter your password")
    @Column(nullable = false, length = 120)
    private String password;

    private String resetPasswordCode;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter valid phone number")
    @Size(min = 10, max = 10, message = "Phone number should be exact 10 characters")
    @NotNull(message = "Please enter your phone number")
    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Email(message = "Please enter valid email")
    @Size(min = 5, max = 80)
    @NotNull(message = "Please enter your email")
    @Column(nullable = false, unique = true, length = 80)
    private String email;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean builtIn=false;

    public User(UserDtoForAdmin userDto, Set<Role> roles) {
        this.roles = roles;
        this.firstName=userDto.getFirstName();
        this.lastName=userDto.getLastName();
        this.password=userDto.getPassword();
        this.phoneNumber=userDto.getPhoneNumber();
        this.email=userDto.getEmail();
        this.builtIn=userDto.getBuiltIn();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    public Set<Role> getRole() {
        return roles;
    }

    public Set<String> getRoles() {
        Set<String> roles1 = new HashSet<>();
        Role[] role = roles.toArray(new Role[roles.size()]);

        for (int i = 0; i < roles.size(); i++) {
            if (role[i].getName().equals(UserRole.ROLE_ADMIN))
                roles1.add("Administrator");
            else if (role[i].getName().equals(UserRole.ROLE_STAFF))
                roles1.add("Employee");
            else if (role[i].getName().equals(UserRole.ROLE_DISPATCHER))
                roles1.add("Dispatcher");
        }
        return roles1;
    }

}
