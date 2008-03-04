package org.selectbf;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;
import org.selectbf.config.DirConfig;
import org.selectbf.config.FtpConfig;
import org.selectbf.config.SourceConfig;
import org.selectbf.event.DownloadEvent;
import org.selectbf.event.DownloadListener;
import org.selectbf.event.FileStatusEvent;
import org.selectbf.event.PersistingStatusListener;
import org.selectbf.event.ProgressListener;
import org.selectbf.event.FileEndEvent;
import org.selectbf.event.FileEventListener;
import org.selectbf.event.FileStartEvent;
import org.selectbf.event.ProcessingEndEvent;
import org.selectbf.event.ProcessingListener;
import org.selectbf.event.SourceEvent;
import org.selectbf.event.SourceEventListener;
import org.selectbf.gui.SelectBfGui;
import org.selectbf.gui.messages.Messages;

import com.jcraft.jzlib.ZInputStream;

public class SelectBf implements Runnable
{
    private static Logger log = Logger.getLogger(SelectBf.class);
    
	private SelectBfConfig sbfConfig;
    private SourceEventListener sel;
    private List processiongListeners = new ArrayList();
    private FileEventListener fel;
    private ProgressListener epl;
    private ProgressListener ppl;   
    private PersistingStatusListener psl;
    private DownloadListener dl;

	public void run()
	{        
        fireStartProcessingEvent();
		log.info("select(bf) 0.5 - A Battlefield XML Log File Parser");
        log.info("----------------------------------------------------");
		log.info("Copyright (C) 2005 Tim Adler");
		log.info("Published under GPL http://www.gnu.org/licenses/gpl.txt");
		log.info("This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY");
		log.info("without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.");
		log.info("Available at http://www.selectbf.org"); 
		log.info("----------------------------------------------------");
		System.out.println("");
        
		try
		{
			SAXBuilder builder = new SAXBuilder();
			Vector dirs = new Vector();
			Vector ftps = new Vector();
			
			try
			{
				//read the configuration
				File config = new File("config.xml");
				if(!config.exists())
				{
					throw new SelectBfException(SelectBfException.CONFIG_FILE_MISSING);
				} 
	
				Document configDoc = builder.build(config);
				Element configRoot = configDoc.getRootElement();
				
				sbfConfig = new SelectBfConfig(configRoot);
		
				//NOW register all the log sources
				Element logs = configRoot.getChild("logs");
				List logdirs = logs.getChildren("dir");
				
				for(Iterator i = logdirs.iterator(); i.hasNext();)
				{
					Element logdir = (Element) i.next();
					
                    DirConfig dc = new DirConfig(logdir);
					
					dirs.add(dc);
				}
				
				List ftpdirs = logs.getChildren("ftp");
				for(Iterator i = ftpdirs.iterator(); i.hasNext();)
				{
					Element ftpdir = (Element) i.next();
										
                    FtpConfig ftpc = new FtpConfig(ftpdir);
					
					ftps.add(ftpc);
				}
			}
			catch(JDOMParseException e)
			{
				e.printStackTrace();
				throw new SelectBfException(SelectBfException.CONFIG_FILE_ERROR,e.toString());
			}
			
			//process normal directories
			for(Iterator ite = dirs.iterator(); ite.hasNext();)
			{			
				DirConfig dc = (DirConfig) ite.next();
                fireBeginSourceEvent(dc);
				
				String targetdir = dc.getDirectory();
				
				//check if dir is declarated "live"
				boolean live = dc.isLive();
				File dir = new File(targetdir);
				
				if(!dir.exists()) 
				{
					log.warn("There is no dir '"+targetdir+"' - SKIPPING");
				}
				else
				{
					log.info("Logs-Directory: "+dir.getAbsolutePath());
					logFilesInDir(dir, live);					
				}
                fireEndSourceEvent(dc);
			}
			
			//process FTP-Directories
			for(Iterator ite = ftps.iterator(); ite.hasNext();)
			{
                FtpConfig ftpc = (FtpConfig) ite.next();				
				processFtpDir(ftpc);
			}
			
			//trim the database if wanted
			if(sbfConfig.isTrimDatabase())
			{
				log.info("-----------------TRIMMING DATABASE-----------------\nPlease wait this takes a while ");
				DatabaseTrimmer dbt = new DatabaseTrimmer(sbfConfig);
				dbt.trim();
				log.info(dbt.toString());
				dbt.close();
			}
			
			//now precache the needed tables
			log.info("-----------------PRECACHING DATA-----------------\nPlease wait this takes a while ");
			DatabaseCacher dbc = new DatabaseCacher(sbfConfig);
			dbc.cacheCharacterTypeUsage();
			dbc.cacheVehicleUsage();
			dbc.cacheWeaponKills();
			dbc.cachePlayerRanking();
			dbc.cacheMapStats();
			dbc.close();
	
		}
		catch(Exception e)
		{
			handleException(e);
		}
		log.info("---------------------------------------------------");
		log.info("Process FINISHED");
		log.info("Thx for using select(bf)!");
		log.info("---------------------------------------------------");
        fireEndProcessingEvent();
	}
	
