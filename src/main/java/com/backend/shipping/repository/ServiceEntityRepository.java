package com.backend.shipping.repository;


import com.backend.shipping.domain.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {

    boolean existsByName(String name);

}
