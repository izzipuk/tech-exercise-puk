import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MyServletDBPuk")
public class MyServletDBPuk extends HttpServlet {
   private static final long serialVersionUID = 1L;
   static String url = "jdbc:mysql://ec2-3-80-134-70.compute-1.amazonaws.com:3306/myGradeDBPuk";
   static String user = "ipuk_remote";
   static String password = "password";
   static Connection connection = null;

   public MyServletDBPuk() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
     
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");//("com.mysql.jdbc.Driver");
      } catch (ClassNotFoundException e) {
         System.out.println("Where is your MySQL JDBC Driver?");
         e.printStackTrace();
         return;
      }
      
      connection = null;
      try {
         connection = DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
         System.out.println("Connection Failed! Check output console");
         e.printStackTrace();
         return;
      }
      if (connection != null) {
         
      } else {
         System.out.println("Failed to make connection!");
      }
      
      PrintWriter out = response.getWriter();
      String title = "Grading Scale";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");
      try {
    	  
         String selectSQL = "SELECT * FROM gradeTablePuk";
         PreparedStatement preparedStatement = connection.prepareStatement(selectSQL); 
         ResultSet rs = preparedStatement.executeQuery();
         
         while (rs.next()) {
            String id = rs.getString("ID");
            String letter = rs.getString("LETTER");
            String decimal = rs.getString("WEIGHT");
            out.format("%s %s <br>", letter,  decimal);
            }
         response.getWriter().append("<a href=/webproject-te-puk/index.html>GPA Claculator</a> <br>");
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }
}