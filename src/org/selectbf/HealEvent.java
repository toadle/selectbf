package org.selectbf;

import java.sql.Date;

import org.jdom.Element;
import org.jdom.Namespace;


public class HealEvent extends Event
{
	public static final int SELFHEAL = 1;
	public static final int OTHERHEAL = 2;
	
	private int player_id;
	private int healed_player;
	
	private int type;
	
	private int amount_healed;
	private float starttimestamp;
	private float endtimestamp;
	private float healtime;	
	
	private int health_pack_before;
	private int health_pack_after;
	
	private boolean finished = false;
	private Date endtime;

	public HealEvent(RoundContext rc, Element e, Namespace ns) throws SelectBfException
	{
		super(rc, e, ns);
		
		String type = e.getAttributeValue("name"); 
		
		if(type.equals("beginMedPack"))
		{
			try
			{
				player_id = Integer.parseInt(valueFromParameters(e,"player_id"));
				healed_player = Integer.parseInt(valueFromParameters(e,"healed_player"));
				
				if(player_id == healed_player)
				{
					this.type = SELFHEAL;
				}
				else
				{
					this.type = OTHERHEAL;
				}
				
				health_pack_before = Integer.parseInt(valueFromParameters(e,"medpack_status"));
				starttimestamp = Float.parseFloat(e.getAttributeValue("timestamp"));
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Need 'beginMedPack' got "+type); 
		}	
	}
	
	public boolean addEndEvent(Element e) throws SelectBfException
	{
		boolean bol = false;
		if(!finished)
		{
			String type = e.getAttributeValue("name"); 
			if(type.equals("endMedPack"))
			{
				try
				{
					String player_id_from_event = valueFromParameters(e,"player_id");
					if(player_id_from_event != null && player_id == Integer.parseInt(player_id_from_event))
					{
						health_pack_after = Integer.parseInt(valueFromParameters(e,"medpack_status"));
						
						amount_healed = health_pack_before - health_pack_after;
						
						endtime = this.rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
						endtimestamp = Float.parseFloat(e.getAttributeValue("timestamp"));
						
						healtime = endtimestamp - starttimestamp;					
						
						finished = true;
						bol = true;
					}
				}
				catch(NumberFormatException ne)
				{
					throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
				}
			}
			else
			{
				throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Need 'endMedPack' got "+type);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.HEAL_ALREADY_FINISHED);
		}
		return bol;
	}
	
	public boolean isFinished()
	{
		return finished;
	}


	public int getAmount_healed() throws SelectBfException
	{
		if(!finished)
		{
			throw new SelectBfException(SelectBfException.HEAL_NOT_FINISHED);
		}
		else
		{
			return amount_healed;	
		}		
	}

	public String toString()
	{
		String str = "";
		if(finished)
		{
			str = "Playerid "+player_id+" healead HealedPlayerId "+healed_player+" ("+amount_healed+" MedPackPoints) took "+healtime+" seconds";
		}
		else
		{
			str = "PlayerId "+player_id+" started healing HealedPlayerId "+healed_player;
		}
		return str;
	}

	public int getHealed_player()
	{
		return healed_player;
	}


	public int getHealth_pack_after()
	{
		return health_pack_after;
	}


	public int getHealth_pack_before()
	{
		return health_pack_before;
	}


	public int getPlayer_id()
	{
		return player_id;
	}


	public int getType()
	{
		return type;
	}

	public Date getEndtime()
	{
		return endtime;
	}

	public float getHealtime() throws SelectBfException
	{
		if(!finished)
	{
		throw new SelectBfException(SelectBfException.HEAL_NOT_FINISHED);
	}
	else
	{
		return healtime;	
	}	
	}

}
