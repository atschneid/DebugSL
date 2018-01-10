package Login;

import Conversion.JSONCreater;
import data.Component;
import edu.mit.jwi.item.POS;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import optimize.Optimize;
import process.WordsComponentBuilderImpl;


@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    
	
	DBConn dbconn=new DBConn();
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String buttonValue = request.getParameter("SubmitButton");
		
		if(buttonValue.equals("Register"))
		{
			RequestDispatcher rd=request.getRequestDispatcher("Registration.jsp");
			rd.forward(request, response);
		}
		
		if(buttonValue.equals("Login"))
		{
                  String uname =  request.getParameter("uname");
                  String password =  request.getParameter("password");
			String UserID = dbconn.VerifyUser(uname,password);
                         HttpSession  session = request.getSession();
                         session.setAttribute("UserID", UserID);
                        if(UserID.isEmpty())
                        {
                           request.setAttribute("ErrorMessage", "Invalid username or password. Please try again!");
                           RequestDispatcher rd=request.getRequestDispatcher("Login.jsp");
			   rd.forward(request, response);
                        }
                        else
                        {
                         String uid = session.getAttribute("UserID").toString();
                           ArrayList<LexiconFields> projectsList = dbconn.GetRecentProjects(uid); 
                           request.setAttribute("ProjectsList", projectsList);
                          
                            RequestDispatcher rd=request.getRequestDispatcher("History.jsp");
			   rd.forward(request, response);
                        }
			
		}
	}
public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String time = request.getParameter("word");
      HttpSession  session = request.getSession();
       String userId = session.getAttribute("UserID").toString();    
  LexiconFields currentProject =  dbconn.GetProjectDetails(time,userId);
  
  String lexiconPath = currentProject.LexiconFilePath;
  String  wordNetPath = currentProject.WordNetPath;
  String partOfSpeech =  currentProject.PartOfSpeech;
  String typePolarity = currentProject.TypeOfPolarity;
  POS pos;
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
    //performed upon successful upload
        request.setAttribute("message", "Your lexicon has been successfully uploaded.");
        request.setAttribute("message2", "If you would like to rerun the test, please adjust your options and resubmit.");
        request.setAttribute("typePolarity", typePolarity);
      //  request.setAttribute("wordnetVersion", wordnetVersion);
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
}
