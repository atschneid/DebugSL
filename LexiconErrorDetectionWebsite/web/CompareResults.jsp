<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="css/main.css" rel="stylesheet"/>
<title>Welcome</title>
<script>
    function DisplayLexForm(){
      var welcomeDiv =  document.getElementById("WelcomeDiv");
     welcomeDiv.style.display="none";
     var lexicondiv = document.getElementById("LexiconForm");
     lexicondiv.style.display="block";  
    }
    function DisplayLexCompForm(){
     var welcomeDiv =  document.getElementById("WelcomeDiv");
     welcomeDiv.style.display="none";
     var lexiconCompdiv = document.getElementById("LexCompForm");
     lexiconCompdiv.style.display="block";
    }
    function DisplaySorted(buttonValue){
       var rows1 = document.getElementsByClassName("ActiveRow");
       var rows2 = document.getElementsByClassName("PassiveRow");
       var rows3 = document.getElementsByClassName("NormalRow");
       for(var i = 0; i < rows1.length; ++i) {
        rows1[i].style.display = 'table-row';
    }
    for(var i = 0; i < rows2.length; ++i) {
        rows2[i].style.display = 'table-row';
    }
    for(var i = 0; i < rows3.length; ++i) {
        rows3[i].style.display = 'table-row';
    }
      if(buttonValue === "NoMatch")
      {
         var lst1 = document.getElementsByClassName("ActiveRow");
         var  lst2 = document.getElementsByClassName("PassiveRow");
      }
       if(buttonValue === "RecordMatched")
      {
         var  lst1 = document.getElementsByClassName("PassiveRow");
         var  lst2 = document.getElementsByClassName("NormalRow");
      }
       if(buttonValue === "PolarityDifference")
      {
         var  lst1 = document.getElementsByClassName("ActiveRow");
         var  lst2 = document.getElementsByClassName("NormalRow");
      }
    for(var i = 0; i < lst1.length; ++i) {
        lst1[i].style.display = 'none';
    }
       for(var i = 0; i < lst2.length; ++i) {
        lst2[i].style.display = 'none';
    }  
    }
</script>
</head>
<body style="background-color:white;background-image:none;">

            <table style="max-height:20px;background-color: black;width: 100%;">
            <tr>
                <td style="max-width: 60px;">
                    <span id="heading">DebugSL </span> 
                </td>
                <td style="cursor: pointer;max-width: 40px;text-align: center;color:#F9F8F8; font-size: 13px;font-weight: bold;font-family: 'Lato', sans-serif ">
                    <a style="text-decoration: none;color:#F9F8F8;" href="Welcome.jsp">  <img style="height: 20px;width: 20px;" src="images/newProj.png" alt=""/><span >Project</span></a> 

                </td>
                <td style="cursor: pointer;max-width: 40px;text-align: center;color:#F9F8F8; font-size: 13px;font-weight: bold;font-family: 'Lato', sans-serif ">
                    <a style="text-decoration: none;color:#F9F8F8;" href="Home">  <img style="height: 20px;width: 20px;" src="images/history.png" alt=""/><span >History</span></a> 
                </td>
                <td style="cursor: pointer;max-width: 40px;text-align: center;color:#F9F8F8; font-size: 13px;font-weight: bold;font-family: 'Lato', sans-serif ">
                    <a style="text-decoration: none;color:#F9F8F8;" href="Home">  <img style="height: 20px;width: 20px;" src="images/home.png" alt=""/><span >Home</span></a> 
                </td>

                <td style="min-width: 40px;">

                </td>
                <td style="min-width: 40px; ">

                </td>
                <td style="min-width: 40px;">

                </td>
                <td style="min-width: 40px;">

                </td>
                <td style="cursor: pointer;max-width: 40px;text-align: center;color:#F9F8F8; font-size: 13px;font-weight: bold;font-family: 'Lato', sans-serif ">
                    <a style="text-decoration: none;color:#F9F8F8;" onclick="SaveGraph();"> <img style="height: 20px;width: 20px;" src="images/save.png" alt=""/><span >Save</span></a> 
                </td>
                <td style="cursor: pointer;max-width: 40px;text-align: center; font-size: 13px;font-weight: bold;font-family: 'Lato', sans-serif ">
                    <a style="text-decoration: none;color:#F9F8F8;" href="LogOut.jsp"> <img style="height: 15px;width: 20px;" src="images/lo.png" alt=""/><span >Log Out</span></a> 
                </td>
            </tr>



        </table>
<!--
    
<div class="navMin">
    <span id="heading">DebugSL  </span>
   
     <a href="LogOut.jsp"><img id="LogOutButton" src="images/logout.jpg" style="width: 30px;height: 30px;float: right;padding-right: 20px;"  alt="LogOut"/>
        </a>
        <a href="Home" ><img id="HomeButton" src="images/Home2.jpg" style="width: 30px;height: 30px;float: right;padding-right: 20px;" alt="Home"/>
        </a>
</div>

-->
   
    <div class="mainDivCompare" >
       
        <div id="CompareDiv1"> 
            <span style="font-weight: bold;color: black;"><%= request.getAttribute("FileName1") %>   </span>    
            <br><br>
             <table id="CompareTable">
                 <tbody > 
            <c:forEach items="${map1}" var="entry">
                
              <tr class="${entry.value}" >
                    <td>
                   ${entry.key}
                    </td>
                   
                </tr>
            </c:forEach>
            </tbody>
            </table>
        </div>
         <div id="CompareDiv2" >
             <span style="font-weight: bold;color: black;"> <%= request.getAttribute("FileName2") %></span>
              <br><br>
             <table id="CompareTable">
                 <tbody > 
            <c:forEach items="${map2}" var="entry">
                <tr  class="${entry.value}" >
                    <td>
                   ${entry.key}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
            </table>
        </div >
     </div>
      
 <table style="width: 1000px;height: 100px;allign:center;margin: auto;font-weight: bold;">
     <tr>
          <td>All: </td>
  <td> <button value="All" name="SortButton" onclick="DisplaySorted('All');" style="background-color: whitesmoke;min-width:75px;height: 30px;margin-right:50px;"> </td>
 
  <td>No Match: </td>
  <td> <button value="No Match" name="SortButton" onclick="DisplaySorted('NoMatch');" style="background-color: lavenderblush;min-width:75px;height: 30px;margin-right:50px;"> </td>
 <td>Matched Records: </td> 
 <td> <button value="Record Matched" name="SortButton" onclick="DisplaySorted('RecordMatched');" style="background-color: greenyellow;min-width:75px;height: 30px;margin-right:50px;"> </td>
<td>Records with Polarity Difference: </td> 
 <td> <button value="Polarity Difference" name="SortButton" onclick="DisplaySorted('PolarityDifference');" style="background-color: gold;min-width:75px;height: 30px;"> </td>
        </tr>
        </table>
    </div>
</body>
</html>