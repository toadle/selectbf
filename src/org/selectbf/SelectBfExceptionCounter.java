package org.selectbf;

import java.lang.reflect.Field;


public class SelectBfExceptionCounter
{
	static int[] exceptions = new int[100];
	static int genericExceptions = 0;
	
	public static void registerSelectBfException(SelectBfException se)
	{
		exceptions[se.getType()]++;
	}
	
	public static void registerException(Exception e)
	{
		genericExceptions++;
	}
	
	public static String getStatistics() throws IllegalArgumentException, IllegalAccessException
	{
		String ret = "";
		ret += "EXCEPTION STATISTICS:\n";
		ret += "SelectBfException-------------------------------------\n";
		Class se = SelectBfException.class;
		
		Field[] fields = se.getFields();
		for(int i = 0; i<fields.length; i++)
		{
			Field f = fields[i];
			
			ret += f.getName()+" occured: "+exceptions[f.getInt(se)]+" times\n";
		}
		
		ret += "Exceptions--------------------------------------------\n";
		ret += "a TOTAL of "+genericExceptions+" non-Sbf-Exceptions occured\n";
		
		return ret;
	}
}
