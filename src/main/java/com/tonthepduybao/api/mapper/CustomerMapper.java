package com.tonthepduybao.api.mapper;

import com.tonthepduybao.api.mapper.dataset.CustomerDataset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * DebtMapper
 *
 * @author khal
 * @since 2023/07/10
 */
@Mapper
public interface CustomerMapper {

    @Select("<script>" +
            "   SELECT" +
            "       tc.id AS id," +
            "       tc.name AS name," +
            "       tc.address AS address," +
            "       tc.email AS email," +
            "       tc.phone AS phone," +
            "       tc.type AS type," +
            "       tc.created_by AS createdBy," +
            "       tc.updated_by AS updatedBy," +
            "       tc.created_at AS createdAt," +
            "       tc.updated_at AS updatedAt" +
            "   FROM " +
            "       ttdb_customer tc " +
            "   WHERE " +
            "       deleted = #{deleted} " +
            "       AND (" +
            "           unaccent(LOWER(tc.name)) LIKE unaccent(#{search}) " +
            "           OR unaccent(LOWER(tc.address)) LIKE unaccent(#{search}) " +
            "           OR unaccent(LOWER(tc.email)) LIKE unaccent(#{search}) " +
            "           OR unaccent(LOWER(tc.phone)) LIKE unaccent(#{search}) " +
            "       )" +
            "   <if test=\"types != null and types.size > 0\">" +
            "       AND tc.type IN " +
            "       <foreach item='item' index='index' collection='types' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   ORDER BY tc.updated_at DESC " +
            "   LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<CustomerDataset> selectCustomer(@Param("search") String search,
                                         @Param("types") List<String> types,
                                         @Param("deleted") boolean deleted,
                                         @Param("limit") int limit,
                                         @Param("offset") int offset);

    @Select("<script>" +
            "   SELECT" +
            "       COUNT(tc.id) " +
            "   FROM " +
            "       ttdb_customer tc " +
            "   WHERE " +
            "       deleted = #{deleted} " +
            "       AND (" +
            "           unaccent(LOWER(tc.name)) LIKE unaccent(#{search}) " +
            "           OR unaccent(LOWER(tc.address)) LIKE unaccent(#{search}) " +
            "           OR unaccent(LOWER(tc.email)) LIKE unaccent(#{search}) " +
            "           OR unaccent(LOWER(tc.phone)) LIKE unaccent(#{search}) " +
            "       )" +
            "   <if test=\"types != null and types.size > 0\">" +
            "       AND tc.type IN " +
            "       <foreach item='item' index='index' collection='types' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "</script>")
    long countCustomer(@Param("search") String search,
                       @Param("deleted") boolean deleted,
                       @Param("types") List<String> types);

}
