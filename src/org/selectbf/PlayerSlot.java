package org.selectbf;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.Vector;

public class PlayerSlot
{
	private int slotId;
	
	private Vector assignPeriods;
	
	public PlayerSlot(int slotId, Player player, Date starttime)
	{
		this.slotId = slotId;
		
		assignPeriods = new Vector();
		assignPeriods.add(new AssignTimePeriod(starttime,player));		
	}

	public Player wasAssignedTo(Date time) throws SelectBfException
	{
		Player p = null;
		
		boolean found = false;
		for(int i = 0; i<(assignPeriods.size()-1) && !found; i++)
		{
			AssignTimePeriod atp = (AssignTimePeriod) assignPeriods.elementAt(i);
			if(atp.contains(time))
			{
				found = true;
				p = atp.getPlayer();
			}			
		}
	
		if(!found)
		{
			p = ((AssignTimePeriod) assignPeriods.lastElement()).getPlayer();
		}

		if(p == null) throw new SelectBfException(SelectBfException.NO_ASSIGNMENT_AT_THAT_TIME);
		return p;

	}

	public void registerReAssignment(Player player, Date starttime) throws SelectBfException
	{
		
		AssignTimePeriod atp = (AssignTimePeriod) assignPeriods.lastElement();
		if(!atp.hasEnded())
		{ 
			atp.endAssignment(starttime); 
		}
			

		for(Iterator i = assignPeriods.iterator(); i.hasNext();)
		{
			AssignTimePeriod assigntimeperiod = (AssignTimePeriod) i.next();
			if(assigntimeperiod.contains(starttime))
			{
				atp.reopenAssignment();
				throw new SelectBfException(SelectBfException.ASSIGNMENT_CONFLICT);
			}
		}
		
		assignPeriods.add(new AssignTimePeriod(starttime, player));				
	}
	
	public void registerUnAssignment(Date endtime) throws SelectBfException
	{
		AssignTimePeriod atp = (AssignTimePeriod) assignPeriods.lastElement();
		if(!atp.hasEnded()) atp.endAssignment(endtime);
	}
	
	public String toString()
	{
		String str = "--------------\nPlayer-Slot: "+slotId+"\n--------------";
		
		for(Iterator i = assignPeriods.iterator(); i.hasNext();)
		{
			AssignTimePeriod astp = (AssignTimePeriod) i.next();
			str += "\n"+astp.toString();
		}
		return str+"\n--------------";
	}
	
	public void writePlayTimeToDb(int playerid,float time,Date endtime, DatabaseContext dc) throws SQLException
	{
		PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_playtimes SET slots_used=(slots_used+1),playtime=(playtime+?),last_seen=? WHERE player_id = ?");
		ps.setDouble(1,time);
		ps.setTimestamp(2,new Timestamp(endtime.getTime()));
		ps.setInt(3,playerid);
		
		int result = ps.executeUpdate();

		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_playtimes (player_id,playtime,last_seen,slots_used) VALUES (?,?,?,1)");
			ps2.setInt(1,playerid);
			ps2.setDouble(2,time);
			ps2.setTimestamp(3,new Timestamp(endtime.getTime()));

			
			ps2.executeUpdate();
		}
	}	
	
	
	public void persist(DatabaseContext dc, PlayerManagementBase pmb) throws SQLException, SelectBfException
	{
		for(Iterator i = assignPeriods.iterator(); i.hasNext();)
		{
			try
			{
				AssignTimePeriod astp = (AssignTimePeriod) i.next();
				
				long l = astp.getEndtime().getTime() - astp.getStarttime().getTime();
				float time = ((float) l/ (float) 1000);
				
				writePlayTimeToDb(pmb.lookupDbId(astp.getPlayer()),time,astp.getEndtime(),dc);
				
			}
			catch(SelectBfException se)
			{
				if(se.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
				{
					//do nothing simply don't add the information
					SelectBfExceptionCounter.registerSelectBfException(se);
				} else
				if(se.getType() == SelectBfException.NO_KEYHASH_AVAILABLE)
				{
					SelectBfExceptionCounter.registerSelectBfException(se);
				}  else
				if(se.getType() == SelectBfException.NO_KEYHASH_AVAILABLE)
				{
					//do nothing, meaning don't register the playerstats
					SelectBfExceptionCounter.registerSelectBfException(se);
				} else throw se;
			}
		}
	}
	
	private class AssignTimePeriod
	{
		private Date starttime;
		public Date endtime;
		private Player player;
						
		private AssignTimePeriod(Date starttime, Player player)
		{
			this.starttime = starttime;
			this.player = player;
			endtime = null;
		}
		
		public void endAssignment(Date endtime) throws SelectBfException
		{
			if(this.endtime==null)
			{
				if(endtime.getTime()>=starttime.getTime())
				{
					this.endtime = endtime;
				}
				else
				{
					//TODO: Error-Counter hochzählen
					this.endtime = starttime;
				}
			}
			else
			{
				throw new SelectBfException(SelectBfException.ASSIGNMENT_ALREADY_ENDED);
			}
		}
		
		public void reopenAssignment() throws SelectBfException
		{
			if(endtime!=null)
			{
				endtime = null;
			}
			else
			{
				throw new SelectBfException(SelectBfException.ASSIGNMENT_HAS_NOT_ENDED);
			}
		}
		
		public Player getPlayer()
		{
			return player;
		}

		public Date getStarttime()
		{
			return starttime;
		}
		
		public boolean contains(Date time) throws SelectBfException
		{
			if(starttime.getTime()< time.getTime() && time.getTime() < getEndtime().getTime())
			{
				return true;
			}
			else
			{
				return false;
			}			
		}
		
		public boolean covers(Date time) throws SelectBfException
		{
			if(starttime.compareTo(time)<=0 && endtime.compareTo(time)>=0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		public Date getEndtime() throws SelectBfException
		{
			if(endtime!=null)
			{
				return endtime;
			}
			else
			{
				throw new SelectBfException(SelectBfException.ASSIGNMENT_HAS_NOT_ENDED);
			}
		}

		public boolean hasEnded()
		{
			return (endtime!=null);
		}
		
		public String toString()
		{
			DateFormat dateformatter = DateFormat.getTimeInstance(DateFormat.MEDIUM);
		
			if(endtime!=null)
			{
				return player+" assigned from "+dateformatter.format(starttime)+" until "+dateformatter.format(endtime);
			}
			else
			{
				return player+" assigned since "+dateformatter.format(starttime);
			}
		}

	}

	public int getSlotId()
	{
		return slotId;
	}
	
	public Vector getDistinctPlayers() throws SelectBfException
	{
		Vector vec = new Vector();
		
		for(Iterator i = assignPeriods.iterator(); i.hasNext();)
		{
			AssignTimePeriod atp = (AssignTimePeriod) i.next();
			
			//first check if player is already in the collection
			boolean found = false;
			for(Iterator j = vec.iterator(); j.hasNext() && !found;)
			{
				Player p = (Player) j.next();
				if(atp.getPlayer().equals(p))
				{
					found = true;
				}
			}
			if(!found)
			{
				vec.add(atp.getPlayer());
			}
		}
		return vec;
	}

	public void registerKeyHash(String hash, Date time) throws SelectBfException
	{
		Player p = wasAssignedTo(time);
		p.setKeyHash(hash);
	}

	/**
	 * @param name
	 * @param time
	 */
	public void registerNickChange(String name, Date time) throws SelectBfException
	{
		Player p = wasAssignedTo(time);
		p.addName(name);
	}
}
