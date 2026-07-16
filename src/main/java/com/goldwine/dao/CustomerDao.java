package com.goldwine.dao;

import com.goldwine.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 编写者：赵靖羽
 * 完成时间：2026-07-05
 * 类的具体功能：提供客户表的增删改查数据库操作。
 */
public class CustomerDao extends BaseDao {
    public void add(Customer customer) {
        String sql = "INSERT INTO customer(name,gender,phone,address,level,register_time) VALUES(?,?,?,?,?,?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            fill(ps, customer);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Customer customer) {
        String sql = "UPDATE customer SET name=?,gender=?,phone=?,address=?,level=?,register_time=? WHERE id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            fill(ps, customer);
            ps.setInt(7, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM customer WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer findById(int id) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM customer WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Customer> findAll() {
        return query("SELECT * FROM customer ORDER BY id", null);
    }

    public List<Customer> search(String keyword) {
        return query("SELECT * FROM customer WHERE name LIKE ? OR phone LIKE ? ORDER BY id", "%" + keyword + "%");
    }

    public boolean hasOrder(int customerId) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM sale_order WHERE customer_id=?")) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean phoneExists(String phone, int excludeId) {
        String sql = "SELECT COUNT(*) FROM customer WHERE phone=? AND id<>?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Customer> query(String sql, String param) {
        List<Customer> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param != null) {
                ps.setString(1, param);
                if (sql.contains(" OR ")) {
                    ps.setString(2, param);
                }
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

    private void fill(PreparedStatement ps, Customer c) throws SQLException {
        ps.setString(1, c.getName());
        ps.setString(2, c.getGender());
        ps.setString(3, c.getPhone());
        ps.setString(4, c.getAddress());
        ps.setString(5, c.getLevel());
        ps.setString(6, c.getRegisterTime());
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setGender(rs.getString("gender"));
        c.setPhone(rs.getString("phone"));
        c.setAddress(rs.getString("address"));
        c.setLevel(rs.getString("level"));
        c.setRegisterTime(rs.getString("register_time"));
        return c;
    }
}
