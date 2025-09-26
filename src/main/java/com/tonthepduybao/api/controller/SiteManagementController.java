package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.model.siteManagement.*;
import com.tonthepduybao.api.service.siteManagement.SiteManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SiteManagementController
 *
 * @author khal
 * @since 2023/03/05
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/sm")
public class SiteManagementController {

    private final SiteManagementService siteManagementService;

    // Site Contact
    @GetMapping(value = "/contact/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SiteContactModel> searchContact(@RequestParam(value = "search", defaultValue = "") String search) {
        return siteManagementService.searchContact(search);
    }

    @PutMapping(value = "/contact/resolve/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void resolveContact(@PathVariable(value = "id") @NotNull Long id) {
        siteManagementService.resolveContact(id);
    }

    @DeleteMapping(value = "/contact/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteContact(@PathVariable(value = "id") @NotNull Long id) {
        siteManagementService.deleteContact(id);
    }

    // Site Category
    @GetMapping(value = "/category/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SiteCategoryModel> searchCategory(@RequestParam(value = "search", defaultValue = "") String search) {
        return siteManagementService.searchCategory(search);
    }

    @PostMapping(value = "/category/upsert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void upsertCategory(@Valid @RequestBody @NotNull SiteCategoryModel model) {
        siteManagementService.upsertCategory(model);
    }

    @DeleteMapping(value = "/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCategory(@PathVariable(value = "id") @NotNull Long id) {
        siteManagementService.deleteCategory(id);
    }

    // Site Partner
    @GetMapping(value = "/partner/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SitePartnerModel> searchPartner(@RequestParam(name = "search", defaultValue = "") String search) {
        return siteManagementService.searchPartner(search);
    }

    @PostMapping(value = "/partner/upsert",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void upsertPartner(@Valid @NotNull SitePartnerModel model) {
        siteManagementService.upsertPartner(model);
    }

    @DeleteMapping(value = "/partner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deletePartner(@PathVariable(name = "id") @NotNull Long id) {
        siteManagementService.deletePartner(id);
    }

    // Site Product
    @GetMapping(value = "/product/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SiteProductModel> searchProduct(@RequestParam(name = "search", defaultValue = "") String search) {
        return siteManagementService.searchProduct(search);
    }

    @PostMapping(value = "/product/upsert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void upsertProduct(@Valid @RequestBody @NotNull SiteProductModel model) {
        siteManagementService.upsertProduct(model);
    }

    @DeleteMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteProduct(@PathVariable(name = "id") @NotNull Long id) {
        siteManagementService.deleteProduct(id);
    }

    // Site Setting
    @DeleteMapping(value = "/setting/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteSetting(@PathVariable(name = "id") @NotNull Long id) {
        siteManagementService.deleteSetting(id);
    }

    @GetMapping(value = "/setting", produces = MediaType.APPLICATION_JSON_VALUE)
    public SiteSettingDataModel getAllSetting() {
        return siteManagementService.getAllSetting();
    }

    @PostMapping(value = "/setting", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveSetting(@RequestBody @NotNull SiteSettingUpsertForm form) {
        siteManagementService.saveSetting(form);
    }

}
