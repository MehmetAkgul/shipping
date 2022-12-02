package com.backend.shipping.domain;

 import com.backend.shipping.domain.enumeration.UserRole;
 import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
 import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    /**
     * Branch Worker within the branch, they have the duties of receiving cargo, sorting cargoes and delivering cargo.
     * Vehicle Driver & Dispatcher The branch drives vehicles in the area of responsibility and delivers the cargo that needs to be delivered to the address to the consignee.
     * Admin users have full rights.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private UserRole name;

    @Override
    public String toString() {
        return "{" + name + '}';
    }

}
