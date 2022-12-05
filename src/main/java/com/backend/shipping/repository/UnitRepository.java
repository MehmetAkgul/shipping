package com.backend.shipping.repository;


import com.backend.shipping.domain.Unit;
import com.backend.shipping.dto.ServiceEntityDto;
import com.backend.shipping.dto.UnitDto;
import com.backend.shipping.dto.UserDtoForGet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional(readOnly = true)
public interface UnitRepository extends JpaRepository<Unit, Long> {



    boolean existsByName(String name);



    @Transactional
    @Query("select new com.backend.shipping.dto.UnitDto(u) from Unit u"  )
    Page<UnitDto> findAllForDto(Pageable pageable);
}
