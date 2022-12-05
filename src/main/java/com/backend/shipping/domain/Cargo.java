package com.backend.shipping.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cargoes")
public class Cargo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
     private Unit unit;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "cargo_service",
            joinColumns = @JoinColumn(name = "cargo_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private ServiceEntity serviceEntity;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Customer sender;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Point outputAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    @NotNull(message = "Please enter your Output Date ")
    @Column(nullable = false)
    private LocalDate outputDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Customer consignee;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Point arrivalAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    @NotNull(message = "Please enter your Output Date ")
    @Column(nullable = false)
    private LocalDate arrivalDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    @Size(min = 2, max = 30)
    @NotNull(message = "Please enter Branch name. Min 2 and max 30 char")
    @Column(nullable = false, length = 30)
    private String name;

    private boolean paymentInfo ;

}
