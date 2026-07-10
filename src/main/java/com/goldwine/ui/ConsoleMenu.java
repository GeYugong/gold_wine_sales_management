package com.goldwine.ui;

import com.goldwine.entity.Customer;
import com.goldwine.entity.Origin;
import com.goldwine.entity.SaleOrder;
import com.goldwine.entity.SaleOrderItem;
import com.goldwine.entity.Wine;
import com.goldwine.service.CustomerService;
import com.goldwine.service.OrderService;
import com.goldwine.service.OriginService;
import com.goldwine.service.StatisticsService;
import com.goldwine.service.WineService;
import com.goldwine.util.DateUtil;
import com.goldwine.util.InputUtil;
import com.goldwine.util.TableUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 编写者：Gold 红酒销售管理系统开发组
 * 完成时间：2026-07-05
 * 类的具体功能：显示控制台菜单，接收用户输入并调用业务层完成系统功能。
 */
public class ConsoleMenu {
    private final InputUtil input = new InputUtil();
    private final WineService wineService = new WineService();
    private final OriginService originService = new OriginService();
    private final CustomerService customerService = new CustomerService();
    private final OrderService orderService = new OrderService();
    private final StatisticsService statisticsService = new StatisticsService();

    public void start() {
        while (true) {
            System.out.println("\n====== Gold 红酒销售管理系统 ======");
            System.out.println("1. 红酒信息管理");
            System.out.println("2. 产地信息管理");
            System.out.println("3. 客户信息管理");
            System.out.println("4. 购买结算管理");
            System.out.println("5. 订单查询管理");
            System.out.println("6. 销售统计管理");
            System.out.println("0. 退出系统");
            int choice = input.readInt("请选择：");
            switch (choice) {
                case 1: wineMenu(); break;
                case 2: originMenu(); break;
                case 3: customerMenu(); break;
                case 4: checkoutMenu(); break;
                case 5: orderMenu(); break;
                case 6: statisticsMenu(); break;
                case 0: System.out.println("已退出系统。"); return;
                default: System.out.println("选项不存在。");
            }
        }
    }

    private void wineMenu() {
        while (true) {
            System.out.println("\n1. 添加红酒  2. 修改红酒  3. 删除红酒  4. 查询全部红酒");
            System.out.println("5. 按名称查询  6. 按类型查询  7. 上架/下架  8. 查询库存不足  9. 按产地查询  0. 返回");
            int choice = input.readInt("请选择：");
            if (choice == 0) return;
            switch (choice) {
                case 1: saveWine(false); break;
                case 2: saveWine(true); break;
                case 3: deleteWine(); break;
                case 4: printWines(wineService.findAll()); break;
                case 5: printWines(wineService.findByName(input.readRequiredString("红酒名称关键词："))); break;
                case 6: printWines(wineService.findByType(input.readRequiredString("红酒类型："))); break;
                case 7: changeWineStatus(); break;
                case 8: printWines(wineService.findLowStock()); break;
                case 9: printWines(wineService.findByOrigin(input.readInt("产地编号："))); break;
                default: System.out.println("选项不存在。");
            }
        }
    }

    private void saveWine(boolean update) {
        Wine wine = update ? readExistingWine("红酒编号：") : new Wine();
        if (wine == null) {
            return;
        }
        wine.setName(input.readRequiredString("名称："));
        wine.setBrand(input.readRequiredString("品牌："));
        wine.setType(input.readRequiredString("类型："));
        wine.setYear(input.readYear("年份："));
        wine.setOriginId(readExistingOriginId("产地编号："));
        wine.setGrapeVarieties(input.readRequiredString("葡萄品种："));
        wine.setPurchasePrice(input.readMoney("进货价："));
        wine.setSalePrice(input.readMoney("销售价："));
        wine.setStock(input.readNonNegativeInt("库存："));
        wine.setStatus(input.readOption("状态（上架/下架）：", "上架", "下架"));
        wine.setSourceUrl(input.readString("来源链接（可空）："));
        String error = update ? wineService.update(wine) : wineService.add(wine);
        System.out.println(error == null ? "保存成功。" : error);
    }

