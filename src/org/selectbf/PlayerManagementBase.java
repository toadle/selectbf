package org.selectbf;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.Namespace;

public class PlayerManagementBase extends ManagementBase
{

	private Vector playerslots;
	private GameContext gc;	
	
	private HashMap dbIdLookupContext;
	
	boolean persistent = false;

	boolean acceptBots = false;
	boolean lanMode = false;
	
	public PlayerManagementBase(boolean acceptBots,boolean lanMode,GameContext gc, Namespace ns)
	{
		super(ns);

		this.acceptBots = acceptBots;
		this.lanMode = lanMode;
		
		playerslots = new Vector();
		dbIdLookupContext = new HashMap();
		this.gc = gc;
	}
	
	public Player getPlayerForSlot(int slotId, Date time) throws SelectBfException
	{
		boolean found = false;
		
		PlayerSlot ps = null;
		for(Iterator i = playerslots.iterator(); i.hasNext() && !found;)
		{
			ps = (PlayerSlot) i.next();
			if(ps.getSlotId() == slotId)
			{
				found = true;
			}
		}
		if(!found)
		{
			throw new SelectBfException(SelectBfException.NO_PLAYERSLOT_FOR_ID);
		}
		else
		{
			return ps.wasAssignedTo(time);
		}
	}
	
