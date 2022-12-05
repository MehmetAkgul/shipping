package com.backend.shipping.repository;


import com.backend.shipping.domain.ServiceEntity;
import com.backend.shipping.dto.ServiceEntityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {

    boolean existsByName(String name);
    @Transactional
    @Query("select new com.backend.shipping.dto.ServiceEntityDto(u) from ServiceEntity u"  )
    Page<ServiceEntityDto> findAllForDto(Pageable pageable);
}
