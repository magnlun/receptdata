package komponenter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class Password extends JDialog implements ActionListener, DocumentListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3936506239760471170L;
	JPasswordField l�senord;
	char[] l�sen = new char[0];
	
	private Password(String server, String username){
		setModal(true);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		JLabel name = new JLabel("Server: " + server);
		add(name, c);
		JLabel logg = new JLabel("Anv�ndarnamn: " + username);
		c.gridy = 1;
		add(logg, c);
		c.gridwidth = 1;
		c.gridy = 2;
		JLabel l�sen = new JLabel("L�senord:");
		l�senord = new JPasswordField();
		l�senord.getDocument().addDocumentListener(this);
		l�senord.setColumns(20);
		l�senord.addActionListener(this);
		JButton ok = new JButton("Ok");
		ok.addActionListener(this);
		add(l�sen, c);
		c.gridx = 1;
		c.gridwidth = 2;
		add(l�senord,c);
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		add(ok,c);
		pack();
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}
	
	public static String l�senord(String server, String username){
		Password temp = new Password(server,username);
		String rc = "";
		for(int i = 0; i < temp.l�sen.length; i++)
			rc += temp.l�sen[i];
		return rc;
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		l�sen = l�senord.getPassword();
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		l�sen = l�senord.getPassword();
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		l�sen = l�senord.getPassword();
	}
	
}
