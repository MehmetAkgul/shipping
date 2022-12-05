package com.backend.shipping.service;

import com.backend.shipping.domain.Customer;
import com.backend.shipping.domain.Point;
import com.backend.shipping.dto.CustomerDto;
import com.backend.shipping.exception.BadRequestException;
import com.backend.shipping.exception.ConflictException;
import com.backend.shipping.exception.ResourceNotFoundException;
import com.backend.shipping.repository.CustomerRepository;
 import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@AllArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository repository;
  //  private final CustomerDtoRepository dtoRepository;

    static String entityName = "Customer";

    private final static String NAME_IS_ALREADY_USE_MSG = "Error: " + entityName + " name is already in use!";
    private final static String ACTIVE_MSG = "Error: If it is active it cannot be deleted!";
    private final static String NOT_FOUND_MSG_D = "Error: The " + entityName + " with %d could not be found";
    private final static String NOT_FOUND_MSG_S = "Error: The " + entityName + " with %s could not be found";


    public void create(@Valid CustomerDto dataDto) {

        if (repository.existsByPhoneNumber(dataDto.getPhoneNumber())) {
            throw new ConflictException(String.format(NAME_IS_ALREADY_USE_MSG));
        }
        Customer data = new Customer(dataDto);
        repository.save(data);
    }


    public CustomerDto findById(Long id) {

        Customer data = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MSG_D, id)));
        return new CustomerDto(data);
    }

    public CustomerDto findByPhone(String phone) {

        Customer data = repository.findByPhoneNumber(phone)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MSG_S, phone)));
        return new CustomerDto(data);
    }

    public CustomerDto findByEmail(String email) {

        Customer data = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MSG_S, email)));
        return new CustomerDto(data);
    }


     public Page<CustomerDto> findAll(Pageable pageable) {
        return repository.findAllForDto(pageable);
    }

    public void update(Long id, CustomerDto data) {
        Customer dataDetail = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_MSG_D, id))
        );

        if (data.getFirstName() != null) dataDetail.setFirstName(data.getFirstName());
        if (data.getLastName() != null) dataDetail.setLastName(data.getLastName());
        if (data.getPhoneNumber() != null) dataDetail.setPhoneNumber(data.getPhoneNumber());
        if (data.getEmail() != null) dataDetail.setEmail(data.getEmail());
        if (data.getBirthDate() != null) dataDetail.setBirthDate(data.getBirthDate());

        repository.save(dataDetail);
    }

    public void deleteById(Long id) {
       repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_MSG_S, id)));
        repository.deleteById(id);
    }
}
