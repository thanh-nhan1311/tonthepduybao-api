package com.tonthepduybao.api.mapper;

import com.tonthepduybao.api.mapper.dataset.InvoiceDataset;
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
public interface InvoiceMapper {

    @Select("<script>" +
            "   SELECT" +
            "       ti.id AS id," +
            "       ti.total_price AS totalPrice, " +
            "       ti.paid_price AS paidPrice, " +
            "       ti.status AS status," +
            "       ti.date AS date," +
            "       ti.note AS note," +
            "       ti.created_by AS createdBy," +
            "       ti.updated_by AS updatedBy," +
            "       ti.created_at AS createdAt," +
            "       ti.updated_at AS updatedAt," +
            "       ti.customer_id AS customerId," +
            "       ti.branch_id AS branchId " +
            "   FROM " +
            "       ttdb_invoice ti " +
            "   WHERE " +
            "       1 = 1" +
            "       AND ti.status = #{status} " +
            "   <if test=\"search != null\">" +
            "       AND ti.id = #{search} " +
            "   </if> " +
            "   <if test=\"branchIds != null and branchIds.size > 0\">" +
            "       AND ti.branch_id IN " +
            "       <foreach item='item' index='index' collection='branchIds' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"customerIds != null and customerIds.size > 0\">" +
            "       AND ti.customer_id IN " +
            "       <foreach item='item' index='index' collection='customerIds' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"fromDate != '' and toDate != ''\">" +
            "       AND ti.date &gt;= #{fromDate} " +
            "       AND ti.date &lt;= #{toDate}" +
            "   </if> " +
            "   ORDER BY ti.updated_at DESC " +
            "   LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<InvoiceDataset> selectInvoice(@Param("status") String status,
                                       @Param("search") Long search,
                                       @Param("fromDate") String fromDate,
                                       @Param("toDate") String toDate,
                                       @Param("branchIds") List<Long> branchIds,
                                       @Param("customerIds") List<Long> customerIds,
                                       @Param("limit") int limit,
                                       @Param("offset") int offset);

    @Select("<script>" +
            "   SELECT" +
            "       COUNT(ti.id) " +
            "   FROM " +
            "       ttdb_invoice ti " +
            "   WHERE " +
            "       1 = 1" +
            "       AND ti.status = #{status} " +
            "   <if test=\"search != null\">" +
            "       AND ti.id = #{search} " +
            "   </if> " +
            "   <if test=\"branchIds != null and branchIds.size > 0\">" +
            "       AND ti.branch_id IN " +
            "       <foreach item='item' index='index' collection='branchIds' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"customerIds != null and customerIds.size > 0\">" +
            "       AND ti.customer_id IN " +
            "       <foreach item='item' index='index' collection='customerIds' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"fromDate != '' and toDate != ''\">" +
            "       AND ti.date &gt;= #{fromDate} " +
            "       AND ti.date &lt;= #{toDate}" +
            "   </if> " +
            "</script>")
    long countInvoice(@Param("status") String status,
                      @Param("search") Long search,
                      @Param("fromDate") String fromDate,
                      @Param("toDate") String toDate,
                      @Param("branchIds") List<Long> branchIds,
                      @Param("customerIds") List<Long> customerIds);

}
