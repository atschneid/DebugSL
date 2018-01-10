
var fullJson

/* global sigma */
;(function(undefined) {
  'use strict';

  if (typeof sigma === 'undefined')
    throw 'sigma is not declared';

  // Initialize package:
  sigma.utils.pkg('sigma.parsers');
  sigma.utils.pkg('sigma.utils');

  /**
   * Just an XmlHttpRequest polyfill for different IE versions.
   *
   * @return {*} The XHR like object.
   */
  sigma.utils.xhr = function() {
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
  };

  /**
   * This function loads a JSON file and creates a new sigma instance or
   * updates the graph of a given instance. It is possible to give a callback
   * that will be executed at the end of the process.
   *
   * @param  {string}       url      The URL of the JSON file.
   * @param  {object|sigma} sig      A sigma configuration object or a sigma
   *                                 instance.
   * @param  {?function}    callback Eventually a callback to execute after
   *                                 having parsed the file. It will be called
   *                                 with the related sigma instance as
   *                                 parameter.
   */
//  sigma.parsers.json = function(url, sig, callback) {
//    var graph,
//        xhr = sigma.utils.xhr();
//
//    if (!xhr)
//      throw 'XMLHttpRequest not supported, cannot load the file.';
//
//    xhr.open('POST', url, true);
//    xhr.onreadystatechange = function() {
//      if (xhr.readyState === 4 ) {
//        fullJson = JSON.parse(xhr.responseText);
//        graph = fullJson.component1;
//        
//        //graph = JSON.parse(xhr.responseText);
//        
////        var fullJson = JSON.parse(xhr.responseText);
////        document.getElementById("headingMessage").innerhtml = "<p>" + fullJson.dog + "</p>";
//
//        // Update the instance's graph:
//        if (sig instanceof sigma) {
//          sig.graph.clear();
//          sig.graph.read(graph);
//
//        // ...or instantiate sigma if needed:
//        } else if (typeof sig === 'object') {
//          sig.graph = graph;
//          sig = new sigma(sig);
//
//        // ...or it's finally the callback:
//        } else if (typeof sig === 'function') {
//          callback = sig;
//          sig = null;
//        }
//
//        // Call the callback if specified:
//        if (callback)
//          callback(sig || graph);
//      }
//    };
//    xhr.send();
//  };
  
  
  sigma.parsers.initialJson = function(url, sig, callback, componentNum) {
    var graph,
        xhr = sigma.utils.xhr();
    //var fullJson = {};

    if (!xhr)
      throw 'XMLHttpRequest not supported, cannot load the file.';

    xhr.open('POST', url, true);
    xhr.onreadystatechange = function() {
      if (xhr.readyState === 4 ) {
        fullJson = JSON.parse(xhr.responseText);
        generateClickableList(fullJson);
        graph = fullJson["component" + componentNum];
        
       
        if (sig instanceof sigma) {
          sig.graph.clear();
          sig.graph.read(graph);

        // ...or instantiate sigma if needed:
        } else if (typeof sig === 'object') {
          sig.graph = graph;
          sig = new sigma(sig);

        // ...or it's finally the callback:
        } else if (typeof sig === 'function') {
          callback = sig;
          sig = null;
        }

        // Call the callback if specified:
        if (callback)
          callback(sig || graph);
      }
    };
    xhr.send();
  };
  
  sigma.parsers.json = function(url, sig, callback, componentNum) {
    var graph,
        xhr = sigma.utils.xhr();
    //var fullJson = {};

    if (!xhr)
      throw 'XMLHttpRequest not supported, cannot load the file.';

    xhr.open('POST', url, true);
    xhr.onreadystatechange = function() {
      if (xhr.readyState === 4 ) {
        fullJson = JSON.parse(xhr.responseText);
        graph = fullJson["component" + componentNum];
        
        //graph = JSON.parse(xhr.responseText);
        
//        var fullJson = JSON.parse(xhr.responseText);
//        document.getElementById("headingMessage").innerhtml = "<p>" + fullJson.dog + "</p>";

        // Update the instance's graph:
        if (sig instanceof sigma) {
          sig.graph.clear();
          sig.graph.read(graph);

        // ...or instantiate sigma if needed:
        } else if (typeof sig === 'object') {
          sig.graph = graph;
          sig = new sigma(sig);

        // ...or it's finally the callback:
        } else if (typeof sig === 'function') {
          callback = sig;
          sig = null;
        }

        // Call the callback if specified:
        if (callback)
          callback(sig || graph);
      }
    };
    xhr.send();
  };
  
 
  
  
 
  sigma.parsers.jsonObject = function(jsonObject, sig, callback,componentNum) {
    var graph,
            
        graph = jsonObject[componentNum];

        if (sig instanceof sigma) {
          sig.graph.clear();
          sig.graph.read(graph);

        // ...or instantiate sigma if needed:
        } else if (typeof sig === 'object') {
          sig.graph = graph;
          sig = new sigma(sig);

        // ...or it's finally the callback:
        } else if (typeof sig === 'function') {
          callback = sig;
          sig = null;
        }

     //  Call the callback if specified:
        if (callback)
          callback(sig || graph);

  };
}).call(this);
