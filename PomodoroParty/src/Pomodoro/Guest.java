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

@WebServlet("/Guest")
public class Guest extends HttpServlet {
	//TODO: temporarily put public string here, probably should be on main server
	//TODO: also rename username, password, and VVVVVVV this part based on database
	public String db = "jdbc:mysql://localhost/PomodoroParty?user=root&password=root"; //do we need useSSL=false?
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("enter guest");
		PrintWriter out = response.getWriter();
		String username = guest_login();
		
		//TODO: do whatever i need to do to return to frontend
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		
		response.setContentType("application/json");
		out.println("{");
		out.println("}");
	}
	
	public String guest_login() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Statement st1 = null;
		Statement st2 = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(db);
			st = conn.createStatement();
			st1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				    ResultSet.CONCUR_READ_ONLY);
			st2 = conn.createStatement();
			st.executeUpdate("INSERT INTO Guests(guest_username)"
					+ " VALUES ('Guest');");
			rs = st1.executeQuery("SELECT * FROM Guests");
			rs.last();
			String cur_guest = rs.getString("guest_username") + rs.getString("guest_id");
			st2.executeUpdate("INSERT INTO CurrentlyOnline(nickname)"
					+ " VALUES ('" + cur_guest + "');");
			return cur_guest;
		}
		catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
			return "";
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
			return "";
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
				if (st2 != null) {
					st2.close();
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
