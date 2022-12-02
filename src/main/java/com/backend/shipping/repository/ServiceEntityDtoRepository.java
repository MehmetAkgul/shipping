package com.backend.shipping.repository;


import com.backend.shipping.domain.ServiceEntity;
import com.backend.shipping.dto.ServiceEntityDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface ServiceEntityDtoRepository extends JpaRepository<ServiceEntityDto, Long> {

    boolean existsByName(String name);

}
