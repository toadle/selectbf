package org.selectbf;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;

public class RoundInfoManagementBase extends ManagementBase
{
	private RoundContext rc;
	
	private Date starttime;
	private int start_tickets_team1;
	private int start_tickets_team2;
	private boolean started = false;

	private Date endtime;
	private int end_tickets_team1 = -1;
	private int end_tickets_team2 = -1;
	private boolean ended = false;
	private int endtype;
	
	public static final int RESTART = 1;
	public static final int REGULAR = 2;
	public static final int FORCED = 3;
	
	private int winning_team;
	
	public RoundInfoManagementBase(RoundContext rc,Namespace ns)
	{
		super(ns);
		this.rc = rc;
	}
	
	public void registerRoundInit(Element e) throws SelectBfException
	{
		
		if(!started)
		{
			String type = e.getAttributeValue("name");
			if(type.equals("roundInit"))
			{
				starttime = rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
				
				start_tickets_team1 = Integer.parseInt(Event.valueFromParameters(e,"tickets_team1",NAMESPACE));
				start_tickets_team2 = Integer.parseInt(Event.valueFromParameters(e,"tickets_team2",NAMESPACE));
				started = true;
			}
			else
			{
				throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Expected a 'roundInit' event got "+type);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_ALREADY_STARTED);
		}
	}
	
	public void registerRoundEnd(Element e) throws NumberFormatException, SelectBfException
	{
		if(!ended)
		{
			if(e.getName().equals("event") && e.getAttributeValue("name").equals("restartMap"))
			{
				endtime = rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
				endtype = RESTART;
				end_tickets_team1 = Integer.parseInt(Event.valueFromParameters(e,"tickets_team1",NAMESPACE));
				end_tickets_team2 = Integer.parseInt(Event.valueFromParameters(e,"tickets_team2",NAMESPACE));
				ended = true;
			} else
			if(e.getName().equals("roundstats"))
			{
				endtime = rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
				endtype = REGULAR;
				
				Element winningteam = e.getChild("winningteam",NAMESPACE);
				winning_team = Integer.parseInt(winningteam.getText());
				
				List teamtickets = e.getChildren("teamtickets",NAMESPACE);
				for(Iterator i = teamtickets.iterator();i.hasNext();)
				{
					Element tickets = (Element) i.next();
					if(tickets.getAttributeValue("team").equals("1"))
					{
						end_tickets_team1 = Integer.parseInt(tickets.getText());
					} else
					if(tickets.getAttributeValue("team").equals("2"))
					{
						end_tickets_team2 = Integer.parseInt(tickets.getText());
					}
				}
				ended = true;
			} else
			if(e.getName().equals("event"))
			{
				endtime = rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
				endtype = FORCED;
				ended = true;
			}

		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_ALREADY_ENDED);
		}
	}
	
	public String toString()
	{
		String str = "";
		
		str += "===Infos about this round===";
		str += "\nRound started: "+starttime;
		str += "\nTeam1-Tickets: "+start_tickets_team1;
		str += "\nTeam2-Tickets: "+start_tickets_team2;
		if(ended)
		{
			str += "\n----------------------------";
			str += "\nRound ended: "+endtime;
			if(endtype==REGULAR)
			{
				str+="\nEndtype is REGULAR";
				str+="\nTeam "+winning_team+" won the game";
			}
			else
			{
				str+="\nEndtype is RESTART";
			}
			str += "\nTeam1-Tickets: "+end_tickets_team1;
			str += "\nTeam2-Tickets: "+end_tickets_team2;
		}
		else
		{
			str += "\nRound has not ended.";
		}
		
		return str;
	}

	public boolean isEnded()
	{
		return ended;
	}


	public boolean isStarted()
	{
		return started;
	}

	/**
	 * @return
	 */
	public int getEnd_tickets_team1() throws SelectBfException
	{
		if(ended)
		{
			return end_tickets_team1;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_NOT_ENDED);
		}
	}

	/**
	 * @return
	 */
	public int getEnd_tickets_team2() throws SelectBfException
	{
		if(ended)
		{
			return end_tickets_team2;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_NOT_ENDED);
		}		
	}

	/**
	 * @return
	 */
	public Date getEndtime() throws SelectBfException
	{
		if(ended)
		{
			return endtime;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_NOT_ENDED);
		}		
	}

	/**
	 * @return
	 */
	public int getEndtype() throws SelectBfException
	{
		if(ended)
		{
			return endtype;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_NOT_ENDED);
		}			
	}

	/**
	 * @return
	 */
	public int getStart_tickets_team1() throws SelectBfException
	{
		if(started)
		{
			return start_tickets_team1;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_NOT_STARTED);
		}
	}

	/**
	 * @return
	 */
	public int getStart_tickets_team2() throws SelectBfException
	{
		if(started)
		{		
			return start_tickets_team2;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_NOT_STARTED);
		}			
	}

	/**
	 * @return
	 */
	public int getWinning_team() throws SelectBfException
	{
		if(ended && endtype==REGULAR)
		{		
			return winning_team;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_NOT_ENDED);
		}				
	}

	/**
	 * @return
	 */
	public Date getStarttime() throws SelectBfException
	{
		if(started)
		{
			return starttime;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ROUND_NOT_STARTED);
		}			
	}

}
