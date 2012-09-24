package model;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Iterator;

import userInterface.RecipeWindow;

import databaskomm.SkrivUt;

public class SkrivUtAllt implements Printable {

	String[] recept;
	ArrayList<ArrayList<String>> pages = new ArrayList<ArrayList<String>>();

	public void skriv(String[] recept) {
		this.recept = recept;
		RecipeWindow[] receptFönster = new RecipeWindow[recept.length];
		for (int i = 0; i < recept.length; i++) {
			try {
				receptFönster[i] = new RecipeWindow(SkrivUt.skriv(recept[i],
						0), recept[i], false);
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
		for(RecipeWindow fönster : receptFönster){
			pages.addAll(fönster.utskrift());
		}
		pages.add(null);
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		Book book = new Book();
		book.append(this, job.defaultPage(), pages.size()-1);
		job.setPageable(book);
		boolean doPrint = job.printDialog();
		if (doPrint) {
			try {
				job.print();
			} catch (PrinterException p) {
				p.printStackTrace();
				System.exit(1);
			}
		}

	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		int margin = 82;
		int enter = 12;
		int y = margin;
		if (pageIndex == 0) {
			graphics.drawString("Magnus kokbok", 250, 400);
			return PAGE_EXISTS;
		}
		if (pageIndex == 1) {
			Font font = new Font("Monospaced", 0, 12);
			graphics.setFont(font);
			for (int j = 0; j < recept.length; j++) {
				String Line = recept[j].replace("  ", "");
				for (int k = Line.length(); k < 50; k++) {
					Line += ".";
				}
				Line += j + 1;
				graphics.drawString(Line, margin, y);
				y += enter;
			}
			return PAGE_EXISTS;
		}
		Font rubrik = new Font("Times New Roman",Font.BOLD,14);
		Font brödtext = new Font("Monospaced",0,12);
		graphics.setFont(brödtext);
		ArrayList<String> page = pages.get(pageIndex-2);
		if(page == null){
			return NO_SUCH_PAGE;
		}
		for(Iterator<String> i = page.iterator() ; i.hasNext();){
			String Line = i.next();
			if(Line == null){
				graphics.setFont(rubrik);
				graphics.drawString(i.next(), margin, y);
				graphics.setFont(brödtext);
				y += enter;
				continue;
			}
			graphics.drawString(Line.replace('–', '-'), margin, y);
			y += enter;
		}
		graphics.drawString(""+pageIndex, 500, 750);
		return PAGE_EXISTS;
	}
}
