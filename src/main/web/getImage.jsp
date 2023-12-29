
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.sql.PreparedStatement" %>

<%@ page import="java.sql.Blob" %>
<%@ page import="model.DBHandler" %>
<%@ page import="model.Const" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    try{
    String accountId = request.getParameter("id");
    byte[] imageData =new byte[1024];
    DBHandler handler =new DBHandler();
    imageData =handler.getImgByName(accountId);

    int len = imageData.length;

    if(imageData!=null){
        int len1 = imageData.length;
        response.setContentType("image/jpeg");
        response.setHeader("Content-disposition","attachment; filename=" +"test");
        response.getOutputStream().write(imageData,0,len);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
    }catch(Exception e) {
    e.printStackTrace();
    }
%>
</body>
</html>
