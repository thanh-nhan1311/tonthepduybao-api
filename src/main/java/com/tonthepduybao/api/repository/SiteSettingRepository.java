package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.SiteSetting;
import com.tonthepduybao.api.entity.enumeration.ESiteSettingKey;
import com.tonthepduybao.api.entity.enumeration.ESiteSettingMasterKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * SiteSettingRepository
 *
 * @author khal
 * @since 2023/04/03
 */
public interface SiteSettingRepository extends JpaRepository<SiteSetting, Long> {

    List<SiteSetting> findByMasterKeyAndKey(ESiteSettingMasterKey masterKey, ESiteSettingKey key);
    void deleteByMasterKeyAndKey(ESiteSettingMasterKey masterKey, ESiteSettingKey key);

}
