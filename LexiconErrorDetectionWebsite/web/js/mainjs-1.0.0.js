/* global Packages */

var editingMode = 0;
//var shiftDown = false;





//temporary cookie for keeping session
document.cookie = "id=theid";

//gets the value of the cookei with the specified name
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length,c.length);
        }
    }
    return null;
} 


//create the word checkboxes
    function createWordCheckbox(word){
        var box = document.createElement("INPUT");
        var lowerCaseWord= word.toLowerCase();
        box.setAttribute("type", "checkbox");
        box.setAttribute("value", lowerCaseWord);
        box.checked = true;
        box.setAttribute("class", "wordCheckbox");
        box.setAttribute("id", lowerCaseWord + "Checkbox");
        box.onchange = function(){
//            if(this.checked){
//                //node.checked = true;
//            }else{
//                //node.checked = false;
//            }
            refreshGraphs();
            refreshGraphs();
        };
        return box;
    }
    //display only the checked words and hide the unchecked ones
    function hideUncheckedWords(wordsArray){
        var checkboxes = document.getElementsByClassName("wordCheckbox");
        var nodes = myGraph.graph.nodes();
        for(var k = 0; k < checkboxes.length; k++){
            for(var j = 0; j < nodes.length; j++){
                if(checkboxes[k].id === nodes[j].id){
                    if(checkboxes[k].checked){
                        nodes[j].hidden = false;
                    }else{
                        nodes[j].hidden = true;
                    }
                    break;
                }
            }
        }
    }



