package komponenter;

import javax.swing.JButton;

import userInterface.FasInput;
import userInterface.Buylist;
import userInterface.SkrivUtKokbok;
import userInterface.RecipeSearch;

import model.LaggTillBild;
import model.Ruta;
import model.TaBortFrame;
import model.skrivPDF;
import model.SkrivUtRecept;

public class button extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7407099534132391221L;
	static Ruta[] list = { new FasInput(), new SkrivUtRecept(),
			new SkrivUtKokbok(), new skrivPDF(), new RecipeSearch(),
			new TaBortFrame(), new LaggTillBild(), new Buylist() };
	int index;

	public button(String text, int index) {
		setText(text);
		this.index = index;
	}

	public void functionCall() {
		list[this.index].kor();
	}
	
	public static void changeList(Ruta frame){
		for(int i = 0; i < list.length; i++)
			if(frame.getClass() == list[i].getClass())
				list[i] = frame;
	}

}
