package com.goldwine.dao;

import com.goldwine.entity.SaleOrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 编写者：何依瞳
 * 完成时间：2026-07-05
 * 类的具体功能：提供订单明细表的数据库操作。
 */
public class SaleOrderItemDao extends BaseDao {
    public void add(Connection c, SaleOrderItem item) throws SQLException {
        String sql = "INSERT INTO sale_order_item(order_id,wine_id,quantity,unit_price,subtotal) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getWineId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.setBigDecimal(5, item.getSubtotal());
            ps.executeUpdate();
        }
    }

    public List<SaleOrderItem> findByOrderId(int orderId) {
        List<SaleOrderItem> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM sale_order_item WHERE order_id=?")) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<SaleOrderItem> findByOrderId(Connection c, int orderId) throws SQLException {
        List<SaleOrderItem> list = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM sale_order_item WHERE order_id=?")) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    private SaleOrderItem map(ResultSet rs) throws SQLException {
        SaleOrderItem item = new SaleOrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setWineId(rs.getInt("wine_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));
        return item;
    }
}
