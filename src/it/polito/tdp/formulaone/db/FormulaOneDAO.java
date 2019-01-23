package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.formulaone.model.Race;
import it.polito.tdp.formulaone.model.Season;

public class FormulaOneDAO {

	public List<Season> getAllSeasons() {
		String sql = "SELECT year, url FROM seasons ORDER BY year";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<Season> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Season(rs.getInt("year"), rs.getString("url")));
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Integer> getAllYears() {
		String sql = "Select year " + 
				"From seasons " + 
				"order by year";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<Integer> list = new ArrayList<>();
			while (rs.next()) {
				list.add(rs.getInt("year"));
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Race> getAllRacesForYear(int myYear) {
		String sql = "Select * " + 
				"From races " + 
				"Where year = ?";
		Connection conn = DBConnect.getConnection();
		
		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, myYear);
			ResultSet rs = st.executeQuery();
			List<Race> list = new ArrayList<>();
			while(rs.next()) {
				Time time = rs.getTime("time");
				LocalTime lt= null;
				if(time!=null) {
					lt= time.toLocalTime();
				}
				list.add(new Race(rs.getInt("raceId"), Year.of(rs.getInt("year")), rs.getInt("round"), rs.getInt("circuitId"), rs.getString("name"),rs.getDate("date").toLocalDate(), lt, rs.getString("url")));
			}
			
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<String> getAllNameRaces(int myYear) {
		String sql = "Select name " + 
				"From races " + 
				"Where year = ?";
		Connection conn = DBConnect.getConnection();
		
		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, myYear);
			ResultSet rs = st.executeQuery();
			List<String> list = new ArrayList<>();
			while(rs.next()) {
				list.add(rs.getString("name"));
			}

			conn.close();
			return list;
			} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getWeight(int myYear, Race r1 , Race r2) {
		String sql = "Select Count(*) as count " + 
				"From results r1 " + 
				"Where r1.statusId = 1 " + 
				"and r1.raceId = ? " + 
				"and r1.driverId in (Select r2.driverId " + 
				"					From results r2,races r " + 
				"					Where r.raceId = r2.raceId " + 
				"					and r2.raceId = ? " + 
				"					and r2.statusId = 1 " + 
				"					and r.year = ?)";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, r1.getRaceId());
			st.setInt(2, r2.getRaceId());
			st.setInt(3, myYear);
			ResultSet rs = st.executeQuery();
			int result = 0;
			if(rs.next()) {
				result = rs.getInt("count");
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}


}
