
package org.selectbf;


public class CancelProcessException extends SelectBfException
{

	public CancelProcessException(String msg)
	{
		super(msg);
		
	}
	
	public String toString()
	{
		String str = "CancelProcessException: "+super.getMessage();
		
		return str;
	}
}
