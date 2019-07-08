/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.sql.*;

/**
 *
 * @author Luminight
 */
public class Database {

	final static String DATABASE_URL = "jdbc:derby://localhost:1527/dungeoncrawler";
	private static Connection conn;

	private Database() {
	}

	public static Connection getConnection()
			throws ClassNotFoundException, SQLException, SQLNonTransientException, SQLNonTransientConnectionException {
		if (conn == null) {
			conn = DriverManager.getConnection(DATABASE_URL, "dungeon", "asd123");
			//TryCreateDatabase();
		}
		return conn;
	}

	static void TryCreateDatabase() throws ClassNotFoundException, SQLException {
		Connection conn = Database.getConnection();
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS dungeoncrawler");
		stmt.execute("USE dungeoncrawler");
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS scoreboard ( Time TIME, Player TEXT, Score INTEGER, Level TEXT);");
	}

	public static ResultSet GetHighScores(String map, int limit) throws SQLException, ClassNotFoundException, SQLNonTransientException, SQLNonTransientConnectionException {

		Connection conn = Database.getConnection();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		ResultSet rs = stmt.executeQuery("SELECT PLAYER, SCORE FROM DUNGEON.SCORES \n"
				+ "WHERE MAP LIKE '" + map + "'\n"
				+ "ORDER BY -SCORE\n"
				+ "FETCH FIRST " + limit + " ROWS ONLY");

		return rs;
	}

	public static void AddScore(String Player, String Map, int score) throws ClassNotFoundException, SQLException {

		Connection conn = Database.getConnection();
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("INSERT INTO DUNGEON.SCORES (PLAYER, MAP, SCORE) \n"
				+ "	VALUES ('" + Player + "', '" + Map + "', " + score + ")\n");

	}

}
