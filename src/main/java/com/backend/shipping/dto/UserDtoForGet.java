package com.backend.library.dto;

import com.backend.library.domain.Role;
import com.backend.library.domain.User;
import com.backend.library.domain.enumeration.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoForGet {

    @Size(max = 15)
    @NotNull(message = "Please enter your first name")
    private String firstName;

    @Size(max = 15)
    @NotNull(message = "Please enter your last name")
    private String lastName;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter valid phone number")
    @Size(min = 10, max = 10, message = "Phone number should be exact 10 characters")
    @NotNull(message = "Please enter your phone number")
    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Please enter your Birth day")
    private LocalDate birthDate;


    @NotNull(message = "Score value must be entered.")
    @Column(nullable = false)
    private Integer score=0;

    @Email(message = "Please enter valid email")
    @Size(min = 5, max = 150)
    @NotNull(message = "Please enter your email")
    private String email;

    @Size(max = 250)
    @NotNull(message = "Please enter your address")
    private String address;

    @Size(max = 15)
    @NotNull(message = "Please enter your zip code")
    private String zipCode;

    private Set<String> roles;
    private Boolean builtIn;



    public UserDtoForGet(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.zipCode = user.getZipCode();
        this.roles = user.getRoles();
        this.builtIn = user.getBuiltIn();
    }



    public void setRoles(Set<Role> roles) {
        Set<String> roles1 = new HashSet<>();
        Role[] role = roles.toArray(new Role[roles.size()]);

        for (int i = 0; i < roles.size(); i++) {
            if (role[i].getName().equals(UserRole.ROLE_ADMIN))
                roles1.add("Administrator");
            else if (role[i].getName().equals(UserRole.ROLE_STAFF))
                roles1.add("Employee");
            else
                roles1.add("Member");
        }
        this.roles = roles1;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                ", score=" + score +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", roles=" + roles +
                ", builtIn=" + builtIn +
                '}';
    }
}
