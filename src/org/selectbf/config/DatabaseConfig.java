/*
 * Created on 02.04.2005
 * Created by Tim Adler
 */
package org.selectbf.config;

import org.jdom.Element;

public class DatabaseConfig
{
    private String host;
    private int port;
    private String user;
    private String password;
    private String database;

    public DatabaseConfig(Element dataBaseXml) throws SelectBfConfigException
    {
        String dbUser = dataBaseXml.getAttributeValue("user");
        String dbPassword = dataBaseXml.getAttributeValue("password");
        String dbName = dataBaseXml.getAttributeValue("database");
        String dbPort = dataBaseXml.getAttributeValue("port");
        if (SelectBfConfigException.checkInt(dbPort))
        {
            throw new SelectBfConfigException("Value for Database-Port is invalid!");
        }

        String dbMachine = dataBaseXml.getText();

        this.host = dbMachine;
        this.port = Integer.parseInt(dbPort);
        this.user = dbUser;
        this.password = dbPassword;
        this.database = dbName;

    }

    /**
     * @param host
     * @param port
     * @param user
     * @param password
     * @param database
     */
    public DatabaseConfig(String host, int port, String user, String password, String database)
    {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public String getDatabase()
    {
        return database;
    }

    public String getHost()
    {
        return host;
    }

    public String getPassword()
    {
        return password;
    }

    public int getPort()
    {
        return port;
    }

    public String getUser()
    {
        return user;
    }

    public void setDatabase(String database)
    {
        this.database = database;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    protected Object clone() throws CloneNotSupportedException
    {
        DatabaseConfig newConfig = new DatabaseConfig(new String(host),port,new String(user),new String(password),new String(database));
        return newConfig;
    }

    public void saveToXml(Element dataBaseXml)
    {
        dataBaseXml.setAttribute("user",user);
        dataBaseXml.setAttribute("password",password);
        dataBaseXml.setAttribute("database",database);
        dataBaseXml.setAttribute("port",""+port);
        dataBaseXml.setText(host);
    }

    
}
