package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFrame;

import databaskomm.LetaRecept;
import databaskomm.SearchForDate;

import model.Kalender;
import model.Ruta;
import model.SkrivUtAllt;

public class SkrivUtKokbok extends JFrame implements Ruta, ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5362905841929890170L;
	
	JButton allt;

	public SkrivUtKokbok(){
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.fill = GridBagConstraints.HORIZONTAL;
		allt = new JButton("Skriv ut allt");
		JButton lite = new JButton("Skriv ut det nya");
		allt.addActionListener(this);
		lite.addActionListener(this);
		add(allt,g);
		g.gridy = 1;
		add(lite,g);
		pack();
	}

	@Override
	public void kor() {
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File fil = new File("senasteutskrift.txt");
		String[] recept = null;
		if(e.getSource() == allt){
			recept = LetaRecept.Recept("");
		}
		else{
			String datum = "";
			String tid = "";
			try{
				FileReader read = new FileReader(fil);
				for(int i = 0; i < 10; i++)
					datum += (char)read.read();
				
				for(int i = 0; i < 5; i++)
					tid += (char) read.read();
				recept = SearchForDate.sök(datum, tid);
				read.close();
			}
			catch(Exception err){
				recept = LetaRecept.Recept("");
			}
		}
		SkrivUtAllt skriv = new SkrivUtAllt();
		skriv.skriv(recept);
		try{
			Kalender datum = new Kalender();
			FileWriter writer = new FileWriter(fil);
			writer.write(datum.getDate());
			writer.write(datum.getClock());
			writer.close();
		}
		catch(Exception err){
			err.printStackTrace();
		}
	}

}