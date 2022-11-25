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
@Table(name = "branches")
public class Branch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Please enter Branch typeId")
    private Long typeId;

    @NotNull(message = "Please enter parentId")
    private Long parentId;

    @NotNull(message = "Please enter PointId")
    private Long pointId;

    @Size(min = 2, max = 30)
    @NotNull(message = "Please enter Branch name. Min 2 and max 30 char")
    @Column(nullable = false, length = 30)
    private String name;

    private boolean active = true;

}
