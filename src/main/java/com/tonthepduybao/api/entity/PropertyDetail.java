package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * PropertyDetail
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
@Table(name = "ttdb_property_detail")
public class PropertyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 500, nullable = false)
    private String name;

    // FK
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @JsonIgnore
    @OneToMany(mappedBy = "propertyDetail")
    private Collection<ProductPropertyDetail> productPropertyDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "propertyDetail")
    private Collection<DebtDetailPropertyDetail> debtDetailPropertyDetails;

}
