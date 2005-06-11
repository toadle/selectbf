/*
 * Created on 03.04.2005
 * Created by Tim Adler
 */
package org.selectbf.config;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

public class TrimConfig
{
    private static final Log log = LogFactory.getLog(TrimConfig.class);
    
    private boolean trimDatabase;
    private boolean keepPlayers;
    private int days;

    public TrimConfig(Element trimXml) throws SelectBfConfigException
    {
        if(SelectBfConfigException.checkBoolean(trimXml.getText()))
        {
            trimDatabase = false;
        }
        else
        {
            if(SelectBfConfigException.checkInt(trimXml.getAttributeValue("days")))
            {
                throw new SelectBfConfigException("<trim-database>-attribute 'days' is not a valid number!");
            }
            days = Integer.parseInt(trimXml.getAttributeValue("days"));
            
            if(SelectBfConfigException.checkBoolean(trimXml.getAttributeValue("keep-players")))
            {
                throw new SelectBfConfigException("<trim-database>-attribute 'keep-players' has no valid value!");
            }           
        }
    }    
    
    private TrimConfig()
    {
        //for cloning-purposes
    }

    public int getDays()
    {
        return days;
    }

    public boolean isKeepPlayers()
    {
        return keepPlayers;
    }

    public boolean isTrimDatabase()
    {
        return trimDatabase;
    }

    protected Object clone() throws CloneNotSupportedException
    {
        TrimConfig newConfig = new TrimConfig();
        
        try
        {
            BeanUtils.copyProperties(newConfig,this);
        }
        catch(Exception e)
        {
            log.fatal("Exception when cloning "+this);
            log.fatal(e);
            throw new RuntimeException(e);
        }
        
        return newConfig;
    }

    public void saveToXml(Element trimXml)
    {
        trimXml.setText(""+trimDatabase);
        trimXml.setAttribute("days",""+days);        
    }

        
}
