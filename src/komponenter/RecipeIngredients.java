package komponenter;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class RecipeIngredients extends JCheckBox implements ActionListener, Comparable<RecipeIngredients>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7151577439756413043L;
	
	JButton button = new JButton(new ImageIcon("Images/Plus.png"));
	ArrayList<JCheckBox> ingredients = new ArrayList<JCheckBox>();
	boolean klickat = false;
	
	public RecipeIngredients(String recipe, ArrayList<String> ingredients){
		super(recipe);
		button.setSize(11,11);
		button.addActionListener(this);
		this.addActionListener(this);
		for(String ingrediens : ingredients){
			this.ingredients.add(new JCheckBox(ingrediens));
			this.ingredients.get(this.ingredients.size() - 1).setSelected(true);
		}
		setSelected(true);
			
	}
	
	public RecipeIngredients(String recipe, String[] ingredients){
		super(recipe);
		this.addActionListener(this);
		for(String ingrediens : ingredients)
			this.ingredients.add(new JCheckBox(ingrediens));
	}
	
	public GridBagConstraints add(Container frame, GridBagConstraints g){
		int temp = g.gridx;
		g.gridx = 0;
		frame.add(button, g);
		g.gridx = 1;
		frame.add(this, g);
		g.gridy++;
		if(klickat)
			g = addButtons(frame, g);
		g.gridx = temp;
		return g;
	}
	
	public GridBagConstraints addButtons(Container frame, GridBagConstraints g){
		for(JCheckBox box : ingredients){
			frame.add(box, g);
			g.gridy++;
		}
		return g;
	}
	
	public void remove(Container frame){
		frame.remove(button);
		frame.remove(this);
		if(klickat)
			removeButtons(frame);
	}
	
	public void removeButtons(Container frame){
		for(JCheckBox box : ingredients){
			frame.remove(box);
		}
	}
	
	public int length(){
		if(klickat){
			return 1 + ingredients.size();
		}
		return 1;
	}

	public boolean clicked(){
		if(!klickat)
			button.setIcon(new ImageIcon("Images/Minus.png"));
		else
			button.setIcon(new ImageIcon("Images/Plus.png"));
		klickat = !klickat;
		return klickat;
	}
	
	public JButton button(){
		return button;
	}
	
	public void remove(ArrayList<JCheckBox> list){
		for(int i = 0; i < list.size(); i++){
			if(ingredients.contains(list.get(i))){
				list.remove(i);
				i--;
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this)
			for(JCheckBox box : ingredients){
				box.setSelected(this.isSelected());
			}
	}
	
	public int number(){
		return ingredients.size();
	}
	
	public ArrayList<JCheckBox> ingredients(){
		return ingredients;
	}
	
	public void setSelected(boolean selected){
		super.setSelected(selected);
		for(JCheckBox box : ingredients){
			box.setSelected(this.isSelected());
		}
	}

	@Override
	public int compareTo(RecipeIngredients o) {
		return getText().compareTo(o.getText());
	}
}
