package com.goldwine.service;

import com.goldwine.dao.StatisticsDao;

import java.math.BigDecimal;
import java.util.List;

/**
 * 编写者：蓝海洋
 * 完成时间：2026-07-05
 * 类的具体功能：处理销售统计业务。
 */
public class StatisticsService {
    private final StatisticsDao statisticsDao = new StatisticsDao();

    public BigDecimal totalAmount(String start, String end) { return statisticsDao.totalAmount(start, end); }
    public List<String> wineRank() { return statisticsDao.wineRank(); }
    public List<String> wineSales(int wineId) { return statisticsDao.wineSales(wineId); }
    public BigDecimal customerTotal(int customerId) { return statisticsDao.customerTotal(customerId); }
}
