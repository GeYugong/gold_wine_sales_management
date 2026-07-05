package com.goldwine.service;

import com.goldwine.dao.CustomerDao;
import com.goldwine.dao.SaleOrderDao;
import com.goldwine.dao.SaleOrderItemDao;
import com.goldwine.dao.WineDao;
import com.goldwine.entity.Customer;
import com.goldwine.entity.SaleOrder;
import com.goldwine.entity.SaleOrderItem;
import com.goldwine.entity.Wine;
import com.goldwine.util.DBUtil;
import com.goldwine.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：处理购买结算、订单创建、取消订单和库存恢复业务。
 */
public class OrderService {
    private final CustomerDao customerDao = new CustomerDao();
    private final WineDao wineDao = new WineDao();
    private final SaleOrderDao orderDao = new SaleOrderDao();
    private final SaleOrderItemDao itemDao = new SaleOrderItemDao();
    private static final String[] VALID_PAY_METHODS = {"现金", "微信", "支付宝", "银行卡"};

    public String createOrder(int customerId, int wineId, int quantity, String payMethod) {
        if (quantity <= 0) {
            return "购买数量必须大于 0。";
        }
        if (!isValidPayMethod(payMethod)) {
            return "支付方式只能是：现金、微信、支付宝、银行卡。";
        }
        Customer customer = customerDao.findById(customerId);
        Wine wine = wineDao.findById(wineId);
        if (customer == null || wine == null) {
            return "客户或红酒不存在。";
        }
        if (!"上架".equals(wine.getStatus())) {
            return "该红酒未上架，不能购买。";
        }
        if (wine.getStock() < quantity) {
            return "库存不足，当前库存：" + wine.getStock();
        }

        BigDecimal total = wine.getSalePrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal rate = discountRate(customer.getLevel());
        BigDecimal finalAmount = total.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discount = total.subtract(finalAmount);

        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);
            try {
                SaleOrder order = new SaleOrder();
                order.setCustomerId(customerId);
                order.setOrderTime(DateUtil.now());
                order.setTotalAmount(total);
                order.setDiscountAmount(discount);
                order.setFinalAmount(finalAmount);
                order.setPayMethod(payMethod);
                order.setStatus("已完成");
                int orderId = orderDao.add(c, order);

                SaleOrderItem item = new SaleOrderItem();
                item.setOrderId(orderId);
                item.setWineId(wineId);
                item.setQuantity(quantity);
                item.setUnitPrice(wine.getSalePrice());
                item.setSubtotal(total);
                itemDao.add(c, item);
                wineDao.changeStock(c, wineId, -quantity);
                c.commit();
                return "订单创建成功，订单编号：" + orderId + "，实付金额：" + finalAmount;
            } catch (Exception e) {
                c.rollback();
                return "订单创建失败：" + e.getMessage();
            }
        } catch (SQLException e) {
            return "数据库错误：" + e.getMessage();
        }
    }

    public String cancelOrder(int orderId) {
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);
            try {
                SaleOrder order = orderDao.findById(c, orderId);
                if (order == null) {
                    return "订单不存在。";
                }
                if ("已取消".equals(order.getStatus())) {
                    return "已取消订单不能重复取消。";
                }
                for (SaleOrderItem item : itemDao.findByOrderId(c, orderId)) {
                    wineDao.changeStock(c, item.getWineId(), item.getQuantity());
                }
                orderDao.updateStatus(c, orderId, "已取消");
                c.commit();
                return "订单已取消，库存已恢复。";
            } catch (Exception e) {
                c.rollback();
                return "取消订单失败：" + e.getMessage();
            }
        } catch (SQLException e) {
            return "数据库错误：" + e.getMessage();
        }
    }

    public List<SaleOrder> findAll() { return orderDao.findAll(); }
    public SaleOrder findById(int id) { return orderDao.findById(id); }
    public List<SaleOrderItem> findItems(int orderId) { return itemDao.findByOrderId(orderId); }
    public List<SaleOrder> findByCustomer(int customerId) { return orderDao.findByCustomer(customerId); }
    public List<SaleOrder> findCompleted() { return orderDao.findByStatus("已完成"); }
    public List<SaleOrder> findCanceled() { return orderDao.findByStatus("已取消"); }

    private BigDecimal discountRate(String level) {
        if ("品鉴会员".equals(level)) {
            return new BigDecimal("0.95");
        }
        if ("窖藏会员".equals(level)) {
            return new BigDecimal("0.90");
        }
        if ("私享会员".equals(level)) {
            return new BigDecimal("0.85");
        }
        return BigDecimal.ONE;
    }

    private boolean isValidPayMethod(String payMethod) {
        for (String validPayMethod : VALID_PAY_METHODS) {
            if (validPayMethod.equals(payMethod)) {
                return true;
            }
        }
        return false;
    }
}
