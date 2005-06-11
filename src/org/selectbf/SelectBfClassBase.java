package org.selectbf;
import org.jdom.Namespace;

public class SelectBfClassBase
{
	protected Namespace NAMESPACE;
    
    public SelectBfClassBase(){};

	public SelectBfClassBase(Namespace ns)
	{
		this.NAMESPACE = ns;
	}
}
