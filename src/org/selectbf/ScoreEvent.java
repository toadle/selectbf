
package org.selectbf;

import org.jdom.Element;
import org.jdom.Namespace;


public class ScoreEvent extends Event
{
	public static final int ATTACK = 1;
	public static final int DEATH = 2;
	public static final int DEATH_NO_MSG = 3;
	public static final int KILL = 4;
	public static final int TK = 5;
	public static final int CAPTURE = 6;	
	
	
	private int player_id;
	private int victim_id = -1;
	private int scoretype;
	private String weapon = "(none)";	
	
	
	public ScoreEvent(RoundContext rc, Element e, Namespace ns) throws SelectBfException
	{
		super(rc,e, ns);
		
		String type = e.getAttributeValue("name"); 
		
		if(type.equals("scoreEvent"))
		{
			try
			{
	
				player_id = Integer.parseInt(valueFromParameters(e,"player_id"));
				
				String st = valueFromParameters(e,"score_type");
				if(st.equals("Kill"))
				{
					this.scoretype = KILL;
					victim_id = Integer.parseInt(valueFromParameters(e,"victim_id"));
					weapon = valueFromParameters(e,"weapon");
				} else
				if(st.equals("Attack"))
				{
					this.scoretype = ATTACK;
				} else
				if(st.equals("Death"))
				{
					this.scoretype = DEATH;
				} else
				if(st.equals("DeathNoMsg"))
				{
					this.scoretype = DEATH_NO_MSG;
				} else
				if(st.equals("TK"))
				{
					this.scoretype = TK;
					victim_id = Integer.parseInt(valueFromParameters(e,"victim_id"));
					weapon = valueFromParameters(e,"weapon");
				}
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}
			catch(NullPointerException ne)
			{
				
			}
			
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Need 'ScoreEvent' got "+type); 
		}

	}

	public int getScoretype()
	{
		return scoretype;
	}


	public int getPlayer_id()
	{
		return player_id;
	}


	public int getVictim_id()
	{
		return victim_id;
	}


	public String getWeapon()
	{
		return weapon;
	}

}
