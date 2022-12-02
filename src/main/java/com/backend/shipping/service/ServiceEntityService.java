package com.backend.shipping.service;

import com.backend.shipping.domain.ServiceEntity;
import com.backend.shipping.domain.Unit;
import com.backend.shipping.dto.ServiceEntityDto;
import com.backend.shipping.dto.UnitDto;
import com.backend.shipping.exception.BadRequestException;
import com.backend.shipping.exception.ConflictException;
import com.backend.shipping.exception.ResourceNotFoundException;
import com.backend.shipping.repository.ServiceEntityDtoRepository;
import com.backend.shipping.repository.ServiceEntityRepository;
import com.backend.shipping.repository.UnitDtoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@AllArgsConstructor
@Service
public class ServiceEntityService {
    private final ServiceEntityRepository repository;
    private final ServiceEntityDtoRepository dtoRepository;

    private final static String NAME_IS_ALREADY_USE_MSG = "Error: Unit name is already in use!";
    private final static String ACTIVE_MSG = "Error: If it is active it cannot be deleted!";
    private final static String NOT_FOUND_ID_MSG = "Error: Unit %d is not found.";


    public void create(@Valid ServiceEntityDto dataDto) {

        if (repository.existsByName(dataDto.getName())) {
            throw new ConflictException(String.format(NAME_IS_ALREADY_USE_MSG));
        }
        ServiceEntity data = new ServiceEntity(dataDto);
        repository.save(data);
    }


    public ServiceEntityDto findById(Long id) {

        ServiceEntity data = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id)));
        return new ServiceEntityDto(data);
    }


    public Page<ServiceEntityDto> findAll(Pageable pageable) {
        return dtoRepository.findAll(pageable);
    }

    public void update(Long id, ServiceEntityDto data) {
        ServiceEntity dataDetail = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id))
        );

        if (data.getName() != null) dataDetail.setName(data.getName());
        if (data.getDescription() != null) dataDetail.setDescription(data.getDescription());

        repository.save(dataDetail);
    }

    public void deleteById(Long id) {
        ServiceEntity data = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id)));
        if (data.isActive())
            throw new BadRequestException(String.format(ACTIVE_MSG));
        repository.deleteById(id);
    }
}
