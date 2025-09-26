package com.tonthepduybao.api.service.siteManagement;

import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.helper.S3Helper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.app.utils.Utils;
import com.tonthepduybao.api.entity.SiteCategory;
import com.tonthepduybao.api.entity.SiteContact;
import com.tonthepduybao.api.entity.SitePartner;
import com.tonthepduybao.api.entity.SiteSetting;
import com.tonthepduybao.api.entity.enumeration.ESiteSettingKey;
import com.tonthepduybao.api.entity.enumeration.ESiteSettingMasterKey;
import com.tonthepduybao.api.model.siteManagement.*;
import com.tonthepduybao.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SiteManagementService
 *
 * @author khal
 * @since 2023/03/05
 */
@Service
@RequiredArgsConstructor
public class SiteManagementServiceImpl implements SiteManagementService {

    private final S3Helper s3Helper;

    private final MessageHelper messageHelper;
    private final SiteContactRepository siteContactRepository;
    private final SiteCategoryRepository siteCategoryRepository;
    private final SitePartnerRepository sitePartnerRepository;
    private final SiteProductCategoryRepository siteProductCategoryRepository;
    private final SiteSettingRepository siteSettingRepository;

    /**
     * Site Contact
     */
    @Override
    public List<SiteContactModel> searchContact(final String search) {
        return DataBuilder.toList(siteContactRepository.searchContact("%" + search.toLowerCase().trim() + "%"), SiteContactModel.class);
    }

