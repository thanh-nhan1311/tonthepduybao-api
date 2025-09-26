package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.model.shippingAddress.ShippingAddressData;
import com.tonthepduybao.api.model.shippingAddress.ShippingAddressForm;
import com.tonthepduybao.api.service.shippingAddress.ShippingAddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ShippingAddressController
 *
 * @author khal
 * @since 2023/03/04
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/shipping-address")
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;

    @PostMapping(value = "/upsert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void upsert(@Valid @RequestBody @NotNull ShippingAddressForm form) {
        shippingAddressService.upsert(form);
    }

    @PutMapping(value = "/default/{id}")
    public void updateDefault(@PathVariable(name = "id") @NotNull Long id,
                              @RequestParam(name = "customerId") @NotNull Long customerId) {
        shippingAddressService.updateDefault(id, customerId);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(name = "id") @NotNull Long id) {
        shippingAddressService.delete(id);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ShippingAddressData> getAll(@RequestParam(name = "customerId") @NotNull Long customerId) {
        return shippingAddressService.getAll(customerId);
    }

}
