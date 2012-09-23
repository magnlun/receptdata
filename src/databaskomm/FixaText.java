package databaskomm;
import java.util.Scanner;


public class FixaText {
	public static String fixaTexten(String text){
		int tecken = 70;
		String temp;
		Scanner sc = new Scanner(text);
		String instruktioner = "";
		while (sc.hasNextLine()) {
			int i = 0;
			temp = sc.nextLine();
			if (temp.equals("")) {
				while (sc.hasNextLine()) {
					if (!(temp = sc.nextLine()).equals(""))
						break;
				}
				instruktioner += "\n";
			}
			if (temp.length() < tecken)
				instruktioner += temp + "\n";
			else {
				Scanner sc2 = new Scanner(temp);
				while (sc2.hasNext()) {
					String temp2 = sc2.next();
					i += temp2.length() + 1;
					if (i > tecken) {
						i = temp2.length() + 1;
						instruktioner += "\n";
					}
					instruktioner += temp2 + " ";
				}
				instruktioner += "\n";
				sc2.close();
			}
		}
		sc.close();
		return instruktioner;
	}
}
