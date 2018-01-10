/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphServlets;

import Conversion.JSONCreater;
import Login.DBConn;
import data.Component;
import edu.mit.jwi.item.POS;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import optimize.Optimize;
import process.WordsComponentBuilderImpl;

/**
 *
 * @author saroja
 */
@WebServlet(name = "Save", urlPatterns = {"/Save"})
public class Save extends HttpServlet {
DBConn dbconn=new DBConn();
     private static final String SAVE_DIR = "uploadedFiles";
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession  session = request.getSession();
   String lexiconPath = session.getAttribute("lexiconPath").toString();
    String wordNetPath = session.getAttribute("wordNetPath").toString();
   String pos = session.getAttribute("pos").toString();
   String userid = session.getAttribute("UserID").toString();
   String typeOfPolarity = session.getAttribute("typePolarity").toString();
     String projectName =  request.getParameter("name");
     String appPath = request.getServletContext().getRealPath("");
   String targetPath = appPath + File.separator + SAVE_DIR+File.separator +userid;
     File fileSaveDir = new File(targetPath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
String destinationFilePath = targetPath+File.separator+projectName+".txt";
  String fileContent =  CopyFileContent(lexiconPath,destinationFilePath);
   
  
   dbconn.SaveFormData(destinationFilePath,wordNetPath,pos,userid,projectName,typeOfPolarity);
   
    POS PartOfSpeech;
 if(pos.equalsIgnoreCase("noun")){
            PartOfSpeech = POS.NOUN;
        } else if(pos.equalsIgnoreCase("verb")){
            PartOfSpeech = POS.VERB;
        }else if(pos.equalsIgnoreCase("adjective")){
            PartOfSpeech = POS.ADJECTIVE;
        }else{
            PartOfSpeech = POS.ADVERB;
        }
    
       
        
        //performed upon successful upload
        request.setAttribute("message", "Your lexicon has been successfully uploaded.");
        request.setAttribute("message2", "If you would like to rerun the test, please adjust your options and resubmit.");
     //   request.setAttribute("typePolarity", typePolarity);
       // request.setAttribute("wordnetVersion", wordnetVersion);
        //request.setAttribute("partOfSpeech", pos);
        request.setAttribute("successfulUpload", true);
        request.setAttribute("jsonFileName", "json/sampleWordSynset_1.json");
   
        List<Component> allComponents;
        ArrayList<Component> problematicComponents;
        
        
        
        WordsComponentBuilderImpl componentBuilder = new process.WordsComponentBuilderImpl(wordNetPath);
        allComponents = componentBuilder.buildComponents(lexiconPath, PartOfSpeech);
       //problematicComponents = new ArrayList<Component>(Optimize.solve(allComponents));
       problematicComponents = Optimize.solve(allComponents);
       session.setAttribute("JSONComponents",problematicComponents );
        JSONCreater jsonCreater = new JSONCreater();
        
        JsonObject finishedJson = jsonCreater.convertComponents(problematicComponents);
        
        //write the json object to a json file
        String jsonFolder = "json";
        String jsonFileName = "temp.json";
        String jsonFilePath = request.getServletContext().getRealPath("") + File.separator + jsonFolder + File.separator + jsonFileName;
        File jsonFileStore = new File(jsonFilePath);
        FileOutputStream jsonFileOutputStream = new FileOutputStream(jsonFileStore);
        JsonWriter writer = Json.createWriter(jsonFileOutputStream);
        writer.writeObject(finishedJson);
       
        getServletContext().getRequestDispatcher("/index.jsp").forward(
                request, response);
   
   
   
   
    }

    
public String  CopyFileContent(String lexiconPath,String targetPath)
{
    File fileToBeRead = new File(lexiconPath);
         
        String Content = "";
        BufferedReader reader = null;
        FileWriter writer = null;
         
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeRead));
          String line = reader.readLine();
             
            while (line != null) 
            {
                Content = Content + line.toLowerCase() + System.lineSeparator();
                 line = reader.readLine();
            }
            writer = new FileWriter(targetPath);
             
            writer.write(Content);
           
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
                writer.close();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        
        return Content;
}
}
