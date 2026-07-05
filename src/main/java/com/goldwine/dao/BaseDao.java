package com.goldwine.dao;

import com.goldwine.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：DAO 层公共父类，提供数据库连接方法。
 */
public class BaseDao {
    protected Connection getConnection() throws SQLException {
        return DBUtil.getConnection();
    }
}
