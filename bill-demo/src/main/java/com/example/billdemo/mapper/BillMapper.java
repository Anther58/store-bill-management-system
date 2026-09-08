package com.example.billdemo.mapper;


import com.example.billdemo.entity.Bill;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;


@Mapper
public interface BillMapper {


    // 查询当前用户账单
    @Select("""
            select 
            id,
            person,
            money,
            type,
            remark,
            create_time as createTime,
            user_id
            from bill
            where user_id=#{userId}
            """)
    List<Bill> findAll(Integer userId);

    @Select("""
    select
    id,
    person,
    money,
    type,
    remark,
    create_time as createTime,
    user_id
    from bill
    where user_id=#{userId}
    order by id desc
    limit #{start},#{size}
    """)




    // 分页查询
    List<Bill> findPage(
            @Param("userId") Integer userId,
            @Param("start") Integer start,
            @Param("size") Integer size
    );


    // 查询总条数
    @Select("""
select count(*)
from bill
where user_id=#{userId}
""")
    Integer count(@Param("userId") Integer userId);


    // 新增账单
    @Insert("""
            insert into bill(
            person,
            money,
            type,
            remark,
            create_time,
            user_id
            )
            values(
            #{person},
            #{money},
            #{type},
            #{remark},
            now(),
            #{userId}
            )
            """)
    void add(Bill bill);



    // 删除账单
    @Delete("""
            delete from bill 
            where id=#{id}
            and user_id=#{userId}
            """)
    void delete(
            @Param("id") Integer id,
            @Param("userId") Integer userId
    );



    // 修改账单
    @Update("""
            update bill
            set 
            person=#{person},
            money=#{money},
            type=#{type},
            remark=#{remark}
            where id=#{id}
            and user_id=#{userId}
            """)
    void update(Bill bill);



    // 根据姓名搜索当前用户账单
    @Select("""
            select 
            id,
            person,
            money,
            type,
            remark,
            create_time as createTime,
            user_id
            from bill
            where person like concat('%',#{person},'%')
            and user_id=#{userId}
            """)
    List<Bill> search(
            @Param("person") String person,
            @Param("userId") Integer userId
    );



    // 当前用户总收入
    @Select("""
            select ifnull(sum(money),0)
            from bill
            where type='收入'
            and user_id=#{userId}
            """)
    Double totalIncome(Integer userId);



    // 当前用户总支出
    @Select("""
            select ifnull(sum(money),0)
            from bill
            where type='支出'
            and user_id=#{userId}
            """)
    Double totalExpense(Integer userId);


    // ===============================
// 每月收入统计
// ===============================
    @Select("""
select
date_format(create_time,'%Y-%m') month,
ifnull(sum(money),0) money
from bill
where type='收入'
and user_id=#{userId}
group by date_format(create_time,'%Y-%m')
order by month
""")
    List<Map<String,Object>> incomeByMonth(Integer userId);

    // ===============================
// 收支占比
// ===============================
    @Select("""
select
type as name,
sum(money) as value
from bill
where user_id=#{userId}
group by type
""")
    List<Map<String,Object>> typeChart(Integer userId);


}
