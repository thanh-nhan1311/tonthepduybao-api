package com.tonthepduybao.api.app.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TimeUtils
 *
 * @author khale
 * @since 2022/10/22
 */
public class TimeUtils {

    public final static ZoneId ZONE_ID = ZoneId.of("Asia/Ho_Chi_Minh");
    public static final String DTF_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String DTF_yyyyMMdd = "yyyyMMdd";
    public static final String DTF_dd_MM_yyyy_HH_mm_ss = "dd-MM-yyyy HH:mm:ss";
    public static final String DTF_dd_MM_yyyy = "dd-MM-yyyy";

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZONE_ID);
    }

    public static ZonedDateTime parse(final String str) {
        return ZonedDateTime.parse(str, DateTimeFormatter.ofPattern(DTF_yyyyMMddHHmmss));
    }

    public static String nowStr() {
        return now().format(DateTimeFormatter.ofPattern(DTF_yyyyMMddHHmmss));
    }

    public static String convert(final String str) {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DTF_yyyyMMddHHmmss))
                .format(DateTimeFormatter.ofPattern(DTF_dd_MM_yyyy_HH_mm_ss));
    }

    public static String convertDate(final String str) {
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(DTF_yyyyMMdd))
                .format(DateTimeFormatter.ofPattern(DTF_dd_MM_yyyy));
    }

}
