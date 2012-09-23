package databaskomm;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Connect;


public class AddKategori {
	public static void kategori(String kategori){
		String query = "INSERT INTO \"Kategorier\"(\n";
        query += "    \"Kategori\")\n";
        query += "VALUES ('" + kategori + "');";
        kommunikation(query);
	}
	
	public static void kommunikation(String query) {
		Connection conn = Connect.getConnection("Databas");
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.exit(0);
		}
	}
}
