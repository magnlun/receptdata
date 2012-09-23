package komponenter;

import java.awt.event.ActionListener;
import java.util.SortedSet;

import javax.swing.JComboBox;


public class DropDownList extends JComboBox<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7638901888683819221L;
	
	HintTextField text;
	int y;
	String valt;
	ActionListener listener;

	public DropDownList(ActionListener listener, int y, SortedSet<String> kategorier){
		this.y = y;
		addItem("");
		for(String kategori : kategorier){
			if(kategori != null)
				addItem(kategori);
		}
		addItem("Ny kategori      ");
		this.listener = listener;
		addActionListener(listener);
	}
	
	public void updateList(SortedSet<String> kategorier){
		this.removeActionListener(listener);
		this.removeAllItems();
		addItem(valt);
		addItem("");
		for(String kategori : kategorier){
			if(kategori != null)
				addItem(kategori);
		}
		addItem("Ny kategori      ");
		this.addActionListener(listener);
	}
	
	public void setValt(String valt){
		this.valt = valt;
	}
	
	public String getValt(){
		return valt;
	}
	
	public void setText(HintTextField textField){
		text = textField;
	}
	
	public HintTextField text(){
		return text;
	}
	
	public int gety(){
		return y;
	}
	
	public void moveUp(){
		y--;
	}
	
	public void remove(Object element){
		for(int i = 0; i < getItemCount(); i++){
			if(getItemAt(i).equals(element)){
				removeItemAt(i);
			}
		}
	}
	
	public void moveDown(){
		y++;
	}
	
}
