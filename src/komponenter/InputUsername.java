package komponenter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class InputUsername extends JDialog implements ActionListener, DocumentListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3936506239760471170L;
	JTextField userName;
	JPasswordField password;
	String namn = "";
	char[] lösen = new char[0];
	
	private InputUsername(String server){
		setModal(true);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		JLabel name = new JLabel("Server: " + server);
		add(name, c);
		c.gridwidth = 1;
		c.gridy = 2;
		JLabel användar = new JLabel("Användarnamn:");
		JLabel lösen = new JLabel("Lösenord:");
		userName = new JTextField();
		password = new JPasswordField();
		userName.setColumns(20);
		userName.addActionListener(this);
		userName.getDocument().addDocumentListener(this);
		password.setColumns(20);
		password.addActionListener(this);
		password.getDocument().addDocumentListener(this);
		JButton ok = new JButton("Ok");
		ok.addActionListener(this);
		add(användar, c);
		c.gridy++;
		add(lösen, c);
		c.gridy--;
		c.gridx = 1;
		c.gridwidth = 2;
		add(userName,c);
		c.gridy++;
		add(password,c);
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		add(ok,c);
		pack();
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
	}
	
	public static String[] användarnamn(String server){
		String lösenord = "";
		InputUsername temp = new InputUsername(server);
		for(int i = 0; i < temp.lösen.length; i++)
			lösenord += temp.lösen[i];
		return new String[] {temp.namn, lösenord};
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		namn = userName.getText();
		lösen = password.getPassword();
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		namn = userName.getText();
		lösen = password.getPassword();
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		namn = userName.getText();
		lösen = password.getPassword();
	}
	
}
