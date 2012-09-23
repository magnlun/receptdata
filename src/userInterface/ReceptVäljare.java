package userInterface;



import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import komponenter.HintTextField;
import model.NumericDocument;
import model.ReceptMottagare;
import databaskomm.LetaRecept;


public class ReceptVäljare extends JFrame implements DocumentListener, ActionListener{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7779404928851942311L;
	
	
	JComboBox<String> receptLista;
	HintTextField inmatning;
	JButton knapp;
	String[] recept;
	JTextField portioner;
	ReceptMottagare parent;
	
	public ReceptVäljare(ReceptMottagare förälder, Boolean dummy){
		this(förälder);
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		add(new JLabel("Portioner: "),g);
		portioner = new JTextField();
		portioner.setDocument(new NumericDocument());
		portioner.setColumns(10);
		g.gridx = 1;
		add(portioner,g);
		pack();
	}
	
	public ReceptVäljare(ReceptMottagare förälder){
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		parent = förälder;
		g.gridy = 1;
		g.gridx = 0;
		g.gridwidth = 2;
		knapp = new JButton("Ok");
		knapp.addActionListener(this);
		receptLista = new JComboBox<String>();
		receptLista.addActionListener(this);
		inmatning = new HintTextField("Recept:");
		inmatning.getDocument().addDocumentListener(this);
		add(inmatning,g);
		inmatning.setColumns(20);
		g.gridwidth = 1;
		g.gridx = 2;
		add(receptLista,g);
		g.gridx = 3;
		add(knapp,g);
		andraRecept();
		pack();
	}

	public void kor() {
		setVisible(true);
	}

	public void insertUpdate(DocumentEvent e) {
		andraRecept();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		andraRecept();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		andraRecept();
		
	}
	
	public String recept(ActionEvent e){
		if(e.getSource() == knapp)
			return receptLista.getItemAt(0);
		return (String) receptLista.getSelectedItem();
	}
	
	private void andraRecept(){
		receptLista.removeActionListener(this);
		recept = LetaRecept.Recept(inmatning.getText());
		receptLista.removeAllItems();
		for(int i = 0; i < recept.length; i++){
			receptLista.addItem(recept[i].replace("  ", " "));
		}
		receptLista.addActionListener(this);
		
	}
	
	public int portioner(){
		try{
			String rc =  portioner.getText();
			return Integer.parseInt(rc);
		}
		catch(Exception e){
			return 0;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		parent.taRecept(recept(e), this);		
	}

}
