/*
 * Created on 01.04.2005
 * Created by Tim Adler
 */
package org.selectbf.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.selectbf.config.DirConfig;
import org.selectbf.config.FtpConfig;
import org.selectbf.config.SelectBfConfig;
import org.selectbf.config.SelectBfConfigException;
import org.selectbf.config.SourceConfig;
import org.selectbf.gui.messages.Messages;

public class SelectBfGuiConfigurator
{
    public static final Integer INPUT_DIR = new Integer(1);
    public static final Integer INPUT_FTP = new Integer(2);
   
    private static final String DIR_NAME_EDITOR = "DIR:NAME:EDITOR";
    private static final String DIR_LIVE_EDITOR = "DIR:LIVE:EDITOR";
    public static final String[] DIR_EDITORS = new String[] {DIR_NAME_EDITOR, DIR_LIVE_EDITOR};
    
    private static final String FTP_NAME_EDITOR = "FTP:NAME:EDITOR";
    private static final String FTP_LIVE_EDITOR = "FTP:LIVE:EDITOR";
    private static final String FTP_HOST_EDITOR = "FTP:HOST:EDITOR";    
    private static final String FTP_PORT_EDITOR = "FTP:PORT:EDITOR";
    private static final String FTP_USER_EDITOR = "FTP:USER:EDITOR";
    private static final String FTP_PASSWORD_EDITOR = "FTP:PASSWORD:EDITOR";
    private static final String FTP_PASSIVE_EDITOR = "FTP:PASSIVE:EDITOR";
    public static final String[] FTP_EDITORS = new String[] {FTP_HOST_EDITOR,FTP_NAME_EDITOR,FTP_LIVE_EDITOR,FTP_PASSIVE_EDITOR,FTP_PASSWORD_EDITOR,FTP_PORT_EDITOR,FTP_USER_EDITOR};
    
    public static final String SOURCE_DATA = "SOURCE:DATA";     
    
    private static Logger log = Logger.getLogger(SelectBfGuiConfigurator.class);

    private static final String[] BOOLEAN_VALUES = new String[] { "selectbf.gui.configuration.boolean.true", "selectbf.gui.configuration.boolean.false" };

    private SelectBfConfig config;
    
    public void disposeItemEditorsTable(Table table, String[] editorAttributes)
    {
        TableItem[] items = table.getItems();
        for(int i = 0; i<items.length; i++)
        {
            SourceConfig sc = (SourceConfig) items[i].getData(SOURCE_DATA);
            
            for(int j = 0; j<editorAttributes.length; j++)
            {
                Control control = (Control) items[i].getData(editorAttributes[j]);
                if(control != null) control.dispose();
            }
            
            items[i].setText(sc.toStringArray());
        }        
    }
    
    private static void configureTable(final Table table, Collection configs)
    {
        for (Iterator i = configs.iterator(); i.hasNext();)
        {
            final SourceConfig sc = (SourceConfig) i.next();
            
            //create the simple tableRow
            String[] values = sc.toStringArray();
            final TableItem item = new TableItem(table, SWT.NULL);
            item.setText(values);
            item.setData(SOURCE_DATA,sc);
        }       
    }
    
    public void addDefaultDirEntry(SelectBfConfig workingCopy,Table table)
    {
        DirConfig defaultConfig = (DirConfig) DirConfig.DEFAULT.duplicate();
        workingCopy.getDirs().add(defaultConfig);        
        addDefaultEntry(defaultConfig,table);
    }

    public void addDefaultFtpEntry(SelectBfConfig workingCopy,Table table)
    {
        FtpConfig defaultConfig = (FtpConfig) FtpConfig.DEFAULT.duplicate();
        config.getFtps().add(defaultConfig);
        addDefaultEntry(FtpConfig.DEFAULT.duplicate(),table);
    }
    
    private void addDefaultEntry(SourceConfig sc, Table table)
    {
        TableItem item = new TableItem(table, SWT.NULL);
        item.setText(sc.toStringArray());
        item.setData(SOURCE_DATA,sc);       
    }
    
    public void configureDirTable(final Table table)
    {
        configureTable(table,config.getDirs());
        
        //register a listener for opening the editors
        table.addListener(SWT.DefaultSelection,new Listener()
        {
            public void handleEvent(Event event)
            {
                TableItem item = table.getSelection()[0];                
                
                // first kill all other editors
                disposeItemEditorsTable(table, DIR_EDITORS);
                
                // now create new editors for the tableitem-values
                final DirConfig dc = (DirConfig) item.getData(SOURCE_DATA);
                createTextTableEditor(dc,"directory",DIR_NAME_EDITOR,0,item);
                createCheckBoxTableEditor(dc,"live",DIR_LIVE_EDITOR,1,item);
                
                item.setText(new String[] {"",""});
            }
        });        
    }

