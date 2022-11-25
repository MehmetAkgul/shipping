package com.backend.shipping.repository;


import com.backend.library.aggregation.model.custom.ReportForMostBorrowers;
import com.backend.library.domain.User;
import com.backend.library.dto.UserDto;
import com.backend.library.exception.BadRequestException;
import com.backend.library.exception.ConflictException;
import com.backend.library.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email) throws ResourceNotFoundException;

    Boolean existsByEmail(String email) throws ConflictException;

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.firstName = ?2, u.lastName = ?3, u.phoneNumber = ?4, u.email = ?5, u.address = ?6, " +
            "u.zipCode = ?7 WHERE u.id = ?1")
    void update(Long id, String firstName, String lastName, String phoneNumber, String email, String address,
                String zipCode) throws BadRequestException;
    @Transactional
    @Query("SELECT new com.backend.library.dto.UserDto(user) FROM User user ")
    Page<UserDto> findAllUserWithPage(Pageable pageable);
    @Transactional
    @Query("Select new com.backend.library.aggregation.model.custom.ReportForMostBorrowers( u, count(l.id) ) FROM User u JOIN Loan l ON l.book.id=u.id group by u.id order by u.id desc ")
    Page<ReportForMostBorrowers> mostBorrowers(Pageable pageable);
}
