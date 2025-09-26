package com.tonthepduybao.api.mapper;

import com.tonthepduybao.api.mapper.dataset.ProductDataset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ProductMapper
 *
 * @author khal
 * @since 2023/07/10
 */
@Mapper
public interface ProductMapper {

    @Select("<script>" +
            "   SELECT" +
            "       tp.id," +
            "       tp.status," +
            "       tp.parent," +
            "       tp.name," +
            "       tp.type," +
            "       tp.created_by AS createdBy," +
            "       tp.updated_by AS updatedBy," +
            "       tp.created_at AS createdAt," +
            "       tp.updated_at AS updatedAt," +
            "       tp.branch_id AS branchId," +
            "       tp.quantity " +
            "   FROM " +
            "       ttdb_product tp " +
            "   WHERE " +
            "       tp.status = #{status} " +
            "       AND unaccent(LOWER(tp.name)) LIKE unaccent(#{search}) " +
            "   <if test=\"types != null and types.size > 0\">" +
            "       AND tp.type IN " +
            "       <foreach item='item' index='index' collection='types' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"branchIds != null and branchIds.size > 0\">" +
            "       AND tp.branch_id IN " +
            "       <foreach item='item' index='index' collection='branchIds' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   ORDER BY tp.updated_at DESC " +
            "   LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<ProductDataset> selectProduct(@Param("status") String status,
                                       @Param("search") String search,
                                       @Param("types") List<String> types,
                                       @Param("branchIds") List<Long> branchIds,
                                       @Param("limit") int limit,
                                       @Param("offset") int offset);

    @Select("<script>" +
            "   SELECT" +
            "       COUNT(tp.id) " +
            "   FROM " +
            "       ttdb_product tp " +
            "   WHERE " +
            "       tp.status = #{status} " +
            "       AND unaccent(LOWER(tp.name)) LIKE unaccent(#{search}) " +
            "   <if test=\"types != null and types.size > 0\">" +
            "       AND tp.type IN " +
            "       <foreach item='item' index='index' collection='types' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "   <if test=\"branchIds != null and branchIds.size > 0\">" +
            "       AND tp.branch_id IN " +
            "       <foreach item='item' index='index' collection='branchIds' open='(' separator=',' close=')'> " +
            "           #{item} " +
            "       </foreach> " +
            "   </if> " +
            "</script>")
    long countProduct(@Param("status") String status,
                      @Param("search") String search,
                      @Param("types") List<String> types,
                      @Param("branchIds") List<Long> branchIds);

}
