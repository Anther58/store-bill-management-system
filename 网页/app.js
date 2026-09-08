const baseURL = "http://127.0.0.1:8080";

let currentBillId = null;

// ===============================
// 分页参数
// ===============================
let currentPage = 1;      // 当前页
const pageSize = 10;      // 每页10条
let totalPage = 1;

// ===============================
// 登录检查
// ===============================
function checkLogin() {

    fetch(baseURL + "/user/check", {
        credentials: "include"
    })
    .then(res => res.json())
    .then(data => {

        if (data === false) {

            alert("请先登录");

            window.location.href = "login.html";
        }

    });

}

// ===============================
// 获取当前登录用户
// ===============================
function loadUser(){

    fetch(baseURL + "/user/info",{

        credentials:"include"

    })

    .then(res=>res.json())

    .then(user=>{

        document.getElementById("username").innerHTML =
            user.username;

    });

}

checkLogin();


// ===============================
// 查询账单
// ===============================
function loadBills() {

    fetch(
    baseURL + "/bill/page?page=" + currentPage,
    {
        credentials: "include"
    }
)

    .then(res => res.json())

    .then(data => {

        totalPage = data.totalPage;
        

        let bills = data.list;

        let html = "";

        let income = 0;

        let expense = 0;

        bills.forEach(bill => {

            if (bill.type == "收入") {

                income += Number(bill.money);

            } else {

                expense += Number(bill.money);

            }

        });

        document.getElementById("income").innerHTML =
            income.toFixed(2);

        document.getElementById("expense").innerHTML =
            expense.toFixed(2);

        document.getElementById("balance").innerHTML =
            (income - expense).toFixed(2);

        data.list.forEach(bill => {

            html += `

<tr>

<td>${bill.person}</td>

<td class="${bill.type=="收入"?"income":"expense"}">

${bill.type=="收入"?"+":"-"}

${Number(bill.money).toFixed(2)}

</td>

<td>${bill.type}</td>

<td>${bill.remark}</td>

<td>${formatTime(bill.createTime)}</td>

<td>

<button onclick="editBill(${bill.id})">
编辑
</button>

<button onclick="deleteBill(${bill.id})">
删除
</button>

</td>

</tr>

`;

        });

        document.getElementById("list").innerHTML = html;

        document.getElementById("pageInfo").innerHTML ="第 " + data.page + " / " + data.totalPage + " 页";

        // ===============================
// 控制分页按钮状态
// ===============================
document.getElementById("prevBtn").disabled =
    (data.page <= 1);

document.getElementById("nextBtn").disabled =
    (data.page >= data.totalPage);

    });

}


// ===============================
// 新增账单
// ===============================
function addBill() {

    let person =
        document.getElementById("person").value;

    let moneyValue =
        document.getElementById("money").value;

    if (person.trim() == "") {

        alert("请输入人员姓名");

        return;

    }

    if (moneyValue.trim() == "") {

        alert("请输入金额");

        return;

    }

    if (isNaN(Number(moneyValue))) {

        alert("金额必须是数字");

        return;

    }

    let bill = {

        person: person,

        money: Number(moneyValue),

        type: document.getElementById("type").value,

        remark: document.getElementById("remark").value

    };

    fetch(baseURL + "/bill/add", {

        credentials: "include",

        method: "POST",

        headers: {

            "Content-Type": "application/json"

        },

        body: JSON.stringify(bill)

    })

    .then(res => res.text())

    .then(data => {

        alert(data);

        document.getElementById("person").value = "";
        document.getElementById("money").value = "";
        document.getElementById("remark").value = "";

        loadBills();

    });

}
// ===============================
// 删除账单
// ===============================
function deleteBill(id) {

    if (!confirm("确定删除这条账单吗？")) {

        return;

    }

    fetch(baseURL + "/bill/delete/" + id, {

        credentials: "include",

        method: "DELETE"

    })

    .then(res => res.text())

    .then(data => {

        alert(data);

        // 如果当前页已经没有数据，则返回上一页
    let rows =
        document.querySelectorAll("#list tr").length;

    if(rows == 1 && currentPage > 1){

        currentPage--;

    }

        loadBills();

    });

}


// ===============================
// 搜索账单
// ===============================
function searchBill() {

    let person =
        document.getElementById("search").value;

    // 输入为空，显示全部
    if (person.trim() == "") {

        loadBills();

        return;

    }

    fetch(baseURL + "/bill/search?person=" + encodeURIComponent(person), {

        credentials: "include"

    })

    .then(res => res.json())

    .then(data => {

        let html = "";

        data.forEach(bill => {

            html += `

<tr>

<td>${bill.person}</td>

<td class="${bill.type=="收入"?"income":"expense"}">

${bill.type=="收入"?"+":"-"}

${Number(bill.money).toFixed(2)}

</td>

<td>${bill.type}</td>

<td>${bill.remark}</td>

<td>${formatTime(bill.createTime)}</td>

<td>

<button onclick="editBill(${bill.id})">
编辑
</button>

<button onclick="deleteBill(${bill.id})">
删除
</button>

</td>

</tr>

`;

        });

        document.getElementById("list").innerHTML = html;

    });

}


// ===============================
// 显示全部账单
// ===============================
function showAll() {

    document.getElementById("search").value = "";

    loadBills();

}


