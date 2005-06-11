package org.selectbf;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;



public class HospitalManagementBase extends ManagementBase
{
	private Vector good_deeds;
	private Vector pending_heals;
	
	private boolean persistent = false;
	
	public HospitalManagementBase()
	{
		good_deeds = new Vector();
		pending_heals = new Vector();
	}
	
	public void registerBeginHealEvent(HealEvent he)
	{
		if(he.isFinished())
		{
			good_deeds.add(he);
		}
		else
		{
			//first checked if player has any pending heals
			//seems to be a bug in the Logging system that not all heals are ended
			int player_id = he.getPlayer_id();
			for(Iterator i = pending_heals.iterator();i.hasNext();)
			{
				HealEvent localhe = (HealEvent) i.next();
				if(localhe.getPlayer_id()==player_id)
				{
					//any non correct heals become dropped
					i.remove();
				}
			}
			pending_heals.add(he);
		}
	}
	
	public void registerEndHealEvent(Element e) throws SelectBfException
	{
		boolean found = false;
		for(int i = 0; i<pending_heals.size() && !found;i++)
		{
			HealEvent he = (HealEvent) pending_heals.elementAt(i);
			if(he.addEndEvent(e))
			{
				found = true;
				pending_heals.remove(i);
				good_deeds.add(he);
			}
		}
	}
	
	public String toString()
	{
		String str = "";
		
		try
		{
			str += "---Finished heals---\n";
			for(Iterator i = good_deeds.iterator();i.hasNext();)
			{
				HealEvent he = (HealEvent) i.next();
				if(he.getType() == HealEvent.SELFHEAL)
				{
					str+= "PlayerId "+he.getPlayer_id()+" healed himself "+he.getAmount_healed()+" MedPack-Points took "+he.getHealtime()+" seconds (start: "+he.getTime()+") (end:"+he.getEndtime()+")\n";
				} else
				{
					str+= "PlayerId "+he.getPlayer_id()+" healed HealedPlayerId "+he.getHealed_player()+" "+he.getAmount_healed()+" MedPack-Points took "+he.getHealtime()+" seconds (start: "+he.getTime()+") (end:"+he.getEndtime()+")\n";
				}
			}
			
			str+= "---Pending Heals---\n";
			for(Iterator i = pending_heals.iterator();i.hasNext();)
			{
				HealEvent he = (HealEvent) i.next();
				if(he.getType() == HealEvent.SELFHEAL)
				{
					str+= "PlayerId "+he.getPlayer_id()+" started healing himself ("+he.getTime()+")\n";
				} else
				{
					str+= "PlayerId "+he.getPlayer_id()+" started healing HealedPlayerId "+he.getHealed_player()+" ("+he.getTime()+")\n";
				}
				
			}
		}
		catch(SelectBfException se)
		{
			str+=se.toString();
		}
		return str;
	}

	public void writeRepairToDb(int playerid,int healed_player,int amount, float healtime, DatabaseContext dc) throws SQLException
	{
		PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_heals SET times_healed=(times_healed+1),healtime=(healtime+?),amount=(amount+?) WHERE player_id = ? AND healed_player_id = ?");
		ps.setFloat(1,healtime);
		ps.setInt(2,amount);	
		ps.setInt(3,playerid);
		ps.setInt(4,healed_player);
		
			
		int result = ps.executeUpdate();

		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_heals (player_id,amount,healtime,healed_player_id,times_healed) VALUES (?,?,?,?,1)");
			ps2.setInt(1,playerid);
			ps2.setInt(2,amount);	
			ps2.setFloat(3,healtime);
			ps2.setInt(4,healed_player);
			
			ps2.executeUpdate();
		}
	}	
	
	public void persist(DatabaseContext dc, int roundId, PlayerManagementBase pmb) throws SQLException, SelectBfException
	{
		if(!persistent)
		{
            int totalNumber = good_deeds.size();
            int number = 0;
			for(Iterator i = good_deeds.iterator(); i.hasNext();)
			{
				try
				{
					HealEvent he = (HealEvent) i.next();
					
					Player player = pmb.getPlayerForSlot(he.getPlayer_id(),he.getTime());
					Player healedplayer = pmb.getPlayerForSlot(he.getHealed_player(),he.getTime());
					
					writeRepairToDb(pmb.lookupDbId(player),pmb.lookupDbId(healedplayer),he.getAmount_healed(), he.getHealtime(), dc);
				}
				catch(SelectBfException e)
				{
					if(e.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
					{
						//do nothing, meaning don't register the heal
						SelectBfExceptionCounter.registerSelectBfException(e);
					} else
					if(e.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
					{
						//do nothing, meaning don't register the heal
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
			throw new SelectBfException(SelectBfException.ALREADY_PERSISTENT,"HospitalManagementBase");
		}
	}

	public boolean isPersistent()
	{
		return persistent;
	}
	
	public int countHealsForPlayer(int healtype,int contextplayerid)
	{
		int count = 0;
		for(Iterator i = good_deeds.iterator();i.hasNext();)
		{
			HealEvent he = (HealEvent) i.next();
			
			if(he.getType() == healtype && he.getPlayer_id() == contextplayerid)
			{
				count++;						
			}
		}
		return count;
	}

}
