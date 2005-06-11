package org.selectbf;


public class SelectBfException extends Exception
{
	private int type;
	private String detailmsg;
	
	public static final int GENERIC = 1;
	public static final int CONFIG_FILE_MISSING = 2;
	public static final int CONFIG_FILE_ERROR = 3;
	public static final int XML_DATA_NOT_VALID = 4;
	public static final int HEAL_ALREADY_FINISHED = 5;
	public static final int HEAL_NOT_FINISHED = 6;
	public static final int REPAIR_ALREADY_FINISHED = 7;
	public static final int REPAIR_NOT_FINISHED = 8;
	public static final int ROUND_ALREADY_STARTED = 9;
	public static final int ROUND_ALREADY_ENDED = 10;
	public static final int RIDE_NOT_FINISHED = 11;
	public static final int RIDE_ALREADY_ENDED = 12;
	public static final int DATABASE_CONNECTION_FAILED = 13;	
	public static final int PLAYERINFOS_ARE_NOT_PERSISTENT = 14;
	public static final int CONTEXTID_NOT_BOUND = 15;
	public static final int ALREADY_PERSISTENT = 16;
	public static final int NOT_ALL_DATA_READY = 17;
	public static final int PERSISTANCE_NOT_TESTED = 18;
	public static final int DATA_DONT_MEET_EXPECTATIONS = 19;
	public static final int RANK_IS_ALREADY_SET = 20;
	public static final int JDOM_EXCEPTION_RETHROW = 21;
	public static final int NO_NAMESPACE_COULD_BE_FOUND = 22;
	public static final int ASSIGNMENT_ALREADY_ENDED = 23;
	public static final int ENDTIME_IS_BEFORE_STARTTIME = 24;
	public static final int ASSIGNMENT_HAS_NOT_ENDED = 25;
	public static final int ASSIGNMENT_CONFLICT = 26;
	public static final int NO_PLAYERSLOT_FOR_ID = 27; 
	public static final int NO_ASSIGNMENT_AT_THAT_TIME = 28;
	public static final int FTP_PROBLEM = 29;
	public static final int ROUND_NOT_STARTED = 30;
	public static final int ROUND_NOT_ENDED = 31;
	public static final int NO_KEYHASH_AVAILABLE = 32;
	public static final int PLAYER_NOT_IN_DATABASE = 33;	
	
	public SelectBfException(int type)
	{
		super();
		this.type = type;
		setErrorMessage(type);
	}
	
	public SelectBfException(int type,String msg)
	{
		super(msg);
		this.type = type;
		setErrorMessage(type);
	}
	
	public SelectBfException(Exception e)
	{
		super(e);		
		this.type = GENERIC;
	}
	
	public SelectBfException(String msg)
	{
		super(msg);		
		this.type = GENERIC;
	}	
	
