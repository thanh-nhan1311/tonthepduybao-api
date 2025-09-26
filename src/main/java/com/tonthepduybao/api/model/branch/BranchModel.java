package com.tonthepduybao.api.model.branch;

import com.tonthepduybao.api.entity.enumeration.EBranchStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * BranchModel
 *
 * @author khal
 * @since 2023/03/04
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BranchModel {

    private Long id;

    @NotBlank
    @Length(max = 255)
    private String name;

    @Length(max = 2048)
    private String mapEmbedUrl;

    @Length(max = 2048)
    private String mapUrl;

    @NotBlank
    @Length(max = 1000)
    private String address;

    @NotBlank
    @Length(max = 20)
    private String phone;

    @Length(max = 20)
    private String zalo;

    @NotBlank
    @Length(max = 255)
    private String manager;

    private EBranchStatus status;

    private String createdAt;
    private String updatedAt;

}
