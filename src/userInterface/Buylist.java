package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import komponenter.RecipeIngredients;
import komponenter.button;

import model.ProcessRecept;
import model.ReceptMottagare;
import model.Ruta;


public class Buylist extends JFrame implements Ruta, ActionListener, DocumentListener, ReceptMottagare{
	
	TreeSet<String> recept = new TreeSet<String>();
	ArrayList<JTextField> ingredienser = new ArrayList<JTextField>();
	JButton klar;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7567385095277191994L;
	
	public Buylist(){
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.fill = GridBagConstraints.HORIZONTAL;
		ingredienser.add(new JTextField());
		ingredienser.get(0).getDocument().addDocumentListener(this);
		ingredienser.get(0).setColumns(10);
		add(ingredienser.get(0), g);
		g.gridx = 1;
		JButton temp = new JButton("Lägg till recept");
		temp.addActionListener(this);
		klar = new JButton("Klar!");
		klar.addActionListener(this);
		add(temp,g);
		g.gridy = 1;
		add(klar,g);
		pack();
	}

	@Override
	public void kor() {
		setVisible(true);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == klar){
			TreeSet<String> ingredienserText = new TreeSet<String>();
			for(int i = 0; i < ingredienser.size(); i++)
				if(ingredienser.get(i).getText().length() > 0)
					ingredienserText.add(ingredienser.get(i).getText());
			ArrayList<RecipeIngredients> tree = ProcessRecept.takeData(recept, ingredienserText);
			System.out.println(tree.size());
			new CreateBuylist(tree,ingredienserText);
			button.changeList(new Buylist());
		}
		else{
			RecipeChooser temp = new RecipeChooser(this);
			temp.setVisible(true);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(e.getDocument().getLength() == 1 && e.getDocument() == ingredienser.get(ingredienser.size()-1).getDocument()){
			JTextField nyttFält = new JTextField();
			nyttFält.setColumns(10);
			int index = ingredienser.size();
			GridBagConstraints g = new GridBagConstraints();
			g.gridy = index;
			g.gridx = 0;
			nyttFält.getDocument().addDocumentListener(this);
			add(nyttFält, g);
			ingredienser.add(nyttFält);
			pack();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {}

	@Override
	public void changedUpdate(DocumentEvent e) {}

	@Override
	public void taRecept(String recept, RecipeChooser ruta) {
		this.recept.add(recept);
		ruta.dispose();
	}
	
}
