package com.goldwine.util;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：封装控制台输入读取和基础校验。
 */
public class InputUtil {
    private final Scanner scanner = new Scanner(System.in);

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
}
