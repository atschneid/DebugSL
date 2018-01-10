<%@page import="Login.LexiconFields"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/main.css" rel="stylesheet"/>
<title>Recent Projects</title>
<script> 


function OpenComments(id){
   var textRow = document.getElementById("row"+id);
   if(textRow.style.display === "none")
   textRow.style.display = "block";
   else
      textRow.style.display = "none"; 
}

</script>
    </head>
    <body >
<div class="navMin">
    <span id="heading">SentLexCC </span>
    
    <a href="LogOut.jsp"><img id="LogOutButton" src="images/logout.jpg" style="padding-top: 10px;width: 30px;height: 30px;float: right;padding-right: 20px;"  alt="LogOut"/>
        </a>

</div>
        <div class="historyTab" style="width: 200px;height: 50px;float: left;background-color: #55D85D; vertical-align: central;text-align: center;margin: auto;font-weight: bold;font-family:cursive;font-size:25px;margin-left: 30px;margin-top: 100px;">
            <a href="Welcome.jsp" style="text-decoration: none;color:white;padding-top: 10px;"><img style="width: 20px;height: 20px; padding-right:  5px;" src="images/create.png" alt=""/>New Project</a> 
            
        </div>
        
         
        <div class="mainDiv" id="RecentProjectsDiv">
           <h1> Recent Projects</h1>
           <table style="position: inherit;width: 100%;">
       <%
 ArrayList<LexiconFields> Plist = ( ArrayList<LexiconFields>) request.getAttribute("ProjectsList");

for(LexiconFields project : Plist) {
%>
             
<tr >
      
    <td >
        <a  href="Login?word= <%= project.getDateModified() %>">
            <%= project.getProjectTitle().toUpperCase() %> </a><br>
        <br>    <%= project.getDateModified() %> 
        <img style="width: 25px;height: 25px;float: right;cursor: pointer;" id="<%= project.getDateModified() %>" src="images/comments.png" onclick="OpenComments(this.id);"/>
             
    </td>
    </tr>
    <tr style="background-color: white;display: none;" id="row<%= project.getDateModified() %>">
        <td>
            <form action="Home" method="post">
              
    <textarea cols = "140" rows = "8" name="txt<%= project.getDateModified() %>"  id="txt<%= project.getDateModified() %>"  style="resize:vertical;" >
    <%= project.getComments() %>
    </textarea>
    <br> <button style="float:right;" name="SaveTA" type="submit"  value="<%= project.getDateModified() %>">Save</button>
         
            </form>
                
            </td></tr>
 <%
}
%>
    </table>
        </div>
    </body>
</html>
