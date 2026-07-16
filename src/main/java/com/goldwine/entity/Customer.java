package com.goldwine.entity;

/**
 * 编写者：赵靖羽
 * 完成时间：2026-07-05
 * 类的具体功能：保存客户和会员信息。
 */
public class Customer {
    private int id;
    private String name;
    private String gender;
    private String phone;
    private String address;
    private String level;
    private String registerTime;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getRegisterTime() { return registerTime; }
    public void setRegisterTime(String registerTime) { this.registerTime = registerTime; }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %s | %s | %s | %s",
                id, name, gender, phone, address, level, registerTime);
    }
}
