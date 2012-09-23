package model;
import javax.swing.text.*;
 
public class NumericDocument extends PlainDocument {
 
     /**
	 * 
	 */
	private static final long serialVersionUID = 7032129863377089899L;

	//Constructor
     public NumericDocument() {
     }
   
     //Insert string method
     public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
          if (str != null){
        	  try{
        		  Integer.parseInt(str);
        	  }
        	  catch(Exception err){
        		  return;
        	  }
               
               //All is fine, so add the character to the text box
               super.insertString(offset, str, attr);
          }
          return;
     }
     
}