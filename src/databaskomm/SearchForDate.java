package databaskomm;

public class SearchForDate {
	public static String[] sök(String datum, String tid){
		String query = "SELECT \"BakNamn\" FROM \"Bakverk\" "
				+ " WHERE \"Tillaggt\" > '"
				+ datum
				+ "' OR ( \"Tillaggt\" = '"
				+ datum
				+ "' AND \"Tid\" > '"
				+ tid
				+ "');";
		try{
			String[][] rc = Fetch.fetching(query);
			String[] retur = new String[rc.length - 1];
			for(int i = 1 ; i < rc.length; i++){
				retur[i-1] = rc[i][0];
			}
			return retur;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
