package model;

import java.util.GregorianCalendar;



public class Kalender extends GregorianCalendar{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4535762481568335420L;

	public String getDate(){
		String rc = get(YEAR) + "-";
		if(get(MONTH) < 9)
			rc += "0";
		rc += (get(MONTH) + 1) + "-";
		if(get(DATE) < 10)
			rc += "0";
		rc += get(DATE);
		
		return rc;
	}
	
	public String getClock(){
		String rc = "";
		if(get(HOUR_OF_DAY) < 10)
			rc += "0";
		rc += get(HOUR_OF_DAY) + ":";
		if(get(MINUTE) < 10)
			rc += "0";
		rc += get(MINUTE);
		return rc;
	}
}