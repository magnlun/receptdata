package databaskomm;

import java.sql.SQLException;

public class LetaRecept {
	public static String[] Recept(String recept) {
		recept = recept.replace('*', '%');
		String query = "SELECT \"BakNamn\" FROM \"Bakverk\" WHERE \"BakNamn\" LIKE '"
				+ recept + "%" + "';";
		String[][] retur;
		try {
			retur = Fetch.fetching(query);
			String[] del1 = new String[retur.length - 1];
			for (int i = 1; i < retur.length; i++) {
				del1[i-1] = retur[i][0];
			}
			String[] del2 = new String[0];
			if(recept.length() > 0 && Character.isLowerCase(recept.charAt(0))){
				String recept2 = recept.replaceFirst(recept.charAt(0)+"",Character.toUpperCase(recept.charAt(0))+"");
				del2 = Recept(recept2);
			}
			String[] rc = new String[del1.length + del2.length];
			for(int i = 0; i < rc.length; i++){
				if(i >= del1.length)
					rc[i] = del2[i - del1.length];
				else
					rc[i] = del1[i];
			}
			return rc;
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;

	}
}
