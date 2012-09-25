package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import komponenter.RecipeIngredients;

import databaskomm.Fetch;

public class ProcessRecept {
	public static ArrayList<RecipeIngredients> takeData(TreeSet<String> Recept, TreeSet<String> ingredienser){
		ArrayList<RecipeIngredients> tree = new ArrayList<RecipeIngredients>();
		for(String recept : Recept){
			String query = "SELECT \"Ingrediens\" FROM \"Innehall\" WHERE\n" +
					"\"BakNamn\" = '"+ recept +"';";
			try{
				String[][] ingrediens = Fetch.fetching(query);
				String[] rc = new String[ingrediens.length-1];
				for(int i = 0; i < rc.length; i++){
					rc[i] = ingrediens[i+1][0];
				}
				tree.add(new RecipeIngredients(recept, rc));
			}
			catch(Throwable err){
				err.printStackTrace();
				continue;
			}
		}
		try{
			File fil = new File("Ink�p.txt");
			BufferedReader br = new BufferedReader(new FileReader(fil));

			String ingrediens = br.readLine();
			while(ingrediens != null && !ingrediens.equals("Recept:")){
				ingredienser.add(ingrediens);
				ingrediens = br.readLine();
			}
			if(ingrediens != null && ingrediens.equals("Recept:"))
				ingrediens = br.readLine();
			while(ingrediens != null){
				ArrayList<String> ingredienserna = new ArrayList<String>();
				String recept = ingrediens;
				ingrediens = br.readLine();
				while(ingrediens != null && ingrediens.charAt(0) == '�'){
					ingredienserna.add(ingrediens);
					ingrediens = br.readLine();
				}
				tree.add(new RecipeIngredients(recept, ingredienserna));
			}
			br.close();
		}
		catch(IOException e){}
		catch(Exception err){
			err.printStackTrace();
		}
		return tree;
	}
}
