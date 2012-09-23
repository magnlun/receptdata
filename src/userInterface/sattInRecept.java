package userInterface;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import komponenter.DropDownList;
import komponenter.HintTextField;
import komponenter.JaNejRuta;
import model.LaggTillBild;
import model.Lista;
import model.NumericDocument;
import model.skrivPDF;
import databaskomm.AddKategori;
import databaskomm.FixaText;
import databaskomm.SattIn;

public class sattInRecept extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 303220668689533880L;
	JTextField namn;
	JTextField betyg;
	JTextArea beskrivning;
	JTextField portioner;
	JScrollPane rulla;
	JRadioButton prefix1;
	JRadioButton prefix2;
	Lista kategori = new Lista();
	String[] faser;
	ArrayList<JTextArea> ingredienser = new ArrayList<JTextArea>();

	sattInRecept(String[] faser) {
		ArrayList<JPanel> paneler = new ArrayList<JPanel>();
		for (int i = 0; i < faser.length; i++) {
			JPanel temp = new JPanel();
			JTextArea text = new JTextArea();
			ingredienser.add(text);
			JScrollPane rulla2 = new JScrollPane(text);
			rulla2.setPreferredSize(new Dimension(150, 155));
			temp.add(rulla2);
			paneler.add(temp);
		}
		this.faser = faser;
		setLayout(new GridBagLayout());
		GridBagConstraints rad1 = new GridBagConstraints();
		rad1.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Namn:"), rad1);
		rad1.gridx = 1;
		add(new JLabel("Kategori:"), rad1);
		rad1.gridx = 2;
		add(new JLabel("Betyg:"), rad1);
		rad1.gridx = 3;
		add(new JLabel("Portioner:"), rad1);
		rad1.gridx = 4;
		add(new JLabel("Ingredienser:"), rad1);
		rad1.gridx = 5;
		add(new JLabel("Beskrivning:"), rad1);
		namn = new JTextField();
		kategori.addNew(this);
		betyg = new JTextField();
		beskrivning = new JTextArea();
		portioner = new JTextField();
		namn.setColumns(10);
		betyg.setColumns(2);
		betyg.setDocument(new NumericDocument());
		beskrivning.setColumns(10);
		portioner.setColumns(10);
		portioner.setDocument(new NumericDocument());
		rulla = new JScrollPane(beskrivning);
		rulla.setPreferredSize(new Dimension(150, 155));
		JButton knapp = new JButton("Klar");
		knapp.addActionListener(this);
		GridBagConstraints rad2 = new GridBagConstraints();
		rad2.gridy = 1;
		add(namn, rad2);
		rad2.gridx = 1;
		add(kategori.get(0), rad2);
		rad2.gridx = 2;
		add(betyg, rad2);
		rad2.gridx = 3;
		add(portioner, rad2);
		rad2.gridheight = 8;
		rad2.gridx = 4;
		JTabbedPane tabbedPane = new JTabbedPane();
		for (int i = 0; i < paneler.size(); i++)
			tabbedPane.add(faser[i], paneler.get(i));
		add(tabbedPane, rad2);
		rad2.gridx = 5;
		add(rulla, rad2);
		rad2.gridx = 6;
		add(knapp, rad2);
		prefix1 = new JRadioButton("Ingrediens volym prefix");
		prefix2 = new JRadioButton("Volym prefix ingrediens");
		ButtonGroup group = new ButtonGroup();
		group.add(prefix1);
		group.add(prefix2);
		GridBagConstraints rad3 = new GridBagConstraints();
		rad3.gridx = 4;
		rad3.gridy = 9;
		add(prefix1, rad3);
		rad3.gridy = 10;
		add(prefix2, rad3);
		this.pack();
		this.setVisible(true);
	}

	public void klickatPåLista(DropDownList lista) {
		String kategorien = (String) lista.getSelectedItem();
		HintTextField nyKategori = lista.text();
		if (kategorien.equals("Ny kategori      ")) {
			if (nyKategori == null) {
				nyKategori = new HintTextField("Ny Kategori");
				lista.setText(nyKategori);
				nyKategori.setColumns(10);
				kategori.addText();
				kategori.moveDownItems(lista.gety() + 1);
				redraw();
			}
		} else if (kategorien.equals("")) {
			if (lista != kategori.getLast()) {
				removeAll();
				kategori.remove(lista);
				addAll();
			}
			return;
		} else if (nyKategori != null) {
			remove(nyKategori);
			lista.setText(null);
			nyKategori = null;
		}
		GridBagConstraints extra = new GridBagConstraints();
		extra.gridx = 1;
		extra.gridy = kategori.gety();
		kategori.fixText(lista);
		if (lista == kategori.getLast()) {
			add(kategori.addNew(this), extra);
		}
		this.pack();
		return;
	}

	public void actionPerformed(ActionEvent e) {
		DropDownList lista = null;
		try {
			lista = (DropDownList) e.getSource();
		} catch (Exception err) {
			klickatPåKnapp();
			return;
		}
		klickatPåLista(lista);
		return;
	}

	public void removeAll() {
		for (int i = 0; i < kategori.size(); i++) {
			remove(kategori.get(i));
			if (kategori.get(i).text() != null) {
				remove(kategori.get(i).text());
			}
		}
	}

	public void addAll() {
		GridBagConstraints add = new GridBagConstraints();
		add.gridx = 1;
		add.gridy = 1;
		for (int i = 0; i < kategori.size(); i++) {
			add(kategori.get(i), add);
			add.gridy++;
			if (kategori.get(i).text() != null) {
				add(kategori.get(i).text(), add);
				add.gridy++;
			}
		}
		this.pack();
	}

	public void redraw() {
		removeAll();
		addAll();
	}

	public void klickatPåKnapp() {
		String[] kategorier = new String[kategori.size() - 1];
		try {
			for (int i = 0; i < kategori.size() - 1; i++) {
				if (kategori.get(i).text() != null) {
					kategorier[i] = kategori.get(i).text().getText();
					AddKategori.kategori(kategorier[i]);
				} else {
					kategorier[i] = (String) kategori.get(i).getSelectedItem();
				}
			}
			String[] temp = new String[faser.length];
			if (prefix2.isSelected()) {
				temp = split();
			} else if (prefix1.isSelected()) {
				for (int i = 0; i < faser.length; i++) {
					temp[i] = (this.ingredienser).get(i).getText();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Välj en ordning", "Fel!!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			ArrayList<ArrayList<String>> ingrediensFaser = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < temp.length; i++) {
				ingrediensFaser.add(new ArrayList<String>());
				ingrediensFaser.get(i).add(faser[i]);
				Scanner sc = new Scanner(temp[i]);
				while (sc.hasNextLine()) {
					String temp2 = sc.nextLine();
					char bokstav = temp2.charAt(0);
					if ((bokstav > 96 && bokstav < 122) || bokstav == 246
							|| bokstav == 228 || bokstav == 229) {
						char a = (char) (temp2.charAt(0) - 32);
						temp2 = temp2
								.replaceFirst(temp2.charAt(0) + "", a + "");
					}
					ingrediensFaser.get(i).add(temp2);
				}
				sc.close();
			}
			String instruktioner = FixaText.fixaTexten(beskrivning.getText());
			if(SattIn.sattIn(namn.getText(), kategorier,
					Integer.parseInt(betyg.getText()), instruktioner,
					Integer.parseInt(portioner.getText()), ingrediensFaser,
					this)){
				JOptionPane.showMessageDialog(this, "Receptet är instoppat");
				setVisible(false);
				if (JaNejRuta.val("Har du en bild på bakverket?", "Fråga", this)) {
					LaggTillBild.sattIn(namn.getText(), this);
				}
				skrivPDF.skriv();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public String[] split() {
		String[] rc = new String[faser.length];
		for (int j = 0; j < faser.length; j++) {
			Scanner sc = new Scanner(ingredienser.get(j).getText());
			String temp3 = "";
			while (sc.hasNextLine()) {
				String temp = sc.nextLine();
				String[] temp2 = temp.split(" ");
				for (int i = 2; i < temp2.length; i++)
					temp3 += temp2[i] + " ";
				temp3 += temp2[0] + " " + temp2[1] + "\n";
			}
			rc[j] = temp3;
			sc.close();
		}
		return rc;
	}
}
