/*
 * Created on 28.03.2005
 * Created by Tim Adler
 */
package org.selectbf;

import org.jdom.Namespace;
import org.selectbf.event.ProgessEvent;
import org.selectbf.event.ProgressListener;

public abstract class ManagementBase extends SelectBfClassBase
{
    private ProgressListener persistingProgressListener;
    
    public ManagementBase() {};

    public ManagementBase(Namespace ns)
    {
        super(ns);
    }

    public void addPersistingProgressListener(ProgressListener listener)
    {        
        this.persistingProgressListener = listener;
    }    
    
    protected void firePersistingProgressEvent(int number, int total)
    {
        if(persistingProgressListener != null)
        {
            persistingProgressListener.progressChanged(new ProgessEvent(number, total));
        }
    }
}
