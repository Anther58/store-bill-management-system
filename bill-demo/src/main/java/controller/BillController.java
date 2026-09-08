package com.example.billdemo.controller;


import com.example.billdemo.entity.Bill;
import com.example.billdemo.mapper.BillMapper;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@RestController
@RequestMapping("/bill")

public class BillController {


    private final BillMapper billMapper;


    public BillController(BillMapper billMapper){

        this.billMapper = billMapper;

    }



    /**
     * 获取当前登录用户ID
     */
    private Integer getUserId(HttpSession session){

        return (Integer) session.getAttribute("userId");

    }



    /**
     * 查询当前用户账单
     */
    @GetMapping("/list")
    public Object list(HttpSession session){




        Integer userId = getUserId(session);


        if(userId == null){

            return "请先登录";

        }


        return billMapper.findAll(userId);

    }


    /**
     * 分页查询账单
     */
    @GetMapping("/page")
    public Object page(
            @RequestParam Integer page,
            HttpSession session
    ){

        Integer userId = getUserId(session);

        if(userId == null){
            return "请先登录";
        }

        int size = 10;

        int start = (page - 1) * size;

        List<Bill> list =
                billMapper.findPage(userId, start, size);

        Integer total =
                billMapper.count(userId);

        int totalPage =
                (int)Math.ceil(total * 1.0 / size);

        java.util.Map<String,Object> result =
                new java.util.HashMap<>();

        result.put("list", list);

        result.put("page", page);

        result.put("total", total);

        result.put("totalPage", totalPage);

        return result;

    }

    @GetMapping("/count")
    public Integer count(HttpSession session){

        Integer userId = getUserId(session);

        if(userId == null){
            return 0;
        }

        return billMapper.count(userId);
    }




    /**
     * 新增账单
     */
    @PostMapping("/add")
    public String addBill(
            @RequestBody Bill bill,
            HttpSession session
    ){


        Integer userId = getUserId(session);


        if(userId == null){

            return "请先登录";

        }


        //绑定当前用户
        bill.setUserId(userId);


        billMapper.add(bill);


        return "success:账单新增完成";

    }





    /**
     * 删除账单
     */
    @DeleteMapping("/delete/{id}")
    public String deleteBill(
            @PathVariable Integer id,
            HttpSession session
    ){


        Integer userId = getUserId(session);


        if(userId == null){

            return "请先登录";

        }


        billMapper.delete(
                id,
                userId
        );


        return "删除成功";

    }





    /**
     * 修改账单
     */
    @PutMapping("/update")
    public String updateBill(
            @RequestBody Bill bill,
            HttpSession session
    ){


        Integer userId = getUserId(session);


        if(userId == null){

            return "请先登录";

        }


        bill.setUserId(userId);


        billMapper.update(bill);


        return "修改成功";

    }





    /**
     * 根据姓名搜索
     */
    @GetMapping("/search")
    public Object searchBill(
            @RequestParam String person,
            HttpSession session
    ){


        Integer userId = getUserId(session);


        if(userId == null){

            return "请先登录";

        }


        return billMapper.search(
                person,
                userId
        );

    }





    /**
     * 当前用户余额
     */
    @GetMapping("/balance")
    public Object balance(HttpSession session){


        Integer userId = getUserId(session);


        if(userId == null){

            return "请先登录";

        }



        Double income =
                billMapper.totalIncome(userId);



        Double expense =
                billMapper.totalExpense(userId);



        if(income == null){

            income = 0D;

        }


        if(expense == null){

            expense = 0D;

        }



        return income - expense;

    }





    /**
     * 当前用户总收入
     */
    @GetMapping("/income")
    public Object income(HttpSession session){


        Integer userId = getUserId(session);


        if(userId == null){

            return "请先登录";

        }



        Double result =
                billMapper.totalIncome(userId);



        return result == null ? 0D : result;

    }





    /**
     * 当前用户总支出
     */
    @GetMapping("/expense")
    public Object expense(HttpSession session){


        Integer userId = getUserId(session);


        if(userId == null){

            return "请先登录";

        }



        Double result =
                billMapper.totalExpense(userId);



        return result == null ? 0D : result;

    }

    // ===============================
// 每月收入统计
// ===============================
    @GetMapping("/chart/income")
    public Object incomeChart(HttpSession session){

        Integer userId = getUserId(session);

        if(userId == null){
            return "请先登录";
        }

        return billMapper.incomeByMonth(userId);

    }

    // ===============================
// 收支占比
// ===============================
    @GetMapping("/chart/type")
    public Object typeChart(HttpSession session){

        Integer userId = getUserId(session);

        if(userId==null){
            return "请先登录";
        }

        return billMapper.typeChart(userId);

    }

    @GetMapping("/export")
    public void export(HttpSession session,
                       HttpServletResponse response) throws IOException {

        Integer userId = getUserId(session);

        if (userId == null) {
            response.setStatus(401);
            return;
        }

        List<Bill> list = billMapper.findAll(userId);

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("账单");

        Row head = sheet.createRow(0);

        head.createCell(0).setCellValue("人员");
        head.createCell(1).setCellValue("金额");
        head.createCell(2).setCellValue("类型");
        head.createCell(3).setCellValue("备注");
        head.createCell(4).setCellValue("时间");

        int rowIndex = 1;

        for (Bill bill : list) {

            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(bill.getPerson());
            row.createCell(1).setCellValue(bill.getMoney());
            row.createCell(2).setCellValue(bill.getType());
            row.createCell(3).setCellValue(bill.getRemark());
            row.createCell(4).setCellValue(
                    bill.getCreateTime().toString()
            );
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=bill.xlsx");

        workbook.write(response.getOutputStream());

        workbook.close();
    }


}