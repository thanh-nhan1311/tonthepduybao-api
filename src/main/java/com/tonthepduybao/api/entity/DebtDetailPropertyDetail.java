package com.tonthepduybao.api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * DebtDetailPropertyDetail
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
@Table(name = "ttdb_debt_detail_property_detail")
public class DebtDetailPropertyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // FK
    @ManyToOne
    @JoinColumn(name = "debt_detail_id", nullable = false)
    private DebtDetail debtDetail;

    @ManyToOne
    @JoinColumn(name = "property_detail_id", nullable = false)
    private PropertyDetail propertyDetail;

}
