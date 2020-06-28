package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.CoppiaTipologie;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	/*
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}*/
	
	public List<String> listCategorie(){
		String sql = "SELECT DISTINCT offense_category_id AS c FROM `events` ORDER BY c";
		List<String> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				list.add(res.getString("c"));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<LocalDate> listGiorni(){
		String sql = "SELECT DISTINCT DATE(reported_date) AS d FROM `events` ORDER BY d";
		List<LocalDate> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				list.add(res.getDate("d").toLocalDate());
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> listTipologie(String categoria, LocalDate giorno){
		String sql = "SELECT DISTINCT offense_type_id AS t FROM `events` AS e1 " + 
				"WHERE DATE(e1.reported_date) = ? AND e1.offense_category_id = ? ORDER BY t";
		List<String> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, giorno.toString());
			st.setString(2, categoria);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				list.add(res.getString("t"));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<CoppiaTipologie> listCoppieTipologie (String categoria, LocalDate giorno) {
		String sql = "SELECT e1.offense_type_id AS t1, e2.offense_type_id AS t2, COUNT(DISTINCT e2.precinct_id) AS c " + 
				"FROM `events` AS e1, `events` AS e2 " + 
				"WHERE DATE(e1.reported_date) = ? AND e1.offense_category_id = ? " + 
				"AND DATE(e2.reported_date) = ? AND e2.offense_category_id = ? " + 
				"AND e1.offense_type_id>e2.offense_type_id AND e1.precinct_id = e2.precinct_id " + 
				"GROUP BY t1, t2";
		List<CoppiaTipologie> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, giorno.toString());
			st.setString(2, categoria);
			st.setString(3, giorno.toString());
			st.setString(4, categoria);
			ResultSet res = st.executeQuery() ;
			while(res.next()) {
				list.add(new CoppiaTipologie(res.getString("t1"), res.getString("t2"), res.getInt("c")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
