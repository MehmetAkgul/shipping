package com.backend.shipping.repository;


import com.backend.shipping.domain.Customer;
import com.backend.shipping.domain.Unit;
import com.backend.shipping.dto.CustomerDto;
import com.backend.shipping.dto.UserDtoForGet;
import com.backend.shipping.exception.ConflictException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    boolean existsByPhoneNumber(String phoneNumber)  throws ConflictException;


    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumber(String phone);



    @Transactional
    @Query("select new com.backend.shipping.dto.CustomerDto(c) from Customer c"  )
    Page<CustomerDto> findAllForDto(Pageable pageable);
}