    public void configureFtpTable(final Table table)
    {
        configureTable(table,config.getFtps());
        
        //register a listener for opening the editors
        table.addListener(SWT.DefaultSelection,new Listener()
        {
            public void handleEvent(Event event)
            {
                TableItem item = table.getSelection()[0];                
                
                // first kill all other editors
                disposeItemEditorsTable(table, FTP_EDITORS);
                
                // now create new editors for the tableitem-values
                final FtpConfig fc = (FtpConfig) item.getData(SOURCE_DATA);                
                createTextTableEditor(fc,"host",FTP_HOST_EDITOR,0,item);
                createTextTableEditor(fc,"port",FTP_PORT_EDITOR,1,item);
                createTextTableEditor(fc,"directory",FTP_NAME_EDITOR,2,item);                
                createTextTableEditor(fc,"user",FTP_USER_EDITOR,3,item);
                createTextTableEditor(fc,"password",FTP_PASSWORD_EDITOR,4,item);
                
                createCheckBoxTableEditor(fc,"passiveMode",FTP_PASSIVE_EDITOR,5,item);
                createCheckBoxTableEditor(fc,"live",FTP_LIVE_EDITOR,6,item);
                
                item.setText(new String[]{"","","","","","",""});
            }
        });        
    }
    
    private static void createCheckBoxTableEditor(final SourceConfig sc, final String property, String itemAttribute, int column, TableItem item)
    {
        try
        {
            final Button checkBox = new Button(item.getParent(),SWT.CHECK);
            checkBox.pack();
            Boolean value = (Boolean) PropertyUtils.getProperty(sc,property);
            checkBox.setSelection(value.booleanValue());
            item.setData(itemAttribute,checkBox);
            checkBox.addSelectionListener(new SelectionListener()
            {
                public void widgetDefaultSelected(SelectionEvent e)
                {
                    widgetSelected(e);
                }
            
                public void widgetSelected(SelectionEvent e)
                {
                    try
                    {
                        BeanUtils.setProperty(sc,property,new Boolean(checkBox.getSelection()));
                    }
                    catch (Exception ex)
                    {
                        log.warn("Exception when setting property '"+property+"' on bean "+sc );
                        log.warn(ex);
                    }                       
                }
            
            });
            
            TableEditor tableEditor = new TableEditor(item.getParent());
            tableEditor.minimumWidth = checkBox.getSize().x;
            tableEditor.horizontalAlignment = SWT.CENTER;
            tableEditor.setEditor(checkBox,item,column); 
        }
        catch(Exception e)
        {
            log.fatal("Exception when creating Editor for "+sc+" property:"+property);
            log.fatal(e);            
        }        
    }
    
    private static void createTextTableEditor(final SourceConfig sc,final String property, String itemAttribute, int column, TableItem item)
    {
        try
        {
            final Text editorText = new Text(item.getParent(),SWT.BORDER);
            editorText.setText(BeanUtils.getProperty(sc,property));
            item.setData(itemAttribute,editorText);
            editorText.addModifyListener(new ModifyListener()
            {
            
                public void modifyText(ModifyEvent e)
                {
                    try
                    {
                        BeanUtils.setProperty(sc,property,editorText.getText());
                    }
                    catch (Exception ex)
                    {
                        log.warn("Exception when setting property '"+property+"' on bean "+sc );
                        log.warn(ex);
                    }            
                }
            
            });
            
            TableEditor itemEditor = new TableEditor(item.getParent());
            itemEditor.grabHorizontal = true;
            itemEditor.grabVertical = true;
            itemEditor.setEditor(editorText,item,column);            
        }
        catch(Exception e)
        {
            log.fatal("Exception when creating Editor for "+sc+" property:"+property);
            log.fatal(e);            
        }
    }

    public static void configureTextBox(final Object bean, final String property, final Text text)
    {
        try
        {
            Object value = PropertyUtils.getProperty(bean, property);
            text.setText(value.toString());
            
            text.addModifyListener(new ModifyListener()
            {
                public void modifyText(ModifyEvent e)
                {
                    try
                    {
                        BeanUtils.setProperty(bean,property,text.getText());
                    }
                    catch (Exception ex)
                    {
                        log.fatal("Exception when handling modifyEvent on TextEditor for "+bean+" property:"+property);
                        log.fatal(e); 
                    }
                }
            });
        }
        catch (Exception e)
        {
            log.fatal(e);
        }
    }

