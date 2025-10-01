package com.localtrip.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 장소 타입 Enum
 */
@Getter
@RequiredArgsConstructor
public enum PlaceType {
    ATTRACTION(1, "관광 명소"),
    RESTAURANT(2, "맛집"),
    ACCOMMODATION(3, "숙소"),
    ETC(4, "기타");

    private final int code;
    private final String description;

    /**
     * code로 PlaceType 찾기
     */
    public static PlaceType fromCode(int code) {
        for (PlaceType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PlaceType code: " + code);
    }

    /**
     * name으로 PlaceType 찾기 (대소문자 구분 없음)
     */
    public static PlaceType fromName(String name) {
        for (PlaceType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PlaceType name: " + name);
    }
}
