package com.goldwine.util;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：提供控制台表格输出的宽度计算和补齐方法。
 */
public class TableUtil {
    public static String cell(Object value, int width) {
        String text = value == null ? "" : String.valueOf(value);
        if (displayWidth(text) > width) {
            text = cut(text, width - 2) + "..";
        }
        return text + spaces(width - displayWidth(text));
    }

    public static String line(int... widths) {
        StringBuilder builder = new StringBuilder();
        for (int width : widths) {
            builder.append("+").append(repeat("-", width + 2));
        }
        return builder.append("+").toString();
    }

    public static String row(Object[] values, int... widths) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < widths.length; i++) {
            builder.append("| ").append(cell(values[i], widths[i])).append(" ");
        }
        return builder.append("|").toString();
    }

    private static String cut(String text, int maxWidth) {
        StringBuilder builder = new StringBuilder();
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int charWidth = charWidth(c);
            if (width + charWidth > maxWidth) {
                break;
            }
            builder.append(c);
            width += charWidth;
        }
        return builder.toString();
    }

    private static int displayWidth(String text) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += charWidth(text.charAt(i));
        }
        return width;
    }

    private static int charWidth(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        if (block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || block == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || block == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || block == Character.UnicodeBlock.HIRAGANA
                || block == Character.UnicodeBlock.KATAKANA) {
            return 2;
        }
        return 1;
    }

    private static String spaces(int count) {
        return repeat(" ", Math.max(0, count));
    }

    private static String repeat(String text, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(text);
        }
        return builder.toString();
    }
}
