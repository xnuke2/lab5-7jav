package servlets;

import model.DBHandler;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginRegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("LoginRegistration.jsp");
        req.getSession().setMaxInactiveInterval(1800);
        req.setAttribute("time", new java.util.Date().getTime());
        requestDispatcher.forward(req, resp);
        req.setAttribute("ErrorRegist",null);
        req.setAttribute("ErrorLogin", null);

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if(req.getParameter("submit").equals("login")){
            req.setAttribute("ErrorLogin", null);
            String login = req.getParameter("name").trim();
            String password =req.getParameter("password").trim();
            if(login.equals("")||password.equals("")){
                req.setAttribute("ErrorLogin", "Одно из полей пусто");
                doGet(req, resp);
                return;
            }
            DBHandler handler=new DBHandler();
            if(handler.CheckUser(login,password)){
                req.getSession().setAttribute("userName", login);
                handler.upNumOfEnter();
                resp.sendRedirect(req.getContextPath()+"/personalAccount");
            }else {
                req.setAttribute("ErrorLogin", "Неверный логин или пароль");
                doGet(req, resp);
                return;
            }
            return;
        }
        if(req.getParameter("submit").equals("regist")){
            req.setAttribute("ErrorRegist", null);
            String login = req.getParameter("name").trim();
            String firstName = req.getParameter("firstName").trim();
            String secondName = req.getParameter("secondName").trim();
            String password =req.getParameter("password").trim();
            DBHandler handler=new DBHandler();
            if(login.equals("")||password.equals("")||firstName.equals("")||secondName.equals("")){
                req.setAttribute("ErrorRegist", "Одно из полей пусто");
                doGet(req, resp);
                return;
            }
            if(handler.CheckUser(login)){
                req.setAttribute("ErrorRegist","логин уже занят");
                doGet(req,resp);
                return;
            }
            handler.singUpUser(login,firstName,secondName,password);
            req.getSession().setAttribute("userName",login);
            handler.upNumOfEnter();
            //doGet(req, resp);
            resp.sendRedirect(req.getContextPath()+"/personalAccount");

        }
    }
}