// ===============================
// 打开编辑窗口
// ===============================
function editBill(id) {

    console.log(id);

    currentBillId = id;

   fetch(baseURL + "/bill/list", {
    credentials:"include"
})
.then(res=>{
    console.log(res.status);
    return res.json();
})


    .then(data => {

        let bill =data.find(item => item.id == id);

        if (!bill) {

            alert("没有找到这条账单");

            return;

        }

        document.getElementById("editPerson").value =
            bill.person;

        document.getElementById("editMoney").value =
            bill.money;

        document.getElementById("editType").value =
            bill.type;

        document.getElementById("editRemark").value =
            bill.remark;

        document.getElementById("editModal").style.display =
            "flex";

    });

}
// ===============================
// 保存编辑
// ===============================
function saveEdit() {

    let person =
        document.getElementById("editPerson").value;

    let money =
        document.getElementById("editMoney").value;

    let type =
        document.getElementById("editType").value;

    let remark =
        document.getElementById("editRemark").value;

    if (person.trim() == "") {

        alert("人员不能为空");

        return;

    }

    if (money.trim() == "") {

        alert("金额不能为空");

        return;

    }

    if (isNaN(Number(money))) {

        alert("金额必须是数字");

        return;

    }

    let bill = {

        id: currentBillId,

        person: person,

        money: Number(money),

        type: type,

        remark: remark

    };

    fetch(baseURL + "/bill/update", {

        credentials: "include",

        method: "PUT",

        headers: {

            "Content-Type": "application/json"

        },

        body: JSON.stringify(bill)

    })

    .then(res => res.text())

    .then(data => {

        alert(data);

        closeEdit();

        loadBills();

    });

}


// ===============================
// 关闭编辑窗口
// ===============================
function closeEdit() {

    document.getElementById("editModal").style.display = "none";

}

// ===============================
// 上一页
// ===============================
function prevPage(){
    if(currentPage <= 1){

        alert("已经是第一页");

        return;

    }

    currentPage--;

    loadBills();

}




// ===============================
// 下一页
// ===============================
function nextPage(){


    if(currentPage >= totalPage){

        alert("已经是最后一页");

        return;

    }

    currentPage++;

    loadBills();

}


// ===============================
// 时间格式化
// ===============================
function formatTime(time) {

    let date = new Date(time);

    return date.getFullYear() + "-" +

        String(date.getMonth() + 1).padStart(2, "0") + "-" +

        String(date.getDate()).padStart(2, "0") + " " +

        String(date.getHours()).padStart(2, "0") + ":" +

        String(date.getMinutes()).padStart(2, "0");

}
// ===============================
// 页面加载
// ===============================
window.onload = function () {

    checkLogin();

    loadUser();

    loadBills();

    loadIncomeChart();

    loadTypeChart();

};

// ===============================
// 每月收入统计图
// ===============================
function loadIncomeChart() {

    fetch(baseURL + "/bill/chart/income", {
        credentials: "include"
    })

    .then(res => res.json())

    .then(data => {

        let months = [];
        let moneys = [];

        data.forEach(item => {
            months.push(item.month);
            moneys.push(item.money);
        });

        let chart = echarts.init(
            document.getElementById("incomeChart")
        );

        let option = {

            title: {
                text: "每月收入统计",
                left: "center"
            },

            tooltip: {
                trigger: "axis"
            },

            grid: {
                left: "6%",
                right: "6%",
                top: "18%",
                bottom: "12%",
                containLabel: true
            },

            xAxis: {
                type: "category",
                data: months
            },

            yAxis: {
                type: "value"
            },

            series: [
                {
                    name: "收入",
                    type: "bar",
                    data: moneys,
                    barWidth: "45%",

                    itemStyle: {
                        borderRadius: [8, 8, 0, 0],
                        color: "#5B8FF9"
                    }
                }
            ]

        };

        chart.setOption(option);
        window.addEventListener("resize", function () {

    chart.resize();

});

    });

}

// ===============================
// 收支占比饼图
// ===============================
function loadTypeChart() {

    fetch(baseURL + "/bill/chart/type", {
        credentials: "include"
    })

    .then(res => res.json())

    .then(data => {

        let chart = echarts.init(
            document.getElementById("typeChart")
        );

        let option = {

            title: {
                text: "收支占比",
                left: "center"
            },

            tooltip: {
                trigger: "item"
            },

            legend: {
                bottom: 10,
                left: "center"
            },

            series: [

                {

                    name: "金额",

                    type: "pie",

                    radius: ["45%", "70%"],

                    center: ["50%", "50%"],

                    avoidLabelOverlap: true,

                    data: data,

                    label: {
                        formatter: "{b}\n{d}%"
                    }

                }

            ]

        };

        chart.setOption(option);
        window.addEventListener("resize", function () {

    chart.resize();

});

    });

}

// ===============================
// 退出登录
// ===============================
function logout() {

    if (!confirm("确定退出登录吗？")) {

        return;

    }

    fetch(baseURL + "/user/logout", {

        credentials: "include",

        method: "GET"

    })

    .then(res => res.text())

    .then(data => {

        alert(data);

        window.location.href = "login.html";



    });

 

}
// ===============================
// 导出Excel
// ===============================
function exportExcel() {

    window.location.href =
        baseURL + "/bill/export";

}
