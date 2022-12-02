package com.backend.shipping.repository.dto;


 import com.backend.shipping.dto.PointDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface PointDtoRepository extends JpaRepository<PointDto, Long> {

}
