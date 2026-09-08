package com.example.billdemo.util;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.example.billdemo.entity.Bill;



public class FileUtil {



    private static final String FILE_PATH =
            "data/bill.txt";



    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");





    // 保存账单

    public static void saveBill(Bill bill)
            throws IOException {



        File file=new File(FILE_PATH);



        if(!file.getParentFile().exists()){

            file.getParentFile().mkdirs();

        }




        try(BufferedWriter bw=
                    new BufferedWriter(
                            new FileWriter(file,true)
                    )){


            bw.write(String.format(

                    "%d,%s,%s,%s,%.2f,%s",

                    bill.getId(),

                    sdf.format(
                            bill.getCreateTime()
                    ),

                    bill.getPerson(),

                    bill.getType(),

                    bill.getMoney(),

                    bill.getRemark()

            ));



            bw.newLine();


        }


    }







    // 查询全部账单

    public static List<Bill> loadBills()
            throws Exception{


        List<Bill> list=new ArrayList<>();



        File file=new File(FILE_PATH);



        if(!file.exists()){

            return list;

        }




        try(BufferedReader br=
                    new BufferedReader(
                            new FileReader(file)
                    )){


            String line;



            while((line=br.readLine())!=null){



                String[] arr=line.split(",");



                Integer id=
                        Integer.parseInt(arr[0]);



                Date time=
                        sdf.parse(arr[1]);



                String person=arr[2];



                String type=arr[3];



                double money=
                        Double.parseDouble(arr[4]);



                String remark=arr[5];




                Bill bill=
                        new Bill(
                                money,
                                type,
                                remark,
                                time,
                                person
                        );



                bill.setId(id);



                list.add(bill);


            }


        }



        return list;


    }








    // 删除账单

    public static void deleteBill(int id)
            throws Exception{



        List<Bill> bills=
                loadBills();



        File file=
                new File(FILE_PATH);




        try(BufferedWriter bw=
                    new BufferedWriter(
                            new FileWriter(file,false)
                    )){


            for(Bill bill:bills){



                if(bill.getId()!=id){



                    bw.write(String.format(

                            "%d,%s,%s,%s,%.2f,%s",

                            bill.getId(),

                            sdf.format(
                                    bill.getCreateTime()),

                            bill.getPerson(),

                            bill.getType(),

                            bill.getMoney(),

                            bill.getRemark()


                    ));



                    bw.newLine();


                }


            }


        }


    }







    // 自动生成下一个ID

    public static int getNextId()
            throws Exception{


        List<Bill> bills=
                loadBills();



        int max=0;



        for(Bill b:bills){



            if(b.getId()>max){


                max=b.getId();


            }


        }



        return max+1;


    }


}