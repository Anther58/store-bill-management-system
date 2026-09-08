package com.example.billdemo.entity;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;


public class Bill {
    public Bill(){

    }
    private Integer userId;
    private Integer id;   // 新增：账单ID，用于删除
    private double money;
    private String type; //收入/支出
    private String remark;

    public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // 新增：归属人
    private String person;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // 构造方法重载，兼容旧代码
    public Bill(double money, String type, String remark, Date createTime) {
        this(money, type, remark, createTime, "默认账户");
    }

    // 新构造，带人员
    public Bill(double money, String type, String remark, Date createTime, String person) {
        this.money = money;
        this.type = type;
        this.remark = remark;
        this.createTime = createTime;
        this.person = person;
    }

    // getter setter
    public double getMoney() {
        return money;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getPerson() {
        return person;
    }

    public void setMoney(double money){
        this.money = money;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }

    public void setPerson(String person){
        this.person = person;
    }

    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }



    @Override
    public String toString() {
        return String.format("人员：%s，时间：%s，类型：%s，金额：%.2f，备注：%s",
                person, createTime, type, money, remark);

    }
}
