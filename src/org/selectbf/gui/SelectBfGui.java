/*
 * Created on 26.03.2005
 * Created by Tim Adler
 */
package org.selectbf.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jdom.JDOMException;
import org.selectbf.SelectBfConfig;
import org.selectbf.config.SelectBfConfigException;
import org.selectbf.gui.common.UserMessage;
import org.selectbf.gui.icons.ImageRepository;
import org.selectbf.gui.messages.Messages;


/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* *************************************
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
* for this machine, so Jigloo or this code cannot be used legally
* for any corporate or commercial purpose.
* *************************************
*/
public class SelectBfGui
{
    private static Logger log = Logger.getLogger(SelectBfGui.class);    
    
    private Display display;
    private Shell shell = null;
    private WorkArea workArea = null;
    private SelectBfGuiConfigurator configurator;
    
    private SelectBfGui()
    {
        //basic initialisation work
        display = new Display();
        
        Exception initFailure = null;
        UserMessage um = null;
        try
        {
            um = init();
        } 
        catch (Exception e)
        {
            initFailure = e;
        }
        
        //customize the screen
        shell = new Shell(display);
        shell.setText(Messages.getString("selectbf.gui.version")); //$NON-NLS-1$
        shell.setLayout(new GridLayout());
        shell.setSize(444, 385);
        shell.setImage(ImageRepository.getImage("application-icon-16"));
        
        //create the menu
        createMenuBar(this);
        
        //fill the mainscreen
        createWorkArea();
        
        //run the stuff
        shell.open();
        
        if(initFailure != null)
        {
            MessageBox errorMsg = new MessageBox(shell,SWT.ICON_ERROR);
            errorMsg.setText(Messages.getString("selectbf.gui.general.error.init")); //$NON-NLS-1$
            if(initFailure.getMessage()!=null)
            {
                errorMsg.setMessage(initFailure.getMessage());                
            }
            else
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                initFailure.printStackTrace(pw);
                
                errorMsg.setMessage(sw.toString());
            }
            initFailure.printStackTrace();
            
            errorMsg.open();
            shell.dispose();            
        }
        
        if(um != null)
        {
            MessageBox userMsg = new MessageBox(shell,um.getIcon());
            userMsg.setText(um.getText());
            userMsg.setMessage(um.getMessage());
            
            userMsg.open();
        }
        
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
            {
                display.sleep();
            }
        }
        display.dispose();
    }
    
    private UserMessage init() throws Exception
    {
        ImageRepository.loadImages(display);
        
        try
        {
            configurator = new SelectBfGuiConfigurator(new FileInputStream("config.xml"));    
        }
        catch(FileNotFoundException fe)
        {
            //try to recreate
            log.warn("Couldn't find config.xml. Will attempt to recreate!");
            SelectBfGuiConfigurator.restoreDefaultConfig();
            configurator = new SelectBfGuiConfigurator(new FileInputStream("config.xml"));
            
            //give user a hint
            return new UserMessage("config.xml recreated!","config.xml NOT found!\nRestored from default. Please adjust your settings!", SWT.ICON_WARNING);
            
        }
        return null;
    }

    /**
     * This method initializes workArea	
     *
     */    
    private void createWorkArea() {
    	GridData gridData10 = new GridData();
    	workArea = new WorkArea(shell, SWT.NONE);		   
    	gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
    	gridData10.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
    	gridData10.grabExcessHorizontalSpace = true;
    	gridData10.grabExcessVerticalSpace = true;
    	workArea.setLayoutData(gridData10);
    }
    
    private void createMenuBar(final SelectBfGui theGui)
    {
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);
//         Menütitel anlegen
        MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
        fileItem.setText(Messages.getString("selectbf.gui.menu.file")); //$NON-NLS-1$
//         Untermenü für diesen Menütitel anlegen
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileItem.setMenu(fileMenu);
//         Menüeintrag anlegen
        MenuItem item = new MenuItem(fileMenu, SWT.NULL);
        item.setText(Messages.getString("selectbf.gui.menu.configuration")); //$NON-NLS-1$
        //Configuration-Item Clicked
        item.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e)
            {
                ConfigurationDialog configDiag = new ConfigurationDialog(shell, SWT.NULL);
                configDiag.setTheGui(theGui);
                configDiag.open();
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
                widgetSelected(e);
                
            }});
        
        
        item = new MenuItem(fileMenu, SWT.NULL);
        item.setText(Messages.getString("selectbf.gui.menu.exit")); //$NON-NLS-1$
        //Exit-Item Clicked
        item.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e)
            {
               shell.dispose();                
            }

            public void widgetDefaultSelected(SelectionEvent e)
            {
                widgetSelected(e);                
            }});
        
        MenuItem aboutItem = new MenuItem(menuBar, SWT.CASCADE);
        aboutItem.setText(Messages.getString("selectbf.gui.menu.about"));   
        aboutItem.addSelectionListener(new SelectionListener()
        {
        
            public void widgetDefaultSelected(SelectionEvent e)
            {
                widgetSelected(e);        
            }
        
            public void widgetSelected(SelectionEvent e)
            {
                AboutDialog aboutDiag = new AboutDialog(shell,SWT.NULL);
                aboutDiag.open();        
            }
        
        });//$NON-NLS-1$
    }

    public static void main(String[] args)
    {
        SelectBfGui sbfg = new SelectBfGui();
    }

    public SelectBfGuiConfigurator getConfigurator()
    {
        return configurator;
    }
    
    
    
    
    
}
