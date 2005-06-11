
package org.selectbf;

import java.sql.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;


public class Event extends SelectBfClassBase
{
	protected RoundContext rc;
	protected Date time;
	
	public Event(RoundContext rc,Element e,Namespace ns)
	{
		super(ns);
		this.rc = rc;
		time = rc.calcTimeFromDiffString(e.getAttributeValue("timestamp"));
	}
	
	protected String valueFromParameters(Element e,String paramname) throws SelectBfException
	{
		List params = e.getChildren("param",NAMESPACE);

		String str = null;
		boolean found = false;
		
		if(params.size()==0)
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Needed Event with Parameters");
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

	protected static String valueFromParameters(Element e,String paramname,Namespace NAMESPACE) throws SelectBfException
	{
		List params = e.getChildren("param",NAMESPACE);

		String str = null;
		boolean found = false;
		
		if(params.size()==0)
		{
			throw new SelectBfException(SelectBfException.XML_DATA_NOT_VALID,"Needed Event with Parameters");
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

	public Date getTime()
	{
		return time;
	}

}
