package org.selectbf;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.Namespace;

public class PlayerStatsManagementBase extends ManagementBase
{
	private Vector playerstats;
	
	private RoundContext rc;
	
	private boolean persistent = false;
	
	
	public PlayerStatsManagementBase(RoundContext rc,Namespace ns)
	{
		super(ns);
		playerstats = new Vector();
		
		this.rc = rc;
	}
		
	public void collectPlayerStats(Element e) throws SelectBfException
	{
		if(e.getName().equals("roundstats"))
		{
			List xmlPlayerstats = e.getChildren("playerstat",NAMESPACE);
			
			Date time = rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
			
			for(Iterator i = xmlPlayerstats.iterator(); i.hasNext();)
			{
				Element xmlPlayerstat = (Element) i.next();
				this.playerstats.add(new PlayerStat(xmlPlayerstat,NAMESPACE,time)); 
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Expected 'roundstats' got something else");
		}
	}
	
	public String toString()
	{
		String str = "---No Playerstats could be found---";
		
		if(!isEmpty())
		{
			str = "===PlayerStats===\n";
			for(Iterator i = playerstats.iterator(); i.hasNext();)
			{
				PlayerStat ps = (PlayerStat) i.next();
				str += ps.toString();
			}
		}
		return str;
	}
	
	public boolean isEmpty()
	{
		if(playerstats.size()==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void persist(DatabaseContext dc, int roundId, PlayerManagementBase pmb, HospitalManagementBase hmb, PitStopManagementBase psmb) throws SelectBfException, SQLException
	{
		if(!hmb.isPersistent() || !psmb.isPersistent())
		{
			throw new SelectBfException(SelectBfException.NOT_ALL_DATA_READY,"Either the HospitalMB or the PitStopMB is not persistent. Make persistent first. Final values are needed.");
		}
		if(!persistent)
		{
			//bubble-sort the vector to find the three top-ranks
			for(int i = 0; i<playerstats.size();i++)
			{
				for(int j = i; j<playerstats.size();j++)
				{
					PlayerStat one = (PlayerStat) playerstats.elementAt(i);
					PlayerStat two = (PlayerStat) playerstats.elementAt(j);
					
					if(!one.isBiggerThan(two))
					{
						playerstats.setElementAt(two,i);
						playerstats.setElementAt(one,j);	
					}
				}
			}

			try
			{
				((PlayerStat) playerstats.elementAt(0)).setRankFirst();
				((PlayerStat) playerstats.elementAt(1)).setRankSecond();
				((PlayerStat) playerstats.elementAt(2)).setRankThree();
			}
			catch(ArrayIndexOutOfBoundsException ae)
			{
				//nothing...happens when there is less than three playerstats in the vector
			}
			
            int totalNumber = playerstats.size();
            int number = 0;
			
			for(Iterator i = playerstats.iterator(); i.hasNext();)
			{
				try
				{
					PlayerStat ps = (PlayerStat) i.next();
					PreparedStatement pst = dc.prepareStatement("INSERT INTO selectbf_playerstats (player_id, team, score, kills, deaths, tks, captures, attacks, defences, objectives, objectivetks, heals, selfheals, repairs, otherrepairs,round_id,first,second,third) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					
					
					Player p = pmb.getPlayerForSlot(ps.getPlayer_id(),ps.getTime());
					
					pst.setInt(1,pmb.lookupDbId(p));
					pst.setInt(2,ps.getTeam());
					pst.setInt(3,ps.getScore());
					pst.setInt(4,ps.getKills());
					pst.setInt(5,ps.getDeaths());
					pst.setInt(6,ps.getTks());
					pst.setInt(7,ps.getCaptures());
					pst.setInt(8,ps.getAttacks());
					pst.setInt(9,ps.getDefences());
					pst.setInt(10,ps.getObjectives());
					pst.setInt(11,ps.getObjectivetks());
					pst.setInt(12,hmb.countHealsForPlayer(HealEvent.OTHERHEAL,ps.getPlayer_id()));
					pst.setInt(13,hmb.countHealsForPlayer(HealEvent.SELFHEAL,ps.getPlayer_id()));
					pst.setInt(14,psmb.countRepairsForPlayer(RepairEvent.REPAIR,ps.getPlayer_id()));
					pst.setInt(15,psmb.countRepairsForPlayer(RepairEvent.REPAIRPLAYER,ps.getPlayer_id()));
					pst.setInt(16,roundId);
					pst.setInt(17,ps.getFirst());
					pst.setInt(18,ps.getSecond());
					pst.setInt(19,ps.getThird());
					pst.execute();
				}
				catch(SelectBfException e)
				{
					if(e.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
					{
						//do nothing, meaning don't register the playerstats
						
						SelectBfExceptionCounter.registerSelectBfException(e);
					} else
					if(e.getType() == SelectBfException.PLAYER_NOT_IN_DATABASE)
					{
						//do nothing, meaning don't register the playerstats
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
		else
		{
			throw new SelectBfException(SelectBfException.ALREADY_PERSISTENT,"PlayerManagementBase");
		}
	}
}
