package com.tonthepduybao.api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * SiteContact
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
@Table(name = "ttdb_site_contact")
public class SiteContact {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "email", length = 320)
    private String email;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "resolved_flag", nullable = false)
    private boolean resolvedFlag;

    @Column(name = "created_at", length = 14, nullable = false)
    private String createdAt;
    
    @Column(name = "updated_at", length = 14)
    private String updatedAt;


}
