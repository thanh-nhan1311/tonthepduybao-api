package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.model.property.PropertyData;
import com.tonthepduybao.api.model.property.PropertyCreateForm;
import com.tonthepduybao.api.model.property.PropertyUpdateForm;
import com.tonthepduybao.api.service.property.PropertyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PropertyController
 *
 * @author khal
 * @since 2023/03/04
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/property")
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid  @RequestBody @NotNull PropertyCreateForm form) {
        propertyService.create(form);
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid  @RequestBody @NotNull PropertyUpdateForm form) {
        propertyService.update(form);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropertyData> getAll(@RequestParam(name = "search", defaultValue = "") String search,
                                     @RequestParam(name = "type") @NotBlank String type) {
        return propertyService.getAll(search, type);
    }

    @GetMapping(value = "/all-by-type", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropertyData> getAllByType(@RequestParam(name = "type") @NotBlank String type) {
        return propertyService.getAllByType(type);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(name = "id") @NotNull Long id) {
        propertyService.delete(id);
    }

}