	private void processFtpDir(FtpConfig ftpc) throws SelectBfException
	{
        fireBeginSourceEvent(ftpc);
		try
		{
			FTPClient ftp = new FTPClient();
			log.info("[FTP] Connecting to "+ftpc.getHost()+":"+ftpc.getPort());
			ftp.connect(ftpc.getHost(),ftpc.getPort());
            if(ftpc.isPassiveMode())
            {
                log.info("Entering PASSIVE mode.");
                ftp.enterLocalPassiveMode();                
            }
			if(ftp.login(ftpc.getUser(),ftpc.getPassword()))
			{
				log.info("[FTP] LOGIN successful!");
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				
				if(ftp.changeWorkingDirectory(ftpc.getDirectory()))
				{
					File _ftp_download = new File("_ftp_download");
					if(!_ftp_download.exists())
					{
						_ftp_download.mkdir();
					}
					
					FTPFile[] ftpfiles = ftp.listFiles();
					
					int files_to_process = 0;
					for(int i = 0; i<ftpfiles.length; i++)
					{
						FTPFile f = ftpfiles[i];
						if(f.getName().endsWith(".xml") || f.getName().endsWith(".zxml"))
						{						
							files_to_process++;
						}
					}
					
					int processed_files = 0;
					for(int i = 0; i<ftpfiles.length; i++)
					{
						FTPFile f = ftpfiles[i];
						
						if(!((i==(ftpfiles.length-1) && ftpc.isLive())))
						{
							if(f.getName().endsWith(".xml") || f.getName().endsWith(".zxml"))
							{
								processed_files++;
								long size = f.getSize();
								log.info("[FTP] Downloading "+f.getName()+" ("+size+" Bytes) ("+processed_files+"/"+files_to_process+")");
                                fireDownloadBeginEvent(f.getName(),size,i,ftpfiles.length);
								OutputStream output = new FileOutputStream("_ftp_download/"+f.getName());
								ftp.retrieveFile(f.getName(),output);
								
								if(sbfConfig.getAfter_download().equals("rename"))
								{
									if(!ftp.rename(f.getName(),f.getName()+".downloaded"))
									{
										log.warn(" COULDN'T RENAME");
									}
									
								} else 
								if(sbfConfig.getAfter_download().equals("delete"))
								{
									if(!ftp.deleteFile(f.getName()))
									{
										log.warn(" COULDN'T DELETE");
									}
								}
								log.info(" FINISHED");
							}
						}	
						else
						{
							log.info("[FTP] Skipping "+f.getName()+" because LIVE is set!");
						}			
					}
				
					log.info("[FTP] Closing connection");
					
					ftp.logout();
					ftp.disconnect();
					
					logFilesInDir(_ftp_download, false);
					
					log.info("[FTP] Cleaning _ftp_download !");
					String[] logs = _ftp_download.list();
					for(int i = 0; i<logs.length; i++)
					{
						String file = logs[i];
						if(file.endsWith(".xml") || file.endsWith(".zxml") || file.endsWith(".parsed") || file.endsWith(".error"))
						{
							new File("_ftp_download"+File.separator+file).delete();
						}
					}
				}
				else
				{
					log.warn("[FTP] The Directory '"+ftpc.getDirectory()+"' does not exist OR has no permission to access!");
					log.warn("[FTP] Closing connection");
					ftp.logout();
					ftp.disconnect();
				}
			}
			else
			{
                log.warn("[FTP] Couldn't login to the FTP-Location, please check your Login-Information!");
			}
						
		}
		catch(IOException io)
		{
			log.warn("[FTP] Couldn't connect to "+ftpc.getHost()+": "+io.toString());
		}	
        fireEndSourceEvent(ftpc);
	}


