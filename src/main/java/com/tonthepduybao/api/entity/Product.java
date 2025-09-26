package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tonthepduybao.api.entity.enumeration.EType;
import com.tonthepduybao.api.entity.enumeration.EProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * DebtDetail
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
@Table(name = "ttdb_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private EType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EProductStatus status;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "created_at", length = 14, nullable = false)
    private String createdAt;

    @Column(name = "updated_at", length = 14, nullable = false)
    private String updatedAt;

    @Column(name = "date", length = 8, nullable = false)
    private String date;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "size")
    private Double size;

    @Column(name = "size_calculator")
    private Double sizeCalculator;

    @Column(name = "parent")
    private Long parent;

    // FK
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Collection<ProductInvoice> productInvoices;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private Collection<ProductPropertyDetail> productPropertyDetails;

}
