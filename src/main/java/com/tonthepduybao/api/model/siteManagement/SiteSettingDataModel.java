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
public class SiteSettingDataModel {

    private List<SiteSettingModel> homeBanners;
    private SiteSettingModel homeAboutUs;
    private List<SiteSettingModel> homeProductCategories;
    private SiteSettingModel homePartner;
    private SiteSettingModel homeContactUs;
    private SiteSettingModel aboutUs;
    private SiteSettingModel contactUs;
    private SiteSettingModel footer;

}
