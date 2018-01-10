/*
 * set these functions to be called when combobox option is selected like this:
 * 
 * <select id="aa" name="aa"> 
<option value="">Please select</option>
<option value="daily">daily</option>
<option value="monthly">monthly</option>
</select>

window.onload=function() {
  document.getElementById("aa").onchange=function() {
    var period = this.value;
    if (period=="") return; // please select - possibly you want something else here

    var report = "script/"+((period == "daily")?"d":"m")+"_report.php";
    loadXMLDoc(report,'responseTag');
    document.getElementById('responseTag').style.visibility='visible';
    document.getElementById('list_report').style.visibility='hidden';
    document.getElementById('formTag').style.visibility='hidden'; 
  } 
}


or originally set the onchange by doing:
<select name="aa" onchange="report(this.value)"> 
<option value="">Please select</option>
<option value="daily">daily</option>
<option value="monthly">monthly</option>
</select>
function report(period) {
  if (period=="") return; // please select - possibly you want something else here

  var report = "script/"+((period == "daily")?"d":"m")+"_report.php";
  loadXMLDoc(report,'responseTag');
  document.getElementById('responseTag').style.visibility='visible';
  document.getElementById('list_report').style.visibility='hidden';
  document.getElementById('formTag').style.visibility='hidden'; 
} 
 */




/* global myGraph, graphViewSettings */

//function updatePolarityDisplay(sigmaInstance, selection){
//    
//    //sigmaInstance.polarityDisplay = selection;
//    
////    if(selection === "basic"){
////        
////    }else if(selection === "color"){
////        
////    }else if(selection === "both"){
////        
////    }else{
////        
////    }
//        
//}


//Update the property of a sigma insatnce. To be called when a graph viewing combobox is changed.
//After the property is updated refresh the graph to reload.
//The canvas drawing of the graph should have conditional statements based on the value of these property names.
//
//For example when drawing nodes first check if a node is a word node. If it is a word node, check the word polarity. 
//Then check the polarityDisplay property to and based on its value have a different way to draw the node(using canvas methods).
function updateGraphViewSetting( selection, propertyName, extraFunction){
    //it may be easier/possible to just do sigma instead of a specific sigmaInstance, since there will only be one sigmaInstance for the life of the webpage
    graphViewSettings[propertyName] = selection;
    if(extraFunction){
        extraFunction(selection);
    }
    refreshGraphs();
    //myGraph.refresh();
    
}

function viewStyleChanged(styleChangedTo){
    if(styleChangedTo === "all"){
        myGraph.graph.nodes().forEach(function(n){
                            n.hidden = false;
                            n.nonCheckHidden = false;
                            n.x = n.ogX;
                            n.y = n.ogY;
                        });
        myGraph.graph.edges().forEach(function(e){
                            e.hidden = false;
                            e.showLabel = false;
                        });
    }else if(styleChangedTo === "progressive"){
        myGraph.graph.edges().forEach(function(e){
                            e.hidden = true;
                            e.showLabel = true;
                        });
        myGraph.graph.nodes().forEach(function(n){
                            if(n.type === "synset"){
                                n.hidden = true;
                                n.nonCheckHidden = true;
                            }else{
                                n.hidden = false;
                                n.nonCheckHidden = false;
                            }
                        });
        reindexVisibleSynsetNodes();
    }
    resetCameras();
}

function changeOrientation(orientation){
    if(orientation === "horizontal"){
        switchXYCoords();
    }else if(orientation === "vertical"){
        switchXYCoords();
    }
    refreshGraphs();
}

function switchXYCoords(){
    var temp;
    myGraph.graph.nodes().forEach(function(n){
            temp = n.x;
            n.x = n.y;
            n.y = temp;
        });
}

function reindexVisibleSynsetNodes(){
    var xCoord = 0;
    var yCoord = 0;
    if(graphViewSettings.orientation === "horizontal"){
    myGraph.graph.nodes().forEach(function(n){
                            if(n.type === "synset"){
                                if(!n.hidden){
                                    n.x = xCoord;
                                    xCoord += 2;
                                }else{
                                    n.x = 0;
                                }
                            }
                        });
    }else if(graphViewSettings.orientation === "vertical"){
        myGraph.graph.nodes().forEach(function(n){
                            if(n.type === "synset"){
                                if(!n.hidden){
                                    n.y = yCoord;
                                    yCoord += 2;
                                }else{
                                    n.y = 0;
                                }
                            }
                        });
    }
    
}

//maybe this is not needed and instead it should always show the user lexicon polarities
//function updateShownPolaritySource(sigmaInstance, selection){
//    if(selection === "wordnet"){
//        
//    }else if(selection === "userLexicon"){
//        
//    }else{
//        
//    }
//}


