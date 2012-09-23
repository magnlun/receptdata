package komponenter;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class JaNejRuta extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7645706451314101689L;
	
	final JOptionPane optionPane;
	private JaNejRuta(String budskap, String titel, JFrame ruta){
		super(ruta, titel, true);
		optionPane = new JOptionPane(budskap, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION);
		setContentPane(optionPane);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
			}
		});
		optionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();

				if (isVisible() && (e.getSource() == optionPane)
						&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
					// If you were going to check something
					// before closing the window, you'd do
					// it here.
					setVisible(false);
				}
			}
		});
		pack();
		setVisible(true);
	}
	
	public static boolean val(String budskap, String titel, JFrame ruta){
		JaNejRuta temp = new JaNejRuta(budskap,titel,ruta);
		int value = ((Integer)temp.optionPane.getValue()).intValue();
		if (value == JOptionPane.YES_OPTION)
		    return true;
		return false;
	}
}
