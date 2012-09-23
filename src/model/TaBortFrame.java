package model;
import javax.swing.JOptionPane;

import komponenter.button;

import userInterface.ReceptV�ljare;



import databaskomm.TaBortRecept;


public class TaBortFrame implements Ruta, ReceptMottagare{

	ReceptV�ljare rutan;
	
	public TaBortFrame(){
		rutan = new ReceptV�ljare(this);
	}
	
	

	public void kor() {
		rutan.setVisible(true);
	}

	@Override
	public void taRecept(String recept, ReceptV�ljare ruta) {
		TaBortRecept.taBortReceptet(recept);
		JOptionPane.showMessageDialog(rutan,"Receptet �r nu borttaget.");
		skrivPDF.skriv();
		ruta.dispose();
		button.changeList(new TaBortFrame());
	}
	
}