    private void deleteWine() {
        int id = readExistingWineId("红酒编号：");
        System.out.println(wineService.delete(id) ? "删除成功。" : "该红酒存在订单明细，不能删除。");
    }

    private void changeWineStatus() {
        int id = readExistingWineId("红酒编号：");
        String status = input.readOption("新状态（上架/下架）：", "上架", "下架");
        wineService.updateStatus(id, status);
        System.out.println("状态已更新。");
    }

    private void originMenu() {
        while (true) {
            System.out.println("\n1. 添加产地  2. 修改产地  3. 删除产地  4. 查询全部产地  0. 返回");
            int choice = input.readInt("请选择：");
            if (choice == 0) return;
            switch (choice) {
                case 1: saveOrigin(false); break;
                case 2: saveOrigin(true); break;
                case 3:
                    System.out.println(originService.delete(readExistingOriginId("产地编号：")) ? "删除成功。" : "该产地下存在红酒，不能删除。");
                    break;
                case 4: printOrigins(originService.findAll()); break;
                default: System.out.println("选项不存在。");
            }
        }
    }

    private void saveOrigin(boolean update) {
        Origin origin = update ? originService.findById(readExistingOriginId("产地编号：")) : new Origin();
        if (origin == null) {
            System.out.println("产地不存在。");
            return;
        }
        origin.setCountry(input.readRequiredString("国家："));
        origin.setDomestic("1".equals(input.readOption("是否国内产地（1是/0否）：", "1", "0")));
        if (origin.isDomestic()) {
            origin.setProvince(input.readRequiredString("省份："));
            origin.setCity(input.readRequiredString("城市："));
            origin.setCounty(input.readRequiredString("区县："));
            origin.setForeignCity("");
        } else {
            origin.setProvince("");
            origin.setCity("");
            origin.setCounty("");
            origin.setForeignCity(input.readRequiredString("国外城市："));
        }
        origin.setDescription(input.readRequiredString("产地说明："));
        String error = update ? originService.update(origin) : originService.add(origin);
        System.out.println(error == null ? "保存成功。" : error);
    }

    private void customerMenu() {
        while (true) {
            System.out.println("\n1. 添加客户  2. 修改客户  3. 删除客户  4. 查询全部客户");
            System.out.println("5. 按姓名或电话查询  6. 查询客户购买记录  0. 返回");
            int choice = input.readInt("请选择：");
            if (choice == 0) return;
            switch (choice) {
                case 1: saveCustomer(false); break;
                case 2: saveCustomer(true); break;
                case 3:
                    System.out.println(customerService.delete(readExistingCustomerId("客户编号：")) ? "删除成功。" : "客户存在订单，不能删除。");
                    break;
                case 4: printCustomers(customerService.findAll()); break;
                case 5: printCustomers(customerService.search(input.readRequiredString("姓名或电话关键词："))); break;
                case 6: printOrders(orderService.findByCustomer(readExistingCustomerId("客户编号："))); break;
                default: System.out.println("选项不存在。");
            }
        }
    }

    private void saveCustomer(boolean update) {
        Customer customer = update ? customerService.findById(readExistingCustomerId("客户编号：")) : new Customer();
        if (customer == null) {
            System.out.println("客户不存在。");
            return;
        }
        customer.setName(input.readRequiredString("姓名："));
        customer.setGender(input.readOption("性别（男/女）：", "男", "女"));
        customer.setPhone(input.readPattern("电话：", "\\d{11}", "手机号必须是 11 位数字。"));
        customer.setAddress(input.readRequiredString("地址："));
        customer.setLevel(readCustomerLevel());
        customer.setRegisterTime(update ? customer.getRegisterTime() : DateUtil.now());
        String error = update ? customerService.update(customer) : customerService.add(customer);
        System.out.println(error == null ? "保存成功。" : error);
    }

