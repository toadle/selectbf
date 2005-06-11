/*
 * Created on 26.03.2005
 * Created by Tim Adler
 */
package org.selectbf.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.selectbf.gui.icons.ImageRepository;


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
public class WorkArea extends Composite
{

    private Composite composite = null;
    private ProgressBar progressBar = null;
    private Label label = null;
    private Table table = null;
    private ToolItem startToolItem;
    private ToolBar toolBar;
    private Label statusLabel = null;
    private Label numberLabel = null;
    public WorkArea(Composite parent, int style) {
    	super(parent, style);
    	initialize();
    }
    private void initialize() {
    	GridLayout gridLayout1 = new GridLayout();
    	createComposite();
    	createTable();
    	this.setLayout(gridLayout1);
    	gridLayout1.horizontalSpacing = 1;
    	gridLayout1.marginHeight = 0;
    	gridLayout1.verticalSpacing = 1;
    	gridLayout1.marginWidth = 0;
    	setSize(new org.eclipse.swt.graphics.Point(300,200));
    }

    /**
     * This method initializes composite	
     *
     */    
    private void createComposite() {
    	GridData gridData13 = new GridData();
    	GridData gridData41 = new org.eclipse.swt.layout.GridData();
    	GridData gridData12 = new GridData();
    	GridData gridData1 = new GridData();
    	GridLayout gridLayout11 = new GridLayout();
    	GridData gridData4 = new GridData();
    	composite = new Composite(this, SWT.NONE);	
        {
            toolBar = new ToolBar(composite, SWT.NONE);
            {
                startToolItem = new ToolItem(toolBar, SWT.NONE);
                startToolItem.setImage(ImageRepository.getImage("start-icon"));
                startToolItem.setHotImage(ImageRepository.getImage("start-icon-hot"));
                startToolItem.setToolTipText("Click to start the parsing process");
                startToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() { 
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                        
                        MessageBox reallyStart = new MessageBox(composite.getShell(),SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                        reallyStart.setText("Just asking!");
                        reallyStart.setMessage("Start the parsing process?");
                        int selection = reallyStart.open();
                        
                        if(selection == SWT.YES)
                        {
                            launchSelectBf();                    
                        }
                    }
                });
            }
        }
        progressBar = new ProgressBar(composite, SWT.SMOOTH);
        label = new Label(composite, SWT.SHADOW_NONE | SWT.RIGHT);
        numberLabel = new Label(composite, SWT.CENTER);
        statusLabel = new Label(composite, SWT.RIGHT);
    	composite.setLayoutData(gridData4);
    	gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
    	gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
    	gridData4.grabExcessHorizontalSpace = true;
        composite.setLayout(gridLayout11);
        gridLayout11.marginHeight = 0;
        gridLayout11.marginWidth = 0;
        gridLayout11.numColumns = 3;
        label.setText("0 %");
        label.setLayoutData(gridData12);
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = false;
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        progressBar.setLayoutData(gridData1);
        progressBar.setMinimum(0);
        progressBar.setSelection(0);
        gridData12.widthHint = 30;
        gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
        statusLabel.setText("");
        statusLabel.setLayoutData(gridData41);
        gridData41.horizontalSpan = 2;
        gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData41.heightHint = 20;
        numberLabel.setText("");
        numberLabel.setLayoutData(gridData13);
        gridData13.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData13.grabExcessHorizontalSpace = false;
    }

    private void launchSelectBf()
    {
        SelectBfAdapter adapter = new SelectBfAdapter(this);
        final SelectBfLauncher launcher = new SelectBfLauncher(adapter);
        startToolItem.setEnabled(false);
        launcher.launch(getShell()); 
    }
    
    public void addTableItem(String text, String image)
    {
        TableItem item = new TableItem(table,SWT.NULL);
        item.setText(text);
        if(image != null)
        {
            item.setImage(ImageRepository.getImage(image));    
        }        
    }
    
    public void adjustProgressBar(int percent)
    {
        progressBar.setSelection(percent);
        label.setText(percent+"%");
    }
    
    public void enableStartButton()
    {
        startToolItem.setEnabled(true);
    }
    
    public void clearNumber()
    {
        numberLabel.setText("");
    }
    
    public void setNumber(int number, int total)
    {
        numberLabel.setText(number+"/"+total);
    }    
    
    public void setProcessingStatus(String status)
    {
        statusLabel.setText(status);
    }

    /**
     * This method initializes table	
     *
     */    
    private void createTable() {
    	GridData gridData11 = new GridData();
    	table = new Table(this, SWT.BORDER | SWT.MULTI);		   
    	gridData11.grabExcessHorizontalSpace = true;
    	gridData11.grabExcessVerticalSpace = true;
    	gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
    	gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
    	gridData11.horizontalSpan = 3;
    	table.setLayoutData(gridData11);
    }
    
}
