package com.tonthepduybao.api.entity.enumeration;

import lombok.Getter;

/**
 * EType
 *
 * @author khal
 * @since 2023/05/14
 */
@Getter
public enum EType {

    IRON(Constant.IRON, "Sắt"),
    STEEL(Constant.STEEL, "Thép"),
    CORRUGATED(Constant.CORRUGATED, "Tôn"),
    SCREW(Constant.SCREW, "Vật liệu khác");

    private final String id;
    private final String name;

    EType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public interface Constant {
        String IRON = "IRON";
        String STEEL = "STEEL";
        String CORRUGATED = "CORRUGATED";
        String SCREW = "SCREW";
    }

}
