package com.tonthepduybao.api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * SiteProductCategory
 *
 * @author khal
 * @since 2022/09/25
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ttdb_site_product_category")
public class SiteProductCategory {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK
    @ManyToOne
    @JoinColumn(name = "site_category_id", nullable = false)
    private SiteCategory siteCategory;

    @ManyToOne
    @JoinColumn(name = "site_product_id", nullable = false)
    private SiteProduct siteProduct;

}
