/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.config;

import org.jdom.Element;

import com.mysql.jdbc.Messages;

public class DirConfig extends SourceConfig
{
    public static final DirConfig DEFAULT = new DirConfig(Messages.getString("selectbf.conf.default.dirconfig.dir"),false);
    
    public DirConfig(Element xmlConfig)
    {
        super(xmlConfig);
    }
    
    private DirConfig(String directory, boolean live)
    {
        super(directory,live);
    }

    public SourceConfig duplicate()
    {
        return new DirConfig(new String(getDirectory()),isLive());
    }
}
