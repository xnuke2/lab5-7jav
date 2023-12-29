<%@ page import="model.DBHandler" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login/Registration</title>
    <link rel="stylesheet" href="Styles/HeaderStyles.css">
    <link rel="stylesheet" href="Styles/LoginRegistrationStyles.css">
</head>
<body>
<!-- Шапка сайта -->
<div class="head" >
    <button onclick="location.href='/lab5-7jav'" class="buttonOnHead">Главная</button>
    <script type="text/javascript">
        var currentDate = new Date(<%=request.getAttribute("time")%>);
        function run() {
            currentDate.setSeconds(currentDate.getSeconds()+1);
            var time = "";
            var hour = currentDate.getHours();
            var minute = currentDate.getMinutes();
            var second = currentDate.getSeconds();
            if(hour < 10){
                time += "0" + hour;
            }else{
                time += hour;
            }
            time += ":";
            if(minute < 10){
                time += "0" + minute;
            }else{
                time += minute;
            }
            time += ":";
            if(second < 10){
                time += "0" + second;
            }else{
                time += second;
            }
            document.getElementById("Time").innerHTML = time;
            setTimeout(run,1000);
        }
    </script>
    <div class="timeOnHead">
                <span>
                    Текущее время:
                </span>
        <span id ="Time">
                    22:05:11
                </span>
    </div>
    <script type="text/javascript">
        run();
    </script>
    <div class="timeOnHead">
                <span>
                    Счётчик вошедших:
                </span>
        <%
            DBHandler handler=new DBHandler();
            int num = handler.getNumOfEnter();
            out.println("<span>"+ num +"</span>");
        %>
    </div>
    <%
        if(request.getSession().getAttribute("userName") != null){
            out.println("<button onclick=\"location.href='/lab5-7jav/personalAccount'\" class=\"buttonOnHead\">\n" +
                    request.getSession().getAttribute("userName") + "</button>");
        }else {
            out.println("<button onclick=\"location.href='/lab5-7jav/LoginRegistration'\" class=\"buttonOnHead\">" +
                    "Авторизация/Регистрация</button>");

        }
    %>
</div>
<div class="registBlock" >
    <form method="post">
        <h4>Регистрация</h4>
        <%
            if(request.getAttribute("ErrorRegist")!=null){
                out.println("<p>"+request.getAttribute("ErrorRegist")+"</p>");
            }
        %>


        <input name="name" placeholder="Логин"><br>
        <input name="firstName" placeholder="Фамилия"><br>
        <input name="secondName" placeholder="Имя"><br>
        <input type="password" name="password" placeholder="Пароль"><br>
        <button class="registButton" type="submit" name="submit" value="regist">Зарегистрироваться</button><br>
    </form>
</div>
<div class="loginBlock" >
    <%
        if(request.getAttribute("ErrorLogin")!=null){
            out.println("<p>"+request.getAttribute("ErrorLogin")+"</p>");
        }
    %>
    <form method="post">
        <h4>Авторизация</h4>
        <input name="name" placeholder="Логин"><br>
        <input type="password" name="password" placeholder="Пароль"><br>
        <button class="loginButton" type="submit" name="submit" value="login">Авторизироваться</button><br>
    </form>
</div>
</body>
</html>
