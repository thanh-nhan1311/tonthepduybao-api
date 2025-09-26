package com.tonthepduybao.api.model.siteManagement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SiteContactModel
 *
 * @author khal
 * @since 2023/03/05
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SiteContactModel {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String content;
    private boolean resolvedFlag;
    private String createdAt;

}
