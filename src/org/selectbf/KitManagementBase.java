
package org.selectbf;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;


public class KitManagementBase extends ManagementBase
{
	private Vector kits;
	
	private boolean persistent = false;
	
	
	public KitManagementBase()
	{
		kits = new Vector();
	}
	
	public void addKitEvent(KitEvent ke)
	{
		kits.add(ke);
	}
	
	public String toString()
	{
		String str = "---Kits taken up---\n";
		
		for(Iterator i = kits.iterator(); i.hasNext();)
		{
			KitEvent ke = (KitEvent) i.next();
			str += "PlayerId "+ke.getPlayerid()+" took up a '"+ke.getKit()+"' at "+ke.getTime()+"\n";
		}
		return str;
	}
	
	public void writeKitToDb(int playerid, String kit, DatabaseContext dc) throws SQLException
	{
		PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_kits SET times_used=(times_used+1) WHERE player_id = ? AND kit = ?");
		ps.setInt(1,playerid);
		ps.setString(2,kit);
			
		int result = ps.executeUpdate();
		
		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_kits (player_id,times_used,kit) VALUES (?,1,?)");
			ps2.setInt(1,playerid);
			ps2.setString(2,kit);
			ps2.executeUpdate();
		}
	}
	
	public void persist(DatabaseContext dc, int roundId, PlayerManagementBase pmb) throws SelectBfException, SQLException
	{
		if(!persistent)
		{
            int totalNumber = kits.size();
            int number = 0;
            
			for(Iterator i = kits.iterator(); i.hasNext();)
			{
				try
				{
					KitEvent ke = (KitEvent) i.next();
					
					Player p = pmb.getPlayerForSlot(ke.getPlayerid(),ke.getTime());

					writeKitToDb(pmb.lookupDbId(p),ke.getKit(),dc);

				}
				catch(SelectBfException e)
				{
					if(e.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
					{
						//do nothing, meaning don't register the pickup, probably is a bot
						SelectBfExceptionCounter.registerSelectBfException(e);
					} else
					if(e.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
					{
						//do nothing, meaning don't register the the pickup
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
			throw new SelectBfException(SelectBfException.ALREADY_PERSISTENT,"KitManagementBase");
		}
	}
}
