
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       
    </head>
    <body>
        <%
          session.invalidate();
         %>
         <div style="height: 400px;width: 400px;padding-top: 100px;padding-left: 400px;">
             <img  src="images/logoutMessage.png" alt="Logged Out"/><br>
             <span style="padding-left: 200px;">Click here to <a href="Login.jsp" >Login</a></span>  
         </div>

    </body>
</html>
