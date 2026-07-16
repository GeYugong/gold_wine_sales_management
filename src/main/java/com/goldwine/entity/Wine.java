package com.goldwine.entity;

import java.math.BigDecimal;

/**
 * 编写者：胡嘉祺
 * 完成时间：2026-07-05
 * 类的具体功能：保存红酒商品信息。
 */
public class Wine {
    private int id;
    private String name;
    private String brand;
    private String type;
    private int year;
    private int originId;
    private String grapeVarieties;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private int stock;
    private String status;
    private String sourceUrl;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getOriginId() { return originId; }
    public void setOriginId(int originId) { this.originId = originId; }
    public String getGrapeVarieties() { return grapeVarieties; }
    public void setGrapeVarieties(String grapeVarieties) { this.grapeVarieties = grapeVarieties; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %s | %d | 产地:%d | 售价:%s | 库存:%d | %s",
                id, name, brand, type, year, originId, salePrice, stock, status);
    }
}
