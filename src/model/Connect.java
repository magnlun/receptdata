package model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import komponenter.FetchPassword;
import komponenter.Password;


public class Connect {
	private static HashMap<String,Connection> conn;
	private Connect(){
		conn = new HashMap<String, Connection>();
		String[] anslutningar = {"jdbc:postgresql://127.0.0.1:5432/Recept"};
		String[] namn = {"Databas"};
		for(int i = 0 ; i < anslutningar.length; i++){
			String connstr = anslutningar[i];
			String username = FetchPassword.fetchName(connstr);
			String password = FetchPassword.fetchPassword(connstr);
			if (password.equals(""))
				password = Password.lösenord(connstr, username);
			FetchPassword.putPassword(connstr, password);
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException cnfe) {
				System.err.println("Hittar ingen JDBC-driver!");
			}
			try {
				conn.put(namn[i], DriverManager.getConnection(connstr, username, password));
			} catch (SQLException e) {
				FetchPassword.removePassword(connstr);
				FetchPassword.removeName(connstr);
				FetchPassword.fetchName(connstr);
				i--;
				continue;
			}
		}
	}
	
	public static synchronized Connection getConnection(String var){
		if(conn == null){
			new Connect();
		}
		return conn.get(var);
	}
	
	public static synchronized void close(){
		try {
			for(Connection connect : conn.values()){
				connect.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
