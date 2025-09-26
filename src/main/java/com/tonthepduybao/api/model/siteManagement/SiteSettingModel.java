package com.tonthepduybao.api.model.siteManagement;

import com.tonthepduybao.api.entity.enumeration.ESiteSettingKey;
import com.tonthepduybao.api.entity.enumeration.ESiteSettingMasterKey;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

/**
 * SiteSettingDataModel
 *
 * @author khal
 * @since 2023/04/03
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteSettingModel {

    private Long id;

    private ESiteSettingMasterKey masterKey;

    private ESiteSettingKey key;

    private String value;

}
