/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.config;

import org.jdom.Element;
import org.selectbf.gui.messages.Messages;

public abstract class SourceConfig
{
    private boolean live;
    private String directory;

    public SourceConfig(Element xmlConfig)
    {
        setDirectory(xmlConfig.getText());
        setLive(xmlConfig.getAttributeValue("live").equals("true"));
    }
    
    protected SourceConfig(String directory, boolean live)
    {
        this.directory = directory;
        this.live = live;
    }

    public boolean isLive()
    {
        return live;
    }

    public void setLive(boolean live)
    {
        this.live = live;
    }

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    public String[] toStringArray()
    {
        return new String[] { getDirectory(), Messages.getString("selectbf.gui.configuration.boolean." + isLive()) };
    }

    protected abstract SourceConfig duplicate();

    public void saveToXml(Element sourceXml)
    {
        sourceXml.setText(directory);
        sourceXml.setAttribute("live",""+live);       
    }

}
