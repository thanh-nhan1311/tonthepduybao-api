package com.tonthepduybao.api.service.branch;

import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.entity.enumeration.EBranchStatus;
import com.tonthepduybao.api.model.branch.BranchModel;
import com.tonthepduybao.api.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * BranchService
 *
 * @author khal
 * @since 2023/03/04
 */
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Override
    public void upsert(final BranchModel model) {
        boolean isEdit = Objects.nonNull(model.getId());
        Branch branch = DataBuilder.to(model, Branch.class);
        String now = TimeUtils.nowStr();

        if (isEdit) branch.setUpdatedAt(now);
        else {
            branch.setCreatedAt(now);
            branch.setUpdatedAt(now);
            branch.setStatus(EBranchStatus.ACTIVE);
        }

        branchRepository.saveAndFlush(branch);
    }

    @Override
    public List<BranchModel> getAll() {
        return DataBuilder.toList(branchRepository.findAllByOrderByUpdatedAtDesc(), BranchModel.class);
    }
}
