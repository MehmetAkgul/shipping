package com.backend.shipping.controller;

import com.backend.shipping.dto.ServiceEntityDto;
import com.backend.shipping.dto.UnitDto;
import com.backend.shipping.service.ServiceEntityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("services")
public class ServiceController {

    public ServiceEntityService service;

    /**
     * It will return the created user object.
     * Default role is member.
     */
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('WORKER')")
    public ResponseEntity<Map<String, Boolean>> create(
            @Valid @RequestBody
            ServiceEntityDto dataDto
    ) {
        service.create(dataDto);
        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
    //users/4
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER') or hasRole('DISPATCHER')")
    public ResponseEntity<ServiceEntityDto> getById(@PathVariable Long id) {
        ServiceEntityDto data = service.findById(id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * page: active page number (optional, default: 0)
     * size: record count in a page (optional, default: 20)
     * sort : sort field name (optional, default: name)
     * type: sorting type (optional, default: asc)
     */
    //users?page=1&size=10&sort=name&type=asc
    @GetMapping("/")
    public ResponseEntity<Page<ServiceEntityDto>> search(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc"))
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        Page<ServiceEntityDto> data = service.findAll(pageable);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    /**
     * It will return the updated user object.
     * An admin can update any type of user, while an employee can update only member-type users.
     */
    //users/4
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> update(
            @PathVariable
            Long id,
            @RequestBody
            ServiceEntityDto data
    ) {
        service.update(id, data);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    //users/4
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<Map<String, Boolean>> delete(
            @PathVariable Long id
    ) {
        service.deleteById(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
