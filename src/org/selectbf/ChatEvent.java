/*
 * Created on 03.02.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.selectbf;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Documentation here
 * 
 * @author tadler
 * @
 */
public class ChatEvent extends Event
{
	int playerid;
	String text;

	public ChatEvent(RoundContext rc,Element e, Namespace ns) throws SelectBfException
	{
		super(rc,e,ns);

		String type = e.getAttributeValue("name"); 
		
		if(type.equals("chat"))
		{
			try
			{
				playerid = Integer.parseInt(valueFromParameters(e,"player_id"));
				text = valueFromParameters(e,"text");
			}
			catch(SelectBfException se)
			{
				if(se.getType() == SelectBfException.XML_DATA_NOT_VALID)
				{
					throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
				}
				else throw se;
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Expected 'chat' got "+type);
		}
	}
	
	/**
	 * @return
	 */
	public int getPlayerid()
	{
		return playerid;
	}

	/**
	 * @return
	 */
	public String getText()
	{
		return text;
	}

}
