<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <link href="css/main.css" rel="stylesheet"/>

<title>SentLexCC Registration</title>
</head>
<body style="background-image: url('images/background.jpg');background-repeat: no-repeat; background-size: inherit; ">
<div class="nav">
    <span id="headingMainPage">SentLexCC </span>
    <div id="SideDiv">
    <a href="help.jsp">Help</a>  
    <a  href="more.jsp">More</a>
   <a  href="about.jsp">About</a>
</div>
</div>
<div class="center">

<div class="ContentBox">
        
<div id="RegistrationDiv">
<h1>Create Account</h1>


<form method="post" action="Registration" >
    <table style="line-height: 40px;">
        <tr>
            <td>
                UserName:
            </td>
             <td>
                <input type="text" name="username" id="username" style="border-radius:5px;">
            </td>
        </tr>
          <tr>
            <td>
                First Name:
            </td>
             <td>
                <input type="text" name="fname" id="fname" style="border-radius:5px;">
            </td>
        </tr>
          <tr>
            <td>
                Last Name:
            </td>
             <td>
                <input type="text" name="lname" id="lname" style="border-radius:5px;">
            </td>
        </tr>
          <tr>
            <td>
                Email:
            </td>
             <td>
                <input type="text" name="email" id="email" style="border-radius:5px;">
            </td>
        </tr>
          <tr>
            <td>
                Password:
            </td>
             <td>
                <input type="password" name="password" id="password" style="border-radius:5px;">
            </td>
        </tr>
    </table>

<input type="submit" value="Submit" name="RegisterButton" class="buttons" >

</form>
</div>
</div>
</div>
</body>
</html>