package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import komponenter.RecipeIngredients;

import model.skrivPDF;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class CreateBuylist extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8291212883166537575L;
	TreeSet<RecipeIngredients> tree;
	JButton alltinget;
	JButton knapp;
	HashMap<JButton, RecipeIngredients> list = new HashMap<JButton, RecipeIngredients>();
	
	public CreateBuylist(TreeSet<RecipeIngredients> tree){
		setLayout(new GridBagLayout());
		this.tree = tree;
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.LINE_START;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridwidth = 2;
		add(new JLabel("Vilka ingredienser vill du lägga till?"), g);
		g.gridwidth = 1;
		g.gridx = 2;
		knapp = new JButton("Klar");
		knapp.addActionListener(this);
		alltinget = new JButton("Markera/avmarkera alla");
		alltinget.addActionListener(this);
		add(knapp, g);
		g.gridy = 1;
		add(alltinget,g);
		g.gridx = 1;
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridy = g.gridy;
		gc.anchor = GridBagConstraints.LINE_START;
		for(RecipeIngredients ingredients : tree){
			ingredients.add(this, gc);
			ingredients.button().addActionListener(this);
			list.put(ingredients.button(), ingredients);
		}
		pack();
		setVisible(true);
	}
	public void Skapa(HashMap<String,LinkedList<String>> ingredienser){
		String file = "C:\\Users\\Magnus\\Documents\\inkopslista.pdf";
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		document.open();
		Paragraph titel = new Paragraph("Ingredienser", FontFactory.getFont(
				FontFactory.HELVETICA, 30, Font.BOLDITALIC));
		titel.setAlignment(Paragraph.ALIGN_CENTER);
		Chapter title = new Chapter(titel, 0);
		title.setNumberDepth(0);
		Iterator<String> itr = ingredienser.keySet().iterator();
		Paragraph list = new Paragraph("",
				FontFactory.getFont(FontFactory.TIMES, 12));
		while(itr.hasNext()){
			String recept = itr.next();
			LinkedList<String> ingred = ingredienser.get(recept);
			Anchor link = new Anchor(recept);
			link.setReference("www.nada.kth.se/~magnlun/kokbok.pdf#"+recept.replace("å", "a").replace("ä", "a").replace("ö","o"));
			list.add(link);
			list.add("\n");
			int i = 0;
			for(String ingrediens : ingred){
				i++;
				list.add("   " + i + ". " + ingrediens);
				list.add("\n");
			}
			
		}
		title.add(list);
		try {
			document.add(title);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();
		skrivPDF.laddaUpp(file, "inkopslista");
		this.dispose();
	}
	
	public HashMap<String,LinkedList<String>> save(){
		HashMap<String,LinkedList<String>> rc = new HashMap<String,LinkedList<String>>();
		try{
			File fil = new File("Inköp.txt");
			FileWriter br = new FileWriter(fil);
			for(RecipeIngredients box : tree){
				boolean checked = false;
				String utskrift = "";
				for(JCheckBox boxen : box.ingredients()){
					if(boxen.isSelected()){
						if(!checked){
							checked = true;
							utskrift += box.getText() + "\n";
							rc.put(box.getText(), new LinkedList<String>());
						}
						utskrift += "¤" + boxen.getText() + "\n";
						rc.get(box.getText()).add(boxen.getText());
					}
				}
				br.write(utskrift);
			}
			br.close();
		}
		catch(Exception err){
			err.printStackTrace();
		}
		return rc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == alltinget){
			markeraalltinget();
			return;
		}
		else if(e.getSource() == knapp){
			HashMap<String, LinkedList<String>> ingredienser = save();
			Skapa(ingredienser);
			return;
		}
		else{
			if(list.get(e.getSource()) != null){
				int index = 0;
				boolean correct = false;
				ArrayList<RecipeIngredients> removed = new ArrayList<RecipeIngredients>();
				RecipeIngredients box = list.get(e.getSource());
				Iterator<RecipeIngredients> itr = tree.iterator();
				while(itr.hasNext()){
					RecipeIngredients boxen = itr.next();
					if(!correct)
						if(boxen != box)
							index += boxen.length();
						else
							index++;
					if(boxen == box || correct){
						correct = true;
						boxen.remove(this);
						removed.add(boxen);
					}
				}
				box.clicked();
				GridBagConstraints gc = new GridBagConstraints();
				gc.anchor = GridBagConstraints.LINE_START;
				gc.gridx = 1;
				gc.gridy = index;
				box.add(this, gc);
				for(int i = 1; i < removed.size(); i++){
					gc = removed.get(i).add(this, gc);
				}
				this.pack();
			}
		}
	}
	public void markeraalltinget(){
		Iterator<RecipeIngredients> itr = tree.iterator();
		RecipeIngredients first = itr.next();
		while(itr.hasNext()){
			itr.next().setSelected(!first.isSelected());
		}
		first.setSelected(!first.isSelected());
	}
}
