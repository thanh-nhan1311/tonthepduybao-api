package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * SiteCategory
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
@Table(name = "ttdb_site_category")
public class SiteCategory {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @Column(name = "seo_url", length = 600, nullable = false, unique = true)
    private String seoUrl;

    @Column(name = "parent")
    private Long parent;

    @Column(name = "updated_at", length = 14, nullable = false)
    private String updatedAt;

    // FK
    @OneToMany(mappedBy = "siteCategory")
    private Collection<SiteProductCategory> siteProductCategories;

}
