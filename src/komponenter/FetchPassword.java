package komponenter;

import java.util.HashMap;

import komponenter.InputUsername;


public class FetchPassword {
	private static HashMap<String,String> anv�ndarnamn = new HashMap<String,String>();
	private static HashMap<String,String> l�senord = new HashMap<String,String>();
	
	public static void putPassword(String key, String password){
		l�senord.put(key, password);
	}
	
	public static void removePassword(String key){
		l�senord.remove(key);
	}
	
	public static String fetchPassword(String key){
		String rc = l�senord.get(key);
		if(rc == null)
			rc = "";
		return rc;
	}
	
	public static void putName(String key, String password){
		anv�ndarnamn.put(key, password);
	}
	
	public static void removeName(String key){
		anv�ndarnamn.remove(key);
	}
	
	public static String fetchName(String key){
		String rc = anv�ndarnamn.get(key);
		if(rc == null){
			String[] rc2 = InputUsername.anv�ndarnamn(key);
			putName(key, rc2[0]);
			putPassword(key, rc2[1]);
			return rc2[0];
		}
		return rc;
	}
}
