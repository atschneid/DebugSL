
package Login;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.CallableStatement;
import java.text.SimpleDateFormat;
import java.util.Date;



public class DBConn {
    String connString;
    String connName;
    String connPW;
    public DBConn() {
        this.connString = "jdbc:mysql://localhost:3306/sentlexccdb?autoReconnect=true&useSSL=false";
        this.connName = "root";
        this.connPW   = "squeeql";
    }
    
    public String VerifyUser(String uname,String password)
    {
        String UserID ="";
        
        try {
		    Class.forName("com.mysql.jdbc.Driver");
		   
		    Connection conn=DriverManager.getConnection(connString,connName,connPW);
		    Statement statement = conn.createStatement();
		    String SQL = "select UserID from userlogin where UserName='"+uname+"' and Password='"+password+"'";
		    PreparedStatement prestat = conn.prepareStatement(SQL); 
		    ResultSet rs =  prestat.executeQuery();
                 while (rs.next()) {
                  UserID = rs.getString("UserID");
                      }
		    conn.close();
		   
		} catch (Exception e) {
			System.out.println(e + "Cannot find the driver in the classpath!");
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
        return UserID;
	}

public int RegisterUser(String fname,String lname,String email,String password,String username){
     int result = 0;
     try {
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection conn=DriverManager.getConnection(connString,connName,connPW);
		   String sql="{call registerUser(?, ?,?,?,?,?)}";
                    CallableStatement  stmt= conn.prepareCall(sql);
                      stmt.setString(1,fname);  
                      stmt.setString(2,lname);  
                      stmt.setString(3,email);  
                      stmt.setString(4,password);
                      stmt.setString(5,username);  
                      stmt.registerOutParameter(6,java.sql.Types.INTEGER);  
                      stmt.execute();  
                      result = stmt.getInt(6);
		    conn.close();
		   
		} catch (Exception e) {
			System.out.println(e + "Cannot find the driver in the classpath!");
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
     return result;
}

public int SaveFormData(String FilePath,String wordNetPath,String pos,String userid,String projectName,String typeOfPolarity) 
{
     int result = 0;
     try {
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection conn=DriverManager.getConnection(connString,connName,connPW);
		   String sql="{call saveForm(?,?,?,?,?,?,?)}";
                    CallableStatement  stmt= conn.prepareCall(sql);
                      stmt.setString(1,FilePath);  
                      stmt.setString(2,wordNetPath);  
                      stmt.setString(3,pos.toString());  
                       stmt.setInt(4,Integer.parseInt(userid) );  
                       stmt.setString(5,projectName);  
                       stmt.setString(6,typeOfPolarity);  
                      stmt.registerOutParameter(7,java.sql.Types.INTEGER);  
                      stmt.execute();  
                      result = stmt.getInt(7);
		    conn.close();
		   
		} catch (Exception e) {
			System.out.println("Cannot find the driver in the classpath!");
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
     return result;
}


public ArrayList<LexiconFields> GetRecentProjects(String UserID)
{
    ArrayList<LexiconFields> projectsList = new ArrayList<LexiconFields>();
     try {
		    Class.forName("com.mysql.jdbc.Driver");
		   
		    Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/sentlexccdb?autoReconnect=true&useSSL=false","root","squeeql");
		    Statement statement = conn.createStatement();
		    String SQL = "Select * from recentforms where userid ="+UserID+" order by DateModified desc";
		    PreparedStatement prestat = conn.prepareStatement(SQL); 
		    ResultSet rs =  prestat.executeQuery();
                 while (rs.next()) {
                     LexiconFields fields= new LexiconFields();
                 fields.setLexiconFilePath(rs.getString("lexiconPath"));
                  fields.setWordNetPath(rs.getString("wordNetPath"));
                 fields.setPartOfSpeech(rs.getString("pos"));
            fields.setProjectTitle(rs.getString("ProjectTitle"));
                  fields.setDateModified(rs.getTimestamp("DateModified").toString());
                 fields.setTypeOfPolarity(rs.getString("TypeOfPolarity"));
                 fields.setComments(rs.getString("Comments"));
                  
                  
                  projectsList.add(fields);
                      }
		    conn.close();
		   
		} catch (Exception e) {
			
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
     
     return projectsList;
}


public LexiconFields GetProjectDetails(String time,String userId){
    LexiconFields project = new LexiconFields();
     try {
		    Class.forName("com.mysql.jdbc.Driver");
		   
		    Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/sentlexccdb?autoReconnect=true&useSSL=false","root","squeeql");
		    Statement statement = conn.createStatement();
		    String SQL = "Select * from recentforms where userid ="+userId+" and DateModified='"+time+"'";
		    PreparedStatement prestat = conn.prepareStatement(SQL); 
		    ResultSet rs =  prestat.executeQuery();
                 while (rs.next()) {
                     
                 project.setLexiconFilePath(rs.getString("lexiconPath"));
                  project.setWordNetPath(rs.getString("wordNetPath"));
                 project.setPartOfSpeech(rs.getString("pos"));
            
                 project.setProjectTitle(rs.getString("ProjectTitle"));
                  project.setDateModified(rs.getTimestamp("DateModified").toString());
                 project.setTypeOfPolarity(rs.getString("TypeOfPolarity"));
                  
                  
                      }
		    conn.close();
		   
		} catch (Exception e) {
			
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
     
     return project;
}

   public void SaveComments(String id, String comments) {
           try {
		    Class.forName("com.mysql.jdbc.Driver");
		    Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/sentlexccdb?autoReconnect=true&useSSL=false","root","squeeql");
		     String sql = "UPDATE sentlexccdb.recentforms  SET Comments ='"+comments+"'  WHERE DateModified = '"+id+"' ";
                   PreparedStatement prestat = conn.prepareStatement(sql); 
		    prestat.executeUpdate();
                    conn.close();
		   
		} catch (Exception e) {
			System.out.println("Cannot find the driver in the classpath!");
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
    }
}
