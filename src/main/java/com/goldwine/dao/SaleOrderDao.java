package com.goldwine.dao;

import com.goldwine.entity.SaleOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：提供订单主表的数据库操作。
 */
public class SaleOrderDao extends BaseDao {
    public int add(Connection c, SaleOrder order) throws SQLException {
        String sql = "INSERT INTO sale_order(customer_id,order_time,total_amount,discount_amount,final_amount,pay_method,status) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getCustomerId());
            ps.setString(2, order.getOrderTime());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setBigDecimal(4, order.getDiscountAmount());
            ps.setBigDecimal(5, order.getFinalAmount());
            ps.setString(6, order.getPayMethod());
            ps.setString(7, order.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void updateStatus(Connection c, int orderId, String status) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE sale_order SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public SaleOrder findById(int id) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM sale_order WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SaleOrder findById(Connection c, int id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT * FROM sale_order WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<SaleOrder> findAll() {
        return query("SELECT * FROM sale_order ORDER BY id DESC", null);
    }

    public List<SaleOrder> findByCustomer(int customerId) {
        return query("SELECT * FROM sale_order WHERE customer_id=? ORDER BY id DESC", customerId);
    }

    public List<SaleOrder> findByStatus(String status) {
        List<SaleOrder> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM sale_order WHERE status=? ORDER BY id DESC")) {
            ps.setString(1, status);
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

    private List<SaleOrder> query(String sql, Integer param) {
        List<SaleOrder> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param != null) {
                ps.setInt(1, param);
            }
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

    private SaleOrder map(ResultSet rs) throws SQLException {
        SaleOrder order = new SaleOrder();
        order.setId(rs.getInt("id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setOrderTime(rs.getString("order_time"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        order.setFinalAmount(rs.getBigDecimal("final_amount"));
        order.setPayMethod(rs.getString("pay_method"));
        order.setStatus(rs.getString("status"));
        return order;
    }
}
