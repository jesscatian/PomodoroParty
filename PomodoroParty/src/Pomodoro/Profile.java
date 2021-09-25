package Pomodoro;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//PROFILE JSON TO BE SENT BACK
@WebServlet("/Profile")
public class Profile extends HttpServlet{
	public static final String db = "jdbc:mysql://localhost/PomodoroParty?user=root&password=root";
	class userProfile
	{
		String username = "";
		int num_streak = 0;
		int total_sessions = 0;
		public userProfile(String username, int num_streak, int total_sessions)
		{
			this.username = username;
			this.num_streak = num_streak;
			this.total_sessions = total_sessions;
		}
		public String getUsername()
		{
			return this.username;
		}
		public int getStreak()
		{
			return this.num_streak;
		}
		public int getSessions()
		{
			return this.total_sessions;
		}
		public void setUsername(String username)
		{
			this.username = username;
		}
		public void setStreak(int streak)
		{
			this.num_streak = streak;
		}
		public void setSessions(int sessions)
		{
			this.total_sessions = sessions;
		}
	}
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("enter profile");
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement st2 = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		Statement st3 = null;
		PreparedStatement ps3 = null;
		ResultSet rs3 = null;
		String username = "";
		int streak = 0;
		int sessions = 0;
//		GET SESSIONS ----- REMEMBER TO CHANGE SESSION NAME
		HttpSession session = request.getSession(true);
		String userID = (String) session.getAttribute("username");
		System.out.println(userID);

		try {
//			GET USERNAME
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(db);
			ps = conn.prepareStatement("SELECT Users.user_nickname FROM Users WHERE Users.user_username = ?");
			ps.setString(1, userID);
			rs = ps.executeQuery();	
			if(rs.next())
			{
				username = rs.getString(1);

			}
//			GET STREAK
			ps2 = conn.prepareStatement("SELECT Users.user_num_streaks FROM Users WHERE Users.user_username = ?");
			ps2.setString(1, userID);
			rs2 = ps2.executeQuery();
			if(rs2.next())
			{
				streak = rs2.getInt(1);
			}
//			GET SESSIONS
			ps3 = conn.prepareStatement("SELECT Users.user_total_sessions FROM Users WHERE Users.user_username = ?");
			ps3.setString(1, userID);
			rs3 = ps3.executeQuery();
			if(rs3.next())
			{
				sessions = rs3.getInt(1);
			}
//			PASS JSON BACK TO FRONT END
			userProfile myProfile = new userProfile(username, streak, sessions);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(myProfile);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(json.toString());
			System.out.println(json);
			out.close();
			
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		}
		catch(ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (st2 != null) {
					st2.close();
				}
				if (rs3 != null) {
					rs3.close();
				}
				if (st3 != null) {
					st3.close();
				}
//				if (conn != null) {
//					conn.close();
//				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}
}