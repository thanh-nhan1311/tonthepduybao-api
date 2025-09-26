package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.app.constant.AppConstant;
import com.tonthepduybao.api.model.debt.DebtForm;
import com.tonthepduybao.api.model.debt.DebtInfoData;
import com.tonthepduybao.api.model.debt.DebtListData;
import com.tonthepduybao.api.service.debt.DebtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping(value = "/ttdb/api/debt")
public class DebtController {

    private final DebtService debtService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody @NotNull DebtForm form) {
        debtService.create(form);
    }

    @PostMapping(value = "/file",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> createFromFile(@Valid MultipartFile file) {
        return debtService.createFromFile(file);
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody @NotNull DebtForm form) {
        debtService.update(form);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DebtInfoData get(@PathVariable(name = "id") @NotNull String id) {
        return debtService.get(id);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public DebtListData getAll(@RequestParam(name = "search", defaultValue = "") String search,
                               @RequestParam(name = "fromDate", defaultValue = "") String fromDate,
                               @RequestParam(name = "toDate", defaultValue = "") String toDate,
                               @RequestParam(name = "type", defaultValue = "") List<String> type,
                               @RequestParam(name = "customerId", defaultValue = "") List<Long> customerId,
                               @RequestParam(name = "page", defaultValue = AppConstant.PAGE_STR) int page,
                               @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE_STR) int pageSize) {
        return debtService.getAll(search, fromDate, toDate, type, customerId, page, pageSize);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(name = "id") @NotNull String id) {
        debtService.delete(id);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<Resource> download(@RequestParam("ids") @NotEmpty List<String> ids) {
        return debtService.download(ids);
    }

    @GetMapping(value = "/download/template")
    public ResponseEntity<Resource> downloadTemplate(@RequestParam(name = "type") @NotBlank String type,
                                                     @RequestParam(name = "propertyIds") @NotEmpty List<Long> propertyIds) {
        return debtService.downloadTemplate(type, propertyIds);
    }

}