	public void addPlayer(Element e) throws SelectBfException
	{
		String type = e.getAttributeValue("name");
		
		if(type.equals("createPlayer"))
		{
			try
			{
				String playername = Event.valueFromParameters(e,"name",NAMESPACE);
				int contextId = Integer.parseInt(Event.valueFromParameters(e,"player_id",NAMESPACE));
				int is_ai = Integer.parseInt(Event.valueFromParameters(e,"is_ai",NAMESPACE));
				
				Player p = new Player(playername,contextId);
				
				//is there already a playerslot for that Id ?
				boolean found = false;
				for(Iterator i = playerslots.iterator(); i.hasNext() && !found;)
				{
					PlayerSlot ps = (PlayerSlot) i.next();
					if(ps.getSlotId() == contextId)
					{
						if((acceptBots && is_ai==1) || (is_ai == 0))
						{
							ps.registerReAssignment(p,gc.calcTimeFromDiffString(e.getAttributeValue("timestamp")));
							found = true;
						}
						
					}
				}
				if(!found)
				{
					playerslots.add(new PlayerSlot(contextId,p,gc.calcTimeFromDiffString(e.getAttributeValue("timestamp"))));								
				}
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Needed event 'createPlayer' got "+type);
		}	
	}
	
	public void disconnectPlayer(Element e) throws SelectBfException
	{
		String type = e.getAttributeValue("name");
			
		if(type.equals("disconnectPlayer"))
		{	
			try
			{
				int contextId = Integer.parseInt(Event.valueFromParameters(e,"player_id",NAMESPACE));
	
				//is there already a playerslot for that Id ?
				boolean found = false;
				for(Iterator i = playerslots.iterator(); i.hasNext() && !found;)
				{
					PlayerSlot ps = (PlayerSlot) i.next();
					if(ps.getSlotId() == contextId)
					{
						ps.registerUnAssignment(gc.calcTimeFromDiffString(e.getAttributeValue("timestamp")));
						found = true;					
					}
				}
				if(!found)
				{
					throw new SelectBfException(SelectBfException.NO_PLAYERSLOT_FOR_ID);								
				}	
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}		
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Needed event 'disconnectPlayer' got "+type);
		}	
	}
	

	
	public String toString()
	{
		return playerslots.toString();
	}
	
	public int lookupDbId(Player p) throws SelectBfException
	{
		if(!persistent)
		{
			throw new SelectBfException(SelectBfException.PLAYERINFOS_ARE_NOT_PERSISTENT);
		}
		Integer i = null;
		if(lanMode)
		{
			i = (Integer) dbIdLookupContext.get(""+p.hashCode());
		}
		else
		{
			i = (Integer) dbIdLookupContext.get(""+p.getKeyHash());
		}
		if(i == null)
		{
			throw new SelectBfException(SelectBfException.PLAYER_NOT_IN_DATABASE);
		}
		
		return i.intValue();
		
	}

	public void closeAllSlots(Date time) throws SelectBfException
	{
		for(Iterator i = playerslots.iterator(); i.hasNext();)
		{
			PlayerSlot p = (PlayerSlot) i.next();	
			p.registerUnAssignment(time);		
		}
	}

	public void persist(DatabaseContext dc) throws SQLException, SelectBfException
	{
		//first collect all DISTINCT players from the slots
		Vector completePlayerList = new Vector();
		
		for(Iterator i = playerslots.iterator(); i.hasNext();)
		{
			PlayerSlot ps = (PlayerSlot) i.next();
			Vector playerslotplayers = ps.getDistinctPlayers();
			
			//now go through all players that have ever been in that slot
			for(Iterator j = playerslotplayers.iterator(); j.hasNext();)
			{
				Player p = (Player) j.next();
				
				boolean found = false;
				//and check if he/she is already in the complete list or not
				for(Iterator k = completePlayerList.iterator(); k.hasNext() && !found;)
				{
					Player p2 = (Player) k.next();
					if(p.equals(p2))
					{
						found = true;
					}
				}
				if(!found)
				{
					completePlayerList.add(p);
				}
			}
		}
		
		//now go through the whole list and persist the Player if needed
		for(Iterator i = completePlayerList.iterator(); i.hasNext();)
		{
			Player p = (Player) i.next();
			if(lanMode)
			{
				persistLanPlayer(p,dc);
			}
			else
			{
				persistPlayer(p,dc);	
			}
		}

		persistent = true;
		
		//finally register the playtimes with the db
		persistsPlayerSlots(dc);
	}
	
	private void persistsPlayerSlots(DatabaseContext dc) throws SQLException, SelectBfException
	{
		//now write all the Playtimes to the database
		for(Iterator i = playerslots.iterator(); i.hasNext();)
		{
			PlayerSlot p = (PlayerSlot) i.next();
			p.persist(dc,this);
		}
	}
	
	private void persistNicknames(Player p,int playerdbId, DatabaseContext dc) throws SQLException
	{
		for(int i = 0; i<p.getNumberOfNames(); i++)
		{
			String nickname = (String) p.getNameAt(i);
			
			PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_nicknames SET times_used=(times_used+1) WHERE player_id = ? AND nickname = ?");
			ps.setInt(1,playerdbId);
			ps.setString(2,nickname);
			
			int result = ps.executeUpdate();
			
			if(result==0)
			{
				PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_nicknames (nickname,times_used,player_id) VALUES (?,1,?)");
				ps2.setString(1,nickname);
				ps2.setInt(2,playerdbId);
				ps2.executeUpdate();
			}
		}
	}
	
	private void persistLanPlayer(Player p, DatabaseContext dc) throws SQLException, SelectBfException
	{
		try
		{
			PreparedStatement pS = dc.prepareStatement("select id from selectbf_players where name=?");
			pS.setString(1,p.getNameAt(0));
			ResultSet rs = pS.executeQuery();
		

			if(rs.next())
			{
				int dbId = rs.getInt(1);
				dbIdLookupContext.put(""+p.hashCode(),new Integer(dbId));
				persistNicknames(p,dbId,dc);
				updateMostUsedPlayerName(dbId,dc);
			}
			else
			{
				PreparedStatement ps =  dc.prepareStatement("INSERT INTO selectbf_players (name, keyhash , inserttime) VALUES (? , '<unknown>' ,now())");
				ps.setString(1,DatabaseContext.addSlashes(p.getNameAt(0)));
				ps.execute();
				int dbId = dc.getLatestId(DatabaseContext.PLAYERS);
				dbIdLookupContext.put(""+p.hashCode(),new Integer(dbId));
				persistNicknames(p,dbId,dc);	
				updateMostUsedPlayerName(dbId,dc);			
			}
		}
		catch(SelectBfException se)
		{
			if(se.getType() == SelectBfException.NO_KEYHASH_AVAILABLE)
			{
				//do nothing because if there is no keyhash the player is not persisted
				SelectBfExceptionCounter.registerSelectBfException(se);
			}
			else
			{
				throw se;
			}
		}
	}
	
	private void persistPlayer(Player p, DatabaseContext dc) throws SQLException, SelectBfException
	{
		try
		{
			PreparedStatement pS = dc.prepareStatement("select id from selectbf_players where keyhash=?");
			pS.setString(1,p.getKeyHash());
			ResultSet rs = pS.executeQuery();
		

			if(rs.next())
			{
				int dbId = rs.getInt(1);
				dbIdLookupContext.put(""+p.getKeyHash(),new Integer(dbId));
				persistNicknames(p,dbId,dc);
				updateMostUsedPlayerName(dbId,dc);
			}
			else
			{
				PreparedStatement ps =  dc.prepareStatement("INSERT INTO selectbf_players (name, keyhash , inserttime) VALUES (? , ? ,now())");
				ps.setString(1,DatabaseContext.addSlashes(p.getNameAt(0)));
				ps.setString(2,p.getKeyHash());
				ps.execute();
				int dbId = dc.getLatestId(DatabaseContext.PLAYERS);
				dbIdLookupContext.put(""+p.getKeyHash(),new Integer(dbId));
				persistNicknames(p,dbId,dc);	
				updateMostUsedPlayerName(dbId,dc);			
			}
		}
		catch(SelectBfException se)
		{
			if(se.getType() == SelectBfException.NO_KEYHASH_AVAILABLE)
			{
				//do nothing because if there is no keyhash the player is not persisted
				SelectBfExceptionCounter.registerSelectBfException(se);
			}
			else
			{
				throw se;
			}
		}
	}

	public void registerKeyHash(Element e) throws SelectBfException
	{
		String type = e.getAttributeValue("name");
			
		if(type.equals("playerKeyHash"))
		{	
			try
			{
				int contextId = Integer.parseInt(Event.valueFromParameters(e,"player_id",NAMESPACE));
				String hash = Event.valueFromParameters(e,"keyhash",NAMESPACE);
				Date time = gc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
				
				//search for the corresponding playerslot
				boolean found = false;
				for(Iterator i = playerslots.iterator(); i.hasNext() && !found;)
				{
					PlayerSlot ps = (PlayerSlot) i.next();
					if(ps.getSlotId() == contextId)
					{
						ps.registerKeyHash(hash,time);
						found = true;					
					}
				}
				if(!found)
				{
					throw new SelectBfException(SelectBfException.NO_PLAYERSLOT_FOR_ID);								
				}	
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}		
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Needed event 'disconnectPlayer' got "+type);
		}			
	}
	
	public void updateMostUsedPlayerName(int playerid, DatabaseContext dc) throws SQLException
	{
		PreparedStatement ps = dc.prepareStatement("SELECT nickname FROM selectbf_nicknames WHERE player_id = ? ORDER BY times_used DESC LIMIT 0,1");
		ps.setInt(1,playerid);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		String most_used_nick = rs.getString("nickname");
		ps = dc.prepareStatement("UPDATE selectbf_players SET name=? WHERE id = ?");
		ps.setString(1,most_used_nick);
		ps.setInt(2,playerid);
		ps.executeUpdate();		
	}
	
	/**
	 * @param e
	 */
	public void registerNickChange(Element e) throws SelectBfException
	{
		String type = e.getAttributeValue("name");
			
		if(type.equals("changePlayerName"))
		{	
			try
			{
				int contextId = Integer.parseInt(Event.valueFromParameters(e,"player_id",NAMESPACE));
				String name = Event.valueFromParameters(e,"name",NAMESPACE);
				Date time = gc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
				
				//search for the corresponding playerslot
				boolean found = false;
				for(Iterator i = playerslots.iterator(); i.hasNext() && !found;)
				{
					PlayerSlot ps = (PlayerSlot) i.next();
					if(ps.getSlotId() == contextId)
					{
						ps.registerNickChange(name,time);
						found = true;					
					}
				}
				if(!found)
				{
					throw new SelectBfException(SelectBfException.NO_PLAYERSLOT_FOR_ID);								
				}	
			}
			catch(NumberFormatException ne)
			{
				throw new SelectBfException(SelectBfException.DATA_DONT_MEET_EXPECTATIONS);
			}		
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Needed event 'playerKeyHash' got "+type);
		}		
	}
}
