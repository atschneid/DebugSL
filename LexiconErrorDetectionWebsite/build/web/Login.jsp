<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <link href="css/main.css" rel="stylesheet"/>

<title>DebugSL</title>
</head>
<body style="background-image: url('images/background.jpg');background-repeat: no-repeat; background-size: inherit; ">
<div class="nav">
    <span id="headingMainPage">DebugSL </span>
    <div id="SideDiv">
    <a href="help.jsp">Help</a>  
    <a  href="more.jsp">More</a>
   <a  href="about.jsp">About</a>
</div>
</div>
<div class="center">

<div class="ContentBox">
        
<div id="LoginDiv">
    <span style="color:white;font-size: 30px;">Login</span>
 <p style="color: red;font-size:13px;display:dynamic;"> 
 <% if(request.getAttribute("ErrorMessage") != null)
{
  out.println(request.getAttribute("ErrorMessage"));
}
%></p>
    
<form method="post" action="Login" >
    <table style="line-height: 40px;">
        <tr>
            <td>
                User Name:
            </td>
            <td>
                <input type="text" name="uname" id="uname" style="border-radius:5px;">
            </td>
        </tr>
           <tr>
            <td>
                Password:
            </td>
            <td>
                 <input type= "password"   name="password" id="password" style="border-radius:5px;"> 
            </td>
        </tr>
    </table>


<a href="" style="font-size:15px;color:white;">Forgot Password?</a><br><br><br>
&nbsp &nbsp<input type="submit" value="Login" name="SubmitButton" class="buttons">&nbsp &nbsp&nbsp &nbsp&nbsp &nbsp
<input type="submit" value="Register" name="SubmitButton" class="buttons" >

</form>
</div>
</div>

</body>
</html>