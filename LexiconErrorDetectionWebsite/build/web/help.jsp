<%-- 
    Document   : help
    Created on : Apr 12, 2016, 1:45:42 AM
    Author     : nmale_000
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Help</title>
        <link href="css/main.css" rel="stylesheet"/>
        
    </head>
    <body>
     <div class="nav">
    <span id="headingMainPage">SentLexCC </span>
    <div id="SideDiv">
    <a href="Login.jsp">Home</a>  
    <a  href="more.jsp">More</a>
   <a  href="about.jsp">About</a>
</div>
</div>
        
        <h1>Help</h1>
        <div>
                <h2>Uploading a Lexicon</h2>
                <p>
                    Lexicons must be uploaded in the form of a text document. Please browse your computer for the desired lexicon by pressing browse on the home page. Once the desired file has been selected adjust the combo boxes to the appropriate selection. Next, press submit and wait for the lexicon to be analyzed. If you receive an error message, please check that your lexicon is formatted properly and try again. 
                </p>
                <h2>
                    Combo Boxes
                </h2>
                <p>
                    There are three combo boxes from which you must choose a selection when uploading a lexicon. The first is the type of polarity of your lexicon. The tool currently supports two polarity types, discrete and continuous. If your lexicon classifies each word as strictly positive, negative, or neutral then select discrete. If your lexicon gives a proportion positive, proportion negative, and proportion neutral then select continuous. The second selection is the part of speech of the words in your lexicon. This is self-explanatory; if your words are verbs choose verb, if your words are nouns choose noun, et cetera.
                    The third selection is the WordNet version you would like to compare to.___More about WordNet___
                </p>
                <h2>
                    Working with the Tool
                </h2>
                <h3>
                    First Glance
                </h3>
                <p>
                    Upon successful upload of a lexicon, you will see a page displaying a list of errors detected, a graphical representation of the first error, and three tabs titled "Graph", "Table", and "Split".
                </p>
                <p>
                    The errors listed on the left hand side are marked unread as can be noted by the _____ symbol. If you click on the error, the graphs and tables will update to display the new error. This error will then be marked read as can be noted by the ____ symbol. Once you have viewed an error and are satisfied with any changes you have made then press the complete button in order to mark the error complete. An error marked complete can be noted by the check symbol.

                </p>
                <h3>
                    The Graph
                </h3>
                <p>
                    The graph displays a section of your lexicon where an error was found. It lists the words along the top row of nodes and the senses along the bottom row of nodes. Matching words and senses are connected by lines. If the user clicks on a node, the corresponding word/sense family will be highlighted. In order to undo the highlighting, simply click on the stage of the graph. You can zoom in and out of the graph by using the mouse wheel.
                </p>
                <h3>
                    The Table
                </h3>
                <p>

                </p>
                <h3>
                    Querying Dictionaries
                </h3>
                <p>
                    Right clicking a word will show a context menu with several options. Three of these options are dictionaries. Clicking on a dictionary will search the word on the specified dictionary and display the results in the same viewing window. You can then compare the senses provided by the online dictionary to the senses that your lexicon contains and consider solving any disparities. The graph, table, and dictionary frames can then be resized by clicking and dragging the vertical bar between them. 
                </p>
                <h3>
                    Making Changes
                </h3>
                <p>
                    Coming Soon
                </p>
                <h3>
                    Saving a Lexicon
                </h3>
                <p>
                    Coming Soon
                </p>
                <h3>
                    Downloading the Updated Lexicon
                </h3>
                <p>
                    Coming Soon
                </p>
                <p>
                    Instructions/ common problems-------
                </p>
</div>  
    </body>
</html>