    private String readCustomerLevel() {
        while (true) {
            String level = input.readRequiredString("会员等级（普通客户/品鉴会员/窖藏会员/私享会员）：");
            if ("普通客户".equals(level) || "品鉴会员".equals(level) || "窖藏会员".equals(level) || "私享会员".equals(level)) {
                return level;
            }
            System.out.println("会员等级只能是：普通客户、品鉴会员、窖藏会员、私享会员，请重新输入。");
        }
    }

    private int readExistingWineId(String prompt) {
        return readExistingWineId(prompt, false);
    }

    private int readExistingWineId(String prompt, boolean allowCancel) {
        while (true) {
            int id = allowCancel ? input.readNonNegativeInt(prompt) : input.readPositiveInt(prompt);
            if (allowCancel && id == 0) {
                return 0;
            }
            if (wineService.findById(id) != null) {
                return id;
            }
            System.out.println("红酒编号不存在，请重新输入。");
        }
    }

    private Wine readExistingWine(String prompt) {
        while (true) {
            int id = input.readPositiveInt(prompt);
            Wine wine = wineService.findById(id);
            if (wine != null) {
                return wine;
            }
            System.out.println("红酒不存在，请重新输入。");
        }
    }

    private int readExistingOriginId(String prompt) {
        while (true) {
            int id = input.readPositiveInt(prompt);
            if (originService.findById(id) != null) {
                return id;
            }
            System.out.println("产地编号不存在，请重新输入。");
        }
    }

    private int readExistingCustomerId(String prompt) {
        return readExistingCustomerId(prompt, false);
    }

    private int readExistingCustomerId(String prompt, boolean allowCancel) {
        while (true) {
            int id = allowCancel ? input.readNonNegativeInt(prompt) : input.readPositiveInt(prompt);
            if (allowCancel && id == 0) {
                return 0;
            }
            if (customerService.findById(id) != null) {
                return id;
            }
            System.out.println("客户编号不存在，请重新输入。");
        }
    }

    private int readExistingOrderId(String prompt) {
        return readExistingOrderId(prompt, false);
    }

    private int readExistingOrderId(String prompt, boolean allowCancel) {
        while (true) {
            int id = allowCancel ? input.readNonNegativeInt(prompt) : input.readPositiveInt(prompt);
            if (allowCancel && id == 0) {
                return 0;
            }
            if (orderService.findById(id) != null) {
                return id;
            }
            System.out.println("订单编号不存在，请重新输入。");
        }
    }

    private void checkoutMenu() {
        while (true) {
            System.out.println("\n1. 创建购买订单  2. 取消订单  0. 返回");
            int choice = input.readInt("请选择：");
            if (choice == 0) return;
            if (choice == 1) {
                printCustomers(customerService.findAll());
                int customerId = readExistingCustomerId("客户编号（输入 0 取消）：", true);
                if (customerId == 0) { System.out.println("已取消创建订单。"); continue; }
                printWines(wineService.findAll());
                int wineId = readExistingWineId("红酒编号（输入 0 取消）：", true);
                if (wineId == 0) { System.out.println("已取消创建订单。"); continue; }
                int quantity = input.readNonNegativeInt("购买数量（输入 0 取消）：");
                if (quantity == 0) { System.out.println("已取消创建订单。"); continue; }
                String payMethod = input.readOption("支付方式（现金/微信/支付宝/银行卡，输入 0 取消）：", "现金", "微信", "支付宝", "银行卡", "0");
                if ("0".equals(payMethod)) { System.out.println("已取消创建订单。"); continue; }
                System.out.println(orderService.createOrder(customerId, wineId, quantity, payMethod));
            } else if (choice == 2) {
                int cancelId = readExistingOrderId("订单编号（输入 0 取消）：", true);
                if (cancelId == 0) { System.out.println("已取消操作。"); continue; }
                System.out.println(orderService.cancelOrder(cancelId));
            } else {
                System.out.println("选项不存在。");
            }
        }
    }

