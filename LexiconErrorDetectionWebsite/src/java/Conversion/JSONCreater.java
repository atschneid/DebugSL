package Conversion;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import data.*;

import javax.json.*;
//import JSON writing classes


/**
 *
 * @author nmale_000
 */
public class JSONCreater {
    public JSONCreater(){
    }
    
    public JsonObject convertComponents(ArrayList<Component> convertingComponents){
        int numComponents = convertingComponents.size();
        
        JsonObject compilationJsonObjects;
        JsonObject singleComponentJson;
        
        JsonObjectBuilder compilationComponentsBuilder, singleComponentBuilder;
        
        JsonArray nodes, edges;
        
        ComponentConverter componentConverter;
        
        compilationComponentsBuilder = Json.createObjectBuilder();
        
        for (int k = 1; k <= convertingComponents.size(); k++){
            componentConverter = new ComponentConverter(convertingComponents.get(k-1));
            singleComponentBuilder = Json.createObjectBuilder();
            singleComponentBuilder.add("nodes", componentConverter.getNodes());
            singleComponentBuilder.add("edges", componentConverter.getEdges());
            singleComponentBuilder.add("numWords", componentConverter.getNumWords());
            singleComponentBuilder.add("words", componentConverter.getGraphWords());
            singleComponentJson = singleComponentBuilder.build();
            compilationComponentsBuilder.add("component" + k, singleComponentJson);
        }
        
        compilationComponentsBuilder.add("numComponents", numComponents);
        compilationJsonObjects = compilationComponentsBuilder.build();
        return compilationJsonObjects;
    }
    
    
    
    
    
    
    
    
    
    
}
