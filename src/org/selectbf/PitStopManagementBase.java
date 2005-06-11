package org.selectbf;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

public class PitStopManagementBase extends ManagementBase
{
	private Vector finished_repairs;
	private Vector pending_repairs;
	
	private boolean persistent = false;
	
	public PitStopManagementBase()
	{
		finished_repairs = new Vector();
		pending_repairs = new Vector();
	}
	
	public void registerBeginRepairEvent(RepairEvent re)
	{
		if(re.isFinished())
		{
			finished_repairs.add(re);
		}
		else
		{
			//first check if the player has any pending repairs
			//this was in the heals so is implemented here just to be sure
			int player_id = re.getPlayer_id();
			for(Iterator i = pending_repairs.iterator();i.hasNext();)
			{
				RepairEvent localre = (RepairEvent) i.next();
				if(localre.getPlayer_id()==player_id)
				{
					//any non correct repairs become dropped
					i.remove();
				}
			}
			pending_repairs.add(re);
		}
	}
	
	public void registerEndRepairEvent(Element e) throws SelectBfException
	{
		boolean found = false;
		for(int i = 0; i<pending_repairs.size() && !found;i++)
		{
			RepairEvent re = (RepairEvent) pending_repairs.elementAt(i);
			if(re.addEndEvent(e))
			{
				found = true;
				pending_repairs.remove(i);
				finished_repairs.add(re);
			}
		}
	}	
	
	public String toString()
	{
		String str = "";
		
		try
		{
			str += "---Finished Repairs---\n";
			for(Iterator i = finished_repairs.iterator();i.hasNext();)
			{
				RepairEvent re = (RepairEvent) i.next();
				if(re.getType() == RepairEvent.REPAIR)
				{
					str+= "PlayerId "+re.getPlayer_id()+" repaired a '"+re.getRepaired_vehicle()+"' with "+re.getAmount_repaired()+" RepairPoints took "+re.getRepairtime()+" seconds (start: "+re.getTime()+") (end:"+re.getEndtime()+")\n";
				} else
				{
					str+= "PlayerId "+re.getPlayer_id()+" repaired VehiclePlayerId "+re.getRepair_player()+"'s '"+re.getRepaired_vehicle()+"' with "+re.getAmount_repaired()+" RepairPoints took "+re.getRepairtime()+" (start: "+re.getTime()+") (end:"+re.getEndtime()+")\n";
				}
			}
			
			str+= "---Pending Repairs---\n";
			for(Iterator i = pending_repairs.iterator();i.hasNext();)
			{
				RepairEvent re = (RepairEvent) i.next();
				if(re.getType() == RepairEvent.REPAIR)
				{
					str+= "PlayerId "+re.getPlayer_id()+" started repairing a '"+re.getRepaired_vehicle()+"' ("+re.getTime()+")\n";
				} else
				{
					str+= "PlayerId "+re.getPlayer_id()+" started repairing HealedPlayerId "+re.getRepair_player()+"'s '"+re.getRepaired_vehicle()+"' ("+re.getTime()+")\n";
				}
				
			}
		}
		catch(SelectBfException se)
		{
			str+=se.toString();
		}
		return str;
	}

	public void writeRepaitToDb(int playerid,String vehicle,int amount, float repairtime, DatabaseContext dc) throws SQLException
	{
		PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_repairs SET times_repaired=(times_repaired+1),repairtime=(repairtime+?),amount=(amount+?) WHERE player_id = ? AND vehicle = ?");
		ps.setFloat(1,repairtime);
		ps.setInt(2,amount);	
		ps.setInt(3,playerid);
		ps.setString(4,vehicle);
		
			
		int result = ps.executeUpdate();

		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_repairs (player_id,amount,repairtime,vehicle,times_repaired) VALUES (?,?,?,?,1)");
			ps2.setInt(1,playerid);
			ps2.setInt(2,amount);	
			ps2.setFloat(3,repairtime);
			ps2.setString(4,vehicle);
			
			ps2.executeUpdate();
		}
	}	

	public void persist(DatabaseContext dc, int roundId, PlayerManagementBase pmb) throws SQLException, SelectBfException
	{
		if(!persistent)
		{
            int totalNumber = finished_repairs.size();
            int number = 0;
            
			for(Iterator i = finished_repairs.iterator(); i.hasNext();)
			{

				try
				{
					RepairEvent re = (RepairEvent) i.next();
					
					Player player = pmb.getPlayerForSlot(re.getPlayer_id(),re.getTime());
					Player reppairplayer = pmb.getPlayerForSlot(re.getRepair_player(),re.getTime());
					
					writeRepaitToDb(pmb.lookupDbId(player),re.getRepaired_vehicle(),re.getAmount_repaired(),re.getRepairtime(),dc);
					
				}
				catch(SelectBfException e)
				{
					if(e.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
					{
						//do nothing, meaning don't register the repair
						SelectBfExceptionCounter.registerSelectBfException(e);
					} else
					if(e.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
					{
						//do nothing, meaning don't register the repair
						SelectBfExceptionCounter.registerSelectBfException(e);
					}else
					if(e.getType() == SelectBfException.NO_KEYHASH_AVAILABLE)
					{
						//do nothing, meaning don't register the playerstats
						SelectBfExceptionCounter.registerSelectBfException(e);
					} else
					{
						throw e;
					}
				}		
                firePersistingProgressEvent(++number,totalNumber);
			}
			persistent = true;
		}
		else
		{
			throw new SelectBfException(SelectBfException.ALREADY_PERSISTENT,"PitStopManagementBase");
		}
		
	}		

	public boolean isPersistent()
	{
		return persistent;
	}
	
	public int countRepairsForPlayer(int repairtype,int contextplayerid)
	{
		int count = 0;
		for(Iterator i = finished_repairs.iterator();i.hasNext();)
		{
			RepairEvent re = (RepairEvent) i.next();
			
			if(re.getType() == repairtype && re.getPlayer_id() == contextplayerid)
			{
				count++;						
			}
		}
		return count;
	}

}
