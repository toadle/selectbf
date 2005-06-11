package org.selectbf;

import java.sql.Date;

import org.jdom.Element;
import org.jdom.Namespace;


public class VehicleEvent extends Event
{

	private int player_id;
	private String vehicle;
	
	private float starttimestamp;
	private float endtimestamp;
	private float drivetime;
	
	private boolean finished = false;
	private Date endtime;

	public VehicleEvent(RoundContext rc, Element e, Namespace ns) throws SelectBfException
	{
		super(rc, e, ns);
		
		String type = e.getAttributeValue("name"); 
		
		if(type.equals("enterVehicle"))
		{
			try
			{
				player_id = Integer.parseInt(valueFromParameters(e,"player_id"));
				
				//NOTE: 'vechicle' is a typo in the XML-Logging system
				vehicle = valueFromParameters(e,"vechicle");
				if(vehicle==null)
				{
					//already here for backup reasons
					vehicle =  valueFromParameters(e,"vehicle");
				}
				starttimestamp = Float.parseFloat(e.getAttributeValue("timestamp"));
			}
			catch(SelectBfException se)
			{
				if(se.getType() == SelectBfException.XML_DATA_NOT_VALID)
				{
					throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
				}
				else
				{
					throw se;
				}
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Need 'enterVehicle' got "+type); 
		}	
	}
	
	public boolean addEndEvent(Element e) throws SelectBfException
	{
		boolean bol = false;
		if(!finished)
		{
			String type = e.getAttributeValue("name"); 
			if(type.equals("exitVehicle"))
			{
				try
				{
					String player_id_from_event = valueFromParameters(e,"player_id");
					if(player_id_from_event!=null && player_id == Integer.parseInt(player_id_from_event))
					{
						endtime = this.rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
						endtimestamp = Float.parseFloat(e.getAttributeValue("timestamp"));
						
						drivetime = endtimestamp - starttimestamp;					
						
						finished = true;
						bol = true;
					}
				}
				catch(SelectBfException se)
				{
					if(se.getType() == SelectBfException.XML_DATA_NOT_VALID)
					{
						throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
					}
					else
					{
						throw se;
					}
				}
			}
			else
			{
				throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Need 'exitVehicle' got "+type);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.RIDE_ALREADY_ENDED);
		}
		return bol;
		
		
	}
	
	public boolean isFinished()
	{
		return finished;
	}


	public String toString()
	{
		String str = "";
		if(finished)
		{
			str = "Playerid "+player_id+" drove a '"+vehicle+"' for "+drivetime+" seconds";
		}
		else
		{
			str = "PlayerId "+player_id+" started driving a '"+vehicle+"'";
		}
		return str;
	}



	public Date getEndtime()
	{
		return endtime;
	}

	/**
	 * @return
	 */
	public float getDrivetime() throws SelectBfException
	{
		if(!finished)
		{
			throw new SelectBfException(SelectBfException.RIDE_NOT_FINISHED);
		}
		return drivetime;
	}

	/**
	 * @return
	 */
	public float getEndtimestamp() throws SelectBfException
	{
		if(!finished)
		{
			throw new SelectBfException(SelectBfException.RIDE_NOT_FINISHED);
		}
		return endtimestamp;
	}

	/**
	 * @return
	 */
	public int getPlayer_id()
	{
		return player_id;
	}

	/**
	 * @return
	 */
	public float getStarttimestamp()
	{
		return starttimestamp;
	}

	/**
	 * @return
	 */
	public String getVehicle()
	{
		return vehicle;
	}

}
