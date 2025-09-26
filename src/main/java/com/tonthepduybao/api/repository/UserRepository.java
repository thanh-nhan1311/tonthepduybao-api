package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.entity.Role;
import com.tonthepduybao.api.entity.User;
import com.tonthepduybao.api.entity.enumeration.EUserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository
 *
 * @author khal
 * @since 2022/05/01
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT tu.fullName FROM User tu WHERE tu.id = :id ")
    String getFullNameById(@Param("id") Long id);

    Optional<User> findByUsernameAndPasswordAndBranchAndDeleted(String username, String password, Branch branch, boolean deleted);

    Optional<User> findByUsernameAndStatusAndDeleted(String username, EUserStatus status, boolean deleted);

    Optional<User> findByUsernameAndDeleted(String username, boolean deleted);

    Optional<User> findByEmailAndDeleted(String email, boolean deleted);

    boolean existsByUsername(String username);

    @Query(value = "SELECT tu " +
            "FROM User tu " +
            "WHERE " +
            "   tu.status IN :status " +
            "   AND tu.role IN :role " +
            "   AND tu.id <> :currentUserId " +
            "   AND tu.fullName ILIKE :search " +
            "   AND tu.deleted = false " +
            "ORDER BY tu.updatedAt DESC ")
    List<User> searchAll(@Param("search") String search,
                         @Param("status") List<EUserStatus> status,
                         @Param("role") List<Role> role,
                         @Param("currentUserId") Long currentUserId);

}
