package com.tonthepduybao.api.model.siteManagement;

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
public class SiteSettingUpsertForm {

    private List<SiteSettingModel> settings;

}