//tabs and preparatory sigma and contextMenu
var tabLinks = new Array();
            var contentDivs = new Array();
            var previouslySelected = "";
            
            //var myGraph;
            var cam1;
            var cam2;
            
            
            function resetCameras(){
                var buttons = document.getElementsByClassName("reset-button");
                for(var k = 0; k<buttons.length; k++ ){
                    buttons[k].blur();
                }
                cam1.goTo({x:0, y:0, ratio:1, angle:0});
                cam2.goTo({x:0, y:0, ratio:1, angle:0});
                refreshGraphs();
                //myGraph.refresh();
            }
            
            
            function initTabs() {
                // Grab the tab links and content divs from the page
                var tabListItems = document.getElementById('tabs').childNodes;
                for ( var i = 0; i < tabListItems.length; i++ ) {
                    if ( tabListItems[i].nodeName == "LI" ) {
                        var tabLink = getFirstChildWithTagName( tabListItems[i], 'A' );
                        var id = getHash( tabLink.getAttribute('href') );
                        tabLinks[id] = tabLink;
                        contentDivs[id] = document.getElementById( id );
                    }
                }

                // Assign onclick events to the tab links, and
                // highlight the first tab
                var i = 0;

                for ( var id in tabLinks ) {
                    tabLinks[id].onclick = showTab;
                    tabLinks[id].onfocus = function() { this.blur() };
                    if ( i == 0 ) tabLinks[id].className = 'selected';
                    i++;
                }

                // Hide all content divs except the first
                var i = 0;

                for ( var id in contentDivs ) {
                    if ( i != 0)
                        contentDivs[id].className = 'tabContent hide';
                    i++;
                }
            }
            
            
            
            function showTab() {
                var selectedId = getHash( this.getAttribute('href') );
                
                //if(!(selectedId == this.getAttribute('previouslySelected'))){
                
                //page marker 2
                // Highlight the selected tab, and dim all others.
                // Also show the selected content div, and hide all others.
                for ( var id in contentDivs ) {
                    if ( id == selectedId ) {
                        tabLinks[id].className = 'selected';
                        contentDivs[id].className = 'tabContent';
                    } else {
                        tabLinks[id].className = '';
                        contentDivs[id].className = 'tabContent hide';
                    }
                }
                refreshGraphs();
                refreshGraphs();
                
                //temporary fix for nodes appearing on top of each other when splti tab is first clicked
//                if(selectedId !== previouslySelected && selectedId !== "table" && previouslySelected !== "table"){
//                    refreshGraphs();
//                    if(previouslySelected === "split" && selectedId === "graph"){
//                    refreshGraphs();
//                    }
//                }
//                previouslySelected = selectedId;
                //refreshGraphs();
                
                //this.previouslySelected = selectedId;
                
                // Stop the browser following the link
                return false;
            //}
            }
            
            
            
            function getFirstChildWithTagName( element, tagName ) {
                for ( var i = 0; i < element.childNodes.length; i++ ) {
                    if ( element.childNodes[i].nodeName == tagName ) 
                        return element.childNodes[i];
                }
            }
            
            
            
            function getHash( url ) {
                var hashPos = url.lastIndexOf ( '#' );
                return url.substring( hashPos + 1 );
            }
            
            
            
            if (document.addEventListener) {
        document.addEventListener('contextmenu', function(e) {
            //here you draw your own menu
            e.preventDefault();
        }, false);
    } else {
        document.attachEvent('oncontextmenu', function() {
            window.event.returnValue = false;
        });
    }
            
            
            
            
            
            
            
    //add code here to select the newly created json file(from the uploaded file) as the file for the graph to be displayed.
        if(successfulUpload === true){
            document.getElementById("all-tabs").style.display = "block";
            document.getElementById("clickableListDiv").style.display = "block";
        } else{
            document.getElementById("all-tabs").style.display = "none";
            document.getElementById("clickableListDiv").style.display = "none";
            //document.getElementById("dictionaryFrame1").style.display = "none";
            //document.getElementById("dictionaryFrame2").style.display = "none";
        }
        
        
        
        
    
    
    
    //contextMenu
    
    var menu = document.querySelector("#context-menu");
    var menuState = 0;
    var active = "context-menu--active";
    var contextMenuItemClassName = "context-menu__item";
    var contextMenuLinkClassName = "context-menu__link";
    
    var clickCoords;
    var clickCoordsX;
    var clickCoordsY;
    var menuWidth;
    var menuHeight;
    var menuPosition;
    var menuPositionX;
    var menuPositionY;

    var windowWidth;
    var windowHeight;
  
    var rightClickedWord;
    var currentComponentNum = "1";
    
    var cursorX;
    var cursorY;
    document.onmousemove = function(e){
        cursorX = e.pageX;
        cursorY = e.pageY;
    };
    
    
    function toggleMenuOn() {
        if ( menuState !== 1 ) {
            menuState = 1;
            menu.classList.add(active);
        }
    }
    function toggleMenuOff() {
        if ( menuState !== 0 ) {
            menuState = 0;
            menu.classList.remove( active );
        }
    }
    function initializeMenu(){
        //contextListener();
        clickListener();
        keyupListener();
    }
    function OpenDictionary(url_dic){
        var word = url_dic+rightClickedWord.toString();
       var xhr = createAjaxRequest();
        if (!xhr)
            throw 'XMLHttpRequest not supported, cannot load the file.';
        xhr.open('POST', "Dictionaries?word="+word+"", true);
         xhr.onreadystatechange = function(){
            if(xhr.readyState === 4){
             document.getElementById("context-menu").style.display = "none";
           var commentsBox =  document.getElementById("CommentsBox");
           var responseObject = document.createElement("div");
        
          commentsBox.innerHTML = xhr.responseText;
          
            }
        };
         xhr.send(word);
         
       
        }
            
    function ChangePolarity(Updatedpolarity){
        var selectedWord =rightClickedWord.toString();
        var originalPolarity ;
         myGraph.graph.nodes().forEach(function(n){
             var id = n.id.toString();
                          if(selectedWord === id)
                          {
                             originalPolarity= n.polarity; 
                              n.polarity = Updatedpolarity;
                              myGraph.refresh();
                              
                          }
                          
                        });
                        
                var word = rightClickedWord.toString()+";"+Updatedpolarity+";"+originalPolarity;
                  
         var xhr = createAjaxRequest();
        if (!xhr)
            throw 'XMLHttpRequest not supported, cannot load the file.';
        xhr.open('POST', "RemoveWord?word="+word+"", true);
         xhr.onreadystatechange = function(){
            if(xhr.readyState === 4){
             document.getElementById("context-menu").style.display = "none";
          
            }
        };
         xhr.send(word);
     
                             
                    }
                    
                        

   
    function clickInsideElement( e, className ) {
    var el = e.srcElement || e.target;
    
    if ( el.classList.contains(className) ) {
      return el;
    } else {
      while ( el = el.parentNode ) {
        if ( el.classList && el.classList.contains(className) ) {
          return el;
        }
      }
    }

    return false;
  }
    
      function clickListener() {
        document.addEventListener( "click", function(e) {
      var clickeElIsLink = clickInsideElement( e, contextMenuLinkClassName );
      
      if ( clickeElIsLink ) {
        //e.preventDefault();
    
        menuItemListener( clickeElIsLink);
      } else {
        var button = e.which || e.button;
        if ( button === 1 ) {
          toggleMenuOff();
        }
      }
    });
      }
      
    function setSearchedWord(word){
        rightClickedWord = word;
    }
    
    function menuItemListener( link  ) {
       if(link.getAttribute("data-type") === "dictionary"){
          
           }else if(link.getAttribute("data-type") === "removeWord" && editingMode === 1){
            //removeWord(rightClickedWord);
            
        }else if(link.getAttribute("data-type") === "changePolarity"){
            
            removeWord(rightClickedWord);
        }else if(link.getAttribute("data-type") === "suggestSense"){
            
        }else if(link.getAttribute("data-type") === "subMenu"){
          document.getElementById("Dictionaries").style.display = "block";
                     
         }else if(link.getAttribute("data-type") === "Polarity"){
          document.getElementById("PolarityList").style.display = "block";
                     
         }
         
       // toggleMenuOff();
    }

      /**
       * Listens for keyup events.
       */
      function keyupListener() {
        window.onkeyup = function(e) {
            if ( e.keyCode === 27 ) {
              toggleMenuOff();
            }
        };
      }
      
      /**
   * Positions the menu properly.
   * 
   * @param {Object} e The event
   */
  function positionMenu() {
    clickCoordsX = cursorX;
    clickCoordsY = cursorY;

    menuWidth = menu.offsetWidth + 4;
    menuHeight = menu.offsetHeight + 4;

    windowWidth = window.innerWidth;
    windowHeight = window.innerHeight;

    if ( (windowWidth - clickCoordsX) < menuWidth ) {
      menu.style.left = windowWidth - menuWidth + "px";
    } else {
      menu.style.left = clickCoordsX + "px";
    }

    if ( (windowHeight - clickCoordsY) < menuHeight ) {
      menu.style.top = windowHeight - menuHeight + "px";
    } else {
      menu.style.top = clickCoordsY + "px";
    }
  }
      
    initializeMenu();
    
    
    
    
    
    
    //sigma code
    
    sigma.classes.graph.addMethod('neighbors', function(nodeId) {
                var k,
                neighbors = {},
                index = this.allNeighborsIndex[nodeId] || {};

                for (k in index)
                    neighbors[k] = this.nodesIndex[k];
                return neighbors;
            });
            
            
            myGraph = new sigma({settings: {
                    defaultLabelColor: '#000000',
                    defaultNodeColor: '#6a7a7a',
                    defaultEdgeColor: '#6a7a7a',
                    defaultEdgeLabelColor: '#e71e12',
                    edgeColor: 'default',
                    labelColor: 'default',
                    edgeLabelColor:'edge',
                    labelThreshold: '1',
                    sideMargin: 1.5,
                    drawEdgeLabels: true,
                    minEdgeSize:'.25',
                    maxEdgeSize:'3.5'
                    
                }});
            cam1 = myGraph.addCamera();
            cam2 = myGraph.addCamera();
            
            
            //this function will update the visible nodes based on the checkboxes and what nodes would be visible if there were no checkboxes
            function refreshGraphs(){
                myGraph.graph.nodes().forEach(function(n){
                    if(n.type === 'word'){
                        if(n.checkbox){ 
                            n.checked = n.checkbox.checked;
                        }else{
                            n.checked = true;
                        }

                        if(!n.checked){
                            n.hidden = true;
                        }else if(n.checked && n.nonCheckHidden){
                            n.hidden = true;
                        }else if(n.checked && !n.nonCheckHidden){
                            n.hidden = false;
                        }
                    }else if(n.type === 'synset'){
                        var connectedToWord = false;
                        myGraph.graph.edges().forEach(function(e){
                            if(e.target === n.id){
                                myGraph.graph.nodes().forEach(function(n2){
                                    if(e.source === n2.id && n2.checked && !n2.nonCheckHidden){
                                        connectedToWord = true;
                                    }
                                });
                            }    
                        });
                        if(!connectedToWord){
                            n.hidden = true;
                        }else if(connectedToWord && n.nonCheckHidden){
                            n.hidden = true;
                        }else if(connectedToWord && !n.nonCheckHidden){
                            n.hidden = false;
                        }
                    }
                });
                myGraph.refresh();
            }
            
