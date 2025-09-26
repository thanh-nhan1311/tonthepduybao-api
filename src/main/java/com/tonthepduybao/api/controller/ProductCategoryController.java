package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.model.productCategory.ProductCategoryData;
import com.tonthepduybao.api.model.productCategory.ProductCategoryForm;
import com.tonthepduybao.api.service.productCategory.ProductCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductCategoryController
 *
 * @author khal
 * @since 2023/03/04
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/product-category")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping(value = "/upsert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void upsert(@Valid  @RequestBody @NotNull ProductCategoryForm form) {
        productCategoryService.upsert(form);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductCategoryData> getAll(@RequestParam(name = "search", defaultValue = "") String search) {
        return productCategoryService.getAll(search);
    }

}
