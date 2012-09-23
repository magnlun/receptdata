package databaskomm;

import java.sql.SQLException;

public class SkrivUt {
	public static String[][] skriv(String recept, int portioner)
			throws SQLException {
		String query = "SELECT \"Portioner\" FROM \"Bakverk\" WHERE \"BakNamn\" = '"
				+ recept + "';";
		String[][] temp = Hämta.hämtning(query);
		float portion = 1;
		if (portioner != 0)
			portion = Integer.parseInt(temp[1][0]) / (float) portioner;
		query = "SELECT  \"Innehall\".\"Ingrediens\", \"Innehall\".\"Volym\", \"Alternativ\".\"AltIngrediens\", \"Alternativ\".\"Volym\",  \"Instruktioner\", \"Innehall\".\"Fas\"" +
				" FROM \"Innehall\" " +
				"NATURAL JOIN \"Bakverk\" " +
				"LEFT JOIN \"Alternativ\" " +
				"ON \"Innehall\".\"Ingrediens\" = \"Alternativ\".\"Ingrediens\" " +
				"AND \"Innehall\".\"BakNamn\" = \"Alternativ\".\"Recept\" " +
				"AND \"Innehall\".\"Fas\" = \"Alternativ\".\"Fas\" " +
				"WHERE \"Innehall\".\"BakNamn\" = '"
				+ recept + "';";
		String[][] rc = Hämta.hämtning(query);
		if (rc.length < 2) {
			System.out.println("Tyvärr hittade jag inte det där");
			return null;
		}
		for (int i = 1; i < rc.length; i++) {
			String[] temp2 = rc[i][1].split(" ");
			temp2[0] = temp2[0].replace(",", ".");
			if (temp2[0].contains("/")) {
				String[] temp3 = temp2[0].split("/");
				temp2[0] = Double.toString(Integer.parseInt(temp3[0])
						/ Double.valueOf(temp3[1]));
			}
			rc[i][1] = Double.toString(Double.valueOf(temp2[0]) / portion)
					+ " " + temp2[1];
		}
		return rc;
	}
}
