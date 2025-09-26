package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tonthepduybao.api.entity.enumeration.ERole;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * Role
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
@Table(name = "ttdb_role")
public class Role {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "id", length = 50, nullable = false)
    private ERole id;

    @Column(name = "name", nullable = false)
    private String name;

    // FK
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Collection<User> users;

}
