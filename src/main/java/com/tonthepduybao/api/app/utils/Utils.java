package com.tonthepduybao.api.app.utils;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * CommonUtils
 *
 * @author khal
 * @since 2022/06/13
 */
public class Utils {

    public static String getExt(final String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String normalize(final String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString)
                .replaceAll("")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D"); // this is special case that you have to handle manually
    }

    public static String convertToSlug(final String str) {
        return normalize(str)
                .replaceAll("[^a-zA-Z0-9\\s+]", "")
                .replaceAll("\\s+", " ")
                .replaceAll(" ", "-")
                .toLowerCase();
    }

    public static String formatCurrency(final double value) {
        Locale locale = new Locale.Builder().setLanguage("vi").setRegion("VN").build();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(value);
    }

}
