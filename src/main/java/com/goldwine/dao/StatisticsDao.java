package com.goldwine.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 编写者：蓝海洋
 * 完成时间：2026-07-05
 * 类的具体功能：提供销售统计相关数据库查询。
 */
public class StatisticsDao extends BaseDao {
    public BigDecimal totalAmount(String start, String end) {
        String sql = "SELECT COALESCE(SUM(final_amount),0) FROM sale_order WHERE status='已完成' AND order_time BETWEEN ? AND ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, start + " 00:00:00");
            ps.setString(2, end + " 23:59:59");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> wineRank() {
        String sql = "SELECT w.id,w.name,COALESCE(SUM(i.quantity),0) qty,COALESCE(SUM(i.subtotal),0) amount " +
                "FROM wine w JOIN sale_order_item i ON w.id=i.wine_id JOIN sale_order o ON i.order_id=o.id " +
                "WHERE o.status='已完成' GROUP BY w.id,w.name ORDER BY qty DESC,amount DESC";
        return list(sql, null);
    }

    public List<String> wineSales(int wineId) {
        String sql = "SELECT w.id,w.name,COALESCE(SUM(i.quantity),0) qty,COALESCE(SUM(i.subtotal),0) amount " +
                "FROM wine w LEFT JOIN sale_order_item i ON w.id=i.wine_id LEFT JOIN sale_order o ON i.order_id=o.id AND o.status='已完成' " +
                "WHERE w.id=? GROUP BY w.id,w.name";
        return list(sql, wineId);
    }

    public BigDecimal customerTotal(int customerId) {
        String sql = "SELECT COALESCE(SUM(final_amount),0) FROM sale_order WHERE status='已完成' AND customer_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> list(String sql, Integer param) {
        List<String> result = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param != null) {
                ps.setInt(1, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(String.format("%d | %s | 销量:%d | 销售额:%s",
                            rs.getInt("id"), rs.getString("name"), rs.getInt("qty"), rs.getBigDecimal("amount")));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
