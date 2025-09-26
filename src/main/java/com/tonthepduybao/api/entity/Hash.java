package com.tonthepduybao.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Hash
 *
 * @author khal
 * @since 2022/05/01
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ttdb_hash")
public class Hash implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonIgnore
    @Column(name = "password", length = 100, nullable = false)
    private String hash;

    @Column(name = "pwd", length = 100, nullable = false)
    private String pwd;

    @Column(name = "user_id", nullable = false)
    private Long userId;

}