//            //this function will refresh the graphs and update the checked status of nodes when a checkbox is changed
//            function checkboxRefreshGraphs(){
//                myGraph.graph.nodes().forEach(function(n){
//                   if(n.checkbox){ 
//                       n.checked = n.checkbox.checked;
//                   }else{
//                       n.checked = true;
//                   }
//                   if(!n.checked){
//                       n.hidden = true;
//                   }
//                });
//                myGraph.refresh();
//            }
            
//            myGraph.graph.addNode({
//            id: 'n',
//            label: 'Node ',
//            x: 20,
//            y: 20,
//            size: 4 + (3 * 5) | 0
//            });
            
            var renderer1 = {
                container: document.getElementById('sigma-container'),
                type: 'canvas',
                camera: cam1,
                settings: {
                    defaultLabelColor: '#000000',
                    defaultEdgeLabelColor: '#000000',
                    edgeColor: 'default',
                    labelColor: 'default',
                    edgeLabelColor:'edge'
                    
                }
            };
            myGraph.addRenderer(renderer1);

            var renderer2 = {
                container: document.getElementById('sigma-container2'),
                type: 'canvas',
                camera: cam2,
                settings: {
                    defaultLabelColor: '#000000',
                    defaultEdgeLabelColor: '#000000',
                    edgeColor: 'default',
                    labelColor: 'default',
                    edgeLabelColor:'edge'
                }
            };
            myGraph.addRenderer(renderer2);
            myGraph.refresh();

            sigma.parsers.initialJson(
                //jsonFileName
                "json/temp.json",
                myGraph,
                function() {
                    myGraph.refresh();
                },
                1
            );
