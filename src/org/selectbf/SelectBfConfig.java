package org.selectbf;
import org.jdom.Element;

public class SelectBfConfig
{
	private  String dbUser;
	private  String dbPassword;
	private  String dbName;
	private  String dbMachine;
	private  String dbPort;
	private  boolean delete_decompressed_xml_files;
	private  String after_parsing;
	private  String archive_folder;
	private  String after_download;
	private  boolean logBots;
	private  boolean renameAtError;
	private  boolean trimDatabase;
	private  int trimDays;
	private  boolean trimPlayers;
	private  boolean consistencyCheck;
	private  boolean memorySafer;
	private  boolean skipEmptyRounds;
	private  boolean lanMode;
	
	public SelectBfConfig(Element configRoot)
	{
		Element database = configRoot.getChild("database");
		dbUser = database.getAttributeValue("user");
		dbPassword = database.getAttributeValue("password");
		dbName = database.getAttributeValue("database");
		dbPort = database.getAttributeValue("port");
		dbMachine = database.getText();
				
		System.out.println("Using Database-Config as follows:");
		System.out.println(dbName+"@"+dbMachine+":"+dbPort);
		System.out.println("User: "+dbUser+"  Password:"+dbPassword);
		System.out.println("---------------------------------------------------");

		Element delete_decompressed = configRoot.getChild("delete-decompressed-xml-files");
		if(delete_decompressed != null && delete_decompressed.getText().equals("true"))
		{
			delete_decompressed_xml_files = true;
		}
		else
		{
			delete_decompressed_xml_files = false;
		}

		Element log_bots = configRoot.getChild("log-bots");
		if(log_bots != null && log_bots.getText().equals("true"))
		{
			logBots = true;
		}
		else
		{
			logBots = false;
		}
		
		Element lan_mode = configRoot.getChild("lan-mode");
		if(lan_mode != null && lan_mode.getText().equals("true"))
		{
			lanMode = true;
		}
		else
		{
			lanMode = false;
		}
				
		Element rename_at_error = configRoot.getChild("rename-at-error");
		if(rename_at_error != null && rename_at_error.getText().equals("true"))
		{
			renameAtError = true;
		}
		else
		{
			renameAtError = false;
		}

		Element consistency_check = configRoot.getChild("consistency-check");
		if(consistency_check != null && consistency_check.getText().equals("true"))
		{
			consistencyCheck = true;
		}
		else
		{
			consistencyCheck = false;
		}
			
		Element memory_safer = configRoot.getChild("memory-safer");
		if(memory_safer != null && memory_safer.getText().equals("true"))
		{
			memorySafer = true;
		}
		else
		{
			memorySafer = false;
		}								
				
		Element afterParse = configRoot.getChild("after-parsing");
		after_parsing = afterParse.getText();
		if(after_parsing == null || after_parsing.equals(""))
		{
			after_parsing = "remain";
		} 

		Element archiveFolder = configRoot.getChild("archive-folder");
		archive_folder = archiveFolder.getText();
		if(archive_folder == null || archive_folder.equals(""))
		{
			archive_folder = "archive/";
		} 
		
		Element afterDownload = configRoot.getChild("after-download");
		after_download = afterDownload.getText();
		if(after_download == null || after_download.equals(""))
		{
			after_download = "remain";
		} 		
				
		Element trim = configRoot.getChild("trim-database");
		if(trim != null && trim.getText().equals("true"))
		{
			trimDatabase = true;
 					
			if(trim.getAttributeValue("days")!=null)
			{
				trimDays = Integer.parseInt(trim.getAttributeValue("days"));
			}
			else
			{
				trimDays = 14;
			}
 					
			if(trim.getAttributeValue("keep-players")!=null && trim.getAttributeValue("keep-players").equals("true"))
			{
				trimPlayers = true;
			}
			else
			{
				trimPlayers = false;
			}
		}
		else
		{
			trimDatabase = false;
		}
		
		Element skip_empty_rounds = configRoot.getChild("skip-empty-rounds");
		if(skip_empty_rounds != null && skip_empty_rounds.getText().equals("true"))
		{
			skipEmptyRounds = true;
		}
		else
		{
			skipEmptyRounds = false;
		}		

	}

	/**
	 * @return
	 */
	public  String getAfter_parsing()
	{
		return after_parsing;
	}
	
	public String getArchive_folder()
	{
		return archive_folder;
	}

	/**
	 * @return
	 */
	public  String getDbMachine()
	{
		return dbMachine;
	}

	/**
	 * @return
	 */
	public  String getDbName()
	{
		return dbName;
	}

	/**
	 * @return
	 */
	public  String getDbPassword()
	{
		return dbPassword;
	}

	/**
	 * @return
	 */
	public  String getDbPort()
	{
		return dbPort;
	}

	/**
	 * @return
	 */
	public  String getDbUser()
	{
		return dbUser;
	}

	/**
	 * @return
	 */
	public  boolean isDelete_decompressed_xml_files()
	{
		return delete_decompressed_xml_files;
	}

	/**
	 * @return
	 */
	public  boolean isLogBots()
	{
		return logBots;
	}

	/**
	 * @return
	 */
	public  boolean isRenameAtError()
	{
		return renameAtError;
	}

	/**
	 * @return
	 */
	public  boolean isTrimDatabase()
	{
		return trimDatabase;
	}

	/**
	 * @return
	 */
	public  int getTrimDays()
	{
		return trimDays;
	}

	/**
	 * @return
	 */
	public  boolean isTrimPlayers()
	{
		return trimPlayers;
	}

	/**
	 * @return
	 */
	public String getAfter_download()
	{
		return after_download;
	}

	/**
	 * @return
	 */
	public boolean isConsistencyCheck()
	{
		return consistencyCheck;
	}

	/**
	 * @return
	 */
	public boolean isMemorySafer()
	{
		return memorySafer;
	}

	/**
	 * @return Returns the skipEmptyRounds.
	 */
	public boolean isSkipEmptyRounds() {
		return skipEmptyRounds;
	}
	/**
	 * @return Returns the lanMode.
	 */
	public boolean isLanMode() {
		return lanMode;
	}
}
