package servlets;

import model.Const;
import model.DBHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class personalAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("personalAccount.jsp");
        req.getSession().setMaxInactiveInterval(1800);
        req.setAttribute("time", new java.util.Date().getTime());
        requestDispatcher.forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if(req.getParameter("submit")!=null) {
            if (req.getParameter("submit").equals("exit")) {
                req.getSession().setAttribute("userName", null);
                resp.sendRedirect(req.getContextPath());
                return;
            }
            if (req.getParameter("submit").equals("createFilmPost")) {
                String name = req.getParameter("name");
                String descr = req.getParameter("descr");
                DBHandler handler = new DBHandler();
                if (name.equals("") || descr.equals("")) {
                    req.setAttribute("ErrorCreatFilmPost", "Есть пустые строки");
                    doGet(req, resp);
                    return;
                }
                if(handler.CheckPost(name)){
                    req.setAttribute("ErrorCreatFilmPost","Пост с таким названием уже есть");
                    doGet(req, resp);
                    return;
                }
                InputStream inputStream = null;
                Part img = req.getPart("photo");
                if (img == null || img.getSize() == 0) {
                    inputStream = null;
                } else {
                    inputStream = img.getInputStream();
                }

                handler.AddNewFilmPost(name, descr, inputStream);
                doGet(req, resp);
                return;
            }
        }
        if(req.getParameter("Delete")!=null){
            String name=req.getParameter("Delete");
            DBHandler handler =new DBHandler();
            handler.DeletePost(name);
            doGet(req, resp);
            return;
        }
        if(req.getParameter("Redact")!=null){
            String Oldname=req.getParameter("Redact");
            String Newname =req.getParameter("name");
            String descr = req.getParameter("descr");
            if (Newname.equals("") || descr.equals("")) {
                req.setAttribute("Error"+Oldname, "Некоторые строки пусты");
                doGet(req, resp);
                return;
            }
            InputStream inputStream = null;
            Part img = req.getPart("photo");
            if (img == null || img.getSize() == 0) {
                inputStream = null;
            } else {
                inputStream = img.getInputStream();
            }
            DBHandler handler = new DBHandler();
            if(inputStream!=null){
                handler.UpdatePost(Oldname,Newname,descr,inputStream);
            }else {
                handler.UpdatePost(Oldname,Newname,descr);
            }
            doGet(req, resp);
            return;
        }
        if (req.getParameter("RemoveFromFavorite")!=null){
            String name = req.getParameter("RemoveFromFavorite");
            DBHandler handler = new DBHandler();
            handler.DeleteFavorite(name);
            doGet(req, resp);
            return;
        }
        if(req.getParameter("Change")!=null){
            String oldname=req.getParameter("Change");
            String newname=req.getParameter("name");
            String firstname=req.getParameter("firstName");
            String secondname=req.getParameter("secondName");
            String oldpass=req.getParameter("oldpassword");
            String newpass=req.getParameter("newpassword");
            if (newname.equals("") || firstname.equals("")||secondname.equals("")||oldpass.equals("")) {
                req.setAttribute("ErrorChange", "Некоторые строки пусты");
                doGet(req, resp);
                return;
            }
            DBHandler handler = new DBHandler();
            if(handler.CheckUser(oldname,oldpass)){
                String tmpRole;
               ResultSet a = handler.GetUser(oldname);
                try {
                    a.next();
                    tmpRole=a.getString(Const.USERS_ROLE);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if(newpass.equals("")){
                    handler.UpdateUser(oldname,newname,firstname,secondname,oldpass, tmpRole);
                }else {
                    handler.UpdateUser(oldname,newname,firstname,secondname,newpass, tmpRole);
                }
                req.getSession().setAttribute("userName",newname);
            }else {
                req.setAttribute("ErrorChange","Неверный логин или пароль");
            }
            doGet(req, resp);
            return;
        }
        if(req.getParameter("AddUser")!=null){
            String name=req.getParameter("name");
            String firstname=req.getParameter("firstName");
            String secondname=req.getParameter("secondName");
            String oldpass=req.getParameter("password");
            String role =req.getParameter("Role");
            if (name.equals("") || firstname.equals("")||secondname.equals("")||oldpass.equals("")||role.equals("")) {
                req.setAttribute("ErrorAddUser", "Некоторые строки пусты");
                doGet(req, resp);
                return;
            }
            DBHandler handler = new DBHandler();
            if(handler.CheckUser(name)){
                req.setAttribute("ErrorAddUser", "Пользователь с таким логином уже есть");
                doGet(req, resp);
                return;
            }
            handler.singUpUser(name,firstname,secondname,oldpass,role);
            doGet(req, resp);
            return;
        }
        if(req.getParameter("ChangeUser")!=null){
            String newname=req.getParameter("newname");
            String oldname=req.getParameter("ChangeUser");
            String firstname=req.getParameter("firstName");
            String secondname=req.getParameter("secondName");
            String oldpass=req.getParameter("password");
            String role =req.getParameter("Role");
            if (newname.equals("") || firstname.equals("")||secondname.equals("")||oldpass.equals("")||role.equals("")) {
                req.setAttribute("ErrorCh"+oldname, "Некоторые строки пусты");
                doGet(req, resp);
                return;
            }
            DBHandler handler = new DBHandler();
            if(!newname.equals(oldname)){
                if(handler.CheckUser(newname)){
                    req.setAttribute("ErrorCh"+oldname, "Пользователь с таким логином уже есть");
                    doGet(req, resp);
                    return;
                }
            }
            handler.UpdateUser(oldname,newname,firstname,secondname,oldpass,role);
            doGet(req, resp);
            return;
        }
        if(req.getParameter("DeleteUser")!=null){
            String name=req.getParameter("DeleteUser");
            DBHandler handler=new DBHandler();
            handler.DeleteUser(name);
            doGet(req, resp);
            return;
        }
    }
}
