package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Role;
import com.tonthepduybao.api.entity.enumeration.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RoleRepository
 *
 * @author khal
 * @since 2022/05/01
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, ERole> {
}
