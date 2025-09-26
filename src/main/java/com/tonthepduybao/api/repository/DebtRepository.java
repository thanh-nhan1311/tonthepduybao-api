package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DebtRepository
 *
 * @author khal
 * @since 2023/07/09
 */
public interface DebtRepository extends JpaRepository<Debt, String> {

    boolean existsById(String id);

}
