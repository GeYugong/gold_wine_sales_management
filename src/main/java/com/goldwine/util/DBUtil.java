package com.goldwine.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：管理 SQLite 连接、建表和首次启动数据初始化。
 */
public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:wine.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
            createTables(stmt);
            seedOrigins(conn);
            seedWines(conn);
            seedCustomers(conn);
            seedOrders(conn);
        } catch (Exception e) {
            throw new RuntimeException("数据库初始化失败：" + e.getMessage(), e);
        }
    }

    private static void createTables(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS origin (id INTEGER PRIMARY KEY AUTOINCREMENT, country TEXT NOT NULL, is_domestic INTEGER NOT NULL, province TEXT, city TEXT, county TEXT, foreign_city TEXT, description TEXT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS wine (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, brand TEXT, type TEXT, year INTEGER, origin_id INTEGER NOT NULL, grape_varieties TEXT, purchase_price NUMERIC NOT NULL, sale_price NUMERIC NOT NULL, stock INTEGER NOT NULL, status TEXT NOT NULL, source_url TEXT, FOREIGN KEY(origin_id) REFERENCES origin(id))");
        stmt.execute("CREATE TABLE IF NOT EXISTS customer (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, gender TEXT, phone TEXT UNIQUE, address TEXT, level TEXT NOT NULL, register_time TEXT NOT NULL)");
        stmt.execute("CREATE TABLE IF NOT EXISTS sale_order (id INTEGER PRIMARY KEY AUTOINCREMENT, customer_id INTEGER NOT NULL, order_time TEXT NOT NULL, total_amount NUMERIC NOT NULL, discount_amount NUMERIC NOT NULL, final_amount NUMERIC NOT NULL, pay_method TEXT NOT NULL, status TEXT NOT NULL, FOREIGN KEY(customer_id) REFERENCES customer(id))");
        stmt.execute("CREATE TABLE IF NOT EXISTS sale_order_item (id INTEGER PRIMARY KEY AUTOINCREMENT, order_id INTEGER NOT NULL, wine_id INTEGER NOT NULL, quantity INTEGER NOT NULL, unit_price NUMERIC NOT NULL, subtotal NUMERIC NOT NULL, FOREIGN KEY(order_id) REFERENCES sale_order(id), FOREIGN KEY(wine_id) REFERENCES wine(id))");
    }

    private static void seedOrigins(Connection conn) throws Exception {
        if (count(conn, "origin") > 0) {
            return;
        }
        String sql = "INSERT INTO origin(id,country,is_domestic,province,city,county,foreign_city,description) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String[] row : readCsv("/data/origin_seed.csv")) {
                ps.setInt(1, Integer.parseInt(row[0]));
                ps.setString(2, row[1]);
                ps.setInt(3, Integer.parseInt(row[2]));
                ps.setString(4, row[3]);
                ps.setString(5, row[4]);
                ps.setString(6, row[5]);
                ps.setString(7, row[6]);
                ps.setString(8, row[7]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void seedWines(Connection conn) throws Exception {
        if (count(conn, "wine") > 0) {
            return;
        }
        String sql = "INSERT INTO wine(id,name,brand,type,year,origin_id,grape_varieties,purchase_price,sale_price,stock,status,source_url) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int inserted = 0;
            for (String[] row : readCsv("/data/wine_seed_118.csv")) {
                ps.setInt(1, Integer.parseInt(row[0]));
                ps.setString(2, row[1]);
                ps.setString(3, row[2]);
                ps.setString(4, row[3]);
                ps.setInt(5, Integer.parseInt(row[4]));
                ps.setInt(6, Integer.parseInt(row[5]));
                ps.setString(7, row[6]);
                ps.setBigDecimal(8, new BigDecimal(row[7]));
                ps.setBigDecimal(9, new BigDecimal(row[8]));
                ps.setInt(10, Integer.parseInt(row[9]));
                ps.setString(11, row[10]);
                ps.setString(12, row[11]);
                ps.addBatch();
                inserted++;
                if (inserted >= 30) {
                    break;
                }
            }
            ps.executeBatch();
        }
    }

    private static void seedCustomers(Connection conn) throws SQLException {
        if (count(conn, "customer") > 0) {
            return;
        }
        String sql = "INSERT INTO customer(name,gender,phone,address,level,register_time) VALUES(?,?,?,?,?,?)";
        Object[][] data = {
                {"张明", "男", "13800000001", "山东烟台", "普通客户"},
                {"李娜", "女", "13800000002", "宁夏银川", "品鉴会员"},
                {"王强", "男", "13800000003", "北京朝阳", "窖藏会员"},
                {"赵静", "女", "13800000004", "上海浦东", "私享会员"},
                {"陈晨", "男", "13800000005", "广东深圳", "品鉴会员"}
        };
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Object[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    ps.setString(i + 1, row[i].toString());
                }
                ps.setString(6, DateUtil.now());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void seedOrders(Connection conn) throws SQLException {
        if (count(conn, "sale_order") > 0) {
            return;
        }
        try (PreparedStatement order = conn.prepareStatement("INSERT INTO sale_order(customer_id,order_time,total_amount,discount_amount,final_amount,pay_method,status) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement item = conn.prepareStatement("INSERT INTO sale_order_item(order_id,wine_id,quantity,unit_price,subtotal) VALUES(?,?,?,?,?)");
             PreparedStatement stock = conn.prepareStatement("UPDATE wine SET stock = stock - ? WHERE id = ?")) {
            BigDecimal total = new BigDecimal("200");
            order.setInt(1, 2);
            order.setString(2, DateUtil.now());
            order.setBigDecimal(3, total);
            order.setBigDecimal(4, new BigDecimal("10"));
            order.setBigDecimal(5, new BigDecimal("190"));
            order.setString(6, "微信");
            order.setString(7, "已完成");
            order.executeUpdate();
            try (ResultSet rs = order.getGeneratedKeys()) {
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    item.setInt(1, orderId);
                    item.setInt(2, 1);
                    item.setInt(3, 2);
                    item.setBigDecimal(4, new BigDecimal("100"));
                    item.setBigDecimal(5, total);
                    item.executeUpdate();
                    stock.setInt(1, 2);
                    stock.setInt(2, 1);
                    stock.executeUpdate();
                }
            }
        }
    }

    private static int count(Connection conn, String table) throws SQLException {
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private static List<String[]> readCsv(String path) throws Exception {
        List<String[]> rows = new ArrayList<>();
        InputStream in = DBUtil.class.getResourceAsStream(path);
        if (in == null) {
            throw new IllegalStateException("找不到资源文件：" + path);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                rows.add(parseCsvLine(line));
            }
        }
        return rows;
    }

    private static String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quote = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                quote = !quote;
            } else if (c == ',' && !quote) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        values.add(current.toString());
        return values.toArray(new String[0]);
    }
}
