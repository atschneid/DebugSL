package GraphServlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.List;
import javax.json.*;

import optimize.*;
import process.*;
import data.*;
import Conversion.*;



import edu.mit.jwi.item.POS;
import javax.servlet.http.HttpSession;


@WebServlet("/Upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class Upload extends HttpServlet {

    private static final String SAVE_DIR = "uploadedFiles";
    
    private String typePolarity;
    private String wordnetVersion;
    private String partOfSpeech;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //SentimentReconciler.main(new String[0]);
     HttpSession  session = request.getSession();
        PrintWriter out = response.getWriter();
      String wordNetPath ;
        String lexiconPath ;
        POS pos;
   //  if(session.getAttribute("lexiconPath") == null)   
   // {
        String lexiconFileName = "";
		
	typePolarity = request.getParameter("typePolarity");
        wordnetVersion = request.getParameter("wordnetVersion");
        partOfSpeech = request.getParameter("partOfSpeech");
        
        // gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR;

        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        for (Part part : request.getParts()) {
            lexiconFileName = extractFileName(part);
           
           
            if (lexiconFileName.length() > 4) {
                if (lexiconFileName.substring(lexiconFileName.length() - 4, lexiconFileName.length()).equals(".txt")) {
                    //this is performed when a text file is selected
                    part.write(savePath + File.separator + lexiconFileName);
                    break;
                } else{
                    //this is performed when an incorrect file is selected
                    request.setAttribute("headingMessage", "File Upload");
                    request.setAttribute("message", "Your lexicon file was not in the form of a text file.");
                    request.setAttribute("typePolarity", typePolarity);
                    request.setAttribute("wordnetVersion", wordnetVersion);
                    request.setAttribute("partOfSpeech", partOfSpeech);
                    request.setAttribute("successfulUpload", false);
                    getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                    return;
                }
            }else {
                //this is performed when no file is selected
                request.setAttribute("headingMessage", "File Upload");
                request.setAttribute("message", "Please check that a correct file was selected.");
                request.setAttribute("typePolarity", typePolarity);
                request.setAttribute("wordnetVersion", wordnetVersion);
                request.setAttribute("partOfSpeech", partOfSpeech);
                request.setAttribute("successfulUpload", false);
                getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }
            

        }
        
        
   
        
         wordNetPath = request.getServletContext().getRealPath(File.separator + "wordNetDBFiles" + File.separator + "wordNet" + wordnetVersion);
         lexiconPath = request.getServletContext().getRealPath(File.separator + "uploadedFiles" + File.separator + lexiconFileName);
        if(partOfSpeech.equalsIgnoreCase("noun")){
            pos = POS.NOUN;
        } else if(partOfSpeech.equalsIgnoreCase("verb")){
            pos = POS.VERB;
        }else if(partOfSpeech.equalsIgnoreCase("adjective")){
            pos = POS.ADJECTIVE;
        }else{
            pos = POS.ADVERB;
        }
      session.setAttribute("typePolarity", typePolarity);
        session.setAttribute("lexiconPath",lexiconPath);
       session.setAttribute("wordNetPath",wordNetPath);
      session.setAttribute("pos",pos);
  /*   }
       else
    {
         lexiconPath = session.getAttribute("lexiconPath").toString();
    wordNetPath = session.getAttribute("wordNetPath").toString();
    partOfSpeech =  session.getAttribute("pos").toString();
 if(partOfSpeech.equalsIgnoreCase("noun")){
            pos = POS.NOUN;
        } else if(partOfSpeech.equalsIgnoreCase("verb")){
            pos = POS.VERB;
        }else if(partOfSpeech.equalsIgnoreCase("adjective")){
            pos = POS.ADJECTIVE;
        }else{
            pos = POS.ADVERB;
        }
    }
       */
        
        //performed upon successful upload
        request.setAttribute("message", "Your lexicon has been successfully uploaded.");
        request.setAttribute("message2", "If you would like to rerun the test, please adjust your options and resubmit.");
        request.setAttribute("typePolarity", typePolarity);
        request.setAttribute("wordnetVersion", wordnetVersion);
        request.setAttribute("partOfSpeech", partOfSpeech);
        request.setAttribute("successfulUpload", true);
        request.setAttribute("jsonFileName", "json/sampleWordSynset_1.json");
   
        List<Component> allComponents;
        ArrayList<Component> problematicComponents;
        
        
        
        WordsComponentBuilderImpl componentBuilder = new process.WordsComponentBuilderImpl(wordNetPath);
        allComponents = componentBuilder.buildComponents(lexiconPath, pos);
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

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                String fileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                return fileName;
            }
        }
        return "";
    }
    
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         
         
         
     }
}
