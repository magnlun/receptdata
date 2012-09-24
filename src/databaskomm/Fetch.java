package databaskomm;

//This Java program created by Magnus Lundberg is licensed under a Creative Commons Attribution 3.0 Unported License.
//http://creativecommons.org/licenses/by/3.0/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayDeque;

import model.Connect;


public class Fetch {

	public static String[][] fetching(String query)
			throws SQLException {
		Connection conn = Connect.getConnection("Databas");
		ResultSet rs = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} 
		ResultSetMetaData rsmd = rs.getMetaData();

		int numColumns = rsmd.getColumnCount();

		final ArrayDeque<String[]> rows = new ArrayDeque<String[]>();
		String[] row = new String[numColumns];
		for (int i = 1; i <= numColumns; i++)
			row[i - 1] = (rsmd.getColumnName(i));
		rows.offer(row);

		while (rs.next()) {
			row = new String[numColumns];
			for (int i = 1; i <= numColumns; i++)
				row[i - 1] = rs.getString(i);
			rows.offer(row);
		}

		final String[][] rc = new String[rows.size()][];
		int i = 0;
		while ((row = rows.poll()) != null)
			rc[i++] = row;
		return rc;
	}
}
