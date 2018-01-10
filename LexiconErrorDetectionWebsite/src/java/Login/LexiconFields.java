package Login;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author saroja
 */
public class LexiconFields {
    
    String LexiconFilePath;
    String WordNetPath;
    String PartOfSpeech;
    String ProjectTitle;
    String DateModified;
    String TypeOfPolarity;
    String Comments;
    
        public void setLexiconFilePath (String value) {  
           LexiconFilePath = value;  
        }  

       public String getLexiconFilePath() {  
           return LexiconFilePath;  
        }      
          
        public void setWordNetPath(String value) {  
           WordNetPath = value;  
        }  

       public String getWordNetPath() {  
           return WordNetPath;  
        } 
       
        public void setPartOfSpeech(String value) {  
           PartOfSpeech = value;  
        }  

       public String getPartOfSpeech() {  
           return PartOfSpeech;  
        } 
       
        public void setProjectTitle (String value) {  
           ProjectTitle = value;  
        }  

       public String getProjectTitle() {  
           return ProjectTitle;  
        } 
       
        public void setDateModified(String value) {  
           DateModified = value;  
        }  

       public String getDateModified() {  
           return DateModified;  
        } 
       
        public void setTypeOfPolarity(String value) {  
           TypeOfPolarity = value;  
        }  

       public String getTypeOfPolarity() {  
           return TypeOfPolarity;  
        } 
       
       
       public void setComments(String value) {  
           Comments = value;  
        }  

       public String getComments() {  
           if(Comments == null)
               Comments = "";
           return Comments;  
        } 
    
}
