package com.backend.shipping.service;

import com.backend.shipping.domain.Point;
import com.backend.shipping.dto.PointDto;
import com.backend.shipping.exception.BadRequestException;
import com.backend.shipping.exception.ConflictException;
import com.backend.shipping.exception.ResourceNotFoundException;
import com.backend.shipping.repository.PointRepository;
import com.backend.shipping.repository.dto.PointDtoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@AllArgsConstructor
@Service
public class PointService {
    private final PointRepository repository;
    private final PointDtoRepository dtoRepository;

    private final static String NAME_IS_ALREADY_USE_MSG = "Error: Unit name is already in use!";
    private final static String ACTIVE_MSG = "Error: If it is active it cannot be deleted!";
    private final static String NOT_FOUND_ID_MSG = "Error: Unit %d is not found.";


    public void create(@Valid PointDto dataDto) {

        if (repository.existsByName(dataDto.getName())) {
            throw new ConflictException(String.format(NAME_IS_ALREADY_USE_MSG));
        }
        Point data = new Point(dataDto);
        repository.save(data);
    }


    public PointDto findById(Long id) {

        Point data = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id)));
        return new PointDto(data);
    }


    public Page<PointDto> findAll(Pageable pageable) {
        return dtoRepository.findAll(pageable);
    }

    public void update(Long id, PointDto data) {
        Point dataDetail = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id))
        );

        if (data.getName() != null) dataDetail.setName(data.getName());
        if (data.getResponsibleBranch() != null) dataDetail.setResponsibleBranch(data.getResponsibleBranch());
        if (data.getCountry() != null) dataDetail.setCountry(data.getCountry());
        if (data.getProvince() != null) dataDetail.setProvince(data.getProvince());
        if (data.getDistrict() != null) dataDetail.setDistrict(data.getDistrict());
        if (data.getNeighbourhood() != null) dataDetail.setNeighbourhood(data.getNeighbourhood());
        if (data.getSubdistrict() != null) dataDetail.setSubdistrict(data.getSubdistrict());
        if (data.getVillage() != null) dataDetail.setVillage(data.getVillage());
        if (data.getStreet() != null) dataDetail.setStreet(data.getStreet());
        if (data.getSite() != null) dataDetail.setSite(data.getSite());
        if (data.getNumber() != null) dataDetail.setNumber(data.getNumber());
        if (data.getAnnotation() != null) dataDetail.setAnnotation(data.getAnnotation());
        if (data.getZipCode() != null) dataDetail.setZipCode(data.getZipCode());
        repository.save(dataDetail);
    }

    public void deleteById(Long id) {
        Point data = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_ID_MSG, id)));
        if (data.isBuiltIn())
            throw new BadRequestException(String.format(ACTIVE_MSG));
        repository.deleteById(id);
    }
}
