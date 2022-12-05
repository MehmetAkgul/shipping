package com.backend.shipping.repository;


import com.backend.shipping.domain.Point;
import com.backend.shipping.dto.PointDto;
import com.backend.shipping.dto.UserDtoForGet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface PointRepository extends JpaRepository<Point, Long> {

    boolean existsByName(String name);


    @Transactional
    @Query("select new com.backend.shipping.dto.PointDto(p) from Point p"  )
    Page<PointDto> findAllForDto(Pageable pageable);
}
