package com.goldwine.dao;

import com.goldwine.entity.Origin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：提供产地表的增删改查数据库操作。
 */
public class OriginDao extends BaseDao {
    public void add(Origin origin) {
        String sql = "INSERT INTO origin(country,is_domestic,province,city,county,foreign_city,description) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            fill(ps, origin, false);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Origin origin) {
        String sql = "UPDATE origin SET country=?,is_domestic=?,province=?,city=?,county=?,foreign_city=?,description=? WHERE id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            fill(ps, origin, false);
            ps.setInt(8, origin.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM origin WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Origin findById(int id) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM origin WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Origin> findAll() {
        List<Origin> list = new ArrayList<>();
        try (Connection c = getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM origin ORDER BY id")) {
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasWine(int id) {
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM wine WHERE origin_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fill(PreparedStatement ps, Origin o, boolean includeId) throws SQLException {
        ps.setString(1, o.getCountry());
        ps.setInt(2, o.isDomestic() ? 1 : 0);
        ps.setString(3, o.getProvince());
        ps.setString(4, o.getCity());
        ps.setString(5, o.getCounty());
        ps.setString(6, o.getForeignCity());
        ps.setString(7, o.getDescription());
        if (includeId) {
            ps.setInt(8, o.getId());
        }
    }

    private Origin map(ResultSet rs) throws SQLException {
        Origin o = new Origin();
        o.setId(rs.getInt("id"));
        o.setCountry(rs.getString("country"));
        o.setDomestic(rs.getInt("is_domestic") == 1);
        o.setProvince(rs.getString("province"));
        o.setCity(rs.getString("city"));
        o.setCounty(rs.getString("county"));
        o.setForeignCity(rs.getString("foreign_city"));
        o.setDescription(rs.getString("description"));
        return o;
    }
}
