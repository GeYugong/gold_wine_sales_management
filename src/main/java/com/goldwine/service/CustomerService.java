package com.goldwine.service;

import com.goldwine.dao.CustomerDao;
import com.goldwine.entity.Customer;

import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：处理客户管理业务规则。
 */
public class CustomerService {
    private final CustomerDao customerDao = new CustomerDao();
    private static final String[] VALID_LEVELS = {"普通客户", "品鉴会员", "窖藏会员", "私享会员"};

    public String add(Customer customer) {
        String error = validate(customer);
        if (error != null) {
            return error;
        }
        customerDao.add(customer);
        return null;
    }

    public String update(Customer customer) {
        String error = validate(customer);
        if (error != null) {
            return error;
        }
        customerDao.update(customer);
        return null;
    }

    public Customer findById(int id) { return customerDao.findById(id); }
    public List<Customer> findAll() { return customerDao.findAll(); }
    public List<Customer> search(String keyword) { return customerDao.search(keyword); }

    public boolean delete(int id) {
        if (customerDao.hasOrder(id)) {
            return false;
        }
        customerDao.delete(id);
        return true;
    }

    private String validate(Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return "客户姓名不能为空。";
        }
        if (!isValidLevel(customer.getLevel())) {
            return "会员等级只能是：普通客户、品鉴会员、窖藏会员、私享会员。";
        }
        return null;
    }

    private boolean isValidLevel(String level) {
        for (String validLevel : VALID_LEVELS) {
            if (validLevel.equals(level)) {
                return true;
            }
        }
        return false;
    }
}
