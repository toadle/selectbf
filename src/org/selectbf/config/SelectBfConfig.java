/*
 * Created on 02.04.2005
 * Created by Tim Adler
 */
package org.selectbf.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

public class SelectBfConfig
{
    private static final Log log = LogFactory.getLog(SelectBfConfig.class); 
    
    private DatabaseConfig dbConfig;
    private AfterConfig afterConfig;
    private TrimConfig trimConfig;
    private boolean deleteDecompressed;
    private boolean logBots;
    private boolean lanMode;
    private boolean renameAtError;
    private boolean consistencyCheck;
    private boolean memorySafer;
    private boolean skipEmptyRounds;
    private List ftps;
    private List dirs;

    public SelectBfConfig(Document doc) throws SelectBfConfigException
    {
        Element config = doc.getRootElement();

        Element database = config.getChild("database");
        this.dbConfig = new DatabaseConfig(database);

        afterConfig = new AfterConfig(config);

        Element trimXml = config.getChild("trim-database");
        trimConfig = new TrimConfig(trimXml);

        setValue("delete-decompressed-xml-files", "deleteDecompressed", config);
        setValue("log-bots", "logBots", config);
        setValue("lan-mode", "lanMode", config);
        setValue("rename-at-error", "renameAtError", config);
        setValue("consistency-check", "consistencyCheck", config);
        setValue("memory-safer", "memorySafer", config);
        setValue("skip-empty-rounds", "skipEmptyRounds", config);

        // Configure sources
        ftps = new ArrayList();
        dirs = new ArrayList();

        Element logsXml = config.getChild("logs");
        for (Iterator i = logsXml.getChildren("ftp").iterator(); i.hasNext();)
        {
            Element ftpXml = (Element) i.next();
            ftps.add(new FtpConfig(ftpXml));
        }

        for (Iterator i = logsXml.getChildren("dir").iterator(); i.hasNext();)
        {
            Element dirXml = (Element) i.next();
            dirs.add(new DirConfig(dirXml));
        }

    }

    private SelectBfConfig()
    {
        // for cloning purposes
    }

    private void setValue(String xmlElementName, String propertyName, Element configXml) throws SelectBfConfigException
    {
        Element elementXml = configXml.getChild(xmlElementName);
        try
        {
            BeanUtils.setProperty(this, propertyName, elementXml.getText());
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new SelectBfConfigException("The value for <" + xmlElementName + "> is invalid!");
        }
    }
    
    private void setXmlValue(String xmlElementName, String value, Element configXml)
    {
        Element elementXml = configXml.getChild(xmlElementName);
        elementXml.setText(value);
    }    

    public AfterConfig getAfterConfig()
    {
        return afterConfig;
    }

    public boolean isConsistencyCheck()
    {
        return consistencyCheck;
    }

    public DatabaseConfig getDbConfig()
    {
        return dbConfig;
    }

    public boolean isDeleteDecompressed()
    {
        return deleteDecompressed;
    }

    public boolean isLanMode()
    {
        return lanMode;
    }

    public boolean isLogBots()
    {
        return logBots;
    }

    public boolean isMemorySafer()
    {
        return memorySafer;
    }

    public boolean isRenameAtError()
    {
        return renameAtError;
    }

    public boolean isSkipEmptyRounds()
    {
        return skipEmptyRounds;
    }

    public TrimConfig getTrimConfig()
    {
        return trimConfig;
    }

    public void setAfterConfig(AfterConfig afterConfig)
    {
        this.afterConfig = afterConfig;
    }

    public void setConsistencyCheck(boolean consistencyCheck)
    {
        this.consistencyCheck = consistencyCheck;
    }

    public void setDbConfig(DatabaseConfig dbConfig)
    {
        this.dbConfig = dbConfig;
    }

    public void setDeleteDecompressed(boolean deleteDecompressed)
    {
        this.deleteDecompressed = deleteDecompressed;
    }

    public void setLanMode(boolean lanMode)
    {
        this.lanMode = lanMode;
    }

