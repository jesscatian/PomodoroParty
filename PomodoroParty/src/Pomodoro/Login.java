package Pomodoro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Login")
public class Login extends HttpServlet {	
	//TODO: temporarily put public string here, probably should be on main server
	//TODO: also rename username, password, and VVVVVVV this part based on database
	public static final String db = "jdbc:mysql://localhost/PomodoroParty?user=root&password=root";
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("enter login");
		PrintWriter out = response.getWriter();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		boolean success = login(username, password);
		System.out.println(success);
		
		//TODO: do whatever i need to do to return to frontend
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		
		System.out.println(session);
		
		response.setContentType("application/json");
		out.println("{");
		out.println("\"success\":" + "\"" + success + "\"");
		out.println("}");
		out.flush();
		out.close();
	}
	
	public static boolean login(String u, String p) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(db);
			st = conn.createStatement();
			//TODO: update execute query based on how the table is formatted
			rs = st.executeQuery("SELECT * from Users");
			while(rs.next()) {
				String user = rs.getString("user_username");
				String pass = rs.getString("user_password");
				String nick = rs.getString("user_nickname");
				if(u.equals(user)) {
					if(!p.equals(pass)) {
						return false;
					}
					else {
						successful_login(user);
						return true;
					}
				}
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
	
	public static void successful_login(String n) {
		Connection conn = null;
		Statement st = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(db);
			st = conn.createStatement();
			//TODO: update execute query based on how the table is formatted
			st.executeUpdate("INSERT INTO CurrentlyOnline(nickname)"
					+ " VALUES ('" + n + "');");
		}
		catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
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
