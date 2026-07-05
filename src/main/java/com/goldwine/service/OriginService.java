package com.goldwine.service;

import com.goldwine.dao.OriginDao;
import com.goldwine.entity.Origin;

import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：处理产地管理业务规则。
 */
public class OriginService {
    private final OriginDao originDao = new OriginDao();

    public void add(Origin origin) { originDao.add(origin); }
    public void update(Origin origin) { originDao.update(origin); }
    public Origin findById(int id) { return originDao.findById(id); }
    public List<Origin> findAll() { return originDao.findAll(); }

    public boolean delete(int id) {
        if (originDao.hasWine(id)) {
            return false;
        }
        originDao.delete(id);
        return true;
    }
}
