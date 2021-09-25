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

import Pomodoro.Profile.userProfile;

//PROFILE JSON TO BE SENT BACK
@WebServlet("/StudyRoom")
public class StudyRoom extends HttpServlet{
	public static final String db = "jdbc:mysql://localhost/PomodoroParty?user=root&password=root";
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("enter studyroom");
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs3 = null;
		int numOnline = 0;
		int streaks = 0;
		String username = "";
//		GET SESSIONS ----- REMEMBER TO CHANGE SESSION NAME

		try {
//			GET USERNAME
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(db);
			ps = conn.prepareStatement("SELECT COUNT(*) FROM CurrentlyOnline");
			rs = ps.executeQuery();	
			if(rs.next())
			{
				numOnline = rs.getInt(1);
			}
			ps2 = conn.prepareStatement("SELECT user_nickname, user_num_streaks FROM Users WHERE user_num_streaks "
					+ "= (SELECT MAX(user_num_streaks) FROM Users)");
			rs2 = ps2.executeQuery();
			if(rs2.next())
			{
				username = rs2.getString("user_nickname");
				streaks = rs2.getInt("user_num_streaks");
			}
			
			studyRoomInfo myInfo = new studyRoomInfo(numOnline, streaks, username);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(myInfo);
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
				
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}
	class studyRoomInfo
	{
		int numOnline;
		int streak;
		String username;
		public studyRoomInfo(int numOnline, int streak, String username)
		{
			this.numOnline = numOnline;
			this.streak =  streak;
			this.username = username;
		}
		public int getNumOnline()
		{
			return this.numOnline;
		}
		public int getStreak()
		{
			return this.streak;
		}
		public String getUsername()
		{
			return this.username;
		}
		public void setNumOnline(int numOnline)
		{
			this.numOnline = numOnline;
		}
		public void setStreak(int streak)
		{
			this.streak = streak;
		}
		public void setUsername(String username)
		{
			this.username = username;
		}
	}

}