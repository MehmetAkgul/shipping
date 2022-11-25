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
@Table(name = "units")
public class Unit implements Serializable {
    /**
     * The unit table is the table where the types of shipments such as file, mi, package, parcel to be sent are kept.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 30)
    @NotNull(message = "Please enter unit name. Min 2 and max 30 char")
    @Column(nullable = false, length = 30)
    private String name;

    @Size(min = 2, max = 80)
    @NotNull(message = "Please enter description. Min 2 and max 80 char")
    @Column(nullable = false, length = 80)
    private String description;


    @NotNull(message = "Please enter Unit width.")
    private Integer width;
    @NotNull(message = "Please enter Unit length.")
    private Integer length;
    @NotNull(message = "Please enter Unit height.")
    private Integer height;

    private Integer desi;

    private boolean active = true;

}
