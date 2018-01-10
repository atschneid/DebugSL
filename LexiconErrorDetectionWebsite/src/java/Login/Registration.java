/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author saroja
 */
@WebServlet(name = "Registration", urlPatterns = {"/Registration"})
public class Registration extends HttpServlet {
	DBConn dbconn=new DBConn();
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String buttonValue = request.getParameter("RegisterButton");
        if(buttonValue.equals("Submit"))
                {
                  String fname =  request.getParameter("fname");
                  String lname =  request.getParameter("lname");
                  String email =  request.getParameter("email");
                  String password =  request.getParameter("password");
                  String username =  request.getParameter("username");
                   int validUser = dbconn.RegisterUser(fname,lname,email,password,username);
                   if(validUser == 1)
                   {
                       RequestDispatcher rd=request.getRequestDispatcher("Welcome.jsp");
			   rd.forward(request, response);
                   }
                   else
                   {
                       request.setAttribute("ErrorMessage", "An account with this Email already exists. "
                               + "Please try to log in with your Email.");
                       RequestDispatcher rd=request.getRequestDispatcher("Login.jsp");
			   rd.forward(request, response);
                   }
                           
                }
    }

  
}
