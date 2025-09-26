package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
@Table(name = "ttdb_debt_detail")
public class DebtDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "unit_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "weight", nullable = false)
    private double weight;

    @Column(name = "avg_proportion", nullable = false)
    private double avgProportion;

    @Column(name = "total_unit_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalUnitPrice;

    @Column(name = "total_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalPrice;

    // FK
    @ManyToOne
    @JoinColumn(name = "debt_id", nullable = false)
    private Debt debt;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @JsonIgnore
    @OneToMany(mappedBy = "debtDetail")
    private Collection<DebtDetailPropertyDetail> debtDetailProperties;

}
