package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tonthepduybao.api.entity.enumeration.ECustomerType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * Customer
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
@Table(name = "ttdb_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @Column(name = "phone", length = 500)
    private String phone;

    @Column(name = "fax", length = 20)
    private String fax;

    @Column(name = "email", length = 320)
    private String email;

    @Column(name = "address", length = 2000)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private ECustomerType type;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "created_at", length = 14, nullable = false)
    private String createdAt;

    @Column(name = "updated_at", length = 14, nullable = false)
    private String updatedAt;

    // FK
    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private Collection<Debt> debts;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private Collection<ShippingAddress> shippingAddresses;


}
