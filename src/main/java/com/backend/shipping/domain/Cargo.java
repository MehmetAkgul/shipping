package com.backend.shipping.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cargo_vehicle")
public class CargoVehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cargo_vehicle_id", referencedColumnName = "id")
    private CargoVehicleType cargoVehicleType;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @Size(min = 2, max = 30)
    @NotNull(message = "Please enter Branch name. Min 2 and max 30 char")
    @Column(nullable = false, length = 30)
    private String name;

    private boolean active = true;

}
