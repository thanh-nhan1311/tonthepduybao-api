package com.tonthepduybao.api.service.siteManagement;

import com.tonthepduybao.api.model.siteManagement.*;

import java.util.List;

/**
 * SiteManagementService
 *
 * @author khal
 * @since 2023/03/05
 */
public interface SiteManagementService {

    List<SiteContactModel> searchContact(String search);
    void resolveContact(Long id);
    void deleteContact(Long id);

    List<SiteCategoryModel> searchCategory(String search);
    void upsertCategory(SiteCategoryModel model);
    void deleteCategory(Long id);

    List<SitePartnerModel> searchPartner(String search);
    void upsertPartner(SitePartnerModel model);
    void deletePartner(Long id);

    List<SiteProductModel> searchProduct(String search);
    void upsertProduct(SiteProductModel model);
    void deleteProduct(Long id);

    void deleteSetting(Long id);
    SiteSettingDataModel getAllSetting();
    void saveSetting(SiteSettingUpsertForm form);

}
