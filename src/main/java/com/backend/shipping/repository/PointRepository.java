package com.backend.shipping.repository;


import com.backend.shipping.domain.Point;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface PointRepository extends JpaRepository<Point, Long> {

    boolean existsByName(String name);
}
