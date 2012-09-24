package komponenter;

import java.util.HashMap;

import komponenter.InputUsername;


public class FetchPassword {
	private static HashMap<String,String> användarnamn = new HashMap<String,String>();
	private static HashMap<String,String> lösenord = new HashMap<String,String>();
	
	public static void putPassword(String key, String password){
		lösenord.put(key, password);
	}
	
	public static void removePassword(String key){
		lösenord.remove(key);
	}
	
	public static String fetchPassword(String key){
		String rc = lösenord.get(key);
		if(rc == null)
			rc = "";
		return rc;
	}
	
	public static void putName(String key, String password){
		användarnamn.put(key, password);
	}
	
	public static void removeName(String key){
		användarnamn.remove(key);
	}
	
	public static String fetchName(String key){
		String rc = användarnamn.get(key);
		if(rc == null){
			String[] rc2 = InputUsername.användarnamn(key);
			putName(key, rc2[0]);
			putPassword(key, rc2[1]);
			return rc2[0];
		}
		return rc;
	}
}
