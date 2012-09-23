package model;

import komponenter.button;
import userInterface.ReceptVäljare;
import userInterface.receptFönstret;

import databaskomm.SkrivUt;

public class SkrivUtRecept implements ReceptMottagare, Ruta {

	ReceptVäljare ruta;

	public SkrivUtRecept() {
		ruta = new ReceptVäljare(this, true);
	}
	
	

	public void kor() {
		ruta.setVisible(true);
	}

	@Override
	public void taRecept(String recept, ReceptVäljare ruta) {
		try {
			new receptFönstret(SkrivUt.skriv(recept, ruta.portioner()), recept);
			ruta.dispose();
			button.changeList(new SkrivUtRecept());
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
	}
}
