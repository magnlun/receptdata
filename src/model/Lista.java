package model;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import komponenter.DropDownList;

import databaskomm.Fetch;


public class Lista extends ArrayList<DropDownList> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1524656210002951395L;
	SortedSet<String> kategorier = new TreeSet<String>();
	int y = 1;

	public Lista() {
		try {
			String[][] kategorier = Fetch
					.fetching("SELECT * FROM \"Kategorier\";");
			for (int i = 1; i < kategorier.length; i++)

				this.kategorier.add(kategorier[i][0].replace("  ", ""));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DropDownList getLast() {
		return get(size() - 1);
	}

	public void moveItems(int index) {
		for (int j = index + 1; j < size(); j++) {

			get(j).moveUp();
		}
	}
	public void repaint(){
		for(DropDownList lista : this){
			lista.updateList(kategorier);
		}
	}

	public boolean remove(DropDownList item) {
		kategorier.add((String)item.getValt());
		for (int i = 0; i < size(); i++) {
			if (get(i) == item) {
				if (item.text() != null) {
					moveItems(i);
					y--;
				}
				moveItems(i);
				y--;
				super.remove(item);
				repaint();
				return true;
			}
		}
		return false;
	}

	public void addText() {
		y++;
	}

	public DropDownList addNew(ActionListener frame) {
		DropDownList rc = new DropDownList(frame, y, kategorier);
		add(rc);
		y++;
		return rc;
	}

	public void fixText(DropDownList meny) {
		meny.setValt((String) meny.getSelectedItem());
		kategorier.add(meny.getValt());
		kategorier.remove((String) meny.getSelectedItem());
		for (int i = 0; i < size(); i++)
			if (get(i) != meny)
				get(i).updateList(kategorier);
	}

	public void moveDownItems(int index) {
		for (int i = index; i < size(); i++) {
			get(i).moveDown();
		}
	}

	public int gety() {
		return y;
	}
}
