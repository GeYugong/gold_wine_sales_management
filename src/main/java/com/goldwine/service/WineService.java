package com.goldwine.service;

import com.goldwine.dao.WineDao;
import com.goldwine.entity.Wine;

import java.math.BigDecimal;
import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：处理红酒管理业务规则。
 */
public class WineService {
    private final WineDao wineDao = new WineDao();

    public String add(Wine wine) {
        String error = validate(wine);
        if (error != null) {
            return error;
        }
        wineDao.add(wine);
        return null;
    }

    public String update(Wine wine) {
        String error = validate(wine);
        if (error != null) {
            return error;
        }
        wineDao.update(wine);
        return null;
    }

    public boolean delete(int id) {
        if (wineDao.hasOrderItem(id)) {
            return false;
        }
        wineDao.delete(id);
        return true;
    }

    public Wine findById(int id) { return wineDao.findById(id); }
    public List<Wine> findAll() { return wineDao.findAll(); }
    public List<Wine> findByName(String name) { return wineDao.findByName(name); }
    public List<Wine> findByType(String type) { return wineDao.findByType(type); }
    public List<Wine> findByOrigin(int originId) { return wineDao.findByOrigin(originId); }
    public List<Wine> findLowStock() { return wineDao.findLowStock(20); }
    public void updateStatus(int id, String status) { wineDao.updateStatus(id, status); }

    private String validate(Wine wine) {
        if (isBlank(wine.getName())) {
            return "红酒名称不能为空。";
        }
        if (isBlank(wine.getBrand())) {
            return "品牌不能为空。";
        }
        if (isBlank(wine.getType())) {
            return "类型不能为空。";
        }
        if (wine.getYear() < 1900) {
            return "年份不能小于 1900。";
        }
        if (isBlank(wine.getGrapeVarieties())) {
            return "葡萄品种不能为空。";
        }
        if (!"上架".equals(wine.getStatus()) && !"下架".equals(wine.getStatus())) {
            return "红酒状态只能是：上架、下架。";
        }
        if (wine.getPurchasePrice().compareTo(BigDecimal.ZERO) < 0 || wine.getSalePrice().compareTo(BigDecimal.ZERO) < 0) {
            return "价格不能小于 0。";
        }
        if (wine.getStock() < 0) {
            return "库存不能小于 0。";
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
