package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * SystemLogRepository
 *
 * @author khal
 * @since 2023/07/15
 */
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {

    @Query("SELECT tsl " +
            "FROM SystemLog tsl " +
            "WHERE " +
            "   tsl.time >= :fromTime " +
            "   AND tsl.time <= :toTime " +
            "ORDER BY tsl.time DESC ")
    List<SystemLog> searchAll(@Param("fromTime") String fromTime,
                              @Param("toTime") String toTime);

}
