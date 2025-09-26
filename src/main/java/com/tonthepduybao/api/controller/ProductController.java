package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.app.constant.AppConstant;
import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.product.ProductDetailData;
import com.tonthepduybao.api.model.product.ProductForm;
import com.tonthepduybao.api.model.product.ProductListForm;
import com.tonthepduybao.api.model.product.ProductOptionData;
import com.tonthepduybao.api.service.product.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController
 *
 * @author khal
 * @since 2023/03/04
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid  @RequestBody @NotNull ProductForm form) {
        productService.create(form);
    }

    @PostMapping(value = "/create-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createAll(@Valid @RequestBody @NotNull ProductListForm listForm) {
        productService.createAll(listForm);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(name = "id") @NotNull Long id) {
        productService.delete(id);
    }

    @DeleteMapping(value = "/delete-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAll(@RequestParam(name = "type", defaultValue = "ALL") @NotBlank String type) {
        productService.deleteAll(type);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingWrapper getAll(@RequestParam(name = "search", defaultValue = "") String search,
                                @RequestParam(name = "type", defaultValue = "") List<String> type,
                                @RequestParam(name = "branchId", defaultValue = "") List<Long> branchId,
                                @RequestParam(name = "page", defaultValue = AppConstant.PAGE_STR) int page,
                                @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE_STR) int pageSize) {
        return productService.getAll(search, type, branchId, page, pageSize);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDetailData get(@PathVariable(name = "id") @NotNull Long id) {
        return productService.get(id);
    }

    @GetMapping(value = "/all-option", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductOptionData> getAllOption(@RequestParam(name = "branchId") @NotNull Long branchId) {
        return productService.getAllOption(branchId);
    }

}