	public void setErrorMessage(int type)
	{
		String str = "SelectBfException:\n";
			
		switch(type)
		{
			case GENERIC:					str += "GENERIC: An unexpected Error occured!";break;
			case CONFIG_FILE_MISSING:		str += "CONFIG_FILE_MISSING: The config file is missing (config.xml).";break;
			case CONFIG_FILE_ERROR:			str += "CONFIG_FILE_ERROR: The config file has errors.";break;
			case XML_DATA_NOT_VALID:		str += "XML_DATA_NOT_VALID: The supplied XML-data are not valid for this part of the program.";break;
			case HEAL_ALREADY_FINISHED: 	str += "HEAL_ALREADY_FINISHED: Heal has already ended and can not be ended again.";break;
			case HEAL_NOT_FINISHED: 		str += "HEAL_NOT_FINISHED: Heal has not been finished, therefore this value isn't available.";break;
			case REPAIR_ALREADY_FINISHED: 	str += "REPAIR_ALREADY_FINISHED: Repair has already ended and can not be ended again.";break;
			case REPAIR_NOT_FINISHED: 		str += "REPAIR_NOT_FINISHED: Repair has not been finished, therefore this value isn't available.";break;
			case ROUND_ALREADY_STARTED:		str += "ROUND_ALREADY_STARTED: Round has already started and can not be started again.";break;
			case ROUND_ALREADY_ENDED: 		str += "ROUND_ALREADY_ENDED: Round has already ended and can not be ended again.";break;
			case RIDE_NOT_FINISHED:			str += "RIDE_NOT_FINISHED: Ride has not been finished, therefore this value isn't available.";break;
			case RIDE_ALREADY_ENDED: 		str += "RIDE_ALREADY_ENDED: Ride has already ended and can not be ended again.";break;
			case DATABASE_CONNECTION_FAILED:str += "DATABASE_CONNECTION_FAILED: Connection to the configured database failed. Check your config.";break;
			case PLAYERINFOS_ARE_NOT_PERSISTENT:str += "PLAYERINFOS_ARE_NOT_PERSISTENT: PlayerInfos have not been made persistent so this function is not available.";break;
			case CONTEXTID_NOT_BOUND:		str += "CONTEXTID_NOT_BOUND: ContextId has no DbId bound.";break;
			case ALREADY_PERSISTENT:		str += "ALREADY_PERSISTENT: This Object has already been made persistent, persisting again causes inconsistency.";break;
			case NOT_ALL_DATA_READY:		str += "NOT_ALL_DATA_READY: Not all data are ready for the desired function.";break;
			case PERSISTANCE_NOT_TESTED:    str += "PERSISTANCE_NOT_TESTED: The persistance of this object has not been tested before. Test before to avoid incosistency.";break;
			case DATA_DONT_MEET_EXPECTATIONS:str+= "DATA_DONT_MEET_EXPECTATIONS: The data supplied don't meet the expectations need for this part of the programm.";break;
			case RANK_IS_ALREADY_SET:		str+= "RANK_IS_ALREADY_SET: The Rank of this PlayerStat is already set, and can not be set again.";break;
			case NO_NAMESPACE_COULD_BE_FOUND:str+= "NO_NAMESPACE_COULD_BE_FOUND: There was no namespace mapped to the prefix 'bf', there must have been something changed in the XML-structure.";break;
			case ASSIGNMENT_ALREADY_ENDED:	str += "ASSIGNMENT_ALREADY_ENDED: The Assignment has already ended and can not be ended again."; break;
			case ENDTIME_IS_BEFORE_STARTTIME:str+= "ENDTIME_IS_BEFORE_STARTTIME: The desired endtime lies before the starttime, this is not a valid timeperiod";break;
			case ASSIGNMENT_HAS_NOT_ENDED:  str += "ASSIGNMENT_HAS_NOT_ENDED: The Assignment has not ended until now."; break;
			case ASSIGNMENT_CONFLICT: 		str += "ASSIGNMENT_CONFLICT: The PlayerSlot is already assign to another Player at that time.";break;
			case NO_PLAYERSLOT_FOR_ID: 		str += "NO_PLAYERSLOT_FOR_ID: There was no Slot for the requested SlotID";break;
			case NO_ASSIGNMENT_AT_THAT_TIME:str += "NO_ASSIGNMENT_AT_THAT_TIME: There was no player in that slot at the requested time"; break;
			case FTP_PROBLEM:				str += "FTP_PROBLEM: Connection problem to the FTP-Location"; break;
			case ROUND_NOT_STARTED:			str += "ROUND_NOT_STARTED: Round has not started, therefore this value isn't accessible."; break;
			case ROUND_NOT_ENDED:			str += "ROUND_NOT_ENDED: Round has not ended, therefore this value isn't accessible"; break;
			case NO_KEYHASH_AVAILABLE:		str += "NO_KEYHASH_AVAILABLE: This player has no key-hash"; break;
			case PLAYER_NOT_IN_DATABASE:	str += "PLAYER_NOT_IN_DATABASE: This player has not been added to the database, probably because he had not CDKEY-hash."; break;
		}
		
		detailmsg = str;	
	}
	
	public String toString()
	{
		return detailmsg;
	}
	
	public int getType()
	{
		return type;
	}

	public String getMessage()
	{
		if(detailmsg!=null)
		{
			return detailmsg;
		}
		else
		{
			return super.getMessage();
		}
	}

}
