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
	ArrayList<JCheckBox> ingredienser = new ArrayList<JCheckBox>();
	ArrayList<RecipeIngredients> tree;
	LinkedList<JCheckBox> boxar = new LinkedList<JCheckBox>();
	JButton alltinget;
	JButton knapp;
	HashMap<JButton, RecipeIngredients> list = new HashMap<JButton, RecipeIngredients>();
	
	public CreateBuylist(ArrayList<RecipeIngredients> tree, TreeSet<String> ingredienser){
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
		for(String ingrediens : ingredienser){
			boxar.add(new JCheckBox(ingrediens, true));
			this.ingredienser.add(boxar.getLast());
			add(boxar.getLast(), g);
			g.gridy++;
		}
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridy = g.gridy;
		gc.anchor = GridBagConstraints.LINE_START;
		for(RecipeIngredients ingredients : tree){
			gc.gridx = 0;
			JButton button = ingredients.button();
			button.addActionListener(this);
			list.put(button,ingredients);
			boxar.add(ingredients);
			add(button,gc);
			gc.gridx = 1;
			add(ingredients,gc);
			gc.gridy++;
		}
		pack();
		setVisible(true);
	}
	public void Skapa(LinkedList<String> ingredienser){
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
		for(String ingrediens : ingredienser){
			Paragraph tjo = new Paragraph(ingrediens,
					FontFactory.getFont(FontFactory.TIMES, 12));
			title.add(tjo);
		}
		try {
			document.add(title);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();
		skrivPDF.laddaUpp(file, "inkopslista");
		this.dispose();
	}
	
	public void save(){
		try{
			File fil = new File("Inköp.txt");
			FileWriter br = new FileWriter(fil);

			for(JCheckBox ingrediens : ingredienser){
				if(ingrediens.isSelected()){
					br.write(ingrediens.getText());
					br.write("\n");
				}
			}
			boolean printed = false;
			for(RecipeIngredients box : tree){
				if(box.isSelected()){
					String utskrift = "";
					if(!printed){
						printed = true;
						utskrift = "Recept:\n";
					}
					utskrift += box.getText() + "\n";
					for(JCheckBox ingrediens : box.ingredients()){
						utskrift += "¤" + ingrediens.getText() + "\n";
					}
					br.write(utskrift);
				}
			}
			br.close();
		}
		catch(Exception err){
			err.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == alltinget){
			markeraalltinget();
			return;
		}
		else if(e.getSource() == knapp){
			save();
			LinkedList<String> ingredienser = new LinkedList<String>();
			for(JCheckBox box : boxar){
				if(box.isSelected()){
					ingredienser.add(box.getText());
				}
			}
			Skapa(ingredienser);
			return;
		}
		else{
			if(list.get(e.getSource()) != null){
				int index = 0;
				boolean correct = false;
				ArrayList<JCheckBox> removed = new ArrayList<JCheckBox>();
				RecipeIngredients box = list.get(e.getSource());
				Iterator<JCheckBox> itr = boxar.iterator();
				while(itr.hasNext()){
					JCheckBox boxen = itr.next();
					if(!correct)
						index++;
					if(boxen == box || correct){
						correct = true;
						try{
							RecipeIngredients temp = (RecipeIngredients) boxen;
							remove(temp.button());
						}
						catch(Exception err){
							//ignore	
						}
						remove(boxen);
						removed.add(boxen);
						itr.remove();
					}
				}
				GridBagConstraints gc = new GridBagConstraints();
				gc.anchor = GridBagConstraints.LINE_START;
				gc.gridx = 1;
				gc.gridy = index;
				boolean addAll = box.clicked();
				add(box, gc);
				gc.gridx = 0;
				add(box.button(),gc);
				gc.gridx = 1;
				boxar.add(box);
				gc.gridy++;
				if(addAll){
					for(JCheckBox boxar : box.ingredients()){
						add(boxar, gc);
						gc.gridy++;
						this.boxar.add(boxar);
					}
				}
				else{
					box.remove(removed);
				}
				for(int i = 1; i < removed.size(); i++){
					add(removed.get(i), gc);
					try{
						gc.gridx = 0;
						RecipeIngredients temp = (RecipeIngredients) removed.get(i);
						add(temp.button(),gc);
						gc.gridx = 1;
					}
					catch(Exception err){
						//ignore	
					}
					gc.gridy++;
					this.boxar.add(removed.get(i));
				}
				this.pack();
			}
		}
	}
	public void markeraalltinget(){
		if(boxar.get(0).isSelected())
			for(JCheckBox box : boxar)
				box.setSelected(false);
		else
			for(JCheckBox box : boxar)
				box.setSelected(true);
			
	}
}