    private void orderMenu() {
        while (true) {
            System.out.println("\n1. 查询全部订单  2. 根据订单编号查询详情  3. 根据客户查询订单");
            System.out.println("4. 查询已完成订单  5. 查询已取消订单  0. 返回");
            int choice = input.readInt("请选择：");
            if (choice == 0) return;
            switch (choice) {
                case 1: printOrders(orderService.findAll()); break;
                case 2: printOrderDetail(readExistingOrderId("订单编号：")); break;
                case 3: printOrders(orderService.findByCustomer(readExistingCustomerId("客户编号："))); break;
                case 4: printOrders(orderService.findCompleted()); break;
                case 5: printOrders(orderService.findCanceled()); break;
                default: System.out.println("选项不存在。");
            }
        }
    }

    private void printOrderDetail(int orderId) {
        SaleOrder order = orderService.findById(orderId);
        if (order == null) {
            System.out.println("订单不存在。");
            return;
        }
        printOrders(java.util.Collections.singletonList(order));
        printOrderItems(orderService.findItems(orderId));
    }

    private void statisticsMenu() {
        while (true) {
            System.out.println("\n1. 查询时间段销售总额  2. 查询红酒销量排行  3. 查询某红酒销售情况");
            System.out.println("4. 查询库存不足红酒  5. 查询某客户消费总额  0. 返回");
            int choice = input.readInt("请选择：");
            if (choice == 0) return;
            switch (choice) {
                case 1:
                    String[] range = readDateRange();
                    BigDecimal amount = statisticsService.totalAmount(range[0], range[1]);
                    printAmount("销售总额", amount);
                    break;
                case 2: printStatisticsRows(statisticsService.wineRank()); break;
                case 3: printStatisticsRows(statisticsService.wineSales(readExistingWineId("红酒编号："))); break;
                case 4: printWines(wineService.findLowStock()); break;
                case 5:
                    printAmount("客户消费总额", statisticsService.customerTotal(readExistingCustomerId("客户编号：")));
                    break;
                default: System.out.println("选项不存在。");
            }
        }
    }

    private void printList(List<?> list) {
        if (list.isEmpty()) {
            System.out.println("暂无数据。");
            return;
        }
        for (Object item : list) {
            System.out.println(item);
        }
    }

    private String[] readDateRange() {
        while (true) {
            String start = input.readDate("开始日期 yyyy-MM-dd：");
            String end = input.readDate("结束日期 yyyy-MM-dd：");
            if (!LocalDate.parse(start).isAfter(LocalDate.parse(end))) {
                return new String[]{start, end};
            }
            System.out.println("开始日期不能晚于结束日期，请重新输入。");
        }
    }

    private void printWines(List<Wine> wines) {
        if (wines.isEmpty()) {
            System.out.println("暂无数据。");
            return;
        }
        int[] widths = {4, 30, 16, 8, 6, 6, 8, 6, 6};
        System.out.println(TableUtil.line(widths));
        System.out.println(TableUtil.row(new Object[]{"编号", "名称", "品牌", "类型", "年份", "产地", "售价", "库存", "状态"}, widths));
        System.out.println(TableUtil.line(widths));
        for (Wine wine : wines) {
            System.out.println(TableUtil.row(new Object[]{
                    wine.getId(),
                    wine.getName(),
                    wine.getBrand(),
                    wine.getType(),
                    wine.getYear(),
                    wine.getOriginId(),
                    wine.getSalePrice(),
                    wine.getStock(),
                    wine.getStatus()
            }, widths));
        }
        System.out.println(TableUtil.line(widths));
    }

    private void printCustomers(List<Customer> customers) {
        if (customers.isEmpty()) {
            System.out.println("暂无数据。");
            return;
        }
        int[] widths = {4, 10, 4, 13, 18, 10, 19};
        System.out.println(TableUtil.line(widths));
        System.out.println(TableUtil.row(new Object[]{"编号", "姓名", "性别", "电话", "地址", "会员等级", "注册时间"}, widths));
        System.out.println(TableUtil.line(widths));
        for (Customer customer : customers) {
            System.out.println(TableUtil.row(new Object[]{
                    customer.getId(),
                    customer.getName(),
                    customer.getGender(),
                    customer.getPhone(),
                    customer.getAddress(),
                    customer.getLevel(),
                    customer.getRegisterTime()
            }, widths));
        }
        System.out.println(TableUtil.line(widths));
    }