//sigma.parsers.json(
//                jsonFileName,
//                myGraph,
//                function() {
//                    myGraph.refresh();
//                }
//            );
            
            // save original colors of nodes and edges
            myGraph.graph.nodes().forEach(function(n) {
            n.originalColor = n.color;
            //n.originalLabel = n.label;
            });
            myGraph.graph.edges().forEach(function(e) {
            e.originalColor = e.color;
            });

            // color neighbor nodes and the appropriate edges on click
            myGraph.bind('clickNode', function(e) {
                var nodeId = e.data.node.id;
               
                var toKeep = myGraph.graph.neighbors(nodeId);
                toKeep[nodeId] = e.data.node;
                
                if(graphViewSettings.viewStyle === "all"){
                    myGraph.graph.nodes().forEach(function(n) {
                        if (toKeep[n.id]){
                            n.color = n.originalColor;
                            //n.label = n.originalLabel;
                        }
                        else{
                            n.color = '#8a9f9f';
                            //n.label = '';
                        }
                    });

                    myGraph.graph.edges().forEach(function(e) {
                        if (toKeep[e.source] && toKeep[e.target]){
                            e.color = e.originalColor;
                            e.hidden = false;
                            e.showLabel = true;
                        }else{
                            //e.color = '#f1f0ee';
                            //e.color = '#df7973';
                            e.hidden = true;
                            e.showLabel = false;
                        }
                    });
                }else if(graphViewSettings.viewStyle === "progressive"){
                    if(e.data.node.type === "word"){
                        if(!e.data.captor.shiftKey){
                            var allNodes = myGraph.graph.nodes();
                            myGraph.graph.nodes().forEach(function(n){
                                        if(n.type === "synset"){
                                            n.nonCheckHidden = true;
                                            n.hidden = true;
                                        }
                            });
                            myGraph.graph.edges().forEach(function(e){
                                if(e.source === nodeId){
                                    myGraph.graph.nodes().forEach(function(n){
                                        if(n.type === "synset" && e.target === n.id){
                                            n.nonCheckHidden = false;
                                            n.hidden = false;
                                            e.hidden = false;
                                        }
                                    });
                                }else{
                                    e.hidden = true;
                                }
                            });
                            reindexVisibleSynsetNodes();
                        } else {
                            myGraph.graph.edges().forEach(function(e){
                                if(e.source === nodeId){
                                    myGraph.graph.nodes().forEach(function(n){
                                        if(n.type === "synset" && e.target === n.id){
                                            n.nonCheckHidden = false;
                                            n.hidden = false;
                                            e.hidden = false;
                                        }
                                    });
                                }
                            });
                            reindexVisibleSynsetNodes();
                        }
                    }else if(e.data.node.type === "synset"){
                        if(!e.data.captor.shiftKey){
                            myGraph.graph.nodes().forEach(function(n){
                                        if(n.type === "synset"){
                                            n.nonCheckHidden = true;
                                            n.hidden = true;
                                            if(n.id === nodeId){
                                            n.nonCheckHidden = false;
                                                n.hidden = false;
                                            }
                                        }
                                    });
                            myGraph.graph.edges().forEach(function(e){
                                if(e.target === nodeId){
                                    e.hidden = false;
                                }else{
                                    e.hidden = true;
                                }
                            });
                        }else{
                            myGraph.graph.edges().forEach(function(e){
                                if(e.target === nodeId){
                                    e.hidden = false;
                                }
                            });
                        }
                    }
                }
                
                
                toggleMenuOff();


                // update the graph
                myGraph.refresh();
            });

            // On stage click returns the original colors of the nodes and edges
            myGraph.bind('clickStage', function(e) {
                if(graphViewSettings.viewStyle === "all"){
                    myGraph.graph.nodes().forEach(function(n) {
                        n.color = n.originalColor;
                        //n.label = n.originalLabel;
                    });

                    myGraph.graph.edges().forEach(function(e) {
                        e.color = e.originalColor;
                        e.hidden = false;
                        e.showLabel = false;
                    });

                    toggleMenuOff();
                }else if(graphViewSettings.viewStyle === "progressive"){
                    myGraph.graph.nodes().forEach(function(n) {
                        if(n.type === "word"){
                            n.nonCheckHidden = false;
                            n.hidden = false;
                        }else if(n.type === "synset"){
                            n.nonCheckHidden = true;
                            n.hidden = true;
                        }
                    });
                    
                    myGraph.graph.edges().forEach(function(e) {
                        e.hidden = true;
                    });
                }
                
                // update the graph
                //myGraph.refresh();
                refreshGraphs();
            });
            
            myGraph.bind('rightClickNode', function(e) {
                    var rightClickedWord = e.data.node.id;
                    //display contextMenu here
                 
                    toggleMenuOn();
                    
//                    var posx = 0;
//                    var posy = 0;
//
//                    if (!e) var e = window.event;
//
//                    if (e.pageX || e.pageY) {
//                      posx = e.pageX;
//                      posy = e.pageY;
//                    } else if (e.clientX || e.clientY) {
//                      posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
//                      posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
//                    }
                    
                    positionMenu();
                    setSearchedWord(rightClickedWord);
                 
                myGraph.refresh();
            });
            
