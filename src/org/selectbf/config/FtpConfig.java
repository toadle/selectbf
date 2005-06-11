/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.config;

import org.jdom.Element;
import org.selectbf.gui.messages.Messages;

public class FtpConfig extends SourceConfig
{
    public static final FtpConfig DEFAULT = new FtpConfig(Messages.getString("selectbf.conf.default.ftpconfig.dir"),false,Messages.getString("selectbf.conf.default.ftpconfig.host"),21,false,Messages.getString("selectbf.conf.default.ftpconfig.user"),Messages.getString("selectbf.conf.default.ftpconfig.password"));
    
    private String host;
    private int port;
    private boolean passiveMode;
    private String user;
    private String password;

    public FtpConfig(Element xmlConfig)
    {
        super(xmlConfig);
        setHost(xmlConfig.getAttributeValue("host"));

        if (xmlConfig.getAttributeValue("port") == null)
        {
            setPort(21);
        } else
            setPort(Integer.parseInt(xmlConfig.getAttributeValue("port")));

        setUser(xmlConfig.getAttributeValue("user"));
        setPassword(xmlConfig.getAttributeValue("password"));

        if (xmlConfig.getAttributeValue("passive") == null)
        {
            setPassiveMode(false);
        } else
            setPassiveMode(xmlConfig.getAttributeValue("passive").equals("true"));
    }

    /**
     * @param host
     * @param port
     * @param passiveMode
     * @param user
     * @param password
     */
    private FtpConfig(String directory, boolean live,String host, int port, boolean passiveMode, String user, String password)
    {
        super(directory,live);
        // TODO Auto-generated constructor stub
        this.host = host;
        this.port = port;
        this.passiveMode = passiveMode;
        this.user = user;
        this.password = password;
    }

    public String[] toStringArray()
    {
        return new String[] { getHost(), "" + getPort(), getDirectory(), getUser(), getPassword(),
                Messages.getString("selectbf.gui.configuration.boolean." + isPassiveMode()),
                Messages.getString("selectbf.gui.configuration.boolean." + isLive()) };
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public boolean isPassiveMode()
    {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode)
    {
        this.passiveMode = passiveMode;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public SourceConfig duplicate()
    {
        return new FtpConfig(new String(getDirectory()),isLive(),new String(host),port,passiveMode,new String(user),new String(password));
    }

    public void saveToXml(Element ftpXml)
    {
        super.saveToXml(ftpXml);
        
        ftpXml.setAttribute("host",host);
        ftpXml.setAttribute("port",""+port);
        ftpXml.setAttribute("user",user);
        ftpXml.setAttribute("password",password);
        ftpXml.setAttribute("passive",""+passiveMode);
        
    }

}
