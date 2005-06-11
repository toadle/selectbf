/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.event;

import org.selectbf.config.SourceConfig;

public class SourceEvent
{
    private SourceConfig config;

    public SourceEvent(SourceConfig config)
    {
        this.config = config;
    }

    public SourceConfig getConfig()
    {
        return config;
    }

    public void setConfig(SourceConfig config)
    {
        this.config = config;
    }

}
