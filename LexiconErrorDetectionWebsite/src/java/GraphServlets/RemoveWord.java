/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraphServlets;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nmale_000
 */
@WebServlet(name = "RemoveWord", urlPatterns = {"/RemoveWord"})
public class RemoveWord extends HttpServlet {

  
 public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

 }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
  String input = request.getParameter("word");
      String[] data = input.split(";");
      String selectedWord = "";
      String changedPolarity = "";
      String originalPolarity ="";
      
      if(data.length == 3)
      {
       selectedWord = data[0];
       changedPolarity = data[1];
       originalPolarity = data[2];
      
      
      
      
           HttpSession  session = request.getSession();
           String lexiconPath = session.getAttribute("lexiconPath").toString();
  
              String oldString= selectedWord+"="+originalPolarity;
              String newString = selectedWord+"="+changedPolarity;
   // Changing the polarity of the selected word in text file
          modifyFile(lexiconPath,  oldString,  newString);
          session.setAttribute("lexiconPath", lexiconPath);
      }
    
   
    }

    /**
     *
     * @param filePath
     * @param oldString
     * @param newString
     */
    public void modifyFile(String filePath, String oldString, String newString)
    {
        File fileToBeModified = new File(filePath);
         
        String oldContent = "";
         
        BufferedReader reader = null;
         
        FileWriter writer = null;
         
        try
        {
            reader = new BufferedReader(new FileReader(fileToBeModified));
             
            //Reading all the lines of input text file into oldContent
             
            String line = reader.readLine();
             
            while (line != null) 
            {
                oldContent = oldContent + line.toLowerCase() + System.lineSeparator();
                 
                line = reader.readLine();
            }
             
            //Replacing oldString with newString in the oldContent
             
            String newContent = oldContent.replaceAll(oldString.toLowerCase(), newString.toLowerCase());
             
            //Rewriting the input text file with newContent
              reader.close();
            writer = new FileWriter(fileToBeModified);
             
            writer.write(newContent);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
       
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
