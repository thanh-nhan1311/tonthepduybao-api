package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.app.constant.AppConstant;
import com.tonthepduybao.api.model.debt.DebtForm;
import com.tonthepduybao.api.model.invoice.InvoiceData;
import com.tonthepduybao.api.model.invoice.InvoiceForm;
import com.tonthepduybao.api.model.invoice.InvoiceListData;
import com.tonthepduybao.api.service.invoice.InvoiceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DebtController
 *
 * @author khal
 * @since 2023/03/04
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody @NotNull InvoiceForm form) {
        invoiceService.create(form);
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody @NotNull DebtForm form) {
//        invoiceService.update(form);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public InvoiceData get(@PathVariable(name = "id") @NotBlank String id) {
        return invoiceService.get(id);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public InvoiceListData getAll(@RequestParam(name = "search", defaultValue = "") Long search,
                                  @RequestParam(name = "fromDate", defaultValue = "") String fromDate,
                                  @RequestParam(name = "toDate", defaultValue = "") String toDate,
                                  @RequestParam(name = "branchId", defaultValue = "") List<Long> branchId,
                                  @RequestParam(name = "customerId", defaultValue = "") List<Long> customerId,
                                  @RequestParam(name = "page", defaultValue = AppConstant.PAGE_STR) int page,
                                  @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE_STR) int pageSize) {
        return invoiceService.getAll(search, fromDate, toDate, branchId, customerId, page, pageSize);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(name = "id") @NotBlank String id) {
        invoiceService.delete(id);
    }

}
