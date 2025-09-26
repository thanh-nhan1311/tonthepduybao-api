package com.tonthepduybao.api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ProductPropertyDetail
 *
 * @author khal
 * @since 2023/05/14
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ttdb_product_property_detail")
public class ProductPropertyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // FK
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "property_detail_id", nullable = false)
    private PropertyDetail propertyDetail;

}
