package model;

import komponenter.button;
import userInterface.RecipeChooser;
import userInterface.RecipeWindow;

import databaskomm.SkrivUt;

public class SkrivUtRecept implements ReceptMottagare, Ruta {

	RecipeChooser ruta;

	public SkrivUtRecept() {
		ruta = new RecipeChooser(this, true);
	}
	
	

	public void kor() {
		ruta.setVisible(true);
	}

	@Override
	public void taRecept(String recept, RecipeChooser ruta) {
		try {
			new RecipeWindow(SkrivUt.skriv(recept, ruta.portioner()), recept);
			ruta.dispose();
			button.changeList(new SkrivUtRecept());
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
	}
}
