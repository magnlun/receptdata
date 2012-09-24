package databaskomm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


public class FileWrite {
	public static void write() throws IOException, SQLException{
		Scanner sc = new Scanner(System.in);
		File file = new File("recept.txt");
		file.createNewFile();
		BufferedWriter skriv = new BufferedWriter(new FileWriter(file));
		System.out.println("Vilket recept vill du hämta?");
		String recept = sc.nextLine();
		if(recept.endsWith("*") || recept.startsWith("*")){
			String query = "SELECT \"BakNamn\" FROM \"Bakverk\" WHERE \"BakNamn\" LIKE '"+recept.replace('*', '%')+"';";
			String[][] rc = Fetch.fetching(query);
			if(rc.length == 2)
				recept = rc[1][0];
			if(rc.length > 2){
				System.out.println("Vilken av:");
				for(int i = 1; i < rc.length; i++){
					System.out.println(i+": " + rc[i][0]);
				}
				recept = rc[sc.nextInt()][0];
				System.out.println(recept);
			}

		}
		String query = "SELECT  \"Innehall\".\"Ingrediens\", \"Innehall\".\"Volym\", \"Alternativ\".\"AltIngrediens\", \"Alternativ\".\"Volym\",  \"Instruktioner\" FROM \"Innehall\" NATURAL JOIN \"Bakverk\" LEFT JOIN \"Alternativ\" ON \"Innehall\".\"Ingrediens\" = \"Alternativ\".\"Ingrediens\" AND \"Innehall\".\"BakNamn\" = \"Alternativ\".\"Recept\" WHERE \"Innehall\".\"BakNamn\" = '" + recept + "';";
		String[][] rc = Fetch.fetching(query);
		if(rc.length < 2){
			System.out.println("Tyvärr hittade jag inte det där");
		}
		else{
			skriv.write("/");
			for(int i = 0; i < 43; i++)
				if(i == 20)
					skriv.write("-");
				else
					skriv.write("-");
			skriv.write("\\");
			skriv.newLine();
			for (int j = 0; j < rc.length; j++) {
				skriv.write("|");
				for (int k = 0; k < 2; k++) {
					skriv.write(rc[j][k].replace("  ", ""));
					if (k == 0) {
						for (int m = 0; m < 20 - rc[j][k].replace("  ","").length(); m++)
							skriv.write(" ");
						skriv.write("|            ");
					}
				}

				for (int n = 0; n < 10 - rc[j][1].replace("  ", "").length(); n++)
					skriv.write(" ");
				skriv.write("|");
				skriv.newLine();

				if (rc[j][2] != null && j > 1) {
					skriv.write("|eller:              |                      |");
					skriv.write("|" + rc[j][2].replace("  ", ""));
					for (int m = 0; m < 20 - rc[j][2].replace("  ", "").length(); m++)
						skriv.write(" ");
					skriv.write("|            ");
					skriv.write(rc[j][3].replace("  ", ""));
					for (int m = 0; m < 10 - rc[j][3].replace("  ", "").length(); m++)
						skriv.write(" ");
					skriv.write("|");
					skriv.newLine();
				}
			}
			skriv.write("\\");
			for(int i = 0; i < 43; i++)
				if(i == 20)
					skriv.write("-");
				else
					skriv.write("-");
			skriv.write("/");
			skriv.newLine();
			rc[1][4] = rc[1][4].replace("\n", "\r\n");
			skriv.write(rc[1][4]);
		}
		skriv.close();
		sc.close();
	}
}
