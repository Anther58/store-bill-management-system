const baseURL =
"http://127.0.0.1:8080";



//登录

function login(){


let username =
document.getElementById("username").value;


let password =
document.getElementById("password").value;



fetch(
baseURL+"/user/login",
{


method:"POST",


headers:{


"Content-Type":"application/json"


},


credentials:"include",


body:JSON.stringify({

username:username,

password:password

})


}

)


.then(res=>res.json())


.then(data=>{


if(data.username){


alert("登录成功");

setTimeout(function () {
    window.location.href = "index.html";
},300);




}else{


alert(data);

}


});


}






//注册

function register(){


let username =
document.getElementById("username").value;


let password =
document.getElementById("password").value;



fetch(
baseURL+"/user/register",
{


method:"POST",


headers:{


"Content-Type":"application/json"


},


body:JSON.stringify({

username:username,

password:password

})


}

)


.then(res=>res.text())


.then(data=>{


alert(data);

if(data=="注册成功")

        window.location.href="login.html";


});


}




function goRegister(){


location.href="register.html";


}