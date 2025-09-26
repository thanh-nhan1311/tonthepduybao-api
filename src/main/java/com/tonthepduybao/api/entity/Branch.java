package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tonthepduybao.api.entity.enumeration.EBranchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * Branch
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
@Table(name = "ttdb_branch")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "map_embed_url", length = 2048)
    private String mapEmbedUrl;

    @Column(name = "map_url", length = 2048)
    private String mapUrl;

    @Column(name = "address", length = 1000, nullable = false)
    private String address;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "zalo", length = 20)
    private String zalo;

    @Column(name = "manager", nullable = false)
    private String manager;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private EBranchStatus status;

    @Column(name = "created_at", length = 14, nullable = false)
    private String createdAt;

    @Column(name = "updated_at", length = 14, nullable = false)
    private String updatedAt;

    // FK
    @JsonIgnore
    @OneToMany(mappedBy = "branch")
    private Collection<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "branch")
    private Collection<Invoice> invoices;

    @JsonIgnore
    @OneToMany(mappedBy = "branch")
    private Collection<DebtDetail> debtDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "branch")
    private Collection<Product> products;

}
