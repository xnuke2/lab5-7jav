
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.sun.jdi.StringReference" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="model.DBHandler" %>
<%@ page import="model.Const" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.io.OutputStream" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head lang="en">
  <meta charset="UTF-8">
  <title>main</title>
  <link rel="stylesheet" href="Styles/HeaderStyles.css">

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
<div class="block">
  <h3 style="text-align: center">Киноафиша</h3>
</div>



<%
    request.setCharacterEncoding("UTF-8");
    handler =new DBHandler();
    ResultSet data=handler.GetPosters();
    String tmp;
    while (data.next()) {
        tmp = "<div class=\"block\">";
        tmp = tmp + "<h4>" + data.getString(Const.POSTER_NAME) + "</h4>";
        tmp = tmp + "<p>" + data.getString(Const.POSTER_DECKIPTION) + "</p>";
        try {
            if (data.getBytes(Const.POSTER_IMAGE) != null) {
                ResultSet resSet = null;
                PreparedStatement pstmt = null;
                byte barray[] = null;
                try {
                    if (!data.getBytes(Const.POSTER_IMAGE).equals(null))
                        tmp = tmp + "<img src=\"" + request.getContextPath() + "/getImage.jsp?id=" + data.getString(Const.POSTER_NAME) + "\" alt=\"" + data.getString(Const.POSTER_NAME) + " post\" border=\"1\" height=\"20%\" width=\"20%\">";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (request.getAttribute("Error" + data.getString(Const.POSTER_NAME))!=null) {
                tmp = tmp + "<p>" + request.getAttribute("Error" + data.getString(Const.POSTER_NAME)) + "</p>";
                request.setAttribute("Error" + data.getString(Const.POSTER_NAME), null);
            }
            if (request.getSession().getAttribute("userName") != null) {
                tmp = tmp + "<form method=\"post\">\n" +
                        "    <button name=\"ToFavorite\" type=\"submit\" value=\"" + data.getString(Const.POSTER_NAME) + "\">Добавить в избранное</button>\n" +
                        "</form>";
            }
            tmp = tmp + "</div>";
            out.println(tmp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
%>

</body>
</html>