    public void setLogBots(boolean logBots)
    {
        this.logBots = logBots;
    }

    public void setMemorySafer(boolean memorySafer)
    {
        this.memorySafer = memorySafer;
    }

    public void setRenameAtError(boolean renameAtError)
    {
        this.renameAtError = renameAtError;
    }

    public void setSkipEmptyRounds(boolean skipEmptyRounds)
    {
        this.skipEmptyRounds = skipEmptyRounds;
    }

    public void setTrimConfig(TrimConfig trimConfig)
    {
        this.trimConfig = trimConfig;
    }

    public List getDirs()
    {
        return dirs;
    }

    public List getFtps()
    {
        return ftps;
    }

    public void setDirs(List dirs)
    {
        this.dirs = dirs;
    }
    

    public void setFtps(List ftps)
    {
        this.ftps = ftps;
    }
    

    public SelectBfConfig createCopy()
    {
        SelectBfConfig newConfig = new SelectBfConfig();
        
        try
        {
            BeanUtils.copyProperties(newConfig,this);
            newConfig.setAfterConfig((AfterConfig) afterConfig.clone());
            newConfig.setTrimConfig((TrimConfig) trimConfig.clone());
            newConfig.setDbConfig((DatabaseConfig) dbConfig.clone());  
            
            ArrayList newDirs = new ArrayList();
            for(Iterator i = dirs.iterator(); i.hasNext();)
            {
                newDirs.add(((DirConfig) i.next()).duplicate());
            }
            
            ArrayList newFtps = new ArrayList();
            for(Iterator i = ftps.iterator(); i.hasNext();)
            {
                newFtps.add(((FtpConfig) i.next()).duplicate());
            }            
            
            newConfig.dirs = newDirs;
            newConfig.ftps = newFtps;
        }
        catch (Exception e)
        {
            log.fatal("Exception when cloning config "+this);
            log.fatal(e);
            throw new RuntimeException(e);
        }
        
        return newConfig;
    }

    public void saveToXml(Document doc)
    {
        Element config = doc.getRootElement();

        Element database = config.getChild("database");
        this.dbConfig.saveToXml(database);
        this.afterConfig.saveToXml(config);

        Element trimXml = config.getChild("trim-database");
        trimConfig.saveToXml(trimXml);
        
        setXmlValue("delete-decompressed-xml-files",""+deleteDecompressed,config);
        setXmlValue("delete-decompressed-xml-files",""+deleteDecompressed,config);
        setXmlValue("log-bots",""+logBots,config);
        setXmlValue("lan-mode",""+lanMode,config);
        setXmlValue("rename-at-error",""+renameAtError,config);
        setXmlValue("consistency-check",""+consistencyCheck,config);
        setXmlValue("memory-safer",""+memorySafer,config);
        setXmlValue("skip-empty-rounds",""+skipEmptyRounds,config);
        
        config.removeChildren("logs");
        
        Element logsXml = new Element("logs");
        config.addContent(logsXml);        

        for (Iterator i =  ftps.iterator(); i.hasNext();)
        {
            FtpConfig fc = (FtpConfig) i.next();
            
            Element ftpXml = new Element("ftp");            
            fc.saveToXml(ftpXml);
            
            logsXml.addContent(ftpXml);
        }

        for (Iterator i =  dirs.iterator(); i.hasNext();)
        {
            DirConfig dc = (DirConfig) i.next();
            
            Element dirXml = new Element("dir");            
            dc.saveToXml(dirXml);
            
            logsXml.addContent(dirXml);
        }
        
    }

    public static void restoreDefaultConfig(OutputStream os) throws IOException
    {
        InputStream is = SelectBfConfig.class.getResourceAsStream("base-config.xml");
        
        //now copy the default config over
        byte[] buf = new byte[1024];
        int i = 0;
        while((i=is.read(buf))!=-1) 
        {
            os.write(buf, 0, i);
        }
        is.close();
        os.close();
    }

    
}
