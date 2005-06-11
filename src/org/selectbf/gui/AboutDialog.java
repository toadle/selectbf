package org.selectbf.gui;

import java.io.IOException;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.RGB;

import com.cloudgarden.resource.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
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
public class AboutDialog extends org.eclipse.swt.widgets.Dialog {

	private Shell dialogShell;
	private CLabel aboutImage;
	private CLabel documentationLabel;
	private CLabel advertLabel;
	private Label paypalLabel;
	private CLabel additionerLabel;
	private CLabel additionalLabel;
	private Button closeButton;
	private CLabel documenterLabel;
	private CLabel developerLabel;
	private CLabel developermentLabel;
	private Composite creditsComposite;
	private CLabel sbfLabel;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Dialog inside a new Shell.
	*/
	public static void main(String[] args) {
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			AboutDialog inst = new AboutDialog(shell, SWT.NULL);
			inst.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AboutDialog(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		try {
			Shell parent = getParent();
			dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

                {
                    //Register as a resource user - SWTResourceManager will
                    //handle the obtaining and disposing of resources
                    SWTResourceManager.registerResourceUser(dialogShell);
                }


                GridLayout dialogShellLayout = new GridLayout();
			dialogShell.setLayout(dialogShellLayout);
			dialogShellLayout.numColumns = 2;
			dialogShell.layout();
			dialogShell.pack();
			dialogShell.setSize(308, 391);
			dialogShell.setText("About");
            {
                aboutImage = new CLabel(dialogShell, SWT.NONE);
                GridData aboutImageLData = new GridData();
                aboutImageLData.grabExcessHorizontalSpace = true;
                aboutImageLData.horizontalAlignment = GridData.FILL;
                aboutImageLData.verticalAlignment = GridData.FILL;
                aboutImageLData.horizontalSpan = 2;
                aboutImage.setLayoutData(aboutImageLData);
                aboutImage.setAlignment(SWT.CENTER);
                aboutImage.setImage(ImageRepository.getImage("about.image"));
            }
            {
                sbfLabel = new CLabel(dialogShell, SWT.NONE);
                sbfLabel.setText("select(bf) 0.5 GUI-Version");
                GridData sbfLabelLData = new GridData();
                sbfLabelLData.horizontalAlignment = GridData.CENTER;
                sbfLabelLData.horizontalSpan = 2;
                sbfLabel.setLayoutData(sbfLabelLData);
                sbfLabel.setAlignment(SWT.CENTER);
            }
            {
                creditsComposite = new Composite(dialogShell, SWT.BORDER);
                GridLayout creditsCompositeLayout = new GridLayout();
                creditsCompositeLayout.makeColumnsEqualWidth = true;
                creditsCompositeLayout.numColumns = 2;
                GridData creditsCompositeLData = new GridData();
                creditsCompositeLData.grabExcessVerticalSpace = true;
                creditsCompositeLData.grabExcessHorizontalSpace = true;
                creditsCompositeLData.horizontalAlignment = GridData.FILL;
                creditsCompositeLData.verticalAlignment = GridData.FILL;
                creditsCompositeLData.horizontalSpan = 2;
                creditsComposite.setLayoutData(creditsCompositeLData);
                creditsComposite.setLayout(creditsCompositeLayout);
                creditsComposite.setBackground(SWTResourceManager.getColor(255, 255, 255));
                {
                    developermentLabel = new CLabel(creditsComposite, SWT.NONE);
                    GridData developermentLabelLData = new GridData();
                    developermentLabelLData.verticalAlignment = GridData.BEGINNING;
                    developermentLabel.setLayoutData(developermentLabelLData);
                    developermentLabel.setText("Development:");
                    developermentLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
                }
                {
                    developerLabel = new CLabel(creditsComposite, SWT.NONE);
                    GridData developerLabelLData = new GridData();
                    developerLabelLData.grabExcessHorizontalSpace = true;
                    developerLabelLData.verticalAlignment = GridData.BEGINNING;
                    developerLabel.setLayoutData(developerLabelLData);
                    developerLabel.setText("Tim Adler");
                    developerLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
                }
                {
                    additionalLabel = new CLabel(creditsComposite, SWT.NONE);
                    GridData additionalLabelLData = new GridData();
                    additionalLabelLData.verticalAlignment = GridData.BEGINNING;
                    additionalLabel.setLayoutData(additionalLabelLData);
                    additionalLabel.setText("Additional Work:");
                    additionalLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
                }
                {
                    additionerLabel = new CLabel(creditsComposite, SWT.NONE);
                    additionerLabel.setText("Andy Abshagen\nGary Chiu");
                    additionerLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
                }
                {
                    documentationLabel = new CLabel(creditsComposite, SWT.NONE);
                    documentationLabel.setText("Documentation:");
                    documentationLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
                }
                {
                    documenterLabel = new CLabel(creditsComposite, SWT.NONE);
                    documenterLabel.setText("Jason Warnes");
                    documenterLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
                }
                {
                    advertLabel = new CLabel(creditsComposite, SWT.NONE);
                    GridData advertLabelLData = new GridData();
                    advertLabelLData.horizontalSpan = 2;
                    advertLabelLData.horizontalAlignment = GridData.CENTER;
                    advertLabel.setLayoutData(advertLabelLData);
                    advertLabel.setText("http://www.selectbf.org");
                    advertLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
                    advertLabel.setForeground(new Color(dialogShell.getDisplay(),new RGB(0,0,170)));
                    advertLabel.setCursor(new Cursor(dialogShell.getDisplay(),SWT.CURSOR_HAND));
                    advertLabel.addMouseListener(new MouseAdapter()
                    {
                        public void mouseUp(MouseEvent e)
                        {
                            Program.launch(Messages.getString("selectbf.gui.about.link"));
                        }
                    
                        public void mouseDoubleClick(MouseEvent e)
                        {
                            mouseUp(e);
                        }
                    
                    });
                }
            }
            {
                paypalLabel = new Label(dialogShell, SWT.NONE);
                GridData paypalLabelLData = new GridData();
                paypalLabelLData.heightHint = 32;
                paypalLabelLData.widthHint = 62;
                paypalLabel.setLayoutData(paypalLabelLData);
                paypalLabel.setBounds(54, -14, 62, 32);
                paypalLabel.setImage(ImageRepository.getImage("about.image.paypal"));
                paypalLabel.setCursor(new Cursor(dialogShell.getDisplay(),SWT.CURSOR_HAND));                
                paypalLabel.addMouseListener(new MouseAdapter()
                {
                    public void mouseUp(MouseEvent e)
                    {
                        Program.launch(Messages.getString("selectbf.gui.about.paypal.link"));
                    }
                
                    public void mouseDoubleClick(MouseEvent e)
                    {
                        mouseUp(e);
                    }
                
                });                
            }
            {
                closeButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
                GridData closeButtonLData = new GridData();
                closeButtonLData.horizontalAlignment = GridData.END;
                closeButtonLData.widthHint = 75;
                closeButton.setLayoutData(closeButtonLData);
                closeButton.setText("OK");
                closeButton.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent evt) {
                        dialogShell.dispose();
                    }
                    public void widgetDefaultSelected(SelectionEvent evt) {
                        widgetSelected(evt);
                    }
                });
            }
			dialogShell.open();
			Display display = dialogShell.getDisplay();
			while (!dialogShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
