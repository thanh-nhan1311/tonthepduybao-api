package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.app.constant.AppConstant;
import com.tonthepduybao.api.model.CommonData;
import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.customer.CustomerForm;
import com.tonthepduybao.api.service.customer.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CustomerController
 *
 * @author khal
 * @since 2023/03/04
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(value = "/upsert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void upsert(@Valid  @RequestBody @NotNull CustomerForm form) {
        customerService.upsert(form);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void delete(@PathVariable(name = "id") @NotNull Long id) {
        customerService.delete(id);
    }

    @DeleteMapping(value = "/undelete/{id}")
    public void undelete(@PathVariable(name = "id") @NotNull Long id) {
        customerService.undelete(id);
    }

    @GetMapping(value = "/all-option", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommonData> getAll(@RequestParam(name = "type", defaultValue = "") String type) {
        return customerService.getAllOption(type);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingWrapper getAll(@RequestParam(name = "search", defaultValue = "") String search,
                                @RequestParam(name = "type", defaultValue = "") List<String> type,
                                @RequestParam(name = "deleted", defaultValue = "false") boolean deleted,
                                @RequestParam(name = "page", defaultValue = AppConstant.PAGE_STR) int page,
                                @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE_STR) int pageSize) {
        return customerService.getAll(search, type, deleted, page, pageSize);
    }

}