//            myGraph.addCamera("cam1");
//            myGraph.addRenderer({
//                container: document.getElementById("sigma-container2"),
//                camera: 'cam1',
//                /* rest of settings */
//            });
//            myGraph.refresh();

              
              //not using this function to avoid a double iteration of the nodes
//            function hideNodes(hidingNodes){
//                myGraph.graph.nodes().forEach
//            }
            function synsetsOfWordNewXCoords(desiredSynsetNodes, wordIndex){
                myGraph.graph.nodes().forEach(function(n){
                    if(n.hidden && n.type === "synset"){
                        n.origX = n.x;
                        //n.x = ;
                    }
                });
            }


//clickable list code
function generateClickableList(componentJson){
    JSONComponents = componentJson;
    for(var k =0; k<componentJson.numComponents;k++){
        var newLi = document.createElement("LI");
        newLi.id = "item" + (k+1);
        newLi.class = "clickableListItem";
        
        newLi.classList.add("dehighlight-text");
        
        var wordList = document.createElement("UL");
        wordList.id = "words" + (k+1);
        wordList.classList.add("wordList");
        wordList.classList.add("dehighlight-text");
        wordList.style.display = "none";

        var wordsContained = "";
       var  currentComponent = componentJson["component" + (k+1)];
        var componentWords = currentComponent.words;
        for(var j = 0; j< componentWords.length - 1; j++){
            var theWord = componentWords[j];
            wordsContained += theWord + ", ";
            
            setupWordLi(theWord, wordList);
        }
        
        var lastWord = componentWords[componentWords.length - 1];
        
        wordsContained += lastWord;
        
        setupWordLi(lastWord, wordList);
        
        newLi.appendChild(wordList);

        newLi.setAttribute("title", wordsContained);
        newLi.setAttribute("z-index", "50");
        



        var listContentLink = document.createElement("A");
        //listContentLink.href = "#error" + (k+1);
        //listContentLink.setAttribute("title", "word1, word2, word3, word4");
        listContentLink.setAttribute("data-errorStatus", "unviewed");
        //if(k === componentJson.numComponents-1){
        //    listContentLink.className = "lastClickableListLink";
        //}else{
            listContentLink.className = "clickableListContentLink";
        //}
        listContentLink.id = "component" + (k+1);
        if(wordsContained.indexOf(',') !== -1)
        {
        var firstWord = wordsContained.substring(0, wordsContained.indexOf(','));
    }
    else
    {
        var firstWord = wordsContained.toString();
    }
        var listContentLinkText = document.createTextNode(firstWord);

        var listContentImage = document.createElement("IMG");
        listContentImage.style.height = "20px";
        listContentImage.style.width = "20px";
        listContentImage.src = "images/solidCircle.png";

    //                var listContentFiller = document.createElement("SPAN");
    //                var filler = document.createTextNode("                                           ");


        listContentLink.appendChild(listContentLinkText);
        listContentLinkText.parentNode.insertBefore(listContentImage, listContentLinkText);
        //listContentLink.appendChild(listContentImage);

    //                listContentFiller.appendChild(filler);
    //                listContentLinkText.parentNode.insertBefore(listContentFiller, listContentLinkText.nextSibling);

        newLi.insertBefore(listContentLink, newLi.firstChild);
        //newLi.appendChild(listContentLink);
        document.getElementById("clickableList").appendChild(newLi);



        listContentLink.onclick = function(){
            showCheckboxes(this.parentNode);
            displayError(this);
           currentComponentNum = this.id.replace("component", "");;
            var elements = document.getElementsByClassName("selected");
            for(var k = 0; k< elements.length; k++){
                //if(!(parseInt(elements[k].id.substring(9)) === fullJson.numComponents)){
                    elements[k].className = "clickableListContentLink";
                //}else{
                    //elements[k].className = "clickableListContentLink";
                //}
            }
            this.className = "selected";
        };
        listContentLink.onfocus = function() { this.style.color = gray; };



        $(document).mousemove(
        function(e){
            pageX = e.pageX;
            pageY = e.pageY;
        });


        var toolTipX, toolTipY;
        $(newLi)
            .each(
                function(){
                    $(this).attr('data-tooltip',$(this).attr('title')).removeAttr('title');
                })
            .hover(
            function(){
                toolTipX = pageX;
                toolTipY = pageY ;
                var tip = $('<div />')
                    .addClass('tooltip')
                    .text($(this).attr('data-tooltip'))
                    .css({
                        'position' : 'absolute',
                        'top' : toolTipY,
                        'left' : toolTipX,
                        'z-index': '50'
                    });
                $(tip).appendTo($(this));
                $(this).mousemove(
                    function(){
                        toolTipX = pageX + 100;
                        toolTipY = pageY + 25;
                        $('.tooltip').css(
                            {
                                'top' : toolTipY,
                                'left' : toolTipX
                            });
                    });
            },
            function(){
                $('.tooltip').remove();
            }
                    );
    }
}


