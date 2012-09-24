package model;
import javax.swing.JOptionPane;

import komponenter.button;

import userInterface.RecipeChooser;



import databaskomm.TaBortRecept;


public class TaBortFrame implements Ruta, ReceptMottagare{

	RecipeChooser rutan;
	
	public TaBortFrame(){
		rutan = new RecipeChooser(this);
	}
	
	

	public void kor() {
		rutan.setVisible(true);
	}

	@Override
	public void taRecept(String recept, RecipeChooser ruta) {
		TaBortRecept.taBortReceptet(recept);
		JOptionPane.showMessageDialog(rutan,"Receptet är nu borttaget.");
		skrivPDF.skriv();
		ruta.dispose();
		button.changeList(new TaBortFrame());
	}
	
}
