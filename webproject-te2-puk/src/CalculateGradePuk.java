

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CalculateGradePuk
 */
@WebServlet("/CalculateGradePuk")
public class CalculateGradePuk extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalculateGradePuk() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		double grade1 = this.getGradePoint("letter1", request, response);
		double credit1 = Double.parseDouble(request.getParameter("credit1"));
		
		double grade2 = this.getGradePoint("letter2", request, response);
		double credit2 = Double.parseDouble(request.getParameter("credit2"));
		
		
		double grade3 = this.getGradePoint("letter3", request, response);
		double credit3 = Double.parseDouble(request.getParameter("credit3"));
		
		
		
		double grade4 = this.getGradePoint("letter4", request, response);
		double credit4 = Double.parseDouble(request.getParameter("credit4"));
		
		
		double grades_total = (grade1 * credit1) + (grade2 * credit2) + (grade3 * credit3) + (grade4 * credit4);
		double total_credit = credit1 + credit2 + credit3 + credit4;
		
		double gpa = grades_total / total_credit;
		
		response.setContentType("text/html");
	      PrintWriter out = response.getWriter();
	      String title = "Semester GPA";
	      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
	            "transitional//en\">\n"; //
	      out.println(docType + //
	            "<html>\n" + //
	            "<head><title>" + title + "</title></head>\n" + //
	            "<body bgcolor=\"#f0f0f0\">\n" + //
	            "<h1 align=\"center\">" + title + "</h1>\n");
	      
	      out.format("Semester GPA = %.2f / %.2f = %.2f <br>", grades_total, total_credit, gpa);
	 
	      out.println("<a href=/webproject-te-puk/index.html>GPA Calculator</a> <br>");
	      out.println("</body></html>");
		
	}
	
	String search(String keyword, HttpServletResponse response) throws IOException {
	      response.setContentType("text/html");
	      String result = null;
	      Connection connection = null;
	      PreparedStatement preparedStatement = null;
	      try {
	         DBConnectionPuk.getDBConnection();
	         connection = DBConnectionPuk.connection;

	         if (keyword.isEmpty()) {
	            result =  "Invalid letter grade";
	         } else {
	            String selectSQL = "SELECT * FROM gradeTablePuk WHERE LETTER LIKE ?";
	            String theGrade = keyword + "%";
	            preparedStatement = connection.prepareStatement(selectSQL);
	            preparedStatement.setString(1, theGrade);
	         }
	         ResultSet rs = preparedStatement.executeQuery();

	         while (rs.next()) {
	        	String letter = rs.getString("letter").trim();
	            String points = rs.getString("weight").trim();
	            
	            

	            if (letter.equals(keyword)) {
	              result = points;
	            }
	         }

	         rs.close();
	         preparedStatement.close();
	         connection.close();
	      } catch (SQLException se) {
	         se.printStackTrace();
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (preparedStatement != null)
	               preparedStatement.close();
	         } catch (SQLException se2) {
	         }
	         try {
	            if (connection != null)
	               connection.close();
	         } catch (SQLException se) {
	            se.printStackTrace();
	         }
	      }
	      
	      return result;
	   }
	
	double getGradePoint(String keyword, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		double result = 0.00;
		String point1 = search(request.getParameter(keyword), response);
		String invalid = "Invalid letter grade";
        if (point1.equals(invalid))
        {
        	response.getWriter().append("Invalid grade entry. Please re-enter grades");
        	response.getWriter().append("<a href=/webproject-te-puk/index.html>GPA Claculator</a> <br>");
        	result = -1;
        }
        else
        {
        	double grade1 = Double.parseDouble(point1);
        	result = grade1;
        }
		
		return result;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
