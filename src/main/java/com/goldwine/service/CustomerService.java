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

    public void add(Customer customer) { customerDao.add(customer); }
    public void update(Customer customer) { customerDao.update(customer); }
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
}
