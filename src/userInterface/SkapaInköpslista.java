package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.skrivPDF;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class SkapaInköpslista extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8291212883166537575L;
	LinkedList<JCheckBox> boxar = new LinkedList<JCheckBox>();
	JButton alltinget;
	
	public SkapaInköpslista(TreeSet<String> ingredienser){
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.anchor=GridBagConstraints.LINE_START;
		g.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Vilka ingredienser vill du lägga till?"), g);
		g.gridx = 1;
		JButton knapp = new JButton("Klar");
		knapp.addActionListener(this);
		alltinget = new JButton("Markera/avmarkera alla");
		alltinget.addActionListener(this);
		add(knapp, g);
		g.gridy = 1;
		add(alltinget,g);
		g.gridx = 0;
		for(String ingrediens : ingredienser){
			boxar.add(new JCheckBox(ingrediens, true));
			add(boxar.getLast(), g);
			g.gridy++;
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
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == alltinget){
			markeraalltinget();
			return;
		}
		LinkedList<String> ingredienser = new LinkedList<String>();
		for(JCheckBox box : boxar){
			if(box.isSelected()){
				ingredienser.add(box.getText());
			}
		}
		File fil = new File("Inköp.txt");
		try {
			FileWriter Write = new FileWriter(fil);
			for(String sak : ingredienser)
				Write.write(sak + "\n");
			Write.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Skapa(ingredienser);
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
