package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.entity.Product;
import com.tonthepduybao.api.entity.enumeration.EType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductRepository
 *
 * @author khal
 * @since 2023/07/12
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT tp.name FROM Product tp WHERE tp.id = :id ")
    String getNameById(@Param("id") Long id);

    List<Product> findAllByBranch(Branch branch);
    List<Product> findAllByType(EType eType);

}
