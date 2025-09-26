package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tonthepduybao.api.entity.enumeration.EDebtStatus;
import com.tonthepduybao.api.entity.enumeration.EType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * Debt
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
@Table(name = "ttdb_debt")
public class Debt {

    @Id
    @Column(name = "id", nullable = false, length = 50, unique = true)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private EDebtStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private EType type;

    @Column(name = "date", length = 8, nullable = false)
    private String date;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "created_at", length = 14, nullable = false)
    private String createdAt;

    @Column(name = "updated_at", length = 14, nullable = false)
    private String updatedAt;

    // FK
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonIgnore
    @OneToMany(mappedBy = "debt")
    private Collection<DebtDetail> debtDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "debt")
    private Collection<DebtProperty> debtProperties;

}
