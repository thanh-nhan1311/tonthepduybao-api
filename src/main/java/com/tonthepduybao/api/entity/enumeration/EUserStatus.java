package com.tonthepduybao.api.entity.enumeration;

import com.tonthepduybao.api.app.exception.model.ParameterException;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.model.CommonData;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EUserStatus
 *
 * @author khal
 * @since 2022/05/02
 */
@Getter
public enum EUserStatus {

    ACTIVE(Constant.ACTIVE, "Đã kích hoạt"),
    BLOCKED(Constant.BLOCKED, "Đã bị khoá"),;

    private final String id;
    private final String name;

    EUserStatus(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static EUserStatus get(final String name) {
        return Arrays.asList(EUserStatus.values())
                .stream()
                .filter(item -> item.name().equals(name))
                .findFirst()
                .orElseThrow(ParameterException.supplier(List.of("EUserStatus", name)));
    }

    public static List<CommonData> get() {
        return Arrays.asList(EUserStatus.values())
                .stream()
                .map(item -> DataBuilder.to(item, CommonData.class))
                .collect(Collectors.toList());
    }

    public interface Constant {
        String ACTIVE = "ACTIVE";
        String BLOCKED = "BLOCKED";
    }

}
