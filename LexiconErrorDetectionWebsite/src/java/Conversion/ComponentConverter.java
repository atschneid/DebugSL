/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conversion;

import data.*;

import java.util.Map;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author nmale_000
 */
public class ComponentConverter {
    private Component component;
    
    private ArrayList<String> synsetIDsAlreadyAdded = new ArrayList<String>(10);
    private JsonArray graphNodes, graphEdges, graphWords;
    private int numWords = 0;
    
    public ComponentConverter(){
        component = null;
    }
    
    public ComponentConverter(Component convertingComponent){
        component = convertingComponent;
        generateNodesEdges();
    }
    
    
    //generate the nodes and edges of the graph
    public final void generateNodesEdges(){
        JsonArrayBuilder compilationNodesBuilder, compilationEdgesBuilder, compilationWordsBuilder;
        
        JsonObject node;
        JsonArray nodes;
        
        JsonObject edge;
        JsonArray edges;
        
        JsonArray words;
        
        compilationNodesBuilder = Json.createArrayBuilder();
        compilationEdgesBuilder = Json.createArrayBuilder();
        compilationWordsBuilder = Json.createArrayBuilder();
        
        String lowerCaseWord, capitalizedWord;
        
        Polarity polarity;
        String wordPolarity;
        
        Map<Synset, Integer> synsetMap;
        
        String synsetID, gloss;
        
        
        String edgeID, edgeLabel, source, target;
        int size;
        
        
        int wordXVal = 2;
        int synsetXVal = 0;
        //look through all words in the component, create the word nodes. Then for each word add the edges and corresponding synsets if not already added
        for(Word word: component.getAllWords()){
            lowerCaseWord = word.getWord().toLowerCase();
            capitalizedWord = lowerCaseWord.substring(0, 1).toUpperCase() + lowerCaseWord.substring(1);
            
            compilationWordsBuilder.add(capitalizedWord);
            
            polarity = word.getPolarity();
            if(null != polarity)switch (polarity) {
                case POSITIVE:
                    wordPolarity = "positive";
                    break;
                case NEGATIVE:
                    wordPolarity = "negative";
                    break;
                default:
                    wordPolarity = "neutral";
                    break;
            }else{
                wordPolarity = null;
            }
            
            node = generateNode(lowerCaseWord, capitalizedWord, wordXVal, 0, 3, "word", wordPolarity);
            compilationNodesBuilder.add(node);
            numWords++;
            
            wordXVal += 2;
            
            synsetMap = word.getFrequenciesBySynsetRelateTo();
            
            
            for(Synset s: synsetMap.keySet()){
                synsetID = s.getId();
                gloss = s.getGloss();
                if(!synsetAlreadyAdded(s)){
                    node = generateNode(synsetID, gloss, synsetXVal, 8, 3, "synset");
                    compilationNodesBuilder.add(node);
                    synsetIDsAlreadyAdded.add(synsetID);
                    synsetXVal += 2;
                }
                
                edgeID = lowerCaseWord + synsetID;
                edgeLabel = synsetMap.get(s).toString();
                source = lowerCaseWord;
                target = synsetID;
                size = determineSize(edgeLabel);
                
                edge = generateEdge(edgeID, edgeLabel, source, target, size);
                compilationEdgesBuilder.add(edge);
            }
        }
        
        nodes = compilationNodesBuilder.build();
        edges = compilationEdgesBuilder.build();
        words = compilationWordsBuilder.build();
        graphNodes = nodes;
        graphEdges = edges;
        graphWords = words;
    }
    
    
    
    //generates a node(JsonObject) with the property values passed as parameters
    private JsonObject generateNode(String id, String label, int xVal, int yVal, int size, String type){
        JsonObjectBuilder nodeBuilder = Json.createObjectBuilder();
        JsonObject node;
        
        nodeBuilder.add("id", id);
        nodeBuilder.add("label", label);
        nodeBuilder.add("x", xVal);
        nodeBuilder.add("y", yVal);
        nodeBuilder.add("ogX", xVal);
        nodeBuilder.add("ogY", yVal);
        nodeBuilder.add("size", size);
        nodeBuilder.add("type", type);
        
        node = nodeBuilder.build();
        return node;
    }
    //generates a node(JsonObject) with the property values passed as parameters
    private JsonObject generateNode(String id, String label, int xVal, int yVal, int size, String type, String polarity){
        JsonObjectBuilder nodeBuilder = Json.createObjectBuilder();
        JsonObject node;
        
        nodeBuilder.add("id", id);
        nodeBuilder.add("label", label);
        nodeBuilder.add("x", xVal);
        nodeBuilder.add("y", yVal);
        nodeBuilder.add("ogX", xVal);
        nodeBuilder.add("ogY", yVal);
        nodeBuilder.add("size", size);
        nodeBuilder.add("type", type);
        nodeBuilder.add("polarity", polarity);
        
        node = nodeBuilder.build();
        return node;
    }
    
    //generates an edge(JsonObject) with the property values passed as parameters
    private JsonObject generateEdge(String id, String label, String source, String target, int size){
        JsonObjectBuilder edgeBuilder = Json.createObjectBuilder();
        JsonObject edge;
        
        edgeBuilder.add("id", id);
        edgeBuilder.add("label", label);
        edgeBuilder.add("source", source);
        edgeBuilder.add("target", target);
        edgeBuilder.add("size", size);
        
        edge = edgeBuilder.build();
        return edge;
    }
    
    private boolean synsetAlreadyAdded(Synset synset){
        String synsetID = synset.getId();
        for(String s: synsetIDsAlreadyAdded){
            if(s.equals(synsetID)){
                return true;
            }
        }
        return false;
    }
    
    private int determineSize(String freqString){
        int freq = Integer.parseInt(freqString);
        int returningSize;
        if(freq <= 50){
            returningSize = 1;
        }else if(freq <= 1000){
            returningSize = 2;
        }else{
            returningSize = 3;
        }
        return returningSize;    
    }
    
    
    public void setComponent(Component convertingComponent){
        component = convertingComponent;
    }
    
    
    public Component getComponent(){
        return component;
    }
    
    public JsonArray getNodes(){
        return graphNodes;
    }
    
    public JsonArray getEdges(){
        return graphEdges;
    }
    
    public int getNumWords(){
        return numWords;
    }
    
    public JsonArray getGraphWords(){
        return graphWords;
    }
    
    public ArrayList<String> getSynsetIDsAlreadyAdded(){
        return synsetIDsAlreadyAdded;
    }
    
}
