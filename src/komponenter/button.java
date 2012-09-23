package komponenter;

import javax.swing.JButton;

import userInterface.FasInput;
import userInterface.Ink�pslista;
import userInterface.SkrivUtKokbok;
import userInterface.receptS�k;

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
			new SkrivUtKokbok(), new skrivPDF(), new receptS�k(),
			new TaBortFrame(), new LaggTillBild(), new Ink�pslista() };
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
