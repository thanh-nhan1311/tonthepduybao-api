package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Debt;
import com.tonthepduybao.api.entity.DebtProperty;
import com.tonthepduybao.api.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DebtPropertyRepository
 *
 * @author khal
 * @since 2023/05/14
 */
public interface DebtPropertyRepository extends JpaRepository<DebtProperty, Long> {

    boolean existsByProperty(Property property);

    void deleteAllByDebt(Debt debt);

}
