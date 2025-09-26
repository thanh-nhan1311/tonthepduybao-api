package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Debt;
import com.tonthepduybao.api.entity.DebtDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * DebtDetailRepository
 *
 * @author khal
 * @since 2023/07/09
 */
public interface DebtDetailRepository extends JpaRepository<DebtDetail, Long> {

    void deleteAllByDebt(Debt debt);

    @Query("SELECT SUM(tdd.totalPrice) FROM DebtDetail tdd WHERE tdd.debt = :debt")
    BigDecimal countTotalPrice(@Param("debt") Debt debt);

    @Query("SELECT SUM(tdd.totalUnitPrice) FROM DebtDetail tdd WHERE tdd.debt = :debt")
    BigDecimal countTotalUnitPrice(@Param("debt") Debt debt);

}
