package com.goldwine.dao;

import com.goldwine.entity.Wine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：提供红酒表的增删改查和库存更新操作。
 */
public class WineDao extends BaseDao {
    public void add(Wine w) {
        String sql = "INSERT INTO wine(name,brand,type,year,origin_id,grape_varieties,purchase_price,sale_price,stock,status,source_url) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            fill(ps, w);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Wine w) {
        String sql = "UPDATE wine SET name=?,brand=?,type=?,year=?,origin_id=?,grape_varieties=?,purchase_price=?,sale_price=?,stock=?,status=?,source_url=? WHERE id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            fill(ps, w);
            ps.setInt(12, w.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM wine WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Wine findById(int id) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM wine WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Wine> findAll() {
        return query("SELECT * FROM wine ORDER BY id", null);
    }

    public List<Wine> findByName(String name) {
        return query("SELECT * FROM wine WHERE name LIKE ? ORDER BY id", "%" + name + "%");
    }

    public List<Wine> findByType(String type) {
        return query("SELECT * FROM wine WHERE type LIKE ? ORDER BY id", "%" + type + "%");
    }

    public List<Wine> findByOrigin(int originId) {
        return query("SELECT * FROM wine WHERE origin_id=? ORDER BY id", String.valueOf(originId));
    }

    public List<Wine> findLowStock(int threshold) {
        return query("SELECT * FROM wine WHERE stock < ? ORDER BY stock,id", String.valueOf(threshold));
    }

    public void updateStatus(int id, String status) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE wine SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeStock(Connection c, int wineId, int delta) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE wine SET stock = stock + ? WHERE id=?")) {
            ps.setInt(1, delta);
            ps.setInt(2, wineId);
            ps.executeUpdate();
        }
    }

    public boolean hasOrderItem(int wineId) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM sale_order_item WHERE wine_id=?")) {
            ps.setInt(1, wineId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Wine> query(String sql, String param) {
        List<Wine> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param != null) {
                if (param.matches("\\d+")) {
                    ps.setInt(1, Integer.parseInt(param));
                } else {
                    ps.setString(1, param);
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

    private void fill(PreparedStatement ps, Wine w) throws SQLException {
        ps.setString(1, w.getName());
        ps.setString(2, w.getBrand());
        ps.setString(3, w.getType());
        ps.setInt(4, w.getYear());
        ps.setInt(5, w.getOriginId());
        ps.setString(6, w.getGrapeVarieties());
        ps.setBigDecimal(7, w.getPurchasePrice());
        ps.setBigDecimal(8, w.getSalePrice());
        ps.setInt(9, w.getStock());
        ps.setString(10, w.getStatus());
        ps.setString(11, w.getSourceUrl());
    }

    private Wine map(ResultSet rs) throws SQLException {
        Wine w = new Wine();
        w.setId(rs.getInt("id"));
        w.setName(rs.getString("name"));
        w.setBrand(rs.getString("brand"));
        w.setType(rs.getString("type"));
        w.setYear(rs.getInt("year"));
        w.setOriginId(rs.getInt("origin_id"));
        w.setGrapeVarieties(rs.getString("grape_varieties"));
        w.setPurchasePrice(rs.getBigDecimal("purchase_price"));
        w.setSalePrice(rs.getBigDecimal("sale_price"));
        w.setStock(rs.getInt("stock"));
        w.setStatus(rs.getString("status"));
        w.setSourceUrl(rs.getString("source_url"));
        return w;
    }
}
