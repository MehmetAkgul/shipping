package com.backend.shipping.repository.dto;


import com.backend.shipping.domain.User;
import com.backend.shipping.dto.UserDtoForGet;
import com.backend.shipping.exception.ConflictException;
import com.backend.shipping.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserDtoRepository extends JpaRepository<UserDtoForGet, Long> {


    @Transactional
    @Query("select new com.backend.shipping.dto.UserDtoForGet (u) from User u ")
    Page<UserDtoForGet> findAll(Pageable pageable) throws ResourceNotFoundException;

}
