package Pomodoro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	public static final String db = "jdbc:mysql://localhost/PomodoroParty?user=root&password=root";
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("enter signup");
		PrintWriter out = response.getWriter();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String check = request.getParameter("passwordCheck");
		String nickname = request.getParameter("nickname");
		boolean success;
		String message = "";
		if(!password.equals(check)) {
			message = "Passwords don't match";
			success = false;
		}
		else {
			success = signup(username, password, nickname);
			if(!success) {
				message = "Username taken";
			}
		}
		System.out.println(success);
		
		//TODO: do whatever i need to do to return to frontend
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		
		response.setContentType("application/json");
		out.println("{");
		out.println("\"success\":" + "\"" + success + "\",");
		out.println("\"message\":" + "\"" + message + "\"");
		out.println("}");
	}
	
	public boolean signup(String u, String p, String n) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			//TODO: how to connect to database
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(db);
			st = conn.createStatement();
			//TODO: update execute query based on how the table is formatted
			rs = st.executeQuery("SELECT * from Users");
			boolean found = false;
			while(rs.next()) {
				String user = rs.getString("user_username");
				if(u.equals(user)) {
					found = true;
					break;
				}
			}
			if(!found) {
				//TODO: username doesn't exist in database
				successful_signup(u, p, n);
				return true;
			}
			return false;
		}
		catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			return false;
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
			return false;
		}
		finally {
			try {
				if (rs!= null) {
					rs.close();
				}
				if (st != null) {
					st.close();
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
	
	public static void successful_signup(String u, String p, String n) {
		Connection conn = null;
		Statement st = null;
		try {
			//TODO: how to connect to database
			conn = DriverManager.getConnection(db);
			st = conn.createStatement();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		    Date date = new Date();  
		    System.out.println(formatter.format(date));  
			//TODO: update execute query based on how the table is formatted
			st.executeUpdate("INSERT INTO Users(user_username, user_password, user_nickname, user_last_session_date, user_num_streaks, user_total_sessions)"
					+ " VALUES ('" + u + "', '" + p + "', '" + n + "', '" + formatter.format(date) + "', '1', '1');");
			Login.successful_login(u);
		}
		catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		finally {
			try {
				if (st != null) {
					st.close();
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
