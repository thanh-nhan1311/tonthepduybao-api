package com.tonthepduybao.api.service.branch;

import com.tonthepduybao.api.model.branch.BranchModel;

import java.util.List;

/**
 * BranchService
 *
 * @author khal
 * @since 2023/03/04
 */
public interface BranchService {

    void upsert(BranchModel model);
    List<BranchModel> getAll();

}
