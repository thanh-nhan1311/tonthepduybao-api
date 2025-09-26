package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Customer;
import com.tonthepduybao.api.entity.enumeration.ECustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * CustomerRepository
 *
 * @author khal
 * @since 2023/05/28
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT tc FROM Customer tc " +
            "WHERE " +
            "   tc.type IN (:type) " +
            "   AND (" +
            "       tc.name ILIKE :search " +
            "       OR tc.phone ILIKE :search " +
            "       OR tc.email ILIKE :search " +
            "       OR tc.address ILIKE :search " +
            "   ) " +
            "ORDER BY tc.updatedAt DESC ")
    List<Customer> searchCustomer(@Param("search") String search,
                                  @Param("type") List<ECustomerType> type);

    List<Customer> findAllByType(ECustomerType type);

    long countAllByType(ECustomerType type);
    long countAllByDeleted(boolean deleted);

}
