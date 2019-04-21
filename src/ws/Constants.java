package ws;

public class Constants {

	public static final  String DEVELOPMENT="development";
	
	public static final String NO_TIMECARD_ID = "{\"error\":\"" + "Timecard Id doesn't exist" + "\"}";
	
	
	public static final  String NO_EMP_ID = "{\"error\":\"" + "Emp_Id doesn't exist" + "\"}";
	
	public static final String ERROR_GET_DEPT = "{\"error\":\"" + "There was a problem getting Department" + "\"}";
	
	public static final  String ERROR_INSERT_DEPT = "{\"error\":\"" + "There was a problem in inserting Department" + "\"}";
	
	public static final String DEPT_ID_EXIST = "{\"error\":\"" + "Dept_no already exist" + "\"}";
	
	public static final String NO_DEPT_ID = "{\"error\":\"" + "Dept Id doesn't exist" + "\"}";
	
	public static final String ERROR_UPDATE_DEPT = "{\"error\":\"" + "There was a problem in updating Department" + "\"}";
	
	public static final String ERROR_DELETE_DEPT = "{\"error\":\"" + "There was a problem deleting Department" + "\"}";
	
	public static final String ERROR_GET_EMP = "{\"error\":\"" + "There was a problem getting employee" + "\"}";
	
	public static final String ERROR_INSERT_EMP = "{\"error\":\"" + "There was a problem in inserting employee" + "\"}";
	
	public static final String ERROR_UPDATE_EMP = "{\"error\":\"" + "There was a problem updating employee" + "\"}";
	
	public static final String ERROR_DELETE_EMP = "{\"error\":\"" + "There was a problem deleting employee" + "\"}";
	
	public static final String ERROR_HIRE_DATE = "{\"error\":\"" + "Hire_date cannot be Saturday or Sunday" + "\"}";
	
	public static final String INVALID_DATE_FORMAT = "{\"error\":\"" + "Invalid Hire_date format" + "\"}";
	
	public static final String INVALID_HIRE_DATE = "{\"error\":\"" + "Not a valid Hire_date in the Employee" + "\"}";
	
	public static final String NO_MANAGER_ID = "{\"error\":\"" + "Mng_id Id doesn't exist in the Employee" + "\"}";
	
	public static final String ERROR_GET_TIMECARD = "{\"error\":\"" + "There was a problem in getting timecard" + "\"}";
	
	public static final String ERROR_INSERT_TIMECARD = "{\"error\":\"" + "There was a problem in inserting timecard" + "\"}";
	
	public static final String ERROR_UPDATE_TIMECARD = "{\"error\":\"" + "There was a problem in updating timecard" + "\"}";

	public static final String ERROR_DELETE_TIMECARD = "{\"error\":\"" + "There was a problem in deleting timecard" + "\"}";
	
	public static final String NO_SAME_START_TIME_EMP = "{\"error\":\"" + "start_time must not be on the same day as any other start_time for that employee"
			+ "\"}";
	
	public static final String NOT_BETWEEN_THE_HOURS = "{\"error\":\"" + "start_time and end_time must be between the 6AM to 6PM" + "\"}";

	public static final String INVALID_START_END_DAY = "{\"error\":\"" + "Start Day or End day cannot be Saturday or Sunday" + "\"}";

	public static final String ERROR_IN_END_TIME = "{\"error\":\"" + "end_time must be at least 1 hour greater than the start_time and be on the same day as the start_time." + "\"}";

	public static final String ERROR_IN_START_TIME = "{\"error\":\"" + "start_time must be equal to the current date or up to 1 week ago from the current date." + "\"}";

	public static final String ERROR_DELETE_COMPANY = "{\"error\":\"" + "There was a problem in deleting company" + "\"}";
	
	public static final String INVALID_START_TIME_FORMAT = "{\"error\":\"" + "Invalid start time format" + "\"}";
	
	public static final String INVALID_END_TIME_FORMAT = "{\"error\":\"" + "Invalid end time format" + "\"}";
	
	public static final String INVALID_TIME_FORMAT = "{\"error\":\"" + "Invalid date and time format" + "\"}";
	
	public static final String INVALID_MNG_EMPL = "{\"error\":\"" + "Mng_Id and Empl_Id can't be same" + "\"}";
}