function displayError(clickedElement){
    //display the desired error graph
    
    myGraph.graph.clear();
    //document.getElementById("headingMessage").innerHTML = "<p>" +jsonText + "</p>";
    var elementID = clickedElement.getAttribute("id");
    var IDNum = elementID.substring(9);
    sigma.parsers.json(
                //jsonFileName
        "json/temp.json",
                myGraph,
                function() {
                    if(graphViewSettings.viewStyle === "progressive"){
                        myGraph.graph.edges().forEach(function(e){
                            e.hidden = false;
                        });
                        myGraph.graph.nodes().forEach(function(n){
                            if(n.type === "synset"){
                                n.nonCheckHidden = true;
                                n.hidden = true;
                            }else if(n.type === "word"){
                                n.nonCheckHidden = false;
                                n.hidden = false;
                            }
                        });
                    }else{
                        myGraph.graph.nodes().forEach(function(n){
                            n.nonCheckHidden = false;
                            n.hidden = false;
                        });
                    }
                    if(graphViewSettings.orientation === "vertical"){
                        switchXYCoords();
                    }
                    var checkboxes = document.getElementById("words" + IDNum).getElementsByTagName("input");
                    myGraph.graph.nodes().forEach(function(n){
                            if(n.type === "word"){
                                var correspondingCheckbox;
                                for(var k = 0; k < checkboxes.length; k++){
                                    if(checkboxes[k].value === n.id){
                                        correspondingCheckbox = checkboxes[k];
                                        break;
                                    }
                                }
                                if(typeof correspondingCheckbox === 'object'){
                                    n.checkbox = correspondingCheckbox;
                                    n.checked = n.checkbox.checked;
                                }else{
                                    n.checkbox = null;
                                    n.checked = true;
                                }
                                //now i need to make this update when a checkbox is changed
                            }
                        });
                    //myGraph.refresh();
                    //refreshGraphs();
                    resetCameras();
                    
                },
                IDNum
            );
    //resetCameras();
    
    

    
    
    

    
    
    //change the image of the list element
    if(clickedElement.getAttribute("data-errorStatus") == "unviewed"){
        var errorIcon = clickedElement.getElementsByTagName('img')[0];
        errorIcon.setAttribute("src", "images/circle.png");
        clickedElement.setAttribute("data-errorStatus", "viewed");
    } else if(clickedElement.getAttribute("data-errorStatus") == "viewed"){
        var errorIcon = clickedElement.getElementsByTagName('img')[0];
        errorIcon.setAttribute("src", "images/check.png");
        clickedElement.setAttribute("data-errorStatus", "unviewed");
    }
    
}
    
    
    
    //functions to enable and disable editing
    function enableEditing(){
        editingMode = 1;
        addRemoveWordToContextMenu();
        switchEditButtons();
    }
    function disableEditing(){
        editingMode = 0;
        removeRemoveWordFromContextMenu();
        switchEditButtons();
    }
    
    //helper function to setup the list element for each word and add the checkbox and text
    function setupWordLi(theWord, wordList){
            var wordLi = document.createElement("LI");
            wordLi.id = theWord.toLowerCase() + "Li";
            wordLi.class = "wordListItem";
            
            var theCheckbox = createWordCheckbox(theWord);
            
            var wordNode = document.createTextNode(theWord);
            
            wordLi.appendChild(wordNode);
            wordNode.parentNode.insertBefore(theCheckbox, wordNode);
            
            wordList.appendChild(wordLi);
            return wordLi;
        }
    
    //helper function to show the corresponding checkboxes (and hide the others)
    function showCheckboxes(containingLi){
        var clickableListWordLists = containingLi.parentNode.getElementsByTagName("ul");
        for(var k = 0; k < clickableListWordLists.length; k++){
            clickableListWordLists[k].style.display = "none";
        }
        containingLi.getElementsByTagName("ul")[0].style.display = "block";
    }
        
    
    //helper functions for enabling and disabling editing
    //adds the removeWord option to the context menu
    function addRemoveWordToContextMenu(){
        var contextMenu = document.getElementById("context-menu");
        var contextMenuUL = contextMenu.children[0];
        var removeWordListItem = document.createElement("LI");
        removeWordListItem.className = "context-menu__item";
        removeWordListItem.id = "removeWordOption";
        var removeWordLink = document.createElement("A");
        removeWordLink.href = "#";
        removeWordLink.className = "context-menu__link";
        removeWordLink.setAttribute("data-type", "removeWord");
        var removeWordText = document.createTextNode("Remove Word");
        removeWordLink.appendChild(removeWordText);
        removeWordListItem.appendChild(removeWordLink);
        contextMenuUL.appendChild(removeWordListItem);
    }
    //removes the removeWord option from the context menu
    function removeRemoveWordFromContextMenu(){
        var removeWordElement = document.getElementById("removeWordOption");
        removeWordElement.parentNode.removeChild(removeWordElement);
    }
    //changes the edit buttons on the page from enable editing to disable editing and vice versa
    function switchEditButtons(){
        var editButtons = document.getElementsByClassName("edit-button");
        if(editButtons[0].name === "enableEditing"){
            for(var k = 0; k < editButtons.length; k++){
                var button = editButtons[k];
                button.name = "disableEditing";
                button.title = "Enter View Only Mode";
                button.setAttribute("onClick", "disableEditing()");
                button.childNodes[0].nodeValue = "Disable Editing";
            }
        }else{
            for(var k = 0; k < editButtons.length; k++){
                var button = editButtons[k];
                button.name = "enableEditing";
                button.title = "Enter Editing Mode";
                button.setAttribute("onClick", "enableEditing()");
                button.childNodes[0].nodeValue = "Enable Editing";
            }
        }
    }
    
    //removes the given word from the data on the server using ajax and 
    function removeWord(word){
        //or potentially add the word to a list and then have a button at the end that says remove all the selected words and rerun the test
        
        var xhr = createAjaxRequest();
        if (!xhr)
            throw 'XMLHttpRequest not supported, cannot load the file.';
        xhr.open('POST', "RemoveWord?word="+word+"", true);
        
        xhr.onreadystatechange = function(){
            if(xhr.readyState === 4){
                
            }
        };
        xhr.send(word);
    }
    
    //creates an XMLHttpRequest
    function createAjaxRequest(){
        if (window.XMLHttpRequest)
            return new XMLHttpRequest();

        var names,
        i;

        if (window.ActiveXObject) {
          names = [
            'Msxml2.XMLHTTP.6.0',
            'Msxml2.XMLHTTP.3.0',
            'Msxml2.XMLHTTP',
            'Microsoft.XMLHTTP'
          ];

        for (i in names)
            try {
              return new ActiveXObject(names[i]);
            } catch (e) {}
        }

        return null;
    }