    @Override
    public void resolveContact(final Long id) {
        SiteContact siteContact = siteContactRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("ID liên hệ", id));
        siteContact.setResolvedFlag(true);
        siteContact.setCreatedAt(TimeUtils.nowStr());
        siteContactRepository.saveAndFlush(siteContact);
    }

    @Override
    public void deleteContact(final Long id) {
        siteContactRepository.deleteById(id);
    }

    /**
     * Site Category
     */
    @Override
    public List<SiteCategoryModel> searchCategory(final String search) {
        return siteCategoryRepository.searchCategory("%" + search.toLowerCase().trim() + "%")
                .stream()
                .map(item -> {
                    SiteCategoryModel model = DataBuilder.to(item, SiteCategoryModel.class);
                    long totalProduct = siteProductCategoryRepository.countAllBySiteCategory(item);
                    model.setTotalProduct(totalProduct);

                    return model;
                }).collect(Collectors.toList());
    }

    @Override
    public void upsertCategory(final SiteCategoryModel model) {
        boolean isEdit = Objects.nonNull(model.getId());
        SiteCategory siteCategory = DataBuilder.to(model, SiteCategory.class);
        siteCategory.setUpdatedAt(TimeUtils.nowStr());

        if (isEdit) {
            String seoUrl = Utils.convertToSlug(siteCategory.getName()) + "-" + siteCategory.getId();
            siteCategory.setSeoUrl(seoUrl);
        } else siteCategory.setSeoUrl("-");

        SiteCategory savedSiteCategory = siteCategoryRepository.saveAndFlush(siteCategory);
        if (!isEdit) {
            String seoUrl = Utils.convertToSlug(savedSiteCategory.getName()) + "-" + savedSiteCategory.getId();
            savedSiteCategory.setSeoUrl(seoUrl);
            siteCategoryRepository.saveAndFlush(savedSiteCategory);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteCategory(final Long id) {
        siteCategoryRepository.deleteById(id);
    }

    /**
     * Site Partner
     */
    @Override
    public List<SitePartnerModel> searchPartner(final String search) {
        return sitePartnerRepository.searchPartner("%" + search.toLowerCase().trim() + "%")
                .stream()
                .map(item -> DataBuilder.to(item, SitePartnerModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void upsertPartner(final SitePartnerModel model) {
        Long id = model.getId();
        SitePartner sitePartner = new SitePartner();

        if (Objects.nonNull(id))
            sitePartner = sitePartnerRepository.findById(id)
                    .orElseThrow(messageHelper.buildDataNotFound("ID đối tác", id));

        String logo;
        String updatedAt = TimeUtils.nowStr();

        if (Objects.nonNull(model.getLogoFile())) {
            s3Helper.delete(sitePartner.getLogo());
            logo = s3Helper.upload("partner", model.getLogoFile(), "doi-tac-" + updatedAt);
        } else logo = model.getLogo();

        sitePartner.setLogo(logo);
        sitePartner.setName(model.getName());
        sitePartner.setUpdatedAt(updatedAt);
        sitePartnerRepository.saveAndFlush(sitePartner);
    }

    @Override
    public void deletePartner(final Long id) {
        SitePartner sitePartner = sitePartnerRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("ID đối tác", id));
        s3Helper.delete(sitePartner.getLogo());
        sitePartnerRepository.deleteById(id);
    }


    /**
     * Site Product
     */
    @Override
    public List<SiteProductModel> searchProduct(final String search) {
        return null;
    }

    @Override
    public void upsertProduct(final SiteProductModel model) {

    }

    @Override
    public void deleteProduct(final Long id) {

    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteSetting(final Long id) {
        siteSettingRepository.deleteById(id);
    }

    /**
     * Site Setting
     */
    @Override
    public SiteSettingDataModel getAllSetting() {
        List<SiteSettingModel> homeBanners = getSettings(ESiteSettingMasterKey.HOME, ESiteSettingKey.BANNER);
        SiteSettingModel homeAboutUs = getSetting(ESiteSettingMasterKey.HOME, ESiteSettingKey.ABOUT_US);
        List<SiteSettingModel> homeProductCategories = getSettings(ESiteSettingMasterKey.HOME, ESiteSettingKey.PRODUCT_CATEGORY);
        SiteSettingModel homePartner = getSetting(ESiteSettingMasterKey.HOME, ESiteSettingKey.PARTNER);
        SiteSettingModel homeContactUs = getSetting(ESiteSettingMasterKey.HOME, ESiteSettingKey.CONTACT_US);
        SiteSettingModel aboutUs = getSetting(ESiteSettingMasterKey.ABOUT_US, null);
        SiteSettingModel contactUs = getSetting(ESiteSettingMasterKey.CONTACT_US, null);
        SiteSettingModel footer = getSetting(ESiteSettingMasterKey.FOOTER, null);

        return SiteSettingDataModel.builder()
                .homeBanners(homeBanners)
                .homeAboutUs(homeAboutUs)
                .homeProductCategories(homeProductCategories)
                .homePartner(homePartner)
                .homeContactUs(homeContactUs)
                .aboutUs(aboutUs)
                .contactUs(contactUs)
                .footer(footer)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveSetting(final SiteSettingUpsertForm form) {
        if (CollectionUtils.isEmpty(form.getSettings())) return;

        form.getSettings().forEach(item -> {
            Long id = item.getId();

            SiteSetting siteSetting = Objects.nonNull(id)
                    ? getSiteSetting(id)
                    : new SiteSetting();

            siteSetting.setMasterKey(item.getMasterKey());
            siteSetting.setKey(item.getKey());
            siteSetting.setValue(item.getValue());
            siteSettingRepository.saveAndFlush(siteSetting);
        });
    }

    /**
     * PRIVATE FUNCTION
     */
    private SiteSettingModel getSetting(final ESiteSettingMasterKey masterKey, final ESiteSettingKey key) {
        List<SiteSettingModel> siteSettings = DataBuilder.toList(siteSettingRepository.findByMasterKeyAndKey(masterKey, key), SiteSettingModel.class);
        return CollectionUtils.isEmpty(siteSettings) ? null : siteSettings.get(0);
    }
    private List<SiteSettingModel> getSettings(final ESiteSettingMasterKey masterKey, final ESiteSettingKey key) {
        return DataBuilder.toList(siteSettingRepository.findByMasterKeyAndKey(masterKey, key), SiteSettingModel.class);
    }
    private SiteSetting getSiteSetting(final Long id) {
        return siteSettingRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("ID cài đặt", id));
    }
}
