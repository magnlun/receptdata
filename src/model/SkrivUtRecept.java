package model;

import komponenter.button;
import userInterface.ReceptV�ljare;
import userInterface.receptF�nstret;

import databaskomm.SkrivUt;

public class SkrivUtRecept implements ReceptMottagare, Ruta {

	ReceptV�ljare ruta;

	public SkrivUtRecept() {
		ruta = new ReceptV�ljare(this, true);
	}
	
	

	public void kor() {
		ruta.setVisible(true);
	}

	@Override
	public void taRecept(String recept, ReceptV�ljare ruta) {
		try {
			new receptF�nstret(SkrivUt.skriv(recept, ruta.portioner()), recept);
			ruta.dispose();
			button.changeList(new SkrivUtRecept());
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
	}
}
