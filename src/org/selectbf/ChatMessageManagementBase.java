/*
 * Created on 03.02.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.selectbf;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Vector;

/**
 * Documentation here
 * 
 * @author tadler
 * @
 */
public class ChatMessageManagementBase extends ManagementBase
{
	private Vector messages;
	
	private boolean persistent = false;
	
	
	public ChatMessageManagementBase()
	{
		messages = new Vector();
	}
	
	public void addChatEvent(ChatEvent ce)
	{
		messages.add(ce);
	}
	
	public String toString()
	{
		String str = "---Chat-Messages sent---\n";
		
		for(Iterator i = messages.iterator(); i.hasNext();)
		{
			ChatEvent ce = (ChatEvent) i.next();
			str += "PlayerId "+ce.getPlayerid()+" said '"+ce.getText()+"' at "+ce.getTime()+"\n";
		}
		return str;
	}
	
	public void persist(DatabaseContext dc, int roundId, PlayerManagementBase pmb) throws SelectBfException, SQLException
	{
		if(!persistent)
		{
            int totalNumber = messages.size();
            int number = 0;
            
			for(Iterator i = messages.iterator(); i.hasNext();)
			{
				try
				{
					ChatEvent ce = (ChatEvent) i.next();
					
					Player p = pmb.getPlayerForSlot(ce.getPlayerid(),ce.getTime());

					PreparedStatement ps = (PreparedStatement) dc.prepareStatement("INSERT INTO selectbf_chatlog (text,player_id, round_id, inserttime) VALUES (?,?,?,?)");
					ps.setString(1,ce.getText());
					ps.setInt(2,pmb.lookupDbId(p));
					ps.setInt(3,roundId);
					ps.setTimestamp(4,new Timestamp(ce.getTime().getTime()));
					
					ps.execute();
					ps.close();

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
					} else
					if(e.getType() == SelectBfException.NO_KEYHASH_AVAILABLE)
					{
							//do nothing, meaning don't register the pickup
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
