package com.tonthepduybao.api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * SiteProductImage
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
@Table(name = "ttdb_site_product_image")
public class SiteProductImage {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", length = 2048, nullable = false)
    private String url;

    // FK
    @ManyToOne
    @JoinColumn(name = "site_product_id", nullable = false)
    private SiteProduct siteProduct;

}
