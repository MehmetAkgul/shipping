package com.backend.shipping.dto;

import com.backend.shipping.domain.Customer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CustomerDto {


    @Size(min = 4, max = 30)
    @NotNull(message = "Please enter your first name. Min 2 char and max 30 char")
    private String firstName;
    @Size(min = 2, max = 30)
    @NotNull(message = "Please enter your last name. Min 2 and max 30 char")
    @Column(nullable = false, length = 30)
    private String lastName;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Please enter your Birth day")
    @Column(nullable = false, length = 150)
    private LocalDate birthDate;

    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean builtIn;

    public CustomerDto(Customer data) {
        this.setFirstName(data.getFirstName());
        this.setLastName(data.getLastName());
        this.setPhoneNumber(data.getPhoneNumber());
        this.setEmail(data.getEmail());
        this.setBirthDate(data.getBirthDate());
    }
}
