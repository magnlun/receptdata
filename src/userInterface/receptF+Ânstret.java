package userInterface;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import databaskomm.FixaText;


public class receptFönstret extends JFrame implements
		ActionListener, Printable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5328238486509160728L;
	JTable innehall;
	JTextArea instruktioner;
	JScrollPane instruktionsPane;
	JButton skrivUt;
	JScrollPane tabell;
	boolean finnsMer = false;
	int fortsattning = 0;
	ArrayList<ArrayList<String>> pages = new ArrayList<ArrayList<String>>();
	
	public receptFönstret(String[][] innehall, String recept){
		this(innehall, recept, true);
	}
	public receptFönstret(String[][] innehall, String recept, Boolean visible) {
		ArrayList<String[]> ingredienser = fixToTable(innehall);
		GridBagConstraints g = new GridBagConstraints();
		setLayout(new GridBagLayout());
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridx = 0;
		g.gridy = 0;
		this.setTitle(recept);
		
		instruktioner = new JTextArea();
		instruktioner.setText(FixaText.fixaTexten(innehall[1][4].replace("  ","")));
		instruktionsPane = new JScrollPane(instruktioner);
		this.innehall = new JTable(ingredienser.size() - 1, 2);
		this.innehall.getColumnModel().getColumn(0).setHeaderValue(ingredienser.get(1)[0]);
		this.innehall.getColumnModel().getColumn(1).setHeaderValue(ingredienser.get(1)[1]);
		for (int i = 0; i < ingredienser.size() - 1; i++) {
			this.innehall.setValueAt(ingredienser.get(i + 1)[0], i, 0);
			this.innehall.setValueAt(ingredienser.get(i + 1)[1], i, 1);
		}
		tabell = new JScrollPane(this.innehall);
		//this.innehall.setPreferredSize(new Dimension(300,this.innehall.getHeight()));
		//tabell.setPreferredSize(new Dimension(this.innehall.getWidth(),this.innehall.getHeight()));
		add(this.innehall, g);
		g.gridx = 1;
		add(instruktionsPane, g);
		g.gridx = 0;
		g.gridy = 1;
		g.gridwidth = 2;
		instruktionsPane.setPreferredSize(new Dimension(400, 225));
		g.anchor = GridBagConstraints.PAGE_END;
		add((skrivUt = new JButton("Skriv ut")),g);
		skrivUt.addActionListener(this);
		pack();
		Dimension size = this.getSize();
		instruktionsPane.setSize(new Dimension(500, (int) (size.getHeight() - 90)));
		setVisible(visible);
		String Line = innehall[1][4].replace("  ","");
		String[] Lines = Line.split("\n");
		ArrayList<String> tabell = uträkning();
		int rader = tabell.size();
		pages.add(tabell);
		int j = 0;
		for(int i = 0; i < Lines.length; i++){
			String nyrad = "";
			String[] rad = Lines[i].split(" ");
			int l = 0;
			for(int k = 0; k < rad.length; k++){
				if(rad[k].length() > 62){
					pages.get(j).add(nyrad);
					pages.get(j).add(rad[k]);
					l = 0;
					nyrad = "";
					continue;
				}
				l += rad[k].length() + 1;
				if(l > 62){
					pages.get(j).add(nyrad);
					nyrad = "";
					l = rad[k].length() + 1;
				}
				nyrad += rad[k] + " ";
			}
			pages.get(j).add(nyrad);
			rader++;
			if(rader > 53){
				rader = 0;
				j++;
				pages.add(new ArrayList<String>());
			}
		}
		pages.add(null);
	}
	
	public ArrayList<ArrayList<String>> utskrift(){
		ArrayList<ArrayList<String>> rc = new ArrayList<ArrayList<String>>();
		for(int i = 0 ; i < pages.size()-1; i++){
			rc.add(pages.get(i));
		}
		return rc;
	}
	
	public static ArrayList<String[]> fixToTable(String[][] innehall){
		ArrayList<String> Faser = new ArrayList<String>();
		ArrayList<String[]> ingredienser = new ArrayList<String[]>();
		for(int i = 0; i < innehall.length; i++){
			if(!Faser.contains(innehall[i][innehall[i].length-1]))
				Faser.add(innehall[i][innehall[i].length-1]);
		}
		for(String fas : Faser){
			if(!fas.replace("  ", "").equals("")){
				ingredienser.add(new String[] {fas.replace("  ", "") + ":",""});
			}
			for (int j = 0; j < innehall.length; j++) {
				String fasen = innehall[j][innehall[j].length-1];
				if((fasen != null) && fasen.equals(fas)){
					ingredienser.add(new String[] { innehall[j][0].replace("  ", ""),
							innehall[j][1].replace("  ", "") });
					if (innehall[j][2] != null && j > 1) {
						ingredienser.add(new String[] { "eller", "" });
						ingredienser.add(new String[] { innehall[j][2].replace("  ", ""),
								innehall[j][3].replace("  ", "") });
					}
				}
			}
		}
		return ingredienser;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		print();
	}
	
	public void print(){
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean doPrint = job.printDialog();
		if (doPrint) {
			try {
				job.print();
			} catch (PrinterException p) {
				p.printStackTrace();
				System.exit(ABORT);
			}
		}
		
	}
	
	public JTable ingredienser(){
		return this.innehall;
	}
	
	public String instruktioner(){
		return this.instruktioner.getText();
	}
	
	public ArrayList<String> uträkning(){
		int maxL = 0;
		int maxR = 0;
		ArrayList<String> rc = new ArrayList<String>();
		rc.add(null);
		rc.add(this.getTitle());
		rc.add("\n");
		String Line = "";
		for (int i = 0; i < innehall.getRowCount(); i++) {
			if (((String) innehall.getValueAt(i, 0)).length() > maxL)
				maxL = ((String) innehall.getValueAt(i, 0)).length();
			if (((String) innehall.getValueAt(i, 1)).length() > maxR)
				maxR = ((String) innehall.getValueAt(i, 1)).length();
		}
		for (int i = fortsattning; i < innehall.getRowCount(); i++) {
			Line = (String) innehall.getValueAt(i, 0);
			for (int j = 0; j < maxL
					- ((String) innehall.getValueAt(i, 0)).length(); j++) {
				Line += " ";
			}
			Line += "|";
			Line += (String) innehall.getValueAt(i, 1);
			rc.add(Line);
			Line = "";
			for (int j = 0; j < maxR + maxL + 1; j++) {
				Line += "-";
			}
			rc.add(Line);
		}
		return rc;
	}
	
	@Override
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		ArrayList<String> sidan = pages.get(page);
		if(sidan == null){
			return NO_SUCH_PAGE;
		}
		int margin = 50;
		int enter = 12;
		int y = margin;
		Graphics2D g2d = (Graphics2D) g;
		Font rubrik = new Font("Times New Roman",Font.BOLD,14);
		Font brödtext = new Font("Monospaced",0,12);
		g.setFont(brödtext);
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		for(Iterator<String> i = sidan.iterator() ; i.hasNext();){
			String Line = i.next();
			if(Line == null){
				g.setFont(rubrik);
				g.drawString(i.next(), margin, y);
				g.setFont(brödtext);
				y += enter;
				continue;
			}
			g.drawString(Line, margin, y);
			y += enter;
		}
		return PAGE_EXISTS;
	}
}
