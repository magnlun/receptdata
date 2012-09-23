package model;
import javax.swing.JOptionPane;

import komponenter.button;

import userInterface.ReceptVäljare;



import databaskomm.TaBortRecept;


public class TaBortFrame implements Ruta, ReceptMottagare{

	ReceptVäljare rutan;
	
	public TaBortFrame(){
		rutan = new ReceptVäljare(this);
	}
	
	

	public void kor() {
		rutan.setVisible(true);
	}

	@Override
	public void taRecept(String recept, ReceptVäljare ruta) {
		TaBortRecept.taBortReceptet(recept);
		JOptionPane.showMessageDialog(rutan,"Receptet är nu borttaget.");
		skrivPDF.skriv();
		ruta.dispose();
		button.changeList(new TaBortFrame());
	}
	
}
