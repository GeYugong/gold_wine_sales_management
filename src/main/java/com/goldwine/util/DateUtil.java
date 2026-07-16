package com.goldwine.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 编写者：曾欣欣
 * 完成时间：2026-07-05
 * 类的具体功能：提供日期时间格式化工具方法。
 */
public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String now() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
