package databaskomm;

//This Java program created by Magnus Lundberg is licensed under a Creative Commons Attribution 3.0 Unported License.
//http://creativecommons.org/licenses/by/3.0/



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Connect;
import model.Kalender;

public class SattIn {
	public static boolean sattIn(String namn, String[] kategorier, int betyg,
			String beskrivning, int portioner,
			ArrayList<ArrayList<String>> ingredienser, JFrame window)
			throws SQLException {
		String query;
		String ingrediens;
		Kalender datum = new Kalender();
		query = "INSERT INTO \"Bakverk\"(\"BakNamn\", \"Betyg\", \"Instruktioner\", \"Portioner\", \"Tillaggt\", \"Tid\")  VALUES ('"
				+ namn
				+ "','"
				+ betyg
				+ "','"
				+ beskrivning
				+ "','"
				+ portioner
				+ "','"
				+ datum.getDate()
				+ "','"
				+ datum.getClock() + "');";
		if (!kommunikationRecept(query, namn, window))
			return false;
		for (int i = 0; i < kategorier.length; i++) {
			query = "INSERT INTO \"TillhorKategori\"(\"BakNamn\", \"Kategori\")VALUES ('"
					+ namn + "','" + kategorier[i] + "');";
			kommunikationRecept(query, namn, window);
		}
		for (int i = 0; i < ingredienser.size(); i++) {
			String fas = ingredienser.get(i).get(0);
			for (int j = 1; j < ingredienser.get(i).size(); j++) {
				ingrediens = ingredienser.get(i).get(j);
				final String[] volym = ingrediens.split(" ");
				String alternativ = "";
				for (int k = 1; k < volym.length - 3; k++) {
					if (volym[k].equals("eller") || volym[k].equals("Eller")) {
						for (int l = k+1; l < volym.length - 2; l++)
							alternativ += volym[l];
					}
				}
				String Ingred = "";
				for (int k = 0; k < volym.length - 2; k++) {
					if (volym[k].equals("eller") || volym[k].equals("Eller")){
						break;
					}
					Ingred += volym[k];
					if (k < volym.length - 3)
						Ingred += " ";
				}
				String hopslag = volym[volym.length - 2] + " "
						+ volym[volym.length - 1];

				if (!alternativ.equals("")) {
					query = "INSERT INTO \"Alternativ\"(\"Ingrediens\", \"AltIngrediens\", \"Recept\", \"Volym\", \"Fas\") VALUES ('"
							+ Ingred
							+ "','"
							+ alternativ
							+ "','"
							+ namn
							+ "','" + hopslag + "','" + fas + "');";
					sattInAlternativ(query, Ingred, alternativ, window);
				}
				query = "INSERT INTO \"Innehall\"(\"BakNamn\", \"Ingrediens\", \"Volym\", \"Fas\") VALUES ('"
						+ namn
						+ "','"
						+ Ingred
						+ "','"
						+ hopslag
						+ "','"
						+ fas
						+ "');";
				kommunikation(query, Ingred, window);
			}
		}
		return true;

	}

	public static boolean kommunikationRecept(String query, String recept,
			JFrame window) {
		Connection conn = Connect.getConnection("Databas");
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException err) {
			err.printStackTrace();
			int n = JOptionPane.showConfirmDialog(window,
					"Receptet fanns redan, vill du ta bort det?", "Krock!",
					JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				TaBortRecept.taBortReceptet(recept);
				return kommunikationRecept(query, recept, window);
			} else {
				return false;
			}
		}
	}

	public static void kommunikation(String query, String ingrediens,
			JFrame window) throws SQLException {
		Connection conn = Connect.getConnection("Databas");
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			String[][] Alias = Hämta
					.hämtning("SELECT \"Ingrediens\" FROM \"Alias\" WHERE \"Alias\" = '"
							+ ingrediens + "';");
			if (Alias.length > 1) {
				query = query.replace(",'" + ingrediens, ",'" + Alias[1][0]);
				kommunikation(query, Alias[1][0], window);
			} else {
				int n = JOptionPane.showConfirmDialog(window, "Ingrediensen "
						+ ingrediens + " finns inte, vill du lägga till den?",
						"Krock!", JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					kommunikation(
							"INSERT INTO \"Ingredienser\"(\"Ingrediens\", \"Viktig\", \"Hemma\", \"Önskad mängd\", \"Framtida behov\") VALUES ('"
									+ ingrediens
									+ "','"
									+ "FALSE"
									+ "','"
									+ 0
									+ "','" + 0 + "','" + 0 + "');", "", window);
					kommunikation(query, ingrediens, window);
				}
			}
		}
	}
	
	public static void sattInAlternativ(String query, String ingrediens, String Alternativ,
			JFrame window) throws SQLException{
		Connection conn = Connect.getConnection("Databas");
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
		}
		catch (SQLException sqle) {
			String[][] Alias = Hämta
					.hämtning("SELECT \"Ingrediens\" FROM \"Alias\" WHERE \"Alias\" = '"
							+ ingrediens + "';");
			if (Alias.length > 1) {
				query = query.replace(",'" + ingrediens, ",'" + Alias[1][0]);
				kommunikation(query, Alias[1][0], window);
			} else {
				int n = JOptionPane.showConfirmDialog(window, "Någon av ingredienserna "
						+ ingrediens + " eller " + Alternativ + " finns inte, vill du lägga till dem?",
						"Krock!", JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					kommunikation(
							"INSERT INTO \"Ingredienser\"(\"Ingrediens\", \"Viktig\", \"Hemma\", \"Önskad mängd\", \"Framtida behov\") VALUES ('"
									+ ingrediens
									+ "','"
									+ "FALSE"
									+ "','"
									+ 0
									+ "','" + 0 + "','" + 0 + "');", "", window);

					kommunikation(
							"INSERT INTO \"Ingredienser\"(\"Ingrediens\", \"Viktig\", \"Hemma\", \"Önskad mängd\", \"Framtida behov\") VALUES ('"
									+ Alternativ
									+ "','"
									+ "FALSE"
									+ "','"
									+ 0
									+ "','" + 0 + "','" + 0 + "');", "", window);
					sattInAlternativ(query, ingrediens, Alternativ, window);
				}
			}
		}
	}
}
