
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Lexicon Visualization</title>

        <link rel="shortcut icon" href=""/>
        <script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
        <script src="js/jquery.splitter-0.17.0.js"></script>
        <link href="css/jquery.splitter.css" rel="stylesheet"/>
        <link href="css/main.css" rel="stylesheet"/>

        <script>

            var myGraph;
            var successfulUpload = false;
            var jsonText = "";
            var jsonFileName = "", headingMessage = "File Upload";
            var graphViewSettings = {
                polarityDisplay: "color",
                viewStyle: "all",
                positiveColor: "#00FF00",
                negativeColor: "#FF0000",
                neutralColor: "#6A7A7A",
                orientation: "horizontal"
            };



            function ExpandList() {

                var listDiv = document.getElementById("ListBox");
                listDiv.style.display = "block";


            }
            ;

            function CollapseList() {

                var words = document.getElementById("Words");
                var listDiv = document.getElementById("ListBox");
                if (listDiv.style.display === "none")
                {
                    // hidden list
                    listDiv.style.display = "block";
                    document.getElementById("ContentRight").style.width = "85%";
                    words.style.backgroundColor = "#F9F8F8"
                } else
                {
                    listDiv.style.display = "none";
                    document.getElementById("ContentRight").style.width = "100%";
                    words.style.backgroundColor = "#53c653";
                }





            }
            ;

            function ExpandComments() {

                document.getElementById("CommentsBox").style.display = "block";
                document.getElementById("GraphBox").style.width = "48.5%";
                document.getElementById("rbtnG").style.display = "none";
                document.getElementById("lbtnC").style.display = "none";
                document.getElementById("lbtnG").style.display = "block";
                document.getElementById("separator").style.display = "block";
                document.getElementById("rbtnC").style.display = "block";

            }
            ;

            function CollapseComments() {

                document.getElementById("CommentsBox").style.display = "none";
                document.getElementById("GraphBox").style.width = "96%";
                document.getElementById("rbtnG").style.display = "none";
                document.getElementById("lbtnG").style.display = "none";
                document.getElementById("rbtnC").style.display = "none";
                document.getElementById("lbtnC").style.display = "block";
                document.getElementById("separator").style.display = "none";
            }
            ;

            function ExpandGraph() {

                var graphDiv = document.getElementById("GraphBox");
                graphDiv.style.display = "block";
                document.getElementById("CommentsBox").style.width = "47%";
                document.getElementById("rbtnG").style.display = "none";
                document.getElementById("lbtnG").style.display = "block";
                document.getElementById("separator").style.display = "block";
                document.getElementById("rbtnC").style.display = "block";

            }
            ;

            function CollapseGraph() {

                var graphDiv = document.getElementById("GraphBox");
                graphDiv.style.display = "none";
                document.getElementById("CommentsBox").style.width = "96%";
                document.getElementById("rbtnG").style.display = "block";
                document.getElementById("lbtnG").style.display = "none";
                document.getElementById("rbtnC").style.display = "none";
                document.getElementById("separator").style.display = "none";
            }
            ;
            function PolarityChange() {

                var polarityDisplay = document.getElementById("polarityDisplay");
                updateGraphViewSetting(polarityDisplay.options[polarityDisplay.selectedIndex].value, 'polarityDisplay', function () {});
            }
            ;
            function StyleChange() {
                var viewStyle = document.getElementById("viewStyle");
                updateGraphViewSetting(viewStyle.options[viewStyle.selectedIndex].value, 'viewStyle', viewStyleChanged);
            }
            ;

            function OrientationChange() {
                var orientation = document.getElementById("orientation");
                updateGraphViewSetting(orientation.options[orientation.selectedIndex].value, 'orientation', changeOrientation);

            }
            ;
            //gets the value of the cookie with the specified name
            function getCookie(cname) {
                var name = cname + "=";
                var ca = document.cookie.split(';');
                for (var i = 0; i < ca.length; i++) {
                    var c = ca[i];
                    while (c.charAt(0) == ' ') {
                        c = c.substring(1);
                    }
                    if (c.indexOf(name) == 0) {
                        return c.substring(name.length, c.length);
                    }
                }
                return "";
            }

            successfulUpload = ${requestScope.successfulUpload};
            if (successfulUpload) {
                if (getCookie("id") === "theid") {
                    jsonFileName = "json/sampleWordSynset_1.json";
                } else {
                    jsonFileName = "json/sampleWordSynset.json";
                }
            }
            //jsonFileName = "${requestScope.jsonFileName}";
            if (!("${requestScope.headingMessage}" === "File Upload") || !("${requestScope.headingMessage}" === "Results")) {
                headingMessage = "$(requestScope.headingMessage)";
            }

            //   


            function SaveGraph() {
                var popup = document.getElementById("popup");
                popup.style.display = "block";
                var parentPage = document.getElementById("graphView");
                parentPage.className = "backgroundblur";


            }
            ;

            function div_hide() {
                var popup = document.getElementById("popup");
                popup.style.display = "none";
                var parentPage = document.getElementById("graphView");
                parentPage.className = "tabContent";
            };


        </script>

        <script src="js/sigma/sigma.require.js"></script>
        <script src="js/sigma/plugins/sigma.parsers.json.js"></script>
        <script src="js/sigma/plugins/sigma.renderers.edgeLabels.min.js"></script>

    </head>
    <body >
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


        <script>
            window.onload = function () {

                initTabs();

            };




        </script>
        <script>
            if ("${requestScope.headingMessage}" === "File Upload" || "${requestScope.headingMessage}" === "Results") {
                document.getElementById("headingMessage").textContent = "${requestScope.headingMessage}";
            }


        </script>





        <div >    
            <input id="ProjectName" name="ProjectName" style="display: none;"  type="text">
            <div id="popup" style="z-index: 50;display: none;font-size: 40px;position: absolute;left: 50%; top: 17%;margin-left:-202px; border-radius: 15px;">
                <img id="close" src="images/close.png" style="height: 20px;width: 20px;float: right;" onclick ="div_hide();">
                <form action="Save" id="form" method="post" style="min-width: 300px;min-height: 300px;" name="form">

                    <table style="line-height: 60px; padding-top: 20px;padding-left: 10px;font-weight:bold;font-family: serif">
                        <tr>
                            <td>
                                Project:
                            </td>
                            <td>
                                <input id="name" name="name" placeholder="Name" type="text" style="border-radius:5px;">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <button type="submit" style="margin-left: 100px;height: 30px;" value="Save" class="buttons" >Save</button>
                            </td>

                        </tr>
                    </table>
                </form>
            </div>


            <div >
                <div class="tabContent" id="graphView">

                    <div id="graphViewingOptions" style="background-color:#F9F8F8;border-style: solid;border-color: grey;border-width: 1px;" >
                        <table style="width: 100%;font-family:cursive;font-size: 12px;font-weight: bold;">
                            <tr>
                                <td>
                                    <span onclick="CollapseList();" class="Active" id="Words" 
                                          style=" background-color: #53c653; cursor: pointer;border-style: solid;height: 25px;border-color: grey;border-width: 1px;padding-top: 2px;padding-bottom: 2px;padding-left: 15px;padding-right: 15px;margin-top: 2px;" > Words </span>

                                </td>
                                <td>
                                    Polarity Display:
                                    <select id="polarityDisplay"  onchange="PolarityChange();"  class="graphViewSelector">
                                        <option id="colorPolarity" value="color">
                                            Color
                                        </option>
                                        <option id="symbolPolarity" value="symbol">
                                            Symbol
                                        </option>
                                        <option id="bothPolarity" value="both">
                                            Both
                                        </option>
                                    </select>
                                </td>
                                <td>
                                    View Style:

                                    <select id="viewStyle" onchange="StyleChange();" class="graphViewSelector">
                                        <option id="allView" value="all">
                                            View All
                                        </option>
                                        <option id="progressiveView" value="progressive">
                                            Progressive View
                                        </option>
                                    </select>
                                </td>
                                <td>
                                    Orientation:

                                    <select id="orientation" onchange="OrientationChange();" class="graphViewSelector">
                                        <option id="horizontalOrientation" value="horizontal">
                                            Horizontal
                                        </option>
                                        <option id="verticalOrientation" value="vertical">
                                            Vertical
                                        </option>
                                    </select> 
                                </td>

                                <td>                                Positive Color:
                                    <input type="color" id="positiveColorSelector" value="#00FF00" onchange="graphViewSettings.positiveColor = this.value;
                                                    myGraph.refresh();">
                                </td>

                                <td>                        Negative Color:
                                    <input type="color" id="negativeColorSelector" value="#FF0000" onchange="graphViewSettings.negativeColor = this.value;
                                                    myGraph.refresh();">
                                </td>
                                <td>
                                    Neutral Color:

                                    <input type="color" id="neutralColorSelector" value="#6A7A7A" onchange="graphViewSettings.neutralColor = this.value; myGraph.refresh();">
                                </td>
                            </tr>
                        </table>
                    </div>


                    <div id="OuterBox" style="background-color: #F9F8F8;border-style: solid;border-color: black;border-width: 1px;">

                        <div id="ListBox" style="display: none;float: left;height: 600px;width: 15%;background-color: #F9F8F8;">

                            <div id="clickableListDiv"  >
                                <ul id="clickableList" style="list-style-type: none;max-height: 600px;overflow-y:scroll;float: left;width: 150px;" >
                                    <!-- Content added dynamically through java script  -->
                                </ul>
                            </div> 
                        </div>
                        <div id="ContentRight" style="float: right;height: 600px;width: 100%;" >

                            <div id="GraphBox" style="float:left;background-color: #F9F8F8;width: 48.5%;height: 600px;padding-top: 5px;">
                                
                                <div style="width: 100%;height: 5px;">

                                </div>
                                <div id="sigma-container" class="MinGraph" >

                                    <div id="all-tabs" >

                                    </div>
                                </div>
                            </div>
                            <div style="float: left;width: 1.5%;height: 600px;background-color: #F9F8F8;">
                                <img src="images/leftArrow.png" onclick="CollapseGraph();" id="lbtnG" style="cursor: pointer;width:30px;height:20px;float: right;" />      

                                <img src="images/rightArrow.png" id="rbtnG" style="cursor: pointer;display: none;width:30px;height:20px;float: right;" onclick="ExpandGraph();"/>

                            </div>
                            <div id="separator" style="float: left;width: 0.5%;height: 600px;background-color: black;">

                            </div>

                            <div style="float: left;width: 2.5%;height: 600px;background-color: #F9F8F8;">
                                <img src="images/leftArrow.png" onclick="ExpandComments();" id="lbtnC" style="display: none;cursor: pointer;width:30px;height:20px;float: right;" />      

                                <img src="images/rightArrow.png" id="rbtnC" style="cursor: pointer;width:30px;height:20px;float: left;" onclick="CollapseComments();"/>

                            </div>

                            <div id="CommentsBox" style=" font-size: 16px;float: right;background-color:  #F9F8F8;height: 600px;width: 47%;background-color: #F9F8F8;overflow: scroll;">

                            </div>
                        </div>





                    </div>

                    <div class="tabContent" id="tableView" style="display: none;padding-left: 100px;">
                        <h2>Table</h2>
                        <div id="splitframe2" class="splitframe">

                        </div>
                    </div>

                    <div class="tabContent" id="splitView" style="display: none;padding-left: 100px;" >
                        <h2>Split</h2>
                        <button onClick="resetCameras()" title="Center and Resize Graph" class="button graph-button">Reset View</button>
                        <button onClick="enableEditing()" name="enableEditing" title="Enter Editing Mode" class="button graph-button edit-button">Enable Editing</button>
                        <div class="split-container">
                            <div id="splitframe3" class="splitframe">
                                <div id="sigma-container2" ></div>
                                <div id="splitframe4" class="splitframe">
                                    <div id="table2">
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>




                </div>
                <nav class="context-menu" id="context-menu">
                    <ul class="context-menu__items">
                        <li class="context-menu__item-list">
                            <a  class="context-menu__link context-menu__subMenu" data-type="subMenu"><!--data-type="subMenu"-->
                                Dictionaries<span class="context-menu__arrow">&#9658</span>
                            </a>

                            <ul class="context-menu__list" id="Dictionaries">
                                <li class="context-menu__item">
                                    <a href="#" class="context-menu__link" onclick="OpenDictionary('http://www.dictionary.com/browse/');" data-sourceURL="http://www.dictionary.com/browse/" data-type="dictionary">
                                        Dictionary.com
                                    </a>
                                </li>
                                <li class="context-menu__item">
                                    <a href="#" class="context-menu__link" onclick="OpenDictionary('http://www.merriam-webster.com/dictionary/');" data-sourceURL="http://www.merriam-webster.com/dictionary/" data-type="dictionary">
                                        Merriam-Webster
                                    </a>
                                </li>
                                <li class="context-menu__item">
                                    <a href="#" class="context-menu__link"  data-sourceURL="http://www.thefreedictionary.com/" data-type="dictionary">
                                        TheFreeDictionary
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class ="context-menu__item-list">
                            <a href="#" class="context-menu__link context-menu__subMenu" data-type="Polarity">
                                Polarity<span class="context-menu__arrow">&#9658</span>
                            </a>

                            <ul class="context-menu__list" id="PolarityList">
                                <li class="context-menu__item">
                                    <a href="#"  value="Positive" class="context-menu__link"  onclick="ChangePolarity('positive');" data-type="changePolarity">
                                        Positive
                                    </a>
                                </li>
                                <li class="context-menu__item">
                                    <a href="#" value="Negative" class="context-menu__link"   onclick="ChangePolarity('negative');" data-type="changePolarity">
                                        Negative
                                    </a>
                                </li>
                                <li class="context-menu__item">
                                    <a href="#" value="Neutral" class="context-menu__link"   onclick="ChangePolarity('neutral');" data-type="changePolarity">
                                        Neutral
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class ="context-menu__item">
                            <a href="#" class="context-menu__link" data-type="suggestSense">
                                Missing Sense
                            </a>
                        </li>
                    </ul>
                </nav>

                <script src="js/ViewingOptions.js"></script>  
                <script src="js/mainjs-1.0.0.js"></script>

                </body>
                </html>
