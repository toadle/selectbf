
package org.selectbf;

import org.jdom.Element;
import org.jdom.Namespace;


public class EventProcessor extends SelectBfClassBase
{
	private RoundContext rc;
	private PlayerManagementBase pmb;
	private ScoreManagementBase smb;
	private HospitalManagementBase hmb;
	private PitStopManagementBase psmb;
	private RoundInfoManagementBase rimb;
	private HighwayManagementBase hwmb;
	private KitManagementBase kmb;
	private ChatMessageManagementBase cmmb;
		
	
	public EventProcessor(Namespace ns, RoundContext rc, PlayerManagementBase pmb,ScoreManagementBase smb,HospitalManagementBase hmb,PitStopManagementBase psmb,RoundInfoManagementBase rimb, HighwayManagementBase hwmb,KitManagementBase kmb, ChatMessageManagementBase cmmb)
	{
		super(ns);
		this.rc = rc;
		this.pmb = pmb;
		this.smb = smb;
		this.hmb = hmb;
		this.psmb = psmb;
		this.rimb = rimb;
		this.hwmb = hwmb;
		this.kmb = kmb;
		this.cmmb = cmmb;
	}
	
	public void processEvent(Element e) throws SelectBfException
	{
		String type = e.getAttributeValue("name");
		
		if(type!=null)
		{
			try
			{
				if(type.equals("createPlayer"))
				{
					pmb.addPlayer(e);			
				} else
				if(type.equals("disconnectPlayer"))
				{
					pmb.disconnectPlayer(e);
				} else	
				if(type.equals("playerKeyHash"))
				{
					pmb.registerKeyHash(e);			
				} else
				if(type.equals("changePlayerName"))
				{
					pmb.registerNickChange(e);
				}
				if(type.equals("scoreEvent"))
				{
					ScoreEvent s = new ScoreEvent(rc,e,NAMESPACE);
					smb.addScoreEvent(s);
				} else
				if(type.equals("beginMedPack"))
				{
					HealEvent he = new HealEvent(rc,e,NAMESPACE);
					hmb.registerBeginHealEvent(he);
				} else
				if(type.equals("endMedPack"))
				{
					hmb.registerEndHealEvent(e);
				} else
				if(type.equals("beginRepair"))
				{
					RepairEvent re = new RepairEvent(rc,e,NAMESPACE);
					psmb.registerBeginRepairEvent(re);
				} else
				if(type.equals("endRepair"))
				{
					psmb.registerEndRepairEvent(e);
				} else
				if(type.equals("roundInit"))
				{
					rimb.registerRoundInit(e);
				} else
				if(type.equals("enterVehicle"))
				{
					VehicleEvent ve = new VehicleEvent(rc,e,NAMESPACE);
					hwmb.registerBeginDriveEvent(ve);
				} else
				if(type.equals("exitVehicle"))
				{
					hwmb.registerEndDriveEvent(e);
				} else
				if(type.equals("restartMap"))
				{
					rimb.registerRoundEnd(e);
				} else
				if(type.equals("pickupKit"))
				{
					KitEvent ke = new KitEvent(rc,e,NAMESPACE);
					kmb.addKitEvent(ke);
				} else
				if(type.equals("chat"))
				{
					ChatEvent ce = new ChatEvent(rc,e,NAMESPACE);
					cmmb.addChatEvent(ce);
				}
			}
			catch(SelectBfException se)
			{
				if(se.getType() == SelectBfException.DATA_DONT_MEET_EXPECTATIONS)
				{
					//do nothing, just cancel further process of an invalid Event
					SelectBfExceptionCounter.registerSelectBfException(se);
				} else
				if(se.getType() == SelectBfException.NO_PLAYERSLOT_FOR_ID)
				{
					//hopefully this works
					SelectBfExceptionCounter.registerSelectBfException(se);
				} else
				{
					throw se;
				}
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Expected an processable Event.");
		}
	}
}
