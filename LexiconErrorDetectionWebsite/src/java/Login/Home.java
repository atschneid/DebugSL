/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author saroja
 */
@WebServlet(name = "Home", urlPatterns = {"/Home"})
public class Home extends HttpServlet {

  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession  session = request.getSession();
         DBConn dbconn=new DBConn();
        String uid = session.getAttribute("UserID").toString();
                           ArrayList<LexiconFields> projectsList = dbconn.GetRecentProjects(uid); 
                           request.setAttribute("ProjectsList", projectsList);
                          
                            RequestDispatcher rd=request.getRequestDispatcher("History.jsp");
			   rd.forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      String id=  request.getParameter("SaveTA");
      String comments = request.getParameter("txt"+id);
      
        HttpSession  session = request.getSession();
         DBConn dbconn=new DBConn();
         dbconn.SaveComments(id,comments);
        String uid = session.getAttribute("UserID").toString();
                           ArrayList<LexiconFields> projectsList = dbconn.GetRecentProjects(uid); 
                           request.setAttribute("ProjectsList", projectsList);
                          
                            RequestDispatcher rd=request.getRequestDispatcher("History.jsp");
			   rd.forward(request, response);
        
    }

  
}
