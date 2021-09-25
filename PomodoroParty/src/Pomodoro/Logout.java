package Pomodoro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Logout")
public class Logout extends HttpServlet {
	public static final String db = "jdbc:mysql://localhost/PomodoroParty?user=root&password=root";

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("enter logout");
		PrintWriter out = response.getWriter();
		
		//TODO: do whatever i need to do to return to frontend
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		session.invalidate();
		logout(username);
		
		response.setContentType("application/json");
		out.println("{");
		out.println("}");
	}
	
	public void logout(String u) {
		Connection conn = null;
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(db);
			st = conn.createStatement();
			//TODO: update execute query based on how the table is formatted
			rs = st.executeQuery("SELECT * from CurrentlyOnline");
			while(rs.next()) {
				String user = rs.getString("nickname");
				System.out.println(user + " " + u);
				if(user.equals(u)) {
					st1 = conn.createStatement();
					st1.executeUpdate("DELETE FROM CurrentlyOnline WHERE nickname='" + user + "';");
				}	
			}
		}
		catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		}
		finally {
			try {
				if (rs!= null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (st1 != null) {
					st1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} 
			catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}
}
