package com.goldwine.util;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：封装控制台输入读取和基础校验。
 */
public class InputUtil {
    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readString(prompt));
            } catch (NumberFormatException e) {
                System.out.println("请输入正确的整数。");
            }
        }
    }

    public int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("请输入大于 0 的整数。");
        }
    }

    public int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("请输入不小于 0 的整数。");
        }
    }

    public BigDecimal readMoney(String prompt) {
        while (true) {
            try {
                BigDecimal value = new BigDecimal(readString(prompt));
                if (value.compareTo(BigDecimal.ZERO) >= 0) {
                    return value;
                }
                System.out.println("金额不能小于 0。");
            } catch (NumberFormatException e) {
                System.out.println("请输入正确的金额。");
            }
        }
    }

    public String readOption(String prompt, String... options) {
        while (true) {
            String value = readString(prompt);
            for (String option : options) {
                if (option.equals(value)) {
                    return value;
                }
            }
            System.out.println("输入不合法，只能输入：" + String.join("、", options) + "。");
        }
    }

    public String readPattern(String prompt, String regex, String errorMessage) {
        while (true) {
            String value = readString(prompt);
            if (value.matches(regex)) {
                return value;
            }
            System.out.println(errorMessage);
        }
    }

    public String readDate(String prompt) {
        while (true) {
            String value = readString(prompt);
            try {
                LocalDate.parse(value);
                return value;
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请输入 yyyy-MM-dd。");
            }
        }
    }
}
