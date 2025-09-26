package com.tonthepduybao.api.entity.enumeration;

import com.tonthepduybao.api.app.exception.model.ParameterException;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.model.CommonData;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ERole
 *
 * @author khal
 * @since 2022/12/24
 */
@Getter
public enum ERole {

    ADMIN(Constant.ADMIN, "Quản trị viên"),
    MANAGER(Constant.MANAGER, "Quản lý"),
    STAFF(Constant.STAFF, "Nhân viên");

    private final String id;
    private final String name;

    ERole(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public interface Constant {
        String ADMIN = "ADMIN";
        String MANAGER = "MANAGER";
        String STAFF = "STAFF";
    }

}
