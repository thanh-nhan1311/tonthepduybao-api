package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.DebtDetail;
import com.tonthepduybao.api.entity.DebtDetailPropertyDetail;
import com.tonthepduybao.api.entity.PropertyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DebtDetailPropertyDetailRepository
 *
 * @author khal
 * @since 2023/05/14
 */
public interface DebtDetailPropertyDetailRepository extends JpaRepository<DebtDetailPropertyDetail, Long> {

    boolean existsByPropertyDetail(PropertyDetail propertyDetail);

    void deleteAllByDebtDetail(DebtDetail debtDetail);

}
