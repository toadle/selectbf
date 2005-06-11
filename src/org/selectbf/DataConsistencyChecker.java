package org.selectbf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class DataConsistencyChecker
{
	private static boolean isLog = false;
	private static boolean isRound = false;
	private static boolean isEvent = false;

	public static void checkAndCorrect(File file) throws IOException
	{
		isLog = false;
		isRound = false;
		isEvent = false;
		
		File buffer_file = new File(file.getParentFile().getAbsolutePath()+File.separatorChar+"buffer.xml");
		if(!buffer_file.exists()) buffer_file.createNewFile();
		
		BufferedReader br = new BufferedReader(new FileReader(file));	
		BufferedWriter bw = new BufferedWriter(new FileWriter(buffer_file));
		
		String buffer = br.readLine();
		while(buffer != null)
		{
			if(buffer.indexOf("<bf:log ")>-1)
			{
				isLog = true;
			} else			
			if(buffer.indexOf("<bf:round ")>-1)
			{
				if(isRound)
				{
					buffer = "</bf:round><!-- inserted by select(bf) consistency checker-->\n"+buffer;
				}
				isRound = true;				
			} else
			if(buffer.indexOf("<bf:event ")>-1)
			{
				if(isEvent) 
				{
					buffer = "</bf:event><!-- inserted by select(bf) consistency checker-->\n"+buffer;
				}
				isEvent = true;
			} else
			if(buffer.indexOf("</bf:event>")>-1)
			{
				isEvent = false;
			} else
			if(buffer.indexOf("</bf:round>")>-1)
			{
				if(isEvent)
				{
					buffer = "</bf:event><!-- inserted by select(bf) consistency checker-->\n"+buffer;
					isEvent = false;
				}				
				isRound = false;
			} else
			if(buffer.indexOf("</bf:log>")>-1)
			{
				if(isEvent)
				{
					buffer = "</bf:event><!-- inserted by select(bf) consistency checker-->\n"+buffer;
					isEvent = false;
				}
				if(isRound)
				{
					buffer = "</bf:round><!-- inserted by select(bf) consistency checker-->\n"+buffer;
					isRound = false;
				}
				
				isLog = false;
			} 
			if(buffer.indexOf("<bf:log")>-1 &&  buffer.indexOf("xmlns:bfv")>-1)
			{
				buffer = buffer.replaceAll("bfv","bf");
				buffer += "<!--Namespace corrected by select(bf) consistency checker-->";
			}
			bw.write(buffer);
			bw.newLine();
			buffer = br.readLine();
		}
		
		if(isEvent)
		{
			bw.write("</bf:event><!-- inserted by select(bf) consistency checker-->");
			bw.newLine();
		}
		
		if(isRound)
		{
			bw.write("</bf:round><!-- inserted by select(bf) consistency checker-->");
			bw.newLine();			
		}
		
		if(isLog)
		{
			bw.write("</bf:log><!-- inserted by select(bf) consistency checker-->");
			bw.newLine();				
		}
		
		br.close();
		bw.close();
		
		file.delete();
		buffer_file.renameTo(file);	
	}
}
