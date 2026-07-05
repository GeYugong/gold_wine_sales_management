package com.goldwine.entity;

import java.math.BigDecimal;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：保存销售订单明细信息。
 */
public class SaleOrderItem {
    private int id;
    private int orderId;
    private int wineId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getWineId() { return wineId; }
    public void setWineId(int wineId) { this.wineId = wineId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    @Override
    public String toString() {
        return String.format("%d | 订单:%d | 红酒:%d | 数量:%d | 单价:%s | 小计:%s",
                id, orderId, wineId, quantity, unitPrice, subtotal);
    }
}