	private void logFilesInDir(File dir, boolean live) throws SelectBfException
	{
		try
		{
			String[] dirlist = dir.list();
			
			//find out which file is newest and determine number of files that have to be processed
			int number_of_files = 0;
			int index_of_most_actual_file = -1;
			long newestTime = -1;
			for(int i = 0; i<dirlist.length; i++)
			{
				if(dirlist[i].endsWith(".zxml") || dirlist[i].endsWith(".xml"))
				{
					number_of_files++;
					File f = new File(dir.getAbsolutePath()+File.separatorChar+dirlist[i]);
					if(f.lastModified()>newestTime)
					{
						newestTime = f.lastModified();
						index_of_most_actual_file = i;
					}
				}
			}
						
			int log_file_number = 0;
			for(int i = 0; i<dirlist.length;i++)
			{
				String file_to_process = dir.getAbsolutePath()+File.separatorChar+dirlist[i];
				
				boolean delete_after_finish = false;
				boolean was_zxml = false;
				String org_filename = "";
				
				boolean isLiveFile = false;
				
				if(file_to_process.endsWith(".zxml"))
				{
					org_filename = file_to_process;
					file_to_process = decompressZXMLFile(file_to_process);
					delete_after_finish = true;
					was_zxml = true;
				}
					
				if(file_to_process.endsWith(".xml"))
				{
					File src = new File(file_to_process);
					log_file_number++;
                    
                    fireFileStartEvent(src,log_file_number,number_of_files);
						
					boolean errors = false;
					
					log.info("-> processing File '"+src.getName()+"' ("+log_file_number+"/"+number_of_files+")");
					
					try
					{
						if(live && i == index_of_most_actual_file)
						{
							isLiveFile = true;
							log.info(" NEWEST FILE - LIVE is set -> Probably under server-access - SKIPPING");
						}
						else
						{
							logFile(src);
							if(sbfConfig.isMemorySafer())
							{
								System.gc();
							}
                            fireFileEndEvent(src,log_file_number,number_of_files);
						}
					}
					catch(JDOMParseException e)
					{
						log.warn(" ERROR  Data-structure of "+src+" CORRUPT:"+e.toString()+" - SKIPPING");
						errors = true;
                        fireFileEndEvent(src,log_file_number,number_of_files,e);                        
					}
					catch(JDOMException e)
					{
						log.warn(" ERROR  Data-structure of "+src+" CORRUPT:"+e.toString()+" - SKIPPING");
						errors = true;
                        fireFileEndEvent(src,log_file_number,number_of_files,e);                        
					}
					catch(CancelProcessException ce)
					{
						//This is when the Cancel is triggered by intention
						log.warn(" ERROR "+ce.getMessage()+" SKIPPED");
						errors = true;
                        fireFileEndEvent(src,log_file_number,number_of_files,ce);                        
					}
					catch(SelectBfException se)
					{
						//This is when the Cancel is forced because of a logic issue
						log.warn(" ERROR This file has LOGIC issues: "+se.getMessage()+" SKIPPED");
						se.printStackTrace();
						errors = true;
                        fireFileEndEvent(src,log_file_number,number_of_files,se);                        
					}
					
					//handle the decompressed files
					if(delete_after_finish && sbfConfig.isDelete_decompressed_xml_files())
					{
						src.delete();
					}
					log.info(" FINISHED");

					if(!isLiveFile)
					{
						//handle the (Z)XML-files
						if(was_zxml)
						{
							file_to_process = org_filename;
						}
						
						if(!errors)
						{
							if(sbfConfig.getAfter_parsing().equals("delete"))
							{
								File f = new File(file_to_process);
								f.delete();
							} else
							if(sbfConfig.getAfter_parsing().equals("rename"))
							{
								File f = new File(file_to_process);
								f.renameTo(new File(file_to_process+".parsed"));
							} else
							if(sbfConfig.getAfter_parsing().equals("archive"))
							{
								File fnew = new File(file_to_process);
								String archive_dir = sbfConfig.getArchive_folder();
								File farchive = new File(archive_dir+File.separatorChar+dirlist[i]);
								if(new File(archive_dir).equals(dir))
								{
									//do nothing cause files a archived anyway then
								}
								else
								{
									copyFile(fnew,farchive);
									File f = new File(file_to_process);
									f.delete();									
								}
							}
						}
						else
						if(sbfConfig.isRenameAtError())
						{
							File f = new File(file_to_process);
							f.renameTo(new File(file_to_process+".error"));					
						}
					}
				}
			}
		}
		catch(SQLException e)
		{
			//if there is a Database problem
			e.printStackTrace();
			log.fatal(" PROBLEM with the database: "+e.toString()+" - Please check you config!");
		}
		catch(Exception e)
		{
			//This is when the Parser crashed hard with one file
			writeFatalErrorMsg(e);
		}	
		log.info("---DONE---");	
	}

