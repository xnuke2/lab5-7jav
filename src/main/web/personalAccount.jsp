<%@ page import="model.DBHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="model.Const" %>
<%@ page import="java.util.Vector" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>personalAccount</title>
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
<style>
    .tab-content {
        display: none;
    }

    .tab-content:target {
        display: block;
    }
</style>
<div class="tab">
    <div class="tab-nav">
        <a class="tab-link" href="#content-1">Личный кабинет</a>
        <%
            handler = new DBHandler();
            if(handler.isModer(request.getSession().getAttribute("userName").toString())){
                out.println("<a class=\"tab-link\" href=\"#content-2\">Кабинет модератора</a>");
            }
            if(handler.isAdmin(request.getSession().getAttribute("userName").toString())){
                out.println("<a class=\"tab-link\" href=\"#content-3\">Кабинет администратора</a>");
            }
        %>
    </div>
    <div class="tab-content" id="content-1">
        <div class="block">
            <h3>Личные данные</h3>
            <%
            ResultSet user = handler.GetUser(request.getSession().getAttribute("userName").toString());
            user.next();
            out.println("       <form method=\"post\">\n" +
                    "                <input value=\""+user.getString(Const.USERS_NAME)+"\" name=\"name\" placeholder=\"Логин\"><br>\n" +
                    "                <input value=\""+user.getString(Const.USERS_FIRST_NAME)+"\" name=\"firstName\" placeholder=\"Фамилия\"><br>\n" +
                    "                <input value=\""+user.getString(Const.USERS_SECOND_NAME)+"\" name=\"secondName\" placeholder=\"Имя\"><br>\n" +
                    "                <input type=\"password\" name=\"oldpassword\" placeholder=\"Старый пароль\"><br>\n" +
                    "                <input type=\"password\" name=\"newpassword\" placeholder=\"Новый пароль\"><br>\n"+
                    "                <button type=\"submit\" name=\"Change\" value=\""+user.getString(Const.USERS_NAME)+"\">Изменить</button><br>\n" +
                    "            </form>");
            if(request.getAttribute("ErrorChange")!=null){
                out.println("<p>"+request.getAttribute("ErrorChange")+"</p>");
                request.setAttribute("ErrorChange",null);
            }
            %>
        </div>
        <div class="block">
            <h3>Избранное</h3>

        <%
            request.setCharacterEncoding("UTF-8");
            handler =new DBHandler();
            ResultSet data1=handler.GetFavoritePost(request.getSession().getAttribute("userName").toString());
            int counter=0;
            Vector<String> names = new Vector<String>();
            while (data1.next()){
                counter++;
                names.add(data1.getString(Const.FAVORITE_NAME_OF_POST));
            }
            ResultSet data;
            for(int i=0;i<names.size();i++){
                 data=handler.GetPost(names.elementAt(i));
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
                                    "    <button name=\"RemoveFromFavorite\" type=\"submit\" value=\"" + data.getString(Const.POSTER_NAME) + "\">Убрать из избранного</button>\n" +
                                    "</form>";
                        }
                        tmp = tmp + "</div>";
                        out.println(tmp);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        %>
        </div>
        <form method="post">
            <button type="submit" name="submit" value="exit">Выход</button>
        </form>
    </div>
    <%
        request.setCharacterEncoding("UTF-8");
        if(handler.isModer(request.getSession().getAttribute("userName").toString())){
            out.println("    <div id=\"content-2\" class=\"tab-content\">\n");
            out.println(
                    "        <div class=\"block\"><form method=\"post\" enctype=\"multipart/form-data\">\n" +
                    "            <h4>Создать новую запись в киноафише</h4>\n" +
                    "            <input name=\"name\" placeholder=\"Название фильма/мультфильма\" type=\"text\">\n" +
                    "            <input name=\"descr\" placeholder=\"Описание\" type=\"text\">\n");
            if(request.getAttribute("ErrorCreatFilmPost")!=null){
                out.println("<p>"+request.getAttribute("ErrorCreatFilmPost")+"</p>");
                request.setAttribute("ErrorCreatFilmPost",null);
            }
                    out.println("            <input type=\"file\" name=\"photo\" size=\"50\" />\n" +
                    "            <button type=\"submit\" name=\"submit\" value=\"createFilmPost\">Добавить</button>\n" +
                    "        </form></div>\n");

            request.setCharacterEncoding("UTF-8");
            handler =new DBHandler();
            data=handler.GetPosters();
            out.println("<div class=\"block\"><h4>Киноафиша</h4>");
            String tmp="";
            while (data.next()) {
                tmp =tmp+ "<div class=\"block\"><form method=\"post\" enctype=\"multipart/form-data\">";
                tmp = tmp + "<input name=\"name\" placeholder=\"Название фильма/мультфильма\" type=\"text\" value=\""+ data.getString(Const.POSTER_NAME) +"\"><br>\n";
                tmp = tmp + "<input name=\"descr\" placeholder=\"Описание\" type=\"text\" value=\"" + data.getString(Const.POSTER_DECKIPTION) +"\"><br>\n";
                try {
                    if (data.getBytes(Const.POSTER_IMAGE) != null) {
                        ResultSet resSet = null;
                        PreparedStatement pstmt = null;
                        byte barray[] = null;
                        try {
                            if (!data.getBytes(Const.POSTER_IMAGE).equals(null))
                                tmp = tmp + "<img src=\"" + request.getContextPath() + "/getImage.jsp?id=" + data.getString(Const.POSTER_NAME) + "\" alt=\"" + data.getString(Const.POSTER_NAME) + " post\" border=\"1\" height=\"20%\" width=\"20%\"><br>";
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        tmp=tmp+"<input type=\"file\" name=\"photo\" size=\"50\" /><br>\n";
                    }
                    if (request.getAttribute("Error" + data.getString(Const.POSTER_NAME))!=null) {
                        tmp = tmp + "<p>" + request.getAttribute("Error" + data.getString(Const.POSTER_NAME)) + "</p>";
                        request.setAttribute("Error" + data.getString(Const.POSTER_NAME), null);
                    }
                    tmp=tmp+"<button name=\"Redact\" type=\"submit\" value=\"" + data.getString(Const.POSTER_NAME) + "\">Редактировать</button>\n<br>";
                    tmp=tmp+"</form>";
                        tmp = tmp + "<form method=\"post\">\n" +
                                "    <button name=\"Delete\" type=\"submit\" value=\"" + data.getString(Const.POSTER_NAME) + "\">Удалить</button>\n" +
                                "</form>";
                    tmp = tmp + "</div>";
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            out.println(tmp);
            out.println("</div>");
            out.println("</div>");
        }
        if(handler.isAdmin(request.getSession().getAttribute("userName").toString())){
            out.println("<div class=\"tab-content\" id=\"content-3\">\n" );
            String tmp1 =
                    "        <div class=\"block\"><form method=\"post\">\n" +
                    "            <h3>Добавить пользователя</h3>\n" +
                    "            <input name=\"name\" placeholder=\"Логин\"><br>\n" +
                    "            <input name=\"firstName\" placeholder=\"Фамилия\"><br>\n" +
                    "            <input name=\"secondName\" placeholder=\"Имя\"><br>\n" +
                    "            <input type=\"password\" name=\"password\" placeholder=\"Пароль\"><br>\n" +
                    "            <select type=\"text\" name=\"Role\">\n" +
                    "                <option value=\"moderator\">moderator</option>\n" +
                    "                <option value=\"basic\">basic</option>\n" +
                    "                <select>\n" +
                    "                    <button type=\"submit\" name=\"AddUser\">Добавить</button><br> ";

            if(request.getParameter("ErrorAddUser")!=null){
                out.println("<p>"+request.getParameter("ErrorAddUser")+"</p>");
            }

            tmp1=tmp1+ "</form></div>";
            ResultSet RS = handler.GetUsers();
            tmp1=tmp1+"<div class=\"block\"><h3>Список пользователей</h3>";
            while (RS.next()){
                if(!RS.getString(Const.USERS_NAME).equals(request.getSession().getAttribute("userName").toString())){



                    tmp1=tmp1+"<div class=\"block\">\n" +
                            "    <form method=\"post\">\n" +
                            "        <input type=\"text\" name=\"newname\" value=\""+RS.getString(Const.USERS_NAME)+"\" placeholder=\"Логин\">\n" +
                            "        <input type=\"text\" name=\"firstName\" value=\""+RS.getString(Const.USERS_FIRST_NAME)+"\" placeholder=\"Фамилия\">\n" +
                            "        <input type=\"text\" name=\"secondName\" value=\""+RS.getString(Const.USERS_SECOND_NAME)+"\" placeholder=\"Имя\">\n" +
                            "        <input type=\"password\" name=\"password\" value=\""+RS.getString(Const.USERS_PASSWORD)+"\" placeholder=\"Пароль\">\n" +
                            "            <select type=\"text\" name=\"Role\">\n" +
                            "                <option value=\"moderator\">moderator</option>\n" +
                            "                <option value=\"basic\">basic</option>\n" +
                            "                <select>\n";
                    if(request.getAttribute("ErrorCh"+RS.getString(Const.USERS_NAME))!=null){
                        tmp1=tmp1+"<p>"+request.getParameter("ErrorCh"+RS.getString(Const.USERS_NAME))+"</p>";
                        request.setAttribute("ErrorCh"+RS.getString(Const.USERS_NAME),null);
                    }
                    tmp1=tmp1+"        <button type=\"submit\" name=\"ChangeUser\" value=\""+RS.getString(Const.USERS_NAME)+"\">Изменить</button>\n" +
                            "    </form>\n" +
                            "    <form method=\"post\">\n" +
                            "        <button type=\"submit\" name=\"DeleteUser\" value=\""+RS.getString(Const.USERS_NAME)+"\">Удалить</button>\n" +
                            "    </form>\n" +
                            "</div>";
                }
            }
            tmp1=tmp1+"</div>";
            tmp1=tmp1+"</div>";
        out.println(tmp1);
        }

    %>





</div>
</body>
</html>
