package servlets;
import model.Const;
import model.DBHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IndexServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("index.jsp");
        req.getSession().setMaxInactiveInterval(1800);
        req.setAttribute("time", new java.util.Date().getTime());
        requestDispatcher.forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if(req.getParameter("ToFavorite")!=null){

            DBHandler handler = new DBHandler();
            if(handler.CheckFavorite(req.getSession().getAttribute("userName").toString(),req.getParameter("ToFavorite"))){
                req.setAttribute("Error"+(String) req.getParameter("ToFavorite").trim(),"уже есть в избранном");
            }else {
                handler.AddFavorite(req.getSession().getAttribute("userName").toString(),req.getParameter("ToFavorite"));
            }

        }
        doGet(req,resp);
    }

}
