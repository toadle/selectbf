package org.selectbf;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;


public class ScoreManagementBase extends ManagementBase
{
	private Vector tks;
	private Vector kills;
	private Vector selfkills;
	
	private boolean persistent = false;
	
	public ScoreManagementBase()
	{
		tks = new Vector();
		kills = new Vector();
		selfkills = new Vector();
	}
	
	public void addScoreEvent(ScoreEvent s)
	{
		 switch(s.getScoretype())
		 {
			case ScoreEvent.KILL:			kills.add(s);break;
			case ScoreEvent.DEATH:			selfkills.add(s);break;
			case ScoreEvent.TK:				tks.add(s);break;
		 }
	}
	
	public String toString()
	{
		String str = "";
		
		str += "---Kills---\n";
		for(Iterator i = kills.iterator();i.hasNext();)
		{
			ScoreEvent s = (ScoreEvent) i.next();
			str+= "PlayerId "+s.getPlayer_id()+" ("+s.getWeapon()+") VictimId "+s.getVictim_id()+" ("+s.getTime()+")\n";
		}
		str+="\n";
		
		str += "---TKs---\n";
		for(Iterator i = tks.iterator();i.hasNext();)
		{
			ScoreEvent s = (ScoreEvent) i.next();
			str+= "PlayerId "+s.getPlayer_id()+" TK("+s.getWeapon()+") VictimId "+s.getVictim_id()+" ("+s.getTime()+")\n";
		}
		str+="\n";

		str += "---SelfKills---\n";
		for(Iterator i = selfkills.iterator();i.hasNext();)
		{
			ScoreEvent s = (ScoreEvent) i.next();
			str+= "PlayerId "+s.getPlayer_id()+" killed self ("+s.getTime()+")\n";
		}
		
		return str;	
	}

	public void writeSelfKillToDb(int playerid, DatabaseContext dc) throws SQLException
	{
		PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_selfkills SET times_killed=(times_killed+1) WHERE player_id = ?");
		ps.setInt(1,playerid);
			
		int result = ps.executeUpdate();
		
		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_selfkills (player_id,times_killed) VALUES (?,1)");
			ps2.setInt(1,playerid);
			ps2.executeUpdate();
		}
	}

	public void writeTeamKillToDb(int playerid,int victim_id , DatabaseContext dc) throws SQLException
	{
		PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_tks SET times_killed=(times_killed+1) WHERE player_id = ? AND victim_id = ?");
		ps.setInt(1,playerid);
		ps.setInt(2,victim_id);		
		
			
		int result = ps.executeUpdate();
		
		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_tks (player_id,victim_id,times_killed) VALUES (?,?,1)");
			ps2.setInt(1,playerid);
			ps2.setInt(2,victim_id);			
			ps2.executeUpdate();
		}
	}	
	
	public void writePlayerKillToDb(int player_id, int victim_id, String weapon, DatabaseContext dc) throws SQLException
	{
		//write the player - victim relation
		PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_kills_player SET times_killed=(times_killed+1) WHERE player_id = ? AND victim_id = ?");
		ps.setInt(1,player_id);
		ps.setInt(2,victim_id);		
		
			
		int result = ps.executeUpdate();
		
		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_kills_player (player_id,victim_id,times_killed) VALUES (?,?,1)");
			ps2.setInt(1,player_id);
			ps2.setInt(2,victim_id);			
			ps2.executeUpdate();
		}	
		
		//write the player - weapon relation
		ps  = dc.prepareStatement("UPDATE selectbf_kills_weapon SET times_used=(times_used+1) WHERE player_id = ? AND weapon = ?");
		ps.setInt(1,player_id);
		ps.setString(2,weapon);		
		
		result = ps.executeUpdate();
		
		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_kills_weapon (player_id,weapon,times_used) VALUES (?,?,1)");
			ps2.setInt(1,player_id);
			ps2.setString(2,weapon);			
			ps2.executeUpdate();
		}	
		
			
	}
	


	public void persist(DatabaseContext dc, int roundId, PlayerManagementBase pmb) throws SQLException, SelectBfException
	{
		if(persistent)
		{
			throw new SelectBfException("This ScoreManagementBase is already persistent. Persisting again would cause inconsistency!");
		}
        
        int totalNumber = tks.size()+kills.size()+selfkills.size();
        int number = 0;
        
		for(Iterator i = tks.iterator(); i.hasNext();)
		{
			ScoreEvent se = (ScoreEvent) i.next();
			
			try
			{
				Player player = pmb.getPlayerForSlot(se.getPlayer_id(),se.getTime());
				Player victim = pmb.getPlayerForSlot(se.getVictim_id(),se.getTime());
				
				writeTeamKillToDb(pmb.lookupDbId(player),pmb.lookupDbId(victim),dc);
			}
			catch(SelectBfException e)
			{
				if(e.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
				{
					//do nothing, meaning don't register the TK
					SelectBfExceptionCounter.registerSelectBfException(e);
				} else
				if(e.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
				{
					//do nothing, meaning don't register the TK
					SelectBfExceptionCounter.registerSelectBfException(e);
				} else
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
		
		for(Iterator i = kills.iterator(); i.hasNext();)
		{
			ScoreEvent se = (ScoreEvent) i.next();
			try
			{		
				// Clarification added by jrivett 2009Mar10
				// If we're not logging bots and the kill was made by a bot, this next
				// line will throw an exception and we won't write anything.
				// If the kill was made by a human, or we're logging bots and the
				// kill was made by a bot, then this line will return a valid player
				// and we will process the kill as usual.
				Player player = pmb.getPlayerForSlot(se.getPlayer_id(),se.getTime());

				// Modification by jrivett 2009Mar05
				// If this kill was a human player killing a bot player, AND we are NOT
				// logging bots, write the kill details using a victim ID of zero, which
				// is ignored by the web stats.  Otherwise, carry on as usual.  This way,
				// all kills of bots by humans are recorded, making the kill details more
				// likely to match the end of round (summary) kill totals.
				if (!pmb.acceptBots && se.getPlayer_id() <= 127 && se.getVictim_id() >= 128)
				{
					writePlayerKillToDb(pmb.lookupDbId(player),0,se.getWeapon(),dc);
				} else
				{
					Player victim = pmb.getPlayerForSlot(se.getVictim_id(),se.getTime());
					writePlayerKillToDb(pmb.lookupDbId(player),pmb.lookupDbId(victim),se.getWeapon(),dc);
				}
	
			}
			catch(SelectBfException e)
			{
				if(e.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
				{
					//do nothing, meaning don't register the Kill
					SelectBfExceptionCounter.registerSelectBfException(e);
				} else
				if(e.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
				{
					//do nothing, meaning don't register the TK
					SelectBfExceptionCounter.registerSelectBfException(e);
				} else
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
		
		for(Iterator i = selfkills.iterator(); i.hasNext();)
		{
			ScoreEvent se = (ScoreEvent) i.next();
			try
			{	
				Player p = pmb.getPlayerForSlot(se.getPlayer_id(),se.getTime());
				writeSelfKillToDb(pmb.lookupDbId(p), dc);
			}
			catch(SelectBfException e)
			{
				if(e.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
				{
					//do nothing, meaning don't register the selfkill
					SelectBfExceptionCounter.registerSelectBfException(e);
				} else
				if(e.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
				{
					//do nothing, meaning don't register the TK
					SelectBfExceptionCounter.registerSelectBfException(e);
				} else
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
	
}
