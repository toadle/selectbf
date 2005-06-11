package org.selectbf;

import java.util.Vector;

class Player
{
	private int contextId;
	private Vector names;
	
	private String keyHash;
	
	public Player(String name, int contextId)
	{
		this.contextId = contextId;
		names = new Vector();
		names.add(name);
	}
	
	public void addName(String str)
	{
		names.add(str);
	}
	
	public int getContextId()
	{
		return contextId;
	}

	public String getNameAt(int i)
	{
		return (String) names.elementAt(i);
	}
	
	public int getNumberOfNames()
	{
		return names.size();
	}
	
	public String toString()
	{
		return "Playername: "+names.elementAt(0)+" KeyHash: "+keyHash+" ContextID: "+contextId;
	}
	
	public boolean equals(Player p) throws SelectBfException
	{
		boolean equal = true;
		
		if(p.getNumberOfNames()!= this.getNumberOfNames())
		{
			equal = false;
		}
		else
		{
			for(int i = 0; i<p.getNumberOfNames() && equal; i++)
			{
	
				String nickname = p.getNameAt(i);
				if(!nickname.equals(this.getNameAt(i)))
				{
					equal = false;
				}
			}
		}
		
		if(equal)
		{
			try
			{
				if(!keyHash.equals(p.getKeyHash())) equal = false;
			}
			catch(NullPointerException ne)
			{
				if(p.getKeyHash() == null) equal = true;
				else equal = false;
			}
			catch(SelectBfException se)
			{
				if(se.getType() == SelectBfException.NO_KEYHASH_AVAILABLE)
				{
					if(keyHash!=null)
					{
						equal = false;
					}
				}
				else throw se;
			}
		}

		return equal;
	}
	

	public void setKeyHash(String hash)
	{
		this.keyHash = hash;
	}
	
	public String getKeyHash() throws SelectBfException
	{
		if(this.keyHash == null) throw new SelectBfException(SelectBfException.NO_KEYHASH_AVAILABLE);
		return keyHash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return this.names.elementAt(0).hashCode();
	}
}
