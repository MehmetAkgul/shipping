package com.backend.shipping.domain;

import com.backend.shipping.dto.PointDto;
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
@Entity
@Table(name = "points")
public class Point implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    private Branch responsibleBranch;

    @Size(min = 2, max = 30, message = "Address Name is exceeded. Min 2 and max 30 char")
    @NotNull(message = "Please enter the Address name")
    @Column(length = 30, nullable = false)
    private String name;

    @Size(min = 2, max = 20, message = "Country Name is exceeded. Min 2 and max 20 char")
    @NotNull(message = "Please enter the Country name")
    @Column(length = 20, nullable = false)
    private String country;

    @Size(min = 2, max = 20, message = "Province Name is exceeded. Min 2 and max 20 char")
    @NotNull(message = "Please enter the Province name")
    @Column(length = 20, nullable = false)
    private String province;

    @Size(min = 2, max = 20, message = "District Name is exceeded. Min 2 and max 20 char")
    @NotNull(message = "Please enter the District name")
    @Column(length = 20, nullable = false)
    private String district;

    @Size(min = 2, max = 20, message = "Neighbourhood Name is exceeded. Min 2 and max 20 char")
    @NotNull(message = "Please enter the Neighbourhood name")
    @Column(length = 20, nullable = false)
    private String neighbourhood;

    @Size(max = 20, message = "If exists, enter the Subdistrict name")
    private String subdistrict;

    @Size(max = 20, message = "If exists, enter the Village name")
    private String village;

    @Size(max = 20, message = "If exists, enter the Street name")
    private String street;

    @Size(max = 20, message = "If exists, enter the Site name")
    private String site;

    @NotNull(message = "Please enter building number")
    private Integer number;

    @Size(max = 150, message = "If you want, you can enter additional information for your address.")
    @Column(length = 150)
    private String annotation;

    @Size(max = 15)
    @NotNull(message = "Please enter your zip code")
    @Column(nullable = false, length = 15)
    private String zipCode;

    @Column(nullable = false)
    private boolean builtIn = false;

    public Point(PointDto dataDto) {
        this.setName(dataDto.getName());
        this.setResponsibleBranch(dataDto.getResponsibleBranch());
        this.setCountry(dataDto.getCountry());
        this.setProvince(dataDto.getProvince());
        this.setDistrict(dataDto.getDistrict());
        this.setNeighbourhood(dataDto.getNeighbourhood());
        this.setSubdistrict(dataDto.getSubdistrict());
        this.setVillage(dataDto.getVillage());
        this.setStreet(dataDto.getStreet());
        this.setSite(dataDto.getSite());
        this.setNumber(dataDto.getNumber());
        this.setAnnotation(dataDto.getAnnotation());
        this.setZipCode(dataDto.getZipCode());
    }
}