package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * BranchRepository
 *
 * @author khal
 * @since 2023/03/04
 */
public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findAllByOrderByUpdatedAtDesc();

}
