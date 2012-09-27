package userInterface;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import komponenter.button;

import model.Ruta;

import databaskomm.SkrivUt;
import databaskomm.Fetch;


public class RecipeSearch extends JFrame implements ActionListener, Ruta{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4555075445286643025L;
	JPanel temp;
	temp fält;
	
	public RecipeSearch(){
		this.setLayout(new GridBagLayout());
		fält = new temp();
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		add(fält, g);
		JButton knapp = new JButton("Ok");
		knapp.addActionListener(this);
		temp = new JPanel();
		temp.add(knapp,g);
		g.gridy = 1;
		add(temp,g);
		pack();
	}
	
	class temp extends JPanel implements MouseListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1774228095837043698L;
		temp2 panel;
		JTable recept;
		JTextField ingrediens;
		JTextField Inteingrediens;
		
		temp(){
			panel = new temp2();
			add(panel);
			recept = new JTable(15,1);
			recept.addMouseListener(this);
			add(recept);
			ingrediens = panel.ingrediens;
			Inteingrediens = panel.Inteingrediens;
			
		}
		class temp2 extends JPanel{
			/**
			 * 
			 */
			private static final long serialVersionUID = 360056638897623543L;
			JTextField ingrediens;
			JTextField Inteingrediens;
			JComboBox<String> Kategori;
			
			temp2(){
				add(new JLabel("Ingrediens:"));
				ingrediens = new JTextField();
				add(ingrediens);
				ingrediens.setColumns(10);
				Inteingrediens = new JTextField();
				add(new JLabel("Exkludera ingrediens:"));
				add(Inteingrediens);
				add(new JLabel("Kategori:"));
				Kategori = new JComboBox<String>();
				Kategori.addItem("Valfri");
				try {
					String[][] kategorier = Fetch.fetching("SELECT * FROM \"Kategorier\";");
					for(int i = 1; i < kategorier.length; i++){
						Kategori.addItem(kategorier[i][0]);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					System.exit(ABORT);
				}
				add(Kategori);
				Inteingrediens.setColumns(10);
				setLayout(new GridLayout(3,2));
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			int row = recept.rowAtPoint(e.getPoint());
			int col = recept.columnAtPoint(e.getPoint());
			if(recept.getValueAt(row, col) != null){
				String recept = (String) (this.recept.getValueAt(row,col));
				String[][] Ingredienser;
				try {
					Ingredienser = SkrivUt.skriv(recept,0);
					button.changeList(new RecipeSearch());
					new RecipeWindow(Ingredienser, recept);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}
	
	public String[][] query(String[] ingrediens, String[] Inteingrediens, String Kategori) throws SQLException{
		String query;
		for(int i = 0; i < ingrediens.length; i++){
			query = "SELECT \n \"Ingrediens\" FROM\n \"Alias\" WHERE \"Alias\" = '" + ingrediens[i] + "';";
			String[][] svar = Fetch.fetching(query);
			if(svar.length > 1){
				ingrediens[i] = svar[1][0];
			}
		}
		query = "SELECT\n  DISTINCT(\"Bakverk\".\"BakNamn\")\nFROM\n  \"Bakverk\"";
		if(!Kategori.equals(""))
			query += ",\n  \"TillhorKategori\"";
		if(!ingrediens[0].equals("")){
			for(int i = 1; i < ingrediens.length; i++){
				query += ",\n  \"Innehall\" \"Ingrediens"+i+"\""+"";
			}
			query += ",\n  \"Innehall\" \"Ingrediens"+ingrediens.length+"\"\n";
		}
		query += "\nWHERE\n";
		query += "  \"Bakverk\".\"BakNamn\" = \"Bakverk\".\"BakNamn\"";
		if(!Kategori.equals("")){
			query += " AND\n  \"TillhorKategori\".\"Kategori\" = '" + Kategori + "' AND\n";
			query += "  \"Bakverk\".\"BakNamn\" = \"TillhorKategori\".\"BakNamn\"";
		}
		if(!ingrediens[0].equals("")){
			for(int i = 0; i < ingrediens.length; i++)
				query += " AND\n  \"Bakverk\".\"BakNamn\" = \"Ingrediens"+(i+1)+"\".\"BakNamn\"";
			for(int i = 1; i < ingrediens.length; i++)
				query += " AND\n  \"Ingrediens"+i+"\".\"Ingrediens\" = '"+ingrediens[i - 1]+"'";
			query += " AND\n  \"Ingrediens"+ingrediens.length+"\".\"Ingrediens\" = '"+ingrediens[ingrediens.length-1]+"'";
		}
		if(!Inteingrediens[0].equals("")){
			query += " AND\n  \"Bakverk\".\"BakNamn\" NOT IN";
			query += "\n  (SELECT DISTINCT(\"BakNamn\") FROM \"Innehall\" WHERE ";
			for(int i = 0; i < Inteingrediens.length - 1; i++)
				query += "\"Ingrediens\" = '"+ Inteingrediens[i] + "' OR ";
			query += "\"Ingrediens\" = '"+ Inteingrediens[Inteingrediens.length - 1] + "')";
		}
		query += ";";
		return Fetch.fetching(query);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] ingrediens = fält.ingrediens.getText().split(",");
		String[] Inteingrediens = fält.Inteingrediens.getText().split(",");
		for(int i = 1; i < ingrediens.length; i++)
			while(ingrediens[i].charAt(0) == ' ')
				ingrediens[i] = ingrediens[i].substring(1);

		for(int i = 1; i < Inteingrediens.length; i++)
			while(Inteingrediens[i].charAt(0) == ' ')
				Inteingrediens[i] = Inteingrediens[i].substring(1);
			
			
		try {
			String kategori = (String)this.fält.panel.Kategori.getSelectedItem();
			if(kategori.equals("Valfri")){
				kategori = "";
			}
			String[][] temp = query(ingrediens,Inteingrediens,kategori);
			for(int i = 1; i < temp.length && i <= fält.recept.getRowCount(); i++){
				fält.recept.setValueAt(temp[i][0], i - 1, 0);
			}
			for(int i = 0 ; i < fält.recept.getRowCount() - temp.length; i++){
				fält.recept.setValueAt("", i+temp.length-1, 0);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(ABORT);
		}
	}

	@Override
	public void kor() {
		setVisible(true);
	}

}
