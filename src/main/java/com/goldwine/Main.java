package com.goldwine;

import com.goldwine.ui.ConsoleMenu;
import com.goldwine.util.DBUtil;

/**
 * 编写者：葛禹贡
 * 完成时间：2026-07-05
 * 类的具体功能：程序入口，初始化数据库并启动控制台菜单。
 */
public class Main {
    public static void main(String[] args) {
        DBUtil.initDatabase();
        new ConsoleMenu().start();
    }
}
