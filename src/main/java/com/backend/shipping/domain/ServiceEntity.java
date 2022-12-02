package com.backend.shipping.domain;


import com.backend.shipping.dto.ServiceEntityDto;
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
@Table(name = "services")
public class ServiceEntity implements Serializable {
    /**
     * It is the table in which the services provided by the company are defined. Delivery of cargo status via SMS, branch delivery, delivery to address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 30)
    @NotNull(message = "Please enter service name. Min 2 and max 30 char")
    @Column(nullable = false, length = 30)
    private String name;

    @Size(min = 2, max = 80)
    @NotNull(message = "Please enter description. Min 2 and max 80 char")
    @Column(nullable = false, length = 80)
    private String description;


    private boolean active = true;

    public ServiceEntity(ServiceEntityDto dataDto) {
        this.setName(dataDto.getName());
        this.setDescription(dataDto.getDescription());
    }
}
