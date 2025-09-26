package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.model.branch.BranchModel;
import com.tonthepduybao.api.service.branch.BranchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BranchController
 *
 * @author khal
 * @since 2023/03/04
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/branch")
public class BranchController {

    private final BranchService branchService;

    @PostMapping(value = "/upsert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void upsert(@Valid  @RequestBody @NotNull BranchModel model) {
        branchService.upsert(model);
    }

    // PUBLIC API
    @GetMapping(value = "/public/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BranchModel> getAll() {
        return branchService.getAll();
    }

}
