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

    public String add(Origin origin) {
        String error = validate(origin);
        if (error != null) {
            return error;
        }
        originDao.add(origin);
        return null;
    }

    public String update(Origin origin) {
        String error = validate(origin);
        if (error != null) {
            return error;
        }
        originDao.update(origin);
        return null;
    }
    public Origin findById(int id) { return originDao.findById(id); }
    public List<Origin> findAll() { return originDao.findAll(); }

    public boolean delete(int id) {
        if (originDao.hasWine(id)) {
            return false;
        }
        originDao.delete(id);
        return true;
    }

    private String validate(Origin origin) {
        if (isBlank(origin.getCountry())) {
            return "国家不能为空。";
        }
        if (origin.isDomestic()) {
            if (isBlank(origin.getProvince()) || isBlank(origin.getCity()) || isBlank(origin.getCounty())) {
                return "国内产地必须填写省份、城市和区县。";
            }
        } else if (isBlank(origin.getForeignCity())) {
            return "国外产地必须填写国外城市。";
        }
        if (isBlank(origin.getDescription())) {
            return "产地说明不能为空。";
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
