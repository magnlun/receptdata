package databaskomm;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Connect;



public class TaBortRecept {
	public static void taBortReceptet(String recept){
		kommunikation("DELETE FROM \"Alternativ\" \n WHERE \"Recept\" = '" + recept + "';");
		kommunikation("DELETE FROM \"TillhorKategori\" \n WHERE \"BakNamn\" = '" + recept + "';");
		kommunikation("DELETE FROM \"Innehall\" \n WHERE \"BakNamn\" = '" + recept + "';");
		kommunikation("DELETE FROM \"Bakverk\" \n WHERE \"BakNamn\" = '" + recept + "';");
	}
	public static void kommunikation(String query){
		Connection conn = Connect.getConnection("Databas");
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

