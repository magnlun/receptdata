package komponenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class RecipeIngredients extends JCheckBox implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7151577439756413043L;
	
	JButton button = new JButton(new ImageIcon("Images/plus.png"));
	ArrayList<JCheckBox> ingredients = new ArrayList<JCheckBox>();
	boolean klickat = false;
	
	public RecipeIngredients(String recipe, ArrayList<String> ingredients){
		super(recipe);
		button.setSize(11,11);
		button.addActionListener(this);
		this.addActionListener(this);
		for(String ingrediens : ingredients)
			this.ingredients.add(new JCheckBox(ingrediens));
	}
	
	public RecipeIngredients(String recipe, String[] ingredients){
		super(recipe);
		this.addActionListener(this);
		for(String ingrediens : ingredients)
			this.ingredients.add(new JCheckBox(ingrediens));
	}

	public boolean clicked(){
		if(!klickat)
			button.setIcon(new ImageIcon("Images/minus.png"));
		else
			button.setIcon(new ImageIcon("Images/plus.png"));
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
}
