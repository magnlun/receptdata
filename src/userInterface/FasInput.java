package userInterface;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import komponenter.button;

import model.Ruta;

public class FasInput extends JFrame implements ActionListener, Ruta{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3739302259008880074L;
	
	ArrayList<JLabel> labels = new ArrayList<JLabel>();
	JComboBox<Integer> antal;
	ArrayList<JTextField> text = new ArrayList<JTextField>();;
	int valt = 0;
	JButton ok;
	
	public FasInput(){
		setTitle("Hur många faser");
		ok = new JButton("OK!");
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		antal = new JComboBox<Integer>();
		for(int i = 1; i < 10; i++)
			antal.addItem(i);
		antal.addActionListener(this);
		c.gridwidth = 2;
		add(new JLabel("Hur många faser har receptet?"),c);
		c.gridx++;
		add(antal,c);
		c.fill = GridBagConstraints.VERTICAL;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridheight = 12;
		ok.addActionListener(this);
		c.gridy = 0;
		add(ok,c);
		valtAntal();
		this.pack();
	}

	public void kor() {
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == antal)
			valtAntal();
		else
			klickatPåOk();
	}
	
	public void klickatPåOk(){
		String[] Faser;
		if(text.size() > 0){
			Faser = new String[text.size()];
			for(int i = 0; i < text.size(); i++){
				Faser[i] = text.get(i).getText();
			}
		}
		else{
			Faser = new String[] {" "};
		}
		button.changeList(new FasInput());
		new sattInRecept(Faser);
		this.dispose();
	}
	
	public void valtAntal(){
		int nyVal = (Integer)antal.getSelectedItem();
		if(nyVal < valt){
			int size = text.size();
			for(int i = nyVal; i < size; i++){
				remove(text.get(nyVal));
				text.remove(nyVal);
				remove(labels.get(nyVal));
				labels.remove(nyVal);
			}
		}
		else{
			for(int i = valt; i < nyVal; i++){
				GridBagConstraints c = new GridBagConstraints();
				c.gridy = i+2;
				c.gridx = 0;
				JLabel temp = new JLabel("Fas "+(i+1)+":"); 
				add(temp,c);
				labels.add(temp);
				c.gridx = 1;
				text.add(new JTextField());
				text.get(i).setColumns(10);
				add(text.get(i),c);
			}
			
		}
		valt = nyVal;
		this.pack();
	}

}