    public static void configureMultipleValueCombo(final Object bean, final String property, final String[] values, final String messagePrefix, final Combo combo)
    {
        combo.removeAll();

        try
        {
            Object value = PropertyUtils.getProperty(bean, property);

            for (int i = 0; i < values.length; i++)
            {
                combo.add(Messages.getString(messagePrefix + "." + values[i]));
                if (values[i].equals(value))
                {
                    combo.select(i);
                }
            }
            
            combo.addModifyListener(new ModifyListener()
            {
                public void modifyText(ModifyEvent e)
                {
                    try
                    {
                        for (int i = 0; i < values.length; i++)
                        {
                            String clearText = Messages.getString(messagePrefix + "." + values[i]);
                            if(clearText.equals(combo.getText()))
                            {
                                PropertyUtils.setProperty(bean,property,values[i]);
                            }
                        }
                    }
                    catch(Exception ex)
                    {
                        log.fatal("Exception when MultipleValueCombo for "+bean+" property:"+property);
                        log.fatal(ex);                       
                    }
                }
            });
        }
        catch (Exception e)
        {
            log.fatal(e);
        }
    }

    public static void configureBooleanCombo(final Object bean, final String booleanProperty,final Combo combo)
    {
        combo.removeAll();
        combo.add(Messages.getString(BOOLEAN_VALUES[0]));
        combo.add(Messages.getString(BOOLEAN_VALUES[1]));
        try
        {
            Boolean value = (Boolean) PropertyUtils.getProperty(bean, booleanProperty);

            if (value.booleanValue())
            {
                combo.select(0);
            } else
            {
                combo.select(1);
            }
            
            combo.addModifyListener(new ModifyListener()
            {
                public void modifyText(ModifyEvent e)
                { 
                    try
                    {
                        if(combo.getText().equals(Messages.getString(BOOLEAN_VALUES[0])))
                        {
                            PropertyUtils.setProperty(bean,booleanProperty, new Boolean(true));
                        } else
                        if(combo.getText().equals(Messages.getString(BOOLEAN_VALUES[1])))
                        {
                            PropertyUtils.setProperty(bean,booleanProperty, new Boolean(false));
                        } else
                        {
                            log.warn("Unkown value "+combo.getText()+" in Boolean-Combo");
                        }
                    }
                    catch(Exception ex)
                    {
                        log.warn("Exception when executing Listener for bean:"+bean+" property:"+booleanProperty);
                        log.warn(ex);
                    }
                }
            });

        }
        catch (Exception e)
        {
            log.fatal("Exception when creating BooleanCombo for "+bean+" property:"+booleanProperty);
            log.fatal(e);  
        }
    }

    /**
     * Creates a new Gui-Configurator from the given XML-InputSource. The source
     * should be a valid configured s(bf)-configfile!
     * 
     * @param in
     *            XML-configfile
     * @throws IOException
     * @throws JDOMException
     * @throws SelectBfConfigException
     */
    public SelectBfGuiConfigurator(InputStream in) throws JDOMException, IOException, SelectBfConfigException
    {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);

        config = new SelectBfConfig(doc);
    }

    public SelectBfConfig createConfigWorkingCopy()
    {
        return config.createCopy();
    }
    
    public void saveConfig(SelectBfConfig workingCopy)
    {
        saveConfigInternal(workingCopy,0);
    }
    
    private void saveConfigInternal(SelectBfConfig workingCopy, int retry)
    {
        log.info("Saving workingCopy-Config to config.xml");
        try
        {        
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new FileInputStream("config.xml"));
            
            workingCopy.saveToXml(doc);     
            
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(doc,new FileOutputStream("config.xml"));
            
            config = workingCopy;
            log.info("SUCCESS!");
        }
        catch (FileNotFoundException e)
        {
            if(retry < 1)
            {
                log.warn("Couldn't find config.xml for saving. Will attempt to recreate!");
                log.warn(e);

                restoreDefaultConfig();
                saveConfigInternal(workingCopy,retry++);              
            }
            else
            {
                log.fatal("Couldn't find config.xml for saving. Retry failed. Giving up!");
                log.fatal(e);             
            }
        }
        catch (JDOMException e)
        {
            if(retry < 1)
            {
                log.warn("config.xml has XML-structure errors when saving. Will attempt to recreate!");
                log.warn(e);

                restoreDefaultConfig();
                saveConfigInternal(workingCopy,retry++);              
            }    
            else
            {
                log.fatal("config.xml has XML-structure errors when saving. Retry failed! Giving up!");
                log.fatal(e);                
            }
        }
        catch (IOException e)
        {
            log.warn("I/O Error when saving. Giving up!");
            log.warn(e);
            throw new RuntimeException(e);
        }
        
    }
    
    public static void restoreDefaultConfig()
    {
        try
        {
            File configFile = new File("config.xml");
            configFile.createNewFile();
            SelectBfConfig.restoreDefaultConfig(new FileOutputStream("config.xml"));
        }
        catch(IOException io)
        {
            log.warn("I/O Error when restoring default config. Giving up!");
            log.warn(io);
            throw new RuntimeException(io);
        }       
    }

}
