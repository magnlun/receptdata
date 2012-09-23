package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import komponenter.button;

import model.Connect;
import model.Lösenord;



public class Meny extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -937759839771070458L;
	ArrayList<button> knappar = new ArrayList<button>();
	
	Meny(){
		setVisible(true);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		knappar.add(new button("Sätta in ett nytt recept", 0));
		knappar.add(new button("Hämta ett redan existerande recept", 1));
		knappar.add(new button("Skriv ut kokbok", 2));
		knappar.add(new button("Skapa kokbok som pdf", 3));
		knappar.add(new button("Sök efter recept mha ingrediens", 4));
		knappar.add(new button("Ta bort recept",5));
		knappar.add(new button("Lägg till en bild till ett recept",6));
		knappar.add(new button("Skapa en inköpslista",7));
		for(int i = 0; i < knappar.size(); i++){
			knappar.get(i).addActionListener(this);
			this.add(knappar.get(i),c);
			c.gridy++;
		}
		this.pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void dispose(){
		Connect.close();
		System.exit(0);
	}

	public void actionPerformed(ActionEvent e) {
		((button)e.getSource()).functionCall();
	}
	
	public static void main(String[] args){
		Lösenord.putName("my.nada.kth.se","magnlun");
		Lösenord.putName("jdbc:postgresql://127.0.0.1:5432/Recept","postgres");
		new Meny();
	}

}
