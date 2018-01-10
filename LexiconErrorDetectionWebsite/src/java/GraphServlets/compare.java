/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphServlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


/**
 *
 * @author saroja
 */
@WebServlet(name = "compare", urlPatterns = {"/compare"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) 

public class compare extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String SAVE_DIR = "uploadedFiles";
        // gets absolute path of the web application
        String appPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR;

        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        String[] fileNames=new String[2];
        String lexiconFileName = "";
        int count=0;
         for (Part part : request.getParts()) {
            lexiconFileName = extractFileName(part);
            if (lexiconFileName.length() > 4) {
                String filename= lexiconFileName.substring(0,lexiconFileName.length() - 4);
                String extension= lexiconFileName.substring(lexiconFileName.length() - 4, lexiconFileName.length());
                if (extension.equals(".txt")) {
                   fileNames[count++] = lexiconFileName;
                    part.write(savePath + File.separator +lexiconFileName);
                }
            }
         }
         
         String lexiconPath1 = request.getServletContext().getRealPath(File.separator + "uploadedFiles" + File.separator + fileNames[0].toString());
         String lexiconPath2 = request.getServletContext().getRealPath(File.separator + "uploadedFiles" + File.separator + fileNames[1].toString());
        request.setAttribute("FileName1", fileNames[0].toString());
        request.setAttribute("FileName2", fileNames[1].toString());
    HashMap<String, String> map1 = new HashMap<String, String>();

    String line;
    BufferedReader reader = new BufferedReader(new FileReader(lexiconPath1));
    while ((line = reader.readLine()) != null)
    {
        String[] parts = line.split("=", 2);
        if (parts.length >= 2)
        {
            String key = parts[0];
            String value = parts[1];
            map1.put(key, value);
        } 
    }
    reader.close();
    
     HashMap<String, String> map2 = new HashMap<String, String>();

    BufferedReader reader1 = new BufferedReader(new FileReader(lexiconPath2));
    while ((line = reader1.readLine()) != null)
    {
        String[] parts = line.split("=", 2);
        if (parts.length >= 2)
        {
            String key = parts[0];
            String value = parts[1];
            map2.put(key, value);
        } 
    }
    reader.close();
   
    HashSet<String> common=new HashSet<String>(map1.keySet());
   boolean success= common.retainAll(map2.keySet());
    HashMap<String, String> samePolarityMap = new HashMap<String, String>();
    HashMap<String, String> diffPolarityMap = new HashMap<String, String>();
    
    for(String value : common)
    {
        String polarity1 = map1.get(value);
        String polarity2 = map2.get(value);
        if(polarity1.equals(polarity2) )
        {
            samePolarityMap.put(value, polarity1);
           // System.out.println("Same:  "+value+"   "+polarity1+"    "+polarity2);
        }else
        {
            diffPolarityMap.put(value, map1.get(value));
            // System.out.println("diff:  "+value+"   "+polarity1+"    "+polarity2);
        }
    }
    HashMap<String, String> File1Content = new HashMap<String, String>();
    HashMap<String, String> File2Content = new HashMap<String, String>();
    SortedSet<String> keys1 = new TreeSet<String>(map1.keySet());
   
   Iterator iterator = keys1.iterator();
   while(iterator.hasNext())
   {
       String key = iterator.next().toString();
       String polarity = map1.get(key);
       String Cssclass="";
       if(samePolarityMap.containsKey(key))
       {
          // Cssclass = "greenyellow";
          Cssclass = "ActiveRow";
       }else if(diffPolarityMap.containsKey(key))
       {
           //Cssclass = "gold";
            Cssclass = "PassiveRow";
       }
        else
       {
          // Cssclass = "lavenderblush";
            Cssclass = "NormalRow";
       }
       File1Content.put(""+key+" = "+polarity, Cssclass);
       
   }
    SortedSet<String> keys2 = new TreeSet<String>(map2.keySet());
    Iterator iterator1 = keys2.iterator();
   while(iterator1.hasNext())
   {
       String key = iterator1.next().toString();
       String polarity = map2.get(key);
       String Cssclass="";
       if(samePolarityMap.containsKey(key))
       {
          // Cssclass = "greenyellow";
            Cssclass = "ActiveRow";
       }else if(diffPolarityMap.containsKey(key))
       {
           //Cssclass = "gold";
            Cssclass = "PassiveRow";
       }
       else
       {
          // Cssclass = "lavenderblush";
            Cssclass = "NormalRow";
       }
       File2Content.put(""+key+" = "+polarity, Cssclass);
        
   }
    request.setAttribute("map1", File1Content);
   request.setAttribute("map2", File2Content);
    getServletContext().getRequestDispatcher("/CompareResults.jsp").forward(
                request, response);
    }

     private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                String path = s.substring(s.indexOf("=") + 2, s.length() - 1);
                int index = path.lastIndexOf("\\");
                return path.substring(index+1);
            }
        }
        return "";
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
