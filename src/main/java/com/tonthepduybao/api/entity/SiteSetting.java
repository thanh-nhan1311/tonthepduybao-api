package com.tonthepduybao.api.entity;

import com.tonthepduybao.api.entity.enumeration.ESiteSettingKey;
import com.tonthepduybao.api.entity.enumeration.ESiteSettingMasterKey;
import jakarta.persistence.*;
import lombok.*;

/**
 * SiteSetting
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
@Table(name = "ttdb_site_setting")
public class SiteSetting {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "master_key", nullable = false)
    private ESiteSettingMasterKey masterKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "key")
    private ESiteSettingKey key;

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

}
