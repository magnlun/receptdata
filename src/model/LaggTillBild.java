package model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import komponenter.button;

import userInterface.ReceptVäljare;

import databaskomm.SattInBild;


public class LaggTillBild implements Ruta, ReceptMottagare{
	
	JFrame ruta;
	public LaggTillBild(){
		ruta = new ReceptVäljare(this);
	}

	@Override
	public void kor() {
		ruta.setVisible(true);
	}
	
	public static void sattIn(String recept, JFrame parent){
		JFileChooser fil = new JFileChooser();
		fil.showOpenDialog(parent);
		parent.dispose();
		String file = fil.getSelectedFile().toString();	
		String protokoll = file.replace(".", "#").split("#")[1];
		try {
			SattInBild.SattIn(ImageIO.read(new File(file)), recept.replace(" ", ""), protokoll);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(JFrame.ABORT);
		}
	}

	@Override
	public void taRecept(String recept, ReceptVäljare ruta) {
		sattIn(recept, ruta);
		button.changeList(new LaggTillBild());
		skrivPDF.skriv();
	}

}
