package org.selectbf;

import org.jdom.Element;
import org.jdom.Namespace;

public class KitEvent extends Event
{
	private String kit;
	private int playerid;
	
	public KitEvent(RoundContext rc, Element e, Namespace ns) throws SelectBfException
	{
		super(rc, e, ns);
		
		String type = e.getAttributeValue("name"); 
		
		if(type.equals("pickupKit"))
		{
			try
			{
				playerid = Integer.parseInt(valueFromParameters(e,"player_id"));
				kit = valueFromParameters(e,"kit");
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
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Expected 'pickupKit' got "+type);
		}
	}

	public String getKit()
	{
		return kit;
	}

	public int getPlayerid()
	{
		return playerid;
	}

}
