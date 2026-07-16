package com.goldwine.entity;

/**
 * 编写者：王福洪
 * 完成时间：2026-07-05
 * 类的具体功能：保存红酒产地信息。
 */
public class Origin {
    private int id;
    private String country;
    private boolean domestic;
    private String province;
    private String city;
    private String county;
    private String foreignCity;
    private String description;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public boolean isDomestic() { return domestic; }
    public void setDomestic(boolean domestic) { this.domestic = domestic; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }
    public String getForeignCity() { return foreignCity; }
    public void setForeignCity(String foreignCity) { this.foreignCity = foreignCity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        String place = domestic ? province + city + county : country + " " + foreignCity;
        return String.format("%d | %s | %s | %s", id, country, place, description);
    }
}
