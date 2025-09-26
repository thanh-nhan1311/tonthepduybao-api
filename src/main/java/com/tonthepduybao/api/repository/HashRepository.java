package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Hash;
import com.tonthepduybao.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

/**
 * HashRepository
 *
 * @author khal
 * @since 2022/05/01
 */
@Repository
public interface HashRepository extends JpaRepository<Hash, String> {

    @Modifying
    Long deleteByUserId(Long userId);

}
