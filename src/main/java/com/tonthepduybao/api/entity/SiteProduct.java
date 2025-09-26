package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tonthepduybao.api.entity.enumeration.ESiteProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;

/**
 * SiteProduct
 *
 * @author khal
 * @since 2022/05/01
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ttdb_site_product")
public class SiteProduct implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @Column(name = "seo_url", length = 600, nullable = false, unique = true)
    private String seoUrl;

    @Column(name = "summary", length = 500)
    private String summary;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail", length = 2048)
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ESiteProductStatus status;

    @Column(name = "created_at", length = 14, nullable = false)
    private String createdAt;

    @Column(name = "updated_at", length = 14, nullable = false)
    private String updatedAt;

    // FK
    @JsonIgnore
    @OneToMany(mappedBy = "siteProduct")
    private Collection<SiteProductCategory> siteProductCategories;

    @JsonIgnore
    @OneToMany(mappedBy = "siteProduct")
    private Collection<SiteProductImage> siteProductImages;
}
