package org.selectbf;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.Element;

public class HighwayManagementBase extends ManagementBase
{
	private Vector finished_drives;
	private Vector pending_drives;
	
	private boolean persistent = false;
	
	public HighwayManagementBase()
	{
		finished_drives = new Vector();
		pending_drives = new Vector();
	}
	
	public void registerBeginDriveEvent(VehicleEvent ve)
	{
		if(ve.isFinished())
		{
			finished_drives.add(ve);
		}
		else
		{
			//first check if the player has any pending drives
			//this was in the heals so is implemented here just to be sure
			int player_id = ve.getPlayer_id();
			for(Iterator i = pending_drives.iterator();i.hasNext();)
			{
				VehicleEvent localve = (VehicleEvent) i.next();
				if(localve.getPlayer_id()==player_id)
				{
					//any non correct drives become dropped
					i.remove();
				}
			}
			pending_drives.add(ve);
		}
	}
	
	public void registerEndDriveEvent(Element e) throws SelectBfException
	{
		boolean found = false;
		for(int i = 0; i<pending_drives.size() && !found;i++)
		{
			VehicleEvent ve = (VehicleEvent) pending_drives.elementAt(i);
			if(ve.addEndEvent(e))
			{
				found = true;
				pending_drives.remove(i);
				finished_drives.add(ve);
			}
		}
	}	
	
	public String toString()
	{
		String str = "";
		
		try
		{
			str += "---Finished Rides---\n";
			for(Iterator i = finished_drives.iterator();i.hasNext();)
			{
				VehicleEvent ve = (VehicleEvent) i.next();
				str += "PlayerId "+ve.getPlayer_id()+" drove a '"+ve.getVehicle()+"' for "+ve.getDrivetime()+" seconds (start:"+ve.getTime()+") (end:"+ve.getEndtime()+")\n";
			}
			
			str+= "---Pending Rides---\n";
			for(Iterator i = pending_drives.iterator();i.hasNext();)
			{
				VehicleEvent ve = (VehicleEvent) i.next();
				str += "PlayerId "+ve.getPlayer_id()+" started driving a '"+ve.getVehicle()+"' ("+ve.getTime()+")\n";
				
			}
		}
		catch(SelectBfException se)
		{
			str+=se.toString();
		}
		return str;
	}

	public void writeDriveToDb(int playerid, String vehicle, float drivetime, DatabaseContext dc) throws SQLException
	{
		PreparedStatement ps  = dc.prepareStatement("UPDATE selectbf_drives SET times_used=(times_used+1), drivetime=(drivetime+?) WHERE player_id = ? AND vehicle = ?");
		ps.setFloat(1,drivetime);
		ps.setInt(2,playerid);
		ps.setString(3,vehicle);
			
		int result = ps.executeUpdate();
		
		if(result==0)
		{
			PreparedStatement ps2 = dc.prepareStatement("INSERT INTO selectbf_drives (player_id,times_used,vehicle,drivetime) VALUES (?,1,?,?)");
			ps2.setInt(1,playerid);
			ps2.setString(2,vehicle);
			ps2.setFloat(3,drivetime);			
			ps2.executeUpdate();
		}
	}

	public void persist(DatabaseContext dc, int roundId, PlayerManagementBase pmb) throws SelectBfException, SQLException
	{
		if(!persistent)
		{
            int totalNumber = finished_drives.size();
            int number = 0;
            
			for(Iterator i = finished_drives.iterator(); i.hasNext();)
			{
				try
				{
					VehicleEvent ve = (VehicleEvent) i.next();
					Player p = pmb.getPlayerForSlot(ve.getPlayer_id(),ve.getTime());
					writeDriveToDb(pmb.lookupDbId(p),ve.getVehicle(),ve.getDrivetime(),dc);
				}
				catch(SelectBfException e)
				{
					if(e.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
					{
						//do nothing, meaning don't register the ride, probably is a bot
						SelectBfExceptionCounter.registerSelectBfException(e);
					} else
					if(e.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
					{
						//do nothing, meaning don't register the drive
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
			throw new SelectBfException(SelectBfException.ALREADY_PERSISTENT,"HighwayManagementBase");
		}
	}		
}