	private static void copyFile(File in, File out) throws Exception
	{
		FileInputStream fis  = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;
		while((i=fis.read(buf))!=-1) 
		{
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.close();
	}

	private void logFile(File src) throws SelectBfException, SQLException, JDOMException, IOException
	{
		//first check for XML-inconsistencies if configured
		if(sbfConfig.isConsistencyCheck())
		{
            fireFileStatusEvent(src,FileStatusEvent.STATUS_CONSISTENCY_CHECKING);
			DataConsistencyChecker.checkAndCorrect(src);	
		}
		
		//try to create starttime from filename
		long time = 0;
		try
		{
			String filename = src.getName();
								
			String[] buffer = filename.split("-");
			buffer = buffer[1].split("_");
								
			int year = Integer.parseInt(buffer[0].substring(0,4));
			int month = Integer.parseInt(buffer[0].substring(4,6));
			int day = Integer.parseInt(buffer[0].substring(6,8));
			int hours = Integer.parseInt(buffer[1].substring(0,2));
			int minutes = Integer.parseInt(buffer[1].substring(2,4));
								
			Calendar c = Calendar.getInstance();
			c.set(year,month-1,day,hours,minutes);
			time = c.getTimeInMillis();
		}
		catch(Exception e)
		{
			//as backup just use the "last modified"-date of the file
			time = src.lastModified();
		}
		
		//now log the file
		try
		{
			SAXBuilder builder = new SAXBuilder();
				
            fireFileStatusEvent(src,FileStatusEvent.STATUS_PARSING_XML);
			Document doc = builder.build(src);
			
			//now create the logical Game-Data
            fireFileStatusEvent(src,FileStatusEvent.STATUS_PROCESSING_DATA);
			GameContext gc = new GameContext(new Date(time),doc.getRootElement(),sbfConfig,epl);
            gc.addPersistingProgressListener(ppl);
            gc.addPersistingStatusListener(psl);

			if(!(sbfConfig.isSkipEmptyRounds() && gc.isEmpty()))
			{
                fireFileStatusEvent(src,FileStatusEvent.STATUS_PERSISTING);
				//now log them to the database
				DatabaseContext dc = new DatabaseContext(sbfConfig);
				gc.persist(dc);
				dc.close();
			}
		}
		catch(IOException e)
		{
			throw new SelectBfException(e);
		}

				
	}



	private void handleException(Exception e)
	{
		if(e instanceof SelectBfException)
		{
			SelectBfException sbe = (SelectBfException) e;
			if(sbe.getType() == SelectBfException.GENERIC)
			{
				writeFatalErrorMsg(e);
			}
			else
			{
				log.warn("ERROR: "+e.toString());
				e.printStackTrace();
			}
		} 
		else
		{
			writeFatalErrorMsg(e);
		}		
        fireEndProcessingEvent(e);
	}
	
	private static void writeFatalErrorMsg(Exception e)
	{
		log.fatal("\n================================================");
        log.fatal("FATAL ERROR");
        log.fatal("------------------------------------------------");
        log.fatal("An unexpected circumstance prevented the Parser");
        log.fatal("from continuing his work.");
        log.fatal("Please report this error and a COPY-PASTE of the");
        log.fatal("following to selectbf@s-h-i-n-y.com OR BETTER");
        log.fatal("in the forums at http://www.selectbf.org, Thanks!");
        log.fatal(e.toString());
        log.fatal("StackTrace:");
		
		StackTraceElement[] ste = e.getStackTrace();
		for(int i = 0; i<ste.length; i++)
		{
            log.fatal(ste[i].toString());
		}
		
        log.fatal("If you want to help even more, also supply a");
        log.fatal("copy of the Log-File that triggered this error.");
        log.fatal("================================================");
	}


	private String decompressZXMLFile(String filename)
	{
		log.info("Decompressing "+filename+" ");
		int blocksize = 8192;
		String zipname, source;
		
		zipname = filename;
		source = zipname.substring(0,zipname.length()-4)+"xml";

        fireFileStatusEvent(new File(zipname),FileStatusEvent.STATUS_DECOMPRESSING);
		try
		{
			ZInputStream zipin = new ZInputStream(new FileInputStream(zipname));

			byte buffer[] = new byte[blocksize];
			FileOutputStream out = new FileOutputStream(source);
			
			int length;
			while((length = zipin.read(buffer,0,blocksize))!=0 && length != -1)
			{
				out.write(buffer,0,length);
			}
			out.close();
			zipin.close();
		}
		catch(IOException e)
		{
			log.warn("ERROR: Couldn't decompress "+filename);
		}	
		
		return source;	
	}
    
    public static void main(String[] args)
    {
        if(args != null && args.length > 0)
        {
            try
            {
                String pattern = Messages.getString("selectbf.logging.pattern");
                PatternLayout pLayout = new PatternLayout(pattern);
                
                if(args[0] != null && (args[0].equals("-q") || args[0].equals("/q")))
                {
                    String fileName = Messages.getString("selectbf.logging.file");                
                    RollingFileAppender rfa = new RollingFileAppender(pLayout,fileName);
                    rfa.setMaxBackupIndex(1);
                    rfa.setMaxFileSize("10MB");
                    
                    log.addAppender(rfa);
                }
                else
                {
                    ConsoleAppender ca = new ConsoleAppender(pLayout);
                    
                    log.addAppender(ca);
                }
            }
            catch(Exception e)
            {
                log.warn("Exception when configuring the logging-system!");
                log.warn(e);
            }
        }
        
        SelectBf sbf = new SelectBf();
        sbf.run();
    }
    
    //Event-Handling stuff
    public void addSourceEventListener(SourceEventListener listener)
    {
        this.sel = listener;
    }
    
    private void fireBeginSourceEvent(SourceConfig config)
    {
        if(this.sel != null)
        {
            sel.beginSourceProcessing(new SourceEvent(config));
        }
    }
    
    private void fireEndSourceEvent(SourceConfig config)
    {
        if(this.sel != null)
        {
            sel.endSourceProcessing(new SourceEvent(config));
        }       
    }  
    
    public void addProcessingListener(ProcessingListener listener)
    {
        processiongListeners.add(listener);
    }
    
    private void fireStartProcessingEvent()
    {
        for(Iterator i = processiongListeners.iterator(); i.hasNext();)
        {
            ((ProcessingListener)i.next()).startProcessing();
        }
    }
    
    private void fireEndProcessingEvent()
    {
        for(Iterator i = processiongListeners.iterator(); i.hasNext();)
        {
            ((ProcessingListener)i.next()).endProcessing(new ProcessingEndEvent());
        }        
    }
    
    private void fireEndProcessingEvent(Exception ex)
    {
        for(Iterator i = processiongListeners.iterator(); i.hasNext();)
        {
            ((ProcessingListener)i.next()).endProcessing(new ProcessingEndEvent(ex));
        }   
    }    
    
    public void addFileEventListener(FileEventListener listener)
    {
        this.fel = listener;
    }
    
    private void fireFileStartEvent(File file, int fileNumber, int totalFiles)
    {
        if(this.fel != null)
        {
            fel.startFileProcessing(new FileStartEvent(file, fileNumber, totalFiles));
        }
    }
    
    private void fireFileStatusEvent(File file, int status)
    {
        if(this.fel != null)
        {
            fel.fileProcessingStatusChanged(new FileStatusEvent(file,status));
        }        
    }
    
    private void fireFileEndEvent(File file, int fileNumber, int totalFiles)
    {
        if(this.fel != null)
        {
            fel.endFileProcessing(new FileEndEvent(file, fileNumber, totalFiles));
        }
    }
    
    private void fireFileEndEvent(File file, int fileNumber, int totalFiles,Exception ex)
    {
        if(this.fel != null)
        {
            fel.endFileProcessing(new FileEndEvent(file, fileNumber, totalFiles,ex));
        }
    }   
    
    public void addEventProgressListener(ProgressListener listener)
    {
        this.epl = listener;
    }
    
    public void addPersistingProgressListener(ProgressListener listener)
    {
        this.ppl = listener;
    }
    
    public void addPersistingStatusListener(PersistingStatusListener listener)
    {
        this.psl = listener;
    }
    
    public void addDownloadListener(DownloadListener listener)
    {
        this.dl = listener;
    }    
    
    private void fireDownloadBeginEvent(String filename, long size, int number, int total)
    {
        if(dl != null)
        {
            dl.beginDownload(new DownloadEvent(filename,size, number, total));
        }
    }
    
    private void fireDownloadEndEvent(String filename, long size, int number, int total)
    {
        if(dl != null)
        {
            dl.endDownload(new DownloadEvent(filename,size, number, total));
        }
    }
    
}
