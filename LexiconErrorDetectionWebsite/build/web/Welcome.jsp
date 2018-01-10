<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
</script>
</head>
<body >
<div class="navMin">
    <span id="heading">DebugSL </span>
   
   
      <a href="LogOut.jsp"><img id="LogOutButton" src="images/logout.jpg" style="padding-top: 10px;width: 30px;height: 30px;float: right;padding-right: 20px;"  alt="LogOut"/>
        </a>
        <a href="Home" ><img id="HomeButton" src="images/Home2.jpg" style="padding-top: 5px;width: 40px;height: 40px;float: right;padding-right: 20px;" alt="Home"/>
        </a>
     

</div>
  
    <div class="mainDivWelcome" >
        <div id="WelcomeDiv" style="padding: 100px;">
        <span style="font-size: 50px;color:black;padding-left: 250px;font-weight: bold"> Welcome!</span>
        <br><br><br>
        <a href="javascript:DisplayLexCompForm();" style="text-decoration: none;padding: 15px;margin-left: 160px;background-color: black;color: white;font-size: 20px;">Compare Lexicons</a>
       &nbsp &nbsp&nbsp &nbsp
    <a href="javascript:DisplayLexForm();" style="text-decoration: none;padding: 15px;background-color: black;color: white;font-size: 20px;">Upload Lexicon</a>
   </div>
       <form id="LexCompForm" method="post" action="compare" enctype="multipart/form-data" style="padding-left:150px;padding-top:20px;display: none;color: black;font-size: 20px;font-weight: 500;" >
            <br><br><br>
Upload Lexicon1:&nbsp &nbsp <input type="file" name="LexiconFile1" class="inputfeilds"><br><br>
Upload Lexicon2:&nbsp &nbsp <input type="file" name="LexiconFile2" class="inputfeilds"><br><br>

 <input type="submit" value="Upload" name="UploadButton1" class="buttons">
        </form>       
        
<form style="padding-left:150px;padding-top:20px;display: none;color: black;font-size: 20px;font-weight: 500;" id="LexiconForm" method="post" action="Upload" enctype="multipart/form-data">
<br><br><br>
Upload Lexicon:&nbsp &nbsp <input type="file" name="LexiconFile" class="inputfeilds"><br><br>
Select Polarity: &nbsp &nbsp
<select name="typePolarity" class="inputfeilds">
<option value="continuous">Continuous</option>
<option selected="selected" value="discrete">Discrete</option>
</select>
<br><br>
What part of speech is your lexicon?
<select title="Select Part of Speech" name="partOfSpeech"  class="inputfeilds">
<option selected="selected" id="adverb" value="adverb">Adverb</option>
<option id="adjective" value="adjective">Adjective</option>
<option id="verb" value="verb">Verb</option>
<option id="noun" value="noun">Noun</option>
</select>
<br><br>
Choose a WordNet version: 
<select title="Select WordNet Version" name="wordnetVersion"  class="inputfeilds">
 <option selected="selected" value="1.7">WordNet 1.7</option>
 <option  value="2.0">WordNet 2.0</option>
 <option  value="2.1">WordNet 2.1</option>
 <option  value="3.0">WordNet 3.0</option>
 <option  value="3.1">WordNet 3.1</option>
 </select>
 <br><br>
 <input type="submit" value="Upload" name="UploadButton" class="buttons">
</form>
     </div>

    </div>
</body>
</html>