package org.selectbf.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.selectbf.config.AfterConfig;
import org.selectbf.config.SelectBfConfig;
import org.selectbf.config.SourceConfig;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * This code was generated using CloudGarden's Jigloo SWT/Swing GUI Builder,
 * which is free for non-commercial use. If Jigloo is being used commercially
 * (ie, by a corporation, company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo. Please visit
 * www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. ************************************* A COMMERCIAL LICENSE
 * HAS NOT BEEN PURCHASED for this machine, so Jigloo or this code cannot be
 * used legally for any corporate or commercial purpose.
 * *************************************
 */
public class ConfigurationDialog extends org.eclipse.swt.widgets.Dialog
{

    private Shell dialogShell;
    private TabItem generalTabItem;
    private Label messageLabel;
    private TabItem logfileTabItem;
    private Combo lanModeCombo;
    private Label lanModeLabel;
    private Combo renameErrorCombo;
    private Label renameErrorLabel;
    private Combo deleteDecompressedCombo;
    private Label deleteDecompLabel;
    private Button archiveFolderButton;
    private Combo afterDownloadCombo;
    private Label afterDownloadLabel;
    private Text archiveFolderText;
    private Label archiveLabel;
    private Combo afterParsingCombo;
    private Group dataManGroup;
    private TableColumn ftpNameColumn;
    private Button ftpSubButton;
    private Button ftpAddButton;
    private Button dirSubButton;
    private Button dirAddButton;
    private Composite ftpButtonComposite;
    private Composite dirButtonComposite;
    private TableColumn ftpLiveColumn;
    private TableColumn ftpPassiveModeColumn;
    private TableColumn ftpPasswordColumn;
    private TableColumn ftpUserColumn;
    private TableColumn ftpPortColumn;
    private TableColumn ftpHostColumn;
    private Table ftpTable;
    private TableEditor ftpTableEditor;
    private TableColumn dirLiveColumn;
    private TableColumn dirNameColumn;
    private Table dirTable;
    private TableEditor dirTableEditor;
    private Group ftpGroup;
    private Group dirGroup;
    private Composite logfilesComposite;
    private Combo keepPlayersCombo;
    private Label keepPlayersLabel;
    private Text daysText;
    private Label daysLabel;
    private Combo trimmingCombo;
    private Label trimmingLabel;
    private Text portText;
    private Label portLabel;
    private Text hostText;
    private Label hostLabel;
    private Text databaseText;
    private Label databaseLabel;
    private Text passwordText;
    private Label passwordLabel;
    private Text loginText;
    private Label loginLabel;
    private Group trimmingGroup;
    private Group loginGroup;
    private Composite databaseComposite;
    private Combo logBotsCombo;
    private Label logBotsLabel;
    private Combo memorySaveCombo;
    private Label memorySaveLabel;
    private Combo consistencyCombo;
    private Label consistencyLabel;
    private Combo skipEmptyCombo;
    private Label skipEmptyLabel;
    private Label afterParsing;
    private Group otherGroup;
    private Group fileManGroup;
    private Composite generalComposite;
    private TabItem databaseTabItem;
    private Button cancelButton;
    private Button okButton;
    private TabFolder tabFolder;

    private SelectBfGui theGui;
    private SelectBfGuiConfigurator configurator;

    /**
     * Auto-generated main method to display this org.eclipse.swt.widgets.Dialog
     * inside a new Shell.
     */
    public static void main(String[] args)
    {
        try
        {
            Display display = Display.getDefault();
            Shell shell = new Shell(display);
            ConfigurationDialog inst = new ConfigurationDialog(shell, SWT.NULL);
            inst.open();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ConfigurationDialog(Shell parent, int style)
    {
        super(parent, style);
    }

    public void open()
    {
        try
        {
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

            {
                // Register as a resource user - SWTResourceManager will
                // handle the obtaining and disposing of resources
                SWTResourceManager.registerResourceUser(dialogShell);
            }

            // Get a copy of the Config
            final SelectBfConfig workingCopy = configurator.createConfigWorkingCopy();

            GridLayout dialogShellLayout = new GridLayout();
            dialogShellLayout.numColumns = 4;
            dialogShell.setLayout(dialogShellLayout);
            dialogShell.layout();
            dialogShell.pack();
            dialogShell.setSize(464, 549);
            dialogShell.setText("Configuration");
            {
                tabFolder = new TabFolder(dialogShell, SWT.NONE);
                GridData tabFolder1LData = new GridData();
                tabFolder1LData.verticalAlignment = GridData.FILL;
                tabFolder1LData.horizontalAlignment = GridData.FILL;
                tabFolder1LData.horizontalSpan = 4;
                tabFolder1LData.grabExcessHorizontalSpace = true;
                tabFolder1LData.grabExcessVerticalSpace = true;
                tabFolder.setLayoutData(tabFolder1LData);
                {
                    generalTabItem = new TabItem(tabFolder, SWT.NONE);
                    generalTabItem.setText("General");
                    {
                        generalComposite = new Composite(tabFolder, SWT.NONE);
                        GridLayout generalCompositeLayout = new GridLayout();
                        generalCompositeLayout.makeColumnsEqualWidth = true;
                        generalComposite.setLayout(generalCompositeLayout);
                        generalTabItem.setControl(generalComposite);
                        {
                            fileManGroup = new Group(generalComposite, SWT.NONE);
                            GridLayout fileManGroupLayout = new GridLayout();
                            fileManGroupLayout.makeColumnsEqualWidth = true;
                            fileManGroupLayout.numColumns = 3;
                            fileManGroup.setLayout(fileManGroupLayout);
                            GridData fileManGroupLData = new GridData();
                            fileManGroupLData.grabExcessHorizontalSpace = true;
                            fileManGroupLData.grabExcessVerticalSpace = true;
                            fileManGroupLData.horizontalAlignment = GridData.FILL;
                            fileManGroupLData.verticalAlignment = GridData.FILL;
                            fileManGroup.setLayoutData(fileManGroupLData);
                            fileManGroup.setText("File Management");
                            {
                                afterParsing = new Label(fileManGroup, SWT.NONE);
                                afterParsing.setText("After Parsing");
                                GridData afterParsingLData = new GridData();
                                afterParsingLData.widthHint = 150;
                                afterParsing.setLayoutData(afterParsingLData);
                                afterParsing
                                        .setToolTipText("How should successfully parsed files be treated.\nremain - Files stay untouched\nrename - Files get renamed to <originalfilename>.parsed\ndelete - Files will be deleted\narchive - Files will be moved to the archive folder");
                            }
                            {
                                afterParsingCombo = new Combo(fileManGroup, SWT.READ_ONLY);
                                GridData afterParsingComboLData = new GridData();
                                afterParsingComboLData.horizontalSpan = 2;
                                afterParsingComboLData.widthHint = 200;
                                afterParsingCombo.setLayoutData(afterParsingComboLData);
                                SelectBfGuiConfigurator.configureMultipleValueCombo(workingCopy.getAfterConfig(), "afterParsing", AfterConfig.AFTER_PARSING,
                                        "selectbf.gui.after.parsing", afterParsingCombo);
                            }
                            {
                                archiveLabel = new Label(fileManGroup, SWT.NONE);
                                GridData archiveLabelLData = new GridData();
                                archiveLabelLData.widthHint = 150;
                                archiveLabel.setLayoutData(archiveLabelLData);
                                archiveLabel.setText("Archive Folder");
                            }
                            {
                                archiveFolderText = new Text(fileManGroup, SWT.BORDER);
                                GridData archiveFolderTextLData = new GridData();
                                archiveFolderTextLData.widthHint = 150;
                                archiveFolderText.setLayoutData(archiveFolderTextLData);
                                SelectBfGuiConfigurator.configureTextBox(workingCopy.getAfterConfig(), "archiveFolder", archiveFolderText);
                            }
                            {
                                archiveFolderButton = new Button(fileManGroup, SWT.PUSH | SWT.CENTER);
                                GridData archiveFolderButtonLData = new GridData();
                                archiveFolderButtonLData.widthHint = 75;
                                archiveFolderButton.setLayoutData(archiveFolderButtonLData);
                                archiveFolderButton.setText("Browse...");
                                archiveFolderButton.addSelectionListener(new SelectionAdapter()
                                {
                                    public void widgetDefaultSelected(SelectionEvent evt)
                                    {
                                        widgetSelected(evt);
                                    }

                                    public void widgetSelected(SelectionEvent evt)
                                    {
                                        DirectoryDialog dirDialog = new DirectoryDialog(dialogShell);
                                        dirDialog.setText("Select a Logfile-directory...");

                                        String newDirectory = dirDialog.open();

                                        if (newDirectory != null)
                                        {
                                            archiveFolderText.setText(newDirectory);
                                        }
                                    }
                                });
                            }
                            {
                                afterDownloadLabel = new Label(fileManGroup, SWT.NONE);
                                GridData afterDownloadLabelLData = new GridData();
                                afterDownloadLabelLData.widthHint = 150;
                                afterDownloadLabel.setLayoutData(afterDownloadLabelLData);
                                afterDownloadLabel.setText("After Download");
                            }
                            {
                                afterDownloadCombo = new Combo(fileManGroup, SWT.READ_ONLY);
                                GridData afterDownloadComboLData = new GridData();
                                afterDownloadComboLData.widthHint = 200;
                                afterDownloadComboLData.horizontalSpan = 2;
                                afterDownloadCombo.setLayoutData(afterDownloadComboLData);
                                SelectBfGuiConfigurator.configureMultipleValueCombo(workingCopy.getAfterConfig(), "afterDownload", AfterConfig.AFTER_DOWNLOAD,
                                        "selectbf.gui.after.download", afterDownloadCombo);
                            }
                            {
                                deleteDecompLabel = new Label(fileManGroup, SWT.NONE);
                                GridData deleteDecompLabelLData = new GridData();
                                deleteDecompLabelLData.widthHint = 150;
                                deleteDecompLabel.setLayoutData(deleteDecompLabelLData);
                                deleteDecompLabel.setText("Delete decompressed Files");
                            }
                            {
                                deleteDecompressedCombo = new Combo(fileManGroup, SWT.READ_ONLY);
                                GridData deleteDecompressedComboLData = new GridData();
                                deleteDecompressedComboLData.horizontalSpan = 2;
                                deleteDecompressedCombo.setLayoutData(deleteDecompressedComboLData);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy, "deleteDecompressed", deleteDecompressedCombo);

                            }
                            {
                                renameErrorLabel = new Label(fileManGroup, SWT.NONE);
                                GridData renameErrorLabelLData = new GridData();
                                renameErrorLabelLData.widthHint = 150;
                                renameErrorLabel.setLayoutData(renameErrorLabelLData);
                                renameErrorLabel.setText("Rename at Error");
                            }
                            {
                                renameErrorCombo = new Combo(fileManGroup, SWT.READ_ONLY);
                                GridData renameErrorComboLData = new GridData();
                                renameErrorComboLData.horizontalSpan = 2;
                                renameErrorCombo.setLayoutData(renameErrorComboLData);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy, "renameAtError", renameErrorCombo);
                            }
                        }
                        {
                            dataManGroup = new Group(generalComposite, SWT.NONE);
                            GridLayout dataManGroupLayout = new GridLayout();
                            dataManGroupLayout.makeColumnsEqualWidth = true;
                            dataManGroupLayout.numColumns = 2;
                            dataManGroup.setLayout(dataManGroupLayout);
                            GridData dataManGroupLData = new GridData();
                            dataManGroupLData.grabExcessHorizontalSpace = true;
                            dataManGroupLData.grabExcessVerticalSpace = true;
                            dataManGroupLData.horizontalAlignment = GridData.FILL;
                            dataManGroupLData.verticalAlignment = GridData.FILL;
                            dataManGroup.setLayoutData(dataManGroupLData);
                            dataManGroup.setText("Data Management");
                            {
                                lanModeLabel = new Label(dataManGroup, SWT.NONE);
                                GridData lanModeLabelLData = new GridData();
                                lanModeLabelLData.widthHint = 150;
                                lanModeLabel.setLayoutData(lanModeLabelLData);
                                lanModeLabel.setText("LAN-Mode");
                            }
                            {
                                lanModeCombo = new Combo(dataManGroup, SWT.READ_ONLY);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy, "lanMode", lanModeCombo);
                            }
                            {
                                skipEmptyLabel = new Label(dataManGroup, SWT.NONE);
                                GridData skipEmptyLabelLData = new GridData();
                                skipEmptyLabelLData.widthHint = 150;
                                skipEmptyLabel.setLayoutData(skipEmptyLabelLData);
                                skipEmptyLabel.setText("Skip empty rounds");
                            }
                            {
                                skipEmptyCombo = new Combo(dataManGroup, SWT.READ_ONLY);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy, "skipEmptyRounds", skipEmptyCombo);
                            }
                            {
                                logBotsLabel = new Label(dataManGroup, SWT.NONE);
                                logBotsLabel.setText("Log Bots");
                            }
                            {
                                logBotsCombo = new Combo(dataManGroup, SWT.READ_ONLY);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy, "logBots", logBotsCombo);
                            }
                        }
                        {
                            otherGroup = new Group(generalComposite, SWT.NONE);
                            GridLayout otherGroupLayout = new GridLayout();
                            otherGroupLayout.makeColumnsEqualWidth = true;
                            otherGroupLayout.numColumns = 2;
                            otherGroup.setLayout(otherGroupLayout);
                            GridData otherGroupLData = new GridData();
                            otherGroupLData.grabExcessHorizontalSpace = true;
                            otherGroupLData.grabExcessVerticalSpace = true;
                            otherGroupLData.horizontalAlignment = GridData.FILL;
                            otherGroupLData.verticalAlignment = GridData.FILL;
                            otherGroup.setLayoutData(otherGroupLData);
                            otherGroup.setText("Other Stuff");
                            {
                                consistencyLabel = new Label(otherGroup, SWT.NONE);
                                GridData consistencyLabelLData = new GridData();
                                consistencyLabelLData.widthHint = 150;
                                consistencyLabel.setLayoutData(consistencyLabelLData);
                                consistencyLabel.setText("Check XML consistency");
                            }
                            {
                                consistencyCombo = new Combo(otherGroup, SWT.READ_ONLY);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy, "consistencyCheck", consistencyCombo);
                            }
                            {
                                memorySaveLabel = new Label(otherGroup, SWT.NONE);
                                GridData memorySaveLabelLData = new GridData();
                                memorySaveLabelLData.widthHint = 150;
                                memorySaveLabel.setLayoutData(memorySaveLabelLData);
                                memorySaveLabel.setText("Try to save memory");
                            }
                            {
                                memorySaveCombo = new Combo(otherGroup, SWT.READ_ONLY);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy, "memorySafer", memorySaveCombo);
                            }
                        }
                    }
                }
                {
                    databaseTabItem = new TabItem(tabFolder, SWT.NONE);
                    databaseTabItem.setText("Database");
                    {
                        databaseComposite = new Composite(tabFolder, SWT.NONE);
                        GridLayout databaseCompositeLayout = new GridLayout();
                        databaseCompositeLayout.makeColumnsEqualWidth = true;
                        databaseComposite.setLayout(databaseCompositeLayout);
                        databaseTabItem.setControl(databaseComposite);
                        {
                            loginGroup = new Group(databaseComposite, SWT.NONE);
                            GridLayout loginGroupLayout = new GridLayout();
                            loginGroupLayout.makeColumnsEqualWidth = true;
                            loginGroupLayout.numColumns = 2;
                            loginGroup.setLayout(loginGroupLayout);
                            GridData loginGroupLData = new GridData();
                            loginGroupLData.grabExcessHorizontalSpace = true;
                            loginGroupLData.grabExcessVerticalSpace = true;
                            loginGroupLData.horizontalAlignment = GridData.FILL;
                            loginGroupLData.verticalAlignment = GridData.FILL;
                            loginGroup.setLayoutData(loginGroupLData);
                            loginGroup.setText("Login Information");
                            {
                                loginLabel = new Label(loginGroup, SWT.NONE);
                                GridData loginLabelLData = new GridData();
                                loginLabelLData.widthHint = 150;
                                loginLabel.setLayoutData(loginLabelLData);
                                loginLabel.setText("User");
                            }
                            {
                                loginText = new Text(loginGroup, SWT.BORDER);
                                GridData loginTextLData = new GridData();
                                loginTextLData.widthHint = 100;
                                loginText.setLayoutData(loginTextLData);
                                SelectBfGuiConfigurator.configureTextBox(workingCopy.getDbConfig(), "user", loginText);
                            }
                            {
                                passwordLabel = new Label(loginGroup, SWT.NONE);
                                GridData passwordLabelLData = new GridData();
                                passwordLabelLData.widthHint = 150;
                                passwordLabel.setLayoutData(passwordLabelLData);
                                passwordLabel.setText("Password");
                            }
                            {
                                passwordText = new Text(loginGroup, SWT.BORDER);
                                GridData text1LData = new GridData();
                                text1LData.widthHint = 100;
                                passwordText.setLayoutData(text1LData);
                                SelectBfGuiConfigurator.configureTextBox(workingCopy.getDbConfig(), "password", passwordText);
                            }
                            {
                                databaseLabel = new Label(loginGroup, SWT.NONE);
                                GridData databaseLabelLData = new GridData();
                                databaseLabelLData.widthHint = 150;
                                databaseLabel.setLayoutData(databaseLabelLData);
                                databaseLabel.setText("Database");
                            }
                            {
                                databaseText = new Text(loginGroup, SWT.BORDER);
                                GridData databaseTextLData = new GridData();
                                databaseTextLData.widthHint = 100;
                                databaseText.setLayoutData(databaseTextLData);
                                SelectBfGuiConfigurator.configureTextBox(workingCopy.getDbConfig(), "database", databaseText);
                            }
                            {
                                hostLabel = new Label(loginGroup, SWT.NONE);
                                hostLabel.setText("Host");
                            }
                            {
                                hostText = new Text(loginGroup, SWT.BORDER);
                                GridData hostTextLData = new GridData();
                                hostTextLData.widthHint = 100;
                                hostText.setLayoutData(hostTextLData);
                                SelectBfGuiConfigurator.configureTextBox(workingCopy.getDbConfig(), "host", hostText);
                            }
                            {
                                portLabel = new Label(loginGroup, SWT.NONE);
                                portLabel.setText("Port");
                            }
                            {
                                GridData portTextLData = new GridData();
                                portTextLData.widthHint = 35;
                                portText = new Text(loginGroup, SWT.BORDER);
                                portText.setLayoutData(portTextLData);
                                SelectBfGuiConfigurator.configureTextBox(workingCopy.getDbConfig(), "port", portText);
                            }
                        }
                        {
                            trimmingGroup = new Group(databaseComposite, SWT.NONE);
                            GridLayout trimmingGroupLayout = new GridLayout();
                            trimmingGroupLayout.makeColumnsEqualWidth = true;
                            trimmingGroupLayout.numColumns = 2;
                            trimmingGroup.setLayout(trimmingGroupLayout);
                            GridData trimmingGroupLData = new GridData();
                            trimmingGroupLData.grabExcessHorizontalSpace = true;
                            trimmingGroupLData.grabExcessVerticalSpace = true;
                            trimmingGroupLData.horizontalAlignment = GridData.FILL;
                            trimmingGroupLData.verticalAlignment = GridData.FILL;
                            trimmingGroup.setLayoutData(trimmingGroupLData);
                            trimmingGroup.setText("Trimming");
                            {
                                trimmingLabel = new Label(trimmingGroup, SWT.NONE);
                                GridData trimmingLabelLData = new GridData();
                                trimmingLabelLData.widthHint = 150;
                                trimmingLabel.setLayoutData(trimmingLabelLData);
                                trimmingLabel.setText("Trim database");
                            }
                            {
                                trimmingCombo = new Combo(trimmingGroup, SWT.READ_ONLY);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy.getTrimConfig(), "trimDatabase", trimmingCombo);
                            }
                            {
                                daysLabel = new Label(trimmingGroup, SWT.NONE);
                                GridData daysLabelLData = new GridData();
                                daysLabelLData.widthHint = 150;
                                daysLabel.setLayoutData(daysLabelLData);
                                daysLabel.setText("Amount of days to keep");
                            }
                            {
                                daysText = new Text(trimmingGroup, SWT.BORDER);
                                GridData daysTextLData = new GridData();
                                daysTextLData.widthHint = 35;
                                daysText.setLayoutData(daysTextLData);
                                SelectBfGuiConfigurator.configureTextBox(workingCopy.getTrimConfig(), "days", daysText);
                            }
                            {
                                keepPlayersLabel = new Label(trimmingGroup, SWT.NONE);
                                keepPlayersLabel.setText("Keep Players");
                            }
                            {
                                keepPlayersCombo = new Combo(trimmingGroup, SWT.READ_ONLY);
                                SelectBfGuiConfigurator.configureBooleanCombo(workingCopy.getTrimConfig(), "keepPlayers", keepPlayersCombo);
                            }
                        }
                    }
                }
                {
                    logfileTabItem = new TabItem(tabFolder, SWT.NONE);
                    logfileTabItem.setText("Logfiles");
                    {
                        logfilesComposite = new Composite(tabFolder, SWT.NONE);
                        GridLayout logfilesCompositeLayout = new GridLayout();
                        logfilesCompositeLayout.makeColumnsEqualWidth = true;
                        logfilesComposite.setLayout(logfilesCompositeLayout);
                        logfileTabItem.setControl(logfilesComposite);
                        {
                            dirGroup = new Group(logfilesComposite, SWT.NONE);
                            GridLayout dirGroupLayout = new GridLayout();
                            dirGroupLayout.makeColumnsEqualWidth = true;
                            dirGroup.setLayout(dirGroupLayout);
                            GridData dirGroupLData = new GridData();
                            dirGroupLData.grabExcessHorizontalSpace = true;
                            dirGroupLData.grabExcessVerticalSpace = true;
                            dirGroupLData.horizontalAlignment = GridData.FILL;
                            dirGroupLData.verticalAlignment = GridData.FILL;
                            dirGroup.setLayoutData(dirGroupLData);
                            dirGroup.setText("Directory");
                            {
                                GridData dirTableLData = new GridData();
                                dirTableLData.grabExcessHorizontalSpace = true;
                                dirTable = new Table(dirGroup, SWT.CHECK | SWT.HIDE_SELECTION | SWT.EMBEDDED | SWT.BORDER);
                                dirTableLData.grabExcessVerticalSpace = true;
                                dirTableLData.horizontalAlignment = GridData.FILL;
                                dirTableLData.verticalAlignment = GridData.FILL;
                                dirTable.setLayoutData(dirTableLData);
                                dirTable.setHeaderVisible(true);
                                {
                                    dirNameColumn = new TableColumn(dirTable, SWT.NONE);
                                    dirNameColumn.setText("Directory");
                                    dirNameColumn.setWidth(350);
                                }
                                {
                                    dirLiveColumn = new TableColumn(dirTable, SWT.NONE);
                                    dirLiveColumn.setText("Live");
                                    dirLiveColumn.setWidth(45);
                                }
                                configurator.configureDirTable(dirTable);

                            }
                            {
                                dirButtonComposite = new Composite(dirGroup, SWT.NONE);
                                RowLayout dirButtonCompositeLayout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
                                dirButtonCompositeLayout.wrap = false;
                                dirButtonCompositeLayout.pack = false;
                                GridData dirButtonCompositeLData = new GridData();
                                dirButtonCompositeLData.grabExcessHorizontalSpace = true;
                                dirButtonCompositeLData.horizontalAlignment = GridData.FILL;
                                dirButtonComposite.setLayoutData(dirButtonCompositeLData);
                                dirButtonComposite.setLayout(dirButtonCompositeLayout);
                                {
                                    dirAddButton = new Button(dirButtonComposite, SWT.PUSH | SWT.CENTER);
                                    dirAddButton.setText("+");
                                    dirAddButton.addSelectionListener(new SelectionAdapter() {
                                        public void widgetSelected(SelectionEvent evt) {
                                            configurator.addDefaultDirEntry(workingCopy,dirTable);
                                        }
                                        public void widgetDefaultSelected(SelectionEvent evt) {
                                            widgetSelected(evt);
                                        }
                                    });
                                }
                                {
                                    dirSubButton = new Button(dirButtonComposite, SWT.PUSH | SWT.CENTER);
                                    RowData dirSubButtonLData = new RowData();
                                    dirSubButtonLData.width = 25;
                                    dirSubButton.setLayoutData(dirSubButtonLData);
                                    dirSubButton.setText("-");
                                    dirSubButton.addSelectionListener(new SelectionAdapter()
                                    {                                        
                                        public void widgetSelected(SelectionEvent evt)
                                        {
                                            configurator.disposeItemEditorsTable(dirTable, SelectBfGuiConfigurator.DIR_EDITORS);

                                            TableItem[] items = dirTable.getItems();
                                            List indices = new ArrayList();
                                            List undeletedConfigs = new ArrayList();
                                            for (int i = 0; i < items.length; i++)
                                            {
                                                if (items[i].getChecked())
                                                {
                                                    indices.add(new Integer(i));
                                                }
                                                else
                                                {
                                                    undeletedConfigs.add(items[i].getData(SelectBfGuiConfigurator.SOURCE_DATA));
                                                }
                                            }
                                            
                                            int[] remIndices = new int[indices.size()];
                                            for (int i = 0; i < indices.size(); i++)
                                            {
                                                remIndices[i] = ((Integer) indices.get(i)).intValue();
                                            }
                                            
                                            
                                            workingCopy.setDirs(undeletedConfigs);
                                            dirTable.remove(remIndices);
                                        }

                                        public void widgetDefaultSelected(SelectionEvent evt)
                                        {
                                            widgetSelected(evt);
                                        }
                                    });
                                }
                            }
                        }
                        {
                            ftpGroup = new Group(logfilesComposite, SWT.NONE);
                            GridLayout ftpGroupLayout = new GridLayout();
                            ftpGroupLayout.makeColumnsEqualWidth = true;
                            ftpGroup.setLayout(ftpGroupLayout);
                            GridData ftpGroupLData = new GridData();
                            ftpGroupLData.grabExcessHorizontalSpace = true;
                            ftpGroupLData.grabExcessVerticalSpace = true;
                            ftpGroupLData.horizontalAlignment = GridData.FILL;
                            ftpGroupLData.verticalAlignment = GridData.FILL;
                            ftpGroup.setLayoutData(ftpGroupLData);
                            ftpGroup.setText("FTP");
                            {
                                GridData ftpTableLData = new GridData();
                                ftpTableLData.grabExcessHorizontalSpace = true;
                                ftpTableLData.grabExcessVerticalSpace = true;
                                ftpTableLData.horizontalAlignment = GridData.FILL;
                                ftpTableLData.verticalAlignment = GridData.FILL;
                                ftpTable = new Table(ftpGroup, SWT.CHECK | SWT.HIDE_SELECTION | SWT.EMBEDDED | SWT.BORDER);
                                ftpTable.setLayoutData(ftpTableLData);
                                ftpTable.setHeaderVisible(true);
                                {
                                    ftpHostColumn = new TableColumn(ftpTable, SWT.NONE);
                                    ftpHostColumn.setText("Host");
                                    ftpHostColumn.setWidth(70);
                                }
                                {
                                    ftpPortColumn = new TableColumn(ftpTable, SWT.NONE);
                                    ftpPortColumn.setText("Port");
                                    ftpPortColumn.setWidth(40);
                                }
                                {
                                    ftpNameColumn = new TableColumn(ftpTable, SWT.NONE);
                                    ftpNameColumn.setText("Directory");
                                    ftpNameColumn.setWidth(70);
                                }
                                {
                                    ftpUserColumn = new TableColumn(ftpTable, SWT.NONE);
                                    ftpUserColumn.setText("User");
                                    ftpUserColumn.setWidth(60);
                                }
                                {
                                    ftpPasswordColumn = new TableColumn(ftpTable, SWT.NONE);
                                    ftpPasswordColumn.setText("Password");
                                    ftpPasswordColumn.setWidth(60);
                                }
                                {
                                    ftpPassiveModeColumn = new TableColumn(ftpTable, SWT.NONE);
                                    ftpPassiveModeColumn.setText("Passive");
                                    ftpPassiveModeColumn.setWidth(50);
                                }
                                {
                                    ftpLiveColumn = new TableColumn(ftpTable, SWT.NONE);
                                    ftpLiveColumn.setText("Live");
                                    ftpLiveColumn.setWidth(45);
                                }
                                configurator.configureFtpTable(ftpTable);
                            }
                            {
                                GridData composite1LData = new GridData();
                                composite1LData.horizontalAlignment = GridData.FILL;
                                composite1LData.grabExcessHorizontalSpace = true;
                                ftpButtonComposite = new Composite(ftpGroup, SWT.NONE);
                                RowLayout composite1Layout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
                                ftpButtonComposite.setLayout(composite1Layout);
                                ftpButtonComposite.setLayoutData(composite1LData);
                                {
                                    ftpAddButton = new Button(ftpButtonComposite, SWT.PUSH | SWT.CENTER);
                                    RowData ftpAddButtonLData = new RowData();
                                    ftpAddButtonLData.width = 25;
                                    ftpAddButton.setLayoutData(ftpAddButtonLData);
                                    ftpAddButton.setText("+");
                                    ftpAddButton.addSelectionListener(new SelectionAdapter() {
                                        public void widgetSelected(SelectionEvent evt) {
                                            configurator.addDefaultFtpEntry(workingCopy,ftpTable);
                                        }
                                        public void widgetDefaultSelected(SelectionEvent evt) {
                                            widgetSelected(evt);
                                        }
                                    });
                                }
                                {
                                    ftpSubButton = new Button(ftpButtonComposite, SWT.PUSH | SWT.CENTER);
                                    RowData ftpSubButtonLData = new RowData();
                                    ftpSubButtonLData.width = 25;
                                    ftpSubButton.setLayoutData(ftpSubButtonLData);
                                    ftpSubButton.setText("-");
                                    ftpSubButton.addSelectionListener(new SelectionAdapter()
                                    {
                                        public void widgetSelected(SelectionEvent evt)
                                        {
                                            configurator.disposeItemEditorsTable(ftpTable, SelectBfGuiConfigurator.FTP_EDITORS);

                                            TableItem[] items = ftpTable.getItems();
                                            List indices = new ArrayList();
                                            List undeletedConfigs = new ArrayList();                                            
                                            for (int i = 0; i < items.length; i++)
                                            {
                                                if (items[i].getChecked())
                                                {
                                                    indices.add(new Integer(i));
                                                }
                                                else
                                                {
                                                    undeletedConfigs.add(items[i].getData(SelectBfGuiConfigurator.SOURCE_DATA));
                                                }
                                            }
                                            
                                            int[] remIndices = new int[indices.size()];
                                            for (int i = 0; i < indices.size(); i++)
                                            {
                                                remIndices[i] = ((Integer) indices.get(i)).intValue();
                                            }
                                            
                                            workingCopy.setFtps(undeletedConfigs);
                                            ftpTable.remove(remIndices);                                            
                                        }

                                        public void widgetDefaultSelected(SelectionEvent evt)
                                        {
                                            widgetSelected(evt);
                                        }
                                    });                                    
                                }
                            }
                        }
                    }
                    tabFolder.setSelection(0);
                }
            }
            {
                messageLabel = new Label(dialogShell, SWT.NONE);
                GridData messageLabelLData = new GridData();
                messageLabelLData.grabExcessHorizontalSpace = true;
                messageLabelLData.horizontalAlignment = GridData.FILL;
                messageLabelLData.horizontalSpan = 2;
                messageLabel.setLayoutData(messageLabelLData);
            }
            {
                okButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                GridData button1LData = new GridData();
                button1LData.horizontalAlignment = GridData.END;
                button1LData.widthHint = 75;
                okButton.setLayoutData(button1LData);
                okButton.setText("OK");
                okButton.addSelectionListener(new SelectionAdapter()
                {
                    public void widgetDefaultSelected(SelectionEvent evt)
                    {
                        configurator.saveConfig(workingCopy);
                        dialogShell.dispose();                        
                    }

                    public void widgetSelected(SelectionEvent evt)
                    {
                        widgetDefaultSelected(evt);
                    }
                });                
            }
            {
                cancelButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                GridData button1LData1 = new GridData();
                button1LData1.widthHint = 75;
                cancelButton.setLayoutData(button1LData1);
                cancelButton.setText("Cancel");
                cancelButton.addSelectionListener(new SelectionAdapter()
                {
                    public void widgetDefaultSelected(SelectionEvent evt)
                    {
                        dialogShell.dispose();
                    }

                    public void widgetSelected(SelectionEvent evt)
                    {
                        widgetDefaultSelected(evt);
                    }
                });
            }
            dialogShell.open();
            Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed())
            {
                if (!display.readAndDispatch())
                    display.sleep();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setTheGui(SelectBfGui theGui)
    {
        this.theGui = theGui;
        this.configurator = theGui.getConfigurator();
    }

}