    private void printOrigins(List<Origin> origins) {
        if (origins.isEmpty()) {
            System.out.println("暂无数据。");
            return;
        }
        int[] widths = {4, 10, 8, 12, 12, 12, 16};
        System.out.println(TableUtil.line(widths));
        System.out.println(TableUtil.row(new Object[]{"编号", "国家", "国内", "省份", "城市", "区县", "国外城市"}, widths));
        System.out.println(TableUtil.line(widths));
        for (Origin origin : origins) {
            System.out.println(TableUtil.row(new Object[]{
                    origin.getId(),
                    origin.getCountry(),
                    origin.isDomestic() ? "是" : "否",
                    origin.getProvince(),
                    origin.getCity(),
                    origin.getCounty(),
                    origin.getForeignCity()
            }, widths));
        }
        System.out.println(TableUtil.line(widths));
    }

    private void printOrders(List<SaleOrder> orders) {
        if (orders.isEmpty()) {
            System.out.println("暂无数据。");
            return;
        }
        int[] widths = {4, 6, 19, 10, 10, 10, 8, 8};
        System.out.println(TableUtil.line(widths));
        System.out.println(TableUtil.row(new Object[]{"编号", "客户", "下单时间", "原价", "优惠", "实付", "支付", "状态"}, widths));
        System.out.println(TableUtil.line(widths));
        for (SaleOrder order : orders) {
            System.out.println(TableUtil.row(new Object[]{
                    order.getId(),
                    order.getCustomerId(),
                    order.getOrderTime(),
                    order.getTotalAmount(),
                    order.getDiscountAmount(),
                    order.getFinalAmount(),
                    order.getPayMethod(),
                    order.getStatus()
            }, widths));
        }
        System.out.println(TableUtil.line(widths));
    }

    private void printOrderItems(List<SaleOrderItem> items) {
        if (items.isEmpty()) {
            System.out.println("暂无明细。");
            return;
        }
        int[] widths = {4, 6, 6, 6, 10, 10};
        System.out.println(TableUtil.line(widths));
        System.out.println(TableUtil.row(new Object[]{"编号", "订单", "红酒", "数量", "单价", "小计"}, widths));
        System.out.println(TableUtil.line(widths));
        for (SaleOrderItem item : items) {
            System.out.println(TableUtil.row(new Object[]{
                    item.getId(),
                    item.getOrderId(),
                    item.getWineId(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getSubtotal()
            }, widths));
        }
        System.out.println(TableUtil.line(widths));
    }

    private void printStatisticsRows(List<String> rows) {
        if (rows.isEmpty()) {
            System.out.println("暂无数据。");
            return;
        }
        int[] widths = {4, 30, 8, 12};
        System.out.println(TableUtil.line(widths));
        System.out.println(TableUtil.row(new Object[]{"编号", "红酒名称", "销量", "销售额"}, widths));
        System.out.println(TableUtil.line(widths));
        for (String row : rows) {
            String[] firstParts = row.split(" \\| ", 3);
            String[] tailParts = firstParts.length > 2 ? firstParts[2].split(" \\| ") : new String[0];
            String qty = tailParts.length > 0 ? tailParts[0].replace("销量:", "") : "";
            String amount = tailParts.length > 1 ? tailParts[1].replace("销售额:", "") : "";
            System.out.println(TableUtil.row(new Object[]{
                    firstParts.length > 0 ? firstParts[0] : "",
                    firstParts.length > 1 ? firstParts[1] : "",
                    qty,
                    amount
            }, widths));
        }
        System.out.println(TableUtil.line(widths));
    }

    private void printAmount(String label, BigDecimal amount) {
        int[] widths = {14, 12};
        System.out.println(TableUtil.line(widths));
        System.out.println(TableUtil.row(new Object[]{"统计项", "金额"}, widths));
        System.out.println(TableUtil.line(widths));
        System.out.println(TableUtil.row(new Object[]{label, amount}, widths));
        System.out.println(TableUtil.line(widths));
    }
}
