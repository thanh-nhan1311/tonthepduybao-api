package com.tonthepduybao.api.model.user;

import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.entity.Role;
import com.tonthepduybao.api.entity.enumeration.EUserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserData
 *
 * @author khal
 * @since 2023/07/17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String avatar;
    private String phone;
    private String email;
    private String address;
    private EUserStatus status;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private Role role;
    private Branch branch;

}
