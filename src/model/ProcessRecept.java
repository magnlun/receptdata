package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;

import userInterface.SkapaInköpslista;

import databaskomm.Hämta;

public class ProcessRecept {
	public static void takeData(TreeSet<String> Recept, TreeSet<String> ingredienser){
		for(String recept : Recept){
			String query = "SELECT \"Ingrediens\" FROM \"Innehall\" WHERE\n" +
					"\"BakNamn\" = '"+ recept +"';";
			try{
				String[][] ingrediens = Hämta.hämtning(query);
				for(int i = 1; i < ingrediens.length; i++){
					ingredienser.add(ingrediens[i][0]);
				}
			}
			catch(Throwable err){
				continue;
			}
		}
		try{
			File fil = new File("Inköp.txt");
			BufferedReader br = new BufferedReader(new FileReader(fil));

			String ingrediens = br.readLine();
			while(ingrediens != null){
				ingredienser.add(ingrediens);
				ingrediens = br.readLine();
			}
			br.close();
		}
		catch(IOException e){}
		catch(Exception err){
			err.printStackTrace();
		}
		try{
			File fil = new File("Inköp.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(fil));
			for(String ingrediens : ingredienser)
				bw.write(ingrediens + "\n");
			bw.close();
		}
		catch(Exception err){
			err.printStackTrace();
		}
		new SkapaInköpslista(ingredienser);
	}
}
