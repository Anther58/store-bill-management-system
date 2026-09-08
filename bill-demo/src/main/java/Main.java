package com.example.billdemo.console;

import java.util.*;
import com.example.billdemo.entity.Bill;
import com.example.billdemo.util.FileUtil;
public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        while(true){
            System.out.println("=====多人共用记账本=====");
            System.out.println("1.新增收支记录");
            System.out.println("2.查看全部账单");
            System.out.println("3.统计全家总收支与结余");
            System.out.println("4.按单人统计收支"); // 新增功能
            System.out.println("0.退出程序");
            System.out.print("请输入操作编号：");
            int op = sc.nextInt();
            sc.nextLine();
            switch (op){
                case 1: addBill(); break;
                case 2: showBills(); break;
                case 3: calcTotal(); break;
                case 4: calcByPerson(); break; // 单人统计方法
                case 0: System.out.println("程序退出");return;
                default: System.out.println("输入的编号无效");
            }
        }
    }

    // 新增账单时，先录入人员名称
    private static void addBill() throws Exception{
        System.out.print("输入记账人姓名：");
        String person = sc.nextLine();
        System.out.print("输入收支类型（收入/支出）：");
        String type = sc.nextLine();
        System.out.print("输入金额：");
        double money = sc.nextDouble();
        sc.nextLine();
        System.out.print("输入备注：");
        String remark = sc.nextLine();
        Bill bill = new Bill(money,type,remark,new Date(),person);
        FileUtil.saveBill(bill);
        System.out.println("记录添加成功");
    }

    private static void showBills() throws Exception{
        List<Bill> bills = FileUtil.loadBills();
        if(bills.isEmpty()){
            System.out.println("暂无账单记录");
            return;
        }
        bills.forEach(System.out::println);
    }

    // 全局总收支
    private static void calcTotal() throws Exception{
        List<Bill> bills = FileUtil.loadBills();
        double income=0,pay=0;
        for(Bill b : bills){
            if("收入".equals(b.getType())) income += b.getMoney();
            else pay += b.getMoney();
        }
        System.out.printf("全家总收入：%.2f 总支出：%.2f 总结余：%.2f%n",income,pay,income-pay);
    }

    // 核心新增：按人员分组计算
    private static void calcByPerson() throws Exception {
        List<Bill> bills = FileUtil.loadBills();
        System.out.print("请输入要查询的人员姓名：");
        String targetPerson = sc.nextLine();

        Map<String, Double> personIncome = new HashMap<>();
        Map<String, Double> personPay = new HashMap<>();

        // 初始化
        personIncome.put(targetPerson, 0.0);
        personPay.put(targetPerson, 0.0);

        for (Bill bill : bills) {
            if (bill.getPerson().equals(targetPerson)) {
                if ("收入".equals(bill.getType())) {
                    personIncome.put(targetPerson, personIncome.get(targetPerson) + bill.getMoney());
                } else {
                    personPay.put(targetPerson, personPay.get(targetPerson) + bill.getMoney());
                }
            }
        }

        double in = personIncome.get(targetPerson);
        double out = personPay.get(targetPerson);
        System.out.println("========================");
        System.out.printf("【%s】个人账单统计：%n", targetPerson);
        System.out.printf("个人总收入：%.2f%n", in);
        System.out.printf("个人总支出：%.2f%n", out);
        System.out.printf("个人当前结余：%.2f%n", in - out);
    }
}
