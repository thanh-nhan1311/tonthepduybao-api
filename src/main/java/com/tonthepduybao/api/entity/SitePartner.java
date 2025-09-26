package com.tonthepduybao.api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * SitePartner
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
@Table(name = "ttdb_site_partner")
public class SitePartner {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "logo", length = 2048, nullable = false)
    private String logo;

    @Column(name = "updated_at", length = 14, nullable = false)
    private String updatedAt;

}
