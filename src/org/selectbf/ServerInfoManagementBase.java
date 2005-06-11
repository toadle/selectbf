
package org.selectbf;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;


public class ServerInfoManagementBase extends SelectBfClassBase
{
	private String server_name;
	private String modid;
	private String mapid;
	private String map;
	private String game_mode;
	private int gametime;
	private int maxplayers;
	private int scorelimit;
	private int spawntime;
	private int soldierff;
	private int vehicleff;
	private int tkpunish;
	private int deathcamtype;
	
	public ServerInfoManagementBase(Element e, Namespace ns) throws SelectBfException
	{
		super(ns);
		if(e.getName().equals("server"))
		{
			server_name = valueFromSettings(e,"server name");
			modid = valueFromSettings(e,"modid");
			mapid = valueFromSettings(e,"mapid");
			map = valueFromSettings(e,"map");
			game_mode = valueFromSettings(e,"game mode");
			gametime = Integer.parseInt(valueFromSettings(e,"gametime"));
			maxplayers = Integer.parseInt(valueFromSettings(e,"maxplayers"));
			scorelimit = Integer.parseInt(valueFromSettings(e,"scorelimit"));
			spawntime = Integer.parseInt(valueFromSettings(e,"spawntime"));
			soldierff = Integer.parseInt(valueFromSettings(e,"soldierff"));
			vehicleff = Integer.parseInt(valueFromSettings(e,"vehicleff"));
			tkpunish = Integer.parseInt(valueFromSettings(e,"tkpunish"));
			
			try
			{
				deathcamtype = Integer.parseInt(valueFromSettings(e,"deathcamtype"));
			}
			catch(NumberFormatException ne)
			{
				deathcamtype = 0;
			}
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Expected 'server' Element.");
		}
	}
	
	protected String valueFromSettings(Element e,String settingname) throws SelectBfException
	{
		List params = e.getChildren("setting",NAMESPACE);

		String str = null;
		boolean found = false;
		
		if(params.size()==0)
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Needed Server with Settings");
		}
		else
		{
			for(Iterator i = params.iterator();i.hasNext() && !found;)
			{
				Element param = (Element) i.next();
				if(param.getAttributeValue("name").equals(settingname))
				{
					str = param.getText();
					found = true;
				}
			}
		}
		return str;
	}

	public String toString()
	{
		String str = "---ServerInfo---\n";
		str+="Servername:"+server_name;
		str+="\nMod:"+modid;
		str+="\nMapid:"+mapid;
		str+="\nMap:"+map;
		str+="\nGameMode:"+game_mode;
		str+="\nGameTime:"+gametime;
		str+="\nMaxPlayers:"+maxplayers;
		str+="\nScoreLimit:"+scorelimit;
		str+="\nSpawntime:"+spawntime;
		str+="\nSoldierff:"+soldierff;
		str+="\nVehicleff:"+vehicleff;
		str+="\nTkpunish:"+tkpunish;
		str+="\nDeathcamtype:"+deathcamtype;	
		return str;
	}



	/**
	 * @return
	 */
	public int getDeathcamtype()
	{
		return deathcamtype;
	}

	/**
	 * @return
	 */
	public String getGame_mode()
	{
		return game_mode;
	}

	/**
	 * @return
	 */
	public int getGametime()
	{
		return gametime;
	}

	/**
	 * @return
	 */
	public String getMap()
	{
		return map;
	}

	/**
	 * @return
	 */
	public String getMapid()
	{
		return mapid;
	}

	/**
	 * @return
	 */
	public int getMaxplayers()
	{
		return maxplayers;
	}

	/**
	 * @return
	 */
	public String getModid()
	{
		return modid;
	}

	/**
	 * @return
	 */
	public int getScorelimit()
	{
		return scorelimit;
	}

	/**
	 * @return
	 */
	public String getServer_name()
	{
		return server_name;
	}

	/**
	 * @return
	 */
	public int getSoldierff()
	{
		return soldierff;
	}

	/**
	 * @return
	 */
	public int getSpawntime()
	{
		return spawntime;
	}

	/**
	 * @return
	 */
	public int getTkpunish()
	{
		return tkpunish;
	}

	/**
	 * @return
	 */
	public int getVehicleff()
	{
		return vehicleff;
	}

}
