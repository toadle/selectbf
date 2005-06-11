/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.selectbf.SelectBf;
import org.selectbf.event.ProcessingEndEvent;
import org.selectbf.event.ProcessingListener;
import org.selectbf.gui.messages.Messages;

public class SelectBfLauncher
{
    private SelectBf program;
    private SelectBfAdapter adapter;
    private Thread t;

    public SelectBfLauncher(SelectBfAdapter adapter)
    {
        this.program = new SelectBf();
        this.adapter = adapter;
        program.addProcessingListener(adapter);
        program.addSourceEventListener(adapter);
        program.addFileEventListener(adapter);
        program.addEventProgressListener(adapter);
        program.addPersistingProgressListener(adapter);
        program.addPersistingStatusListener(adapter);
        program.addDownloadListener(adapter);
    }

    public void launch(final Shell shell)
    {
        Menu menuBar = shell.getMenuBar();
        final MenuItem exitItem = menuBar.getItems()[0].getMenu().getItems()[1];
        
        //register a listener that will prevent closing
        final Listener l = new Listener()
        {
            public void handleEvent(Event event)
            {
                if(isRunning())
                {
                    event.doit = false;
                    
                    MessageBox errorMsg = new MessageBox(shell,SWT.ICON_ERROR);
                    errorMsg.setText(Messages.getString("selectbf.gui.error.noclose.title"));
                    errorMsg.setMessage(Messages.getString("selectbf.gui.error.noclose.text"));
                    errorMsg.open();
                }
            }
        };
        shell.addListener(SWT.Close,l);
        exitItem.setEnabled(false);
        
        //remove the listener when finished
        program.addProcessingListener(new ProcessingListener()
        {
            public void endProcessing(ProcessingEndEvent event)
            {
                shell.getDisplay().asyncExec(new Runnable()
                {
                    public void run()
                    {
                        shell.removeListener(SWT.Close,l); 
                        exitItem.setEnabled(true);                
                    }
                });
            }
            public void startProcessing(){}
        });        
        
        //lets go then
        t = new Thread(program);
        t.start();
    }
    
    public boolean isRunning()
    {
        return t == null ? false : t.isAlive();
    }

    public SelectBfAdapter getAdapter()
    {
        return adapter;
    }

    public void setAdapter(SelectBfAdapter adapter)
    {
        this.adapter = adapter;
    }

}
