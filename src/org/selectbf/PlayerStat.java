package org.selectbf;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;


public class PlayerStat extends SelectBfClassBase
{
	private int player_id;
	private String player_name;
	private int team;
	private int score;
	private int kills;
	private int deaths;
	private int tks;
	private int captures;
	private int attacks;
	private int defences;
	private int objectives;
	private int objectivetks;
	
	private int is_ai;
	
	private Date time;
	
	private boolean rankSet = false;
	private int first = 0;
	private int second = 0;
	private int third = 0;
	
	public PlayerStat(Element e,Namespace ns, Date time) throws SelectBfException
	{
		super(ns);
		
		if(e.getName().equals("playerstat"))
		{
			player_id = Integer.parseInt(e.getAttributeValue("playerid"));
			
			player_name = valueFromParameters(e,"player_name");
			
			team = Integer.parseInt(valueFromParameters(e,"team"));
			score = Integer.parseInt(valueFromParameters(e,"score"));
			kills = Integer.parseInt(valueFromParameters(e,"kills"));
			deaths = Integer.parseInt(valueFromParameters(e,"deaths"));
			tks = Integer.parseInt(valueFromParameters(e,"tks"));
			captures = Integer.parseInt(valueFromParameters(e,"captures"));
			attacks = Integer.parseInt(valueFromParameters(e,"attacks"));
			defences = Integer.parseInt(valueFromParameters(e,"defences"));
			try
			{
				objectives = Integer.parseInt(valueFromParameters(e,"objectives"));
				objectivetks = Integer.parseInt(valueFromParameters(e,"objectivetks"));
			}
			catch(NumberFormatException ne)
			{
				objectives = 0;
				objectivetks = 0;
			}
			
			is_ai = Integer.parseInt(valueFromParameters(e,"is_ai"));
			
			this.time = time;
		}
		else
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Expected a 'playerstat' Element, got something else");			
		}
	}
	
	protected String valueFromParameters(Element e,String paramname) throws SelectBfException
	{
		List params = e.getChildren("statparam",NAMESPACE);

		String str = null;
		boolean found = false;
		
		if(params.size()==0)
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Needed Playerstats with Parameters");
		}
		else
		{
			for(Iterator i = params.iterator();i.hasNext() && !found;)
			{
				Element param = (Element) i.next();
				if(param.getAttributeValue("name").equals(paramname))
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
		String str = "";
		
		str += "---PlayerStats for Id "+player_id+"---\n";
		str += "Name: "+player_name+"\n";
		str += "Team: "+team+"\n";
		str += "Score: "+score+"\n";
		str += "Kills: "+kills+"\n";
		str += "Deaths: "+deaths+"\n";
		str += "TKs: "+tks+"\n";
		str += "Captures: "+captures+"\n";
		str += "Attacks: "+attacks+"\n";
		str += "Defences: "+defences+"\n";
		str += "Objectives: "+objectives+"\n";
		str += "ObjectiveTKs: "+objectivetks+"\n";
		return str;
	}

	public int getAttacks()
	{
		return attacks;
	}


	public int getCaptures()
	{
		return captures;
	}


	public int getDeaths()
	{
		return deaths;
	}


	public int getDefences()
	{
		return defences;
	}


	public int getKills()
	{
		return kills;
	}


	public int getObjectives()
	{
		return objectives;
	}


	public int getObjectivetks()
	{
		return objectivetks;
	}


	public int getPlayer_id()
	{
		return player_id;
	}


	public String getPlayer_name()
	{
		return player_name;
	}


	public int getScore()
	{
		return score;
	}


	public int getTeam()
	{
		return team;
	}


	public int getTks()
	{
		return tks;
	}

	public void setRankFirst() throws SelectBfException
	{
		if(rankSet)
		{
			throw new SelectBfException(SelectBfException.RANK_IS_ALREADY_SET);
		}
		first = 1;
		rankSet = true;
	}
	
	public void setRankSecond() throws SelectBfException
	{
		if(rankSet)
		{
			throw new SelectBfException(SelectBfException.RANK_IS_ALREADY_SET);
		}
		second = 1;
		rankSet = true;
	}
	
	public void setRankThree() throws SelectBfException
	{
		if(rankSet)
		{
			throw new SelectBfException(SelectBfException.RANK_IS_ALREADY_SET);
		}
		third = 1;
		rankSet = true;
	}
	
	public boolean isBiggerThan(PlayerStat ps)
	{
		return this.score>ps.getScore();
	}

	public int getFirst()
	{
		return first;
	}


	public int getSecond()
	{
		return second;
	}


	public int getThird()
	{
		return third;
	}

	public boolean isAI()
	{
		if(is_ai == 0)
		{
			return false;
		}else
		{
			return true;
		}
	}

	public Date getTime()
	{
		return time;
	}

}
