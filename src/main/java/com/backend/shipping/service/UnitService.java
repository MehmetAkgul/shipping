package com.backend.shipping.service;

import com.backend.shipping.domain.Unit;
import com.backend.shipping.dto.UnitDto;
import com.backend.shipping.exception.BadRequestException;
import com.backend.shipping.exception.ConflictException;
import com.backend.shipping.exception.ResourceNotFoundException;
import com.backend.shipping.repository.UnitDtoRepository;
import com.backend.shipping.repository.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@AllArgsConstructor
@Service
public class UnitService {
    private final UnitRepository repository;
    private final UnitDtoRepository dtoRepository;


    private final static String NAME_IS_ALREADY_USE_MSG = "Error: Unit name is already in use!";
    private final static String ACTIVE_MSG = "Error: If it is active it cannot be deleted!";
    private final static String NOT_FOUND_ID_MSG = "Error: Unit %d is not found.";


    public void create(@Valid UnitDto unitDto) {

        if (repository.existsByName(unitDto.getName())) {
            throw new ConflictException(String.format(NAME_IS_ALREADY_USE_MSG));
        }
        if (unitDto.getDesi() == null)
            unitDto.setDesi(unitDto.getWidth() * unitDto.getHeight() * unitDto.getLength());

        Unit data = new Unit(unitDto);
        repository.save(data);
    }


    public UnitDto findById(Long id) {

        Unit data = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id)));
        return new UnitDto(data);
    }


    public Page<UnitDto> findAll(Pageable pageable) {
        return dtoRepository.findAll(pageable);
    }

    public void update(Long id, UnitDto data) {
        Unit dataDetail = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id))
        );

        if (data.getName() != null) dataDetail.setName(data.getName());
        if (data.getDescription() != null) dataDetail.setDescription(data.getDescription());
        if (data.getWidth() != null) dataDetail.setWidth(data.getWidth());
        if (data.getLength() != null) dataDetail.setLength(data.getLength());
        if (data.getHeight() != null) dataDetail.setHeight(data.getHeight());
        if (data.getDesi() != null) dataDetail.setDesi(data.getDesi());

        repository.save(dataDetail);
    }

    public void deleteById(Long id) {
        Unit data = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id)));
        if (data.isActive())
            throw new BadRequestException(String.format(ACTIVE_MSG));
        repository.deleteById(id);
    }
}
