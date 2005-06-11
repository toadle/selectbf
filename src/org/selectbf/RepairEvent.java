package org.selectbf;

import java.sql.Date;

import org.jdom.Element;
import org.jdom.Namespace;



public class RepairEvent extends Event
{
	public static final int REPAIR = 1;
	public static final int REPAIRPLAYER = 2;
	
	private int player_id;
	private int repair_player;
	
	private String repaired_vehicle;
	
	private int type;
	
	private int amount_repaired;
	private float starttimestamp;
	private float endtimestamp;
	private float repairtime;
	
	
	private int repair_status_before;
	private int repair_status_after;
	
	private boolean finished = false;
	private Date endtime;	
	
	
	public RepairEvent(RoundContext rc, Element e, Namespace ns) throws SelectBfException
	{
		super(rc, e, ns);
		
		String type = e.getAttributeValue("name"); 
		
		if(type.equals("beginRepair"))
		{
			try
			{
	
				player_id = Integer.parseInt(valueFromParameters(e,"player_id"));
				
				repaired_vehicle = valueFromParameters(e,"vehicle_type");
				//the typo
				if(repaired_vehicle == null) valueFromParameters(e,"vechicle_type");
				
				String repplay = valueFromParameters(e,"vehicle_player");
				if(repplay==null)
				{
					this.type = REPAIR;				
				}
				else
				{
					this.type = REPAIRPLAYER;
					repair_player = Integer.parseInt(repplay);
				}
				
				repair_status_before = Integer.parseInt(valueFromParameters(e,"repair_status"));
				starttimestamp = Float.parseFloat(e.getAttributeValue("timestamp"));
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Need 'beginRepair' got "+type); 
		}			
		
	}

	public boolean addEndEvent(Element e) throws SelectBfException
	{
		boolean bol = false;
		if(!finished)
		{
			String type = e.getAttributeValue("name"); 
			if(type.equals("endRepair"))
			{
				try
				{
					if(player_id == Integer.parseInt(valueFromParameters(e,"player_id")))
					{
						repair_status_after = Integer.parseInt(valueFromParameters(e,"repair_status"));
						
						amount_repaired = repair_status_before - repair_status_after;
						
						endtime = this.rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
						endtimestamp = Float.parseFloat(e.getAttributeValue("timestamp"));
						
						repairtime = endtimestamp - starttimestamp;		
						
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
				throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Need 'endRepair' got "+type);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.REPAIR_ALREADY_FINISHED);
		}
		return bol;
	}
	
	public int getAmount_repaired() throws SelectBfException
	{
		if(!finished)
		{
			throw new SelectBfException(SelectBfException.REPAIR_NOT_FINISHED);
		}
		else
		{
			return amount_repaired;	
		}		
	}

	public String toString()
	{
		String str = "";
		
		if(finished)
		{
			switch(type)
			{
				case REPAIR:			str = "PlayerId "+player_id+" repaired Vehicle '"+repaired_vehicle+"' with "+amount_repaired+" RepairPoints";break;
				case REPAIRPLAYER:		str = "PlayerId "+player_id+" repaired VehiclePlayerId "+repair_player+"'s vehicle '"+repaired_vehicle+"' with "+amount_repaired+" RepairPoints";break;
			}
		}
		else
		{
			switch(type)
			{
				case REPAIR:			str = "PlayerId "+player_id+" started repairing Vehicle '"+repaired_vehicle+"'";break;
				case REPAIRPLAYER:		str = "PlayerId "+player_id+" started repairing VehiclePlayerId "+repair_player+"'s vehicle '"+repaired_vehicle+"'";break;
			}			
		}
		
		return str;
	}		
	

	public Date getEndtime()
	{
		return endtime;
	}


	public boolean isFinished()
	{
		return finished;
	}


	public int getPlayer_id()
	{
		return player_id;
	}


	public int getRepair_player()
	{
		return repair_player;
	}


	public int getRepair_status_after()
	{
		return repair_status_after;
	}


	public int getRepair_status_before()
	{
		return repair_status_before;
	}


	public String getRepaired_vehicle()
	{
		return repaired_vehicle;
	}


	public int getType()
	{
		return type;
	}

	/**
	 * @return
	 */
	public float getRepairtime() throws SelectBfException
	{
		if(!finished)
		{
			throw new SelectBfException(SelectBfException.REPAIR_NOT_FINISHED);
		}
		return repairtime;
	}

	/**
	 * @param i
	 */
	public void setPlayer_id(int i)
	{
		player_id = i;
	}

}
