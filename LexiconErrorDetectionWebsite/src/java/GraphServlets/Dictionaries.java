
package GraphServlets;

import java.io.IOException;
import static java.lang.System.out;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@WebServlet(name = "Dictionaries", urlPatterns = {"/Dictionaries"})
public class Dictionaries extends HttpServlet {

  
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String input = request.getParameter("word");
       
      Document document = Jsoup.connect(input).get();
      Element mainDiv = null ;
      if(input.contains("dictionary.com"))
      {
       mainDiv = document.select("div.def-list").first();     
      }
      if(input.contains("merriam-webster.com"))
      {
       mainDiv = document.select("div.vg").first();
      
      }
      if(input.contains("thefreedictionary.com"))
      {
       mainDiv = document.select("div#MainTxt").first();
      
      }
      if(mainDiv != null)
      {
      response.setContentType("text/HTML");
     response.getWriter().print(mainDiv.toString());
      }
    }

  
}
