package com.tonthepduybao.api.mapper;

import com.tonthepduybao.api.mapper.dataset.DebtDataset;
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
public interface DebtMapper {

    @Select("<script>" +
            "   SELECT" +
            "       td.id AS id," +
            "       (SELECT SUM(tdd.total_price) FROM ttdb_debt_detail tdd WHERE tdd.debt_id = td.id) AS totalPrice," +
            "       (SELECT SUM(tdd.total_unit_price) FROM ttdb_debt_detail tdd WHERE tdd.debt_id = td.id) AS totalUnitPrice," +
            "       td.status AS status," +
            "       td.type AS type," +
            "       td.date AS date," +
            "       td.created_by AS createdBy," +
            "       td.updated_by AS updatedBy," +
            "       td.created_at AS createdAt," +
            "       td.updated_at AS updatedAt," +
            "       td.customer_id AS customerId" +
            "   FROM " +
            "       ttdb_debt td " +
            "   WHERE " +
            "       td.status = #{status} " +
            "       AND unaccent(LOWER(td.id)) LIKE unaccent(#{search}) " +
            "   <if test=\"types != null and types.size > 0\">" +
            "       AND td.type IN " +
            "       <foreach item='item' index='index' collection='types' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"customerIds != null and customerIds.size > 0\">" +
            "       AND td.customer_id IN " +
            "       <foreach item='item' index='index' collection='customerIds' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"fromDate != '' and toDate != ''\">" +
            "       AND td.date &gt;= #{fromDate} " +
            "       AND td.date &lt;= #{toDate}" +
            "   </if> " +
            "   ORDER BY td.updated_at DESC " +
            "   LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<DebtDataset> selectDebt(@Param("status") String status,
                                 @Param("search") String search,
                                 @Param("fromDate") String fromDate,
                                 @Param("toDate") String toDate,
                                 @Param("types") List<String> types,
                                 @Param("customerIds") List<Long> customerIds,
                                 @Param("limit") int limit,
                                 @Param("offset") int offset);

    @Select("<script>" +
            "   SELECT" +
            "       COUNT(td.id) " +
            "   FROM " +
            "       ttdb_debt td " +
            "   WHERE " +
            "       td.status = #{status} " +
            "       AND unaccent(LOWER(td.id)) LIKE unaccent(#{search}) " +
            "   <if test=\"types != null and types.size > 0\">" +
            "       AND td.type IN " +
            "       <foreach item='item' index='index' collection='types' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"customerIds != null and customerIds.size > 0\">" +
            "       AND td.customer_id IN " +
            "       <foreach item='item' index='index' collection='customerIds' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"fromDate != '' and toDate != ''\">" +
            "       AND td.date &gt;= #{fromDate} " +
            "       AND td.date &lt;= #{toDate}" +
            "   </if> " +
            "</script>")
    long countDebt(@Param("status") String status,
                   @Param("search") String search,
                   @Param("fromDate") String fromDate,
                   @Param("toDate") String toDate,
                   @Param("types") List<String> types,
                   @Param("customerIds") List<Long> customerIds);

}
