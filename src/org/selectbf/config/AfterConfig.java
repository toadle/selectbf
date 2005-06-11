/*
 * Created on 03.04.2005
 * Created by Tim Adler
 */
package org.selectbf.config;

import org.jdom.Element;

public class AfterConfig
{
    public static final String[] AFTER_PARSING = new String[] { "remain", "rename", "delete", "archive" };
    public static final String[] AFTER_DOWNLOAD = new String[] { "remain", "rename", "delete" };

    private String afterParsing;
    private String afterDownload;
    private String archiveFolder;

    public AfterConfig(Element configXml) throws SelectBfConfigException
    {
        Element afterParsingXml = configXml.getChild("after-parsing");
        afterParsing = afterParsingXml.getText();

        if (!isInValues(afterParsing, AFTER_PARSING))
        {
            throw new SelectBfConfigException("<after-parsing> doesn't hold a valid value!");
        }

        Element archiveFolderXml = configXml.getChild("archive-folder");
        archiveFolder = archiveFolderXml.getText();

        if (afterParsing.equals("archive") && (archiveFolder == null || archiveFolder.trim().equals("")))
        {
            throw new SelectBfConfigException("<after-parsing> is set to 'archive', but no archive-folder is given!");
        }

        Element afterDownloadXml = configXml.getChild("after-download");
        afterDownload = afterDownloadXml.getText();

        if (!isInValues(afterDownload, AFTER_DOWNLOAD))
        {
            throw new SelectBfConfigException("<after-download> doesn't hold a valid value!");
        }
    }

    public String getAfterDownload()
    {
        return afterDownload;
    }

    public String getAfterParsing()
    {
        return afterParsing;
    }

    public String getArchiveFolder()
    {
        return archiveFolder;
    }

    private static boolean isInValues(String string, String[] values)
    {
        for (int i = 0; i < values.length; i++)
        {
            if (values[i].equals(string))
            {
                return true;
            }
        }
        return false;
    }

    public void setAfterDownload(String afterDownload)
    {
        this.afterDownload = afterDownload;
    }

    public void setAfterParsing(String afterParsing)
    {
        this.afterParsing = afterParsing;
    }

    public void setArchiveFolder(String archiveFolder)
    {
        this.archiveFolder = archiveFolder;
    }
    
    private AfterConfig(String afterDownload, String afterParsing, String archiveFolder)
    {
        this.afterDownload = afterDownload;
        this.afterParsing = afterParsing;
        this.archiveFolder = archiveFolder;
    }

    protected Object clone() throws CloneNotSupportedException
    {
        return new AfterConfig(new String(afterDownload),new String(afterParsing),new String(archiveFolder));
    }

    public void saveToXml(Element configXml)
    {
        Element afterParsingXml = configXml.getChild("after-parsing");
        
        afterParsingXml.setText(afterParsing);
        
        Element archiveFolderXml = configXml.getChild("archive-folder");
        archiveFolderXml.setText(archiveFolder);

        Element afterDownloadXml = configXml.getChild("after-download");
        afterDownloadXml.setText(afterDownload);        
    }

    
}
