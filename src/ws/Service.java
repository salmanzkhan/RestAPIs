package ws;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import companydata.DataLayer;
import companydata.Department;
import companydata.Employee;
import companydata.Timecard;

/**
 * Service class which has RESTful API method in Java for a company to allow
 * them to track timecards for employees
 * 
 * @author salman
 *
 */
@Path("CompanyServices")
public class Service {

	DataLayer dl;

	/**
	 * getDeptInfo is Method to get information like dept_no and dept_id of all the
	 * departments exist in database
	 * 
	 * @param company
	 * @return set of values
	 * @throws Exception
	 */
	public HashSet<Object> getDeptInfo(String company) throws Exception {
		dl = new DataLayer(Constants.DEVELOPMENT);
		List<Department> departments = dl.getAllDepartment(company);
		HashSet<Object> set = new HashSet<>();
		for (Department d : departments) {
			set.add(d.getDeptNo());
			set.add(d.getId());
		}
		return set;

	}

	/**
	 * getEmplInfo is Method to get the information like Mng_id,emp_id of all the
	 * employees exist in database
	 * 
	 * @param company
	 * @return
	 * @throws Exception
	 */
	public HashSet<Object> getEmplInfo(String company) throws Exception {
		dl = new DataLayer(Constants.DEVELOPMENT);

		List<Employee> employees = dl.getAllEmployee(company);

		HashSet<Object> set = new HashSet<>();
		for (Employee e : employees) {
			set.add(e.getId());
			set.add(e.getMngId());
		}
		return set;
	}

	/**
	 * getTimeCardInfo is Method to get the information timecard_id of all the
	 * Timecard exist in database
	 * 
	 * @param emp_id
	 * @return
	 * @throws Exception
	 */
	public HashSet<Object> getTimeCardInfo(int emp_id) throws Exception {
		dl = new DataLayer(Constants.DEVELOPMENT);
		List<Timecard> timecards = dl.getAllTimecard(emp_id);
		HashSet<Object> set = new HashSet<>();
		for (Timecard t : timecards) {
			set.add(t.getId());
		}
		return set;

	}

	/**
	 * validStartDate is a method which gets the start_time of that emp_id
	 * 
	 * @param emp_id
	 * @return
	 * @throws Exception
	 */
	public HashSet<Object> validStartDate(int emp_id) throws Exception {

		List<Timecard> timecards = dl.getAllTimecard(emp_id);
		HashSet<Object> set = new HashSet<>();
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (Timecard t : timecards) {

			java.util.Date parsedStartDate = dateFormat1.parse(t.getStartTime().toString());
			java.sql.Date sql_start_timeStamp = new java.sql.Date(parsedStartDate.getTime());
			set.add(sql_start_timeStamp.toString());
			set.add(t.getEmpId());
		}
		return set;
	}

	/**
	 * getTimeStamp is method which converts input date which is in string into
	 * timestamp
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public Timestamp getTimeStamp(String date) throws ParseException {

		Timestamp timeStamp = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime());
		return timeStamp;
	}

	/**
	 * getSqlDate is method which converts string date into SQL formate date
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public java.sql.Date getSqlDate(String date) throws ParseException {

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date parsedDate = dateFormat1.parse(date);
		java.sql.Date sql_start_timeStamp = new java.sql.Date(parsedDate.getTime());
		return sql_start_timeStamp;

	}

	/**
	 * getCurrentDay is method to get the current date
	 * 
	 * @return
	 */
	public java.sql.Date getCurrentDay() {

		java.sql.Date sqlCurrentDate = new java.sql.Date(new java.util.Date().getTime());
		return sqlCurrentDate;
	}

	/**
	 * getDay is method to get the day which is used to check if the day is Saturday
	 * or Sunday
	 * 
	 * @param sql_start_timeStamp
	 * @return
	 */
	public String getDay(java.sql.Date sql_start_timeStamp) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
		String day = simpleDateFormat.format(sql_start_timeStamp);
		return day;
	}

	/**
	 * getMilliSeconds is method used to get the duration of start_time and end_time
	 * 
	 * @param start_time
	 * @param end_time
	 * @return
	 * @throws ParseException
	 */
	public long getMilliSeconds(String start_time, String end_time) throws ParseException {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date parsedStartDate = dateFormat1.parse(start_time);
		java.util.Date parsedEndDate = dateFormat1.parse(end_time);
		return parsedEndDate.getTime() - parsedStartDate.getTime();
	}

	/**
	 * getCal is method to get the startHour,end_hour,end_min and end_sec
	 * information of the given the SQL date
	 * 
	 * @param sql_start_timeStamp
	 * @param sql_end_timeStamp
	 * @return
	 */
	public HashMap<String, Integer> getCal(java.sql.Date sql_start_timeStamp, java.sql.Date sql_end_timeStamp) {
		HashMap<String, Integer> map = new HashMap<>();

		Calendar cal3 = Calendar.getInstance();
		cal3.setTime(sql_start_timeStamp);
		int startHour = cal3.get(Calendar.HOUR_OF_DAY);

		cal3.setTime(sql_end_timeStamp);
		int end_hour = cal3.get(Calendar.HOUR_OF_DAY);
		int end_min = cal3.get(Calendar.MINUTE);
		int end_sec = cal3.get(Calendar.SECOND);
		map.put("startHour", startHour);
		map.put("end_hour", end_hour);
		if (end_hour >= 18 && end_min > 0) {
			map.put("end_min", -1);
		} else {
			map.put("end_min", end_min);
		}
		if (end_hour >= 18 && end_sec > 0) {
			map.put("end_sec", -1);
		} else {
			map.put("end_sec", end_sec);
		}
		return map;
	}

	/**
	 * isThisValidDate method validate hire_date with the given format and date
	 * string
	 * 
	 * @param ValidateDate
	 * @param dateFromat
	 * @return
	 * @throws Exception
	 */
	public static boolean isThisValidDate(String ValidateDate, String dateFromat) throws Exception {

		if (ValidateDate == null) {
			return false;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFromat);
		simpleDateFormat.setLenient(false);

		try {

			// it will throw ParseException if not valid
			java.util.Date date = simpleDateFormat.parse(ValidateDate);

		} catch (ParseException e) {

			// e.printStackTrace();
			return false;
		}

		return true;
	} // close isThisValidDate block

	/**
	 * deleteCompany is method which Deletes all Department, Employee and Timecard
	 * records in the database for the given company.
	 * 
	 * @param company
	 * @return
	 */
	@Path("company")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delCompany(@QueryParam("company") String company) {
		try {

			dl = new DataLayer(Constants.DEVELOPMENT);
			if (!company.isEmpty()) {

				dl = new DataLayer(Constants.DEVELOPMENT);
				List<Department> departments = dl.getAllDepartment(company);

				String success = "{\"Success\":\"" + "companyName's information deleted." + "\"}";
				if (departments.size() == 0) {
					return Response.ok(Constants.ERROR_DELETE_COMPANY).build();
				} else {
					int numRows = dl.deleteCompany(company);
					return Response.ok(success).build();
				}

			} else {
				return Response.ok(Constants.ERROR_DELETE_COMPANY).build();
			}

		} catch (Exception e) {
			return Response.ok(Constants.ERROR_DELETE_COMPANY).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block
	}// close deleteCompany block

	/**
	 * getDepartment method Returns the requested Department as a JSON String.
	 * 
	 * @param company
	 * @param dept_id
	 * @return
	 */
	@Path("department")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDepartment(@DefaultValue("sk7684") @QueryParam("company") String company,
			@QueryParam("dept_id") int dept_id) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			Department dept = new Department();
			dept = dl.getDepartment(company, dept_id);

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			if (dept == null)
				return Response.ok(Constants.ERROR_GET_DEPT).build();
			else
				return Response.ok(gson.toJson(dept)).build();
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_GET_DEPT).build();

		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close getDepartment block

	/**
	 * getAllDepartment method Returns the requested list of Departments
	 * 
	 * @param company
	 * @return
	 */
	@Path("departments")
	@GET
	@Produces(MediaType.APPLICATION_JSON)

	public Response getAllDepartment(@DefaultValue("sk7684") @QueryParam("company") String company) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			List<Department> departments = dl.getAllDepartment(company);

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			if (departments.size() == 0)
				return Response.ok(Constants.ERROR_GET_DEPT).build();
			else
				return Response.ok(gson.toJson(departments)).build();
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_GET_DEPT).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close getAlldepartment block

	/**
	 * addDepartment method add the new department in database and Returns the new
	 * department which added as a JSON String.
	 * 
	 * @param dept
	 * @return
	 */
	@Path("department")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response addDepartment(Dept dept) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			Department deptnt = null;

			List<Department> departments = dl.getAllDepartment("sk7684");
			System.out.println("length: " + departments.size());
			HashSet<Object> set = new HashSet<>();
			Service ser = new Service();
			set = ser.getDeptInfo("sk7684");
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			if(dept.getDept_name().isEmpty()) {
				String success = "{\"error\":\"" + "Dept_name can't be empty" + "\"}";
				return Response.ok(success).build();
			}
			if(dept.getCompany().isEmpty()) {
				String success = "{\"error\":\"" + "Company name can't be empty" + "\"}";
				return Response.ok(success).build();
			}
			if(dept.getLocation().isEmpty()) {
				String success = "{\"error\":\"" + "Location can't be empty" + "\"}";
				return Response.ok(success).build();
			}
			if (!set.contains(dept.dept_no)) { // check for dept_no
				deptnt = new Department(dept.getCompany(), dept.dept_name, dept.getDept_no(), dept.location);
				deptnt = dl.insertDepartment(deptnt);
			} else {
				return Response.ok(Constants.DEPT_ID_EXIST).build();
			}

			return Response.ok(gson.toJson(deptnt)).build();
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_INSERT_DEPT).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close addDepartment block

	/**
	 * updateDepartment method updates the department and Returns the updated
	 * Department as a JSON String. Input is passed as FormParams:
	 * 
	 * @param dept_id
	 * @param company
	 * @param dept_name
	 * @param dept_no
	 * @param location
	 * @return
	 */
	@Path("department")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDepartment(@FormParam("dept_id") int dept_id, @FormParam("company") String company,
			@FormParam("dept_name") String dept_name, @FormParam("dept_no") String dept_no,
			@FormParam("location") String location) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			Department department = null;

			HashSet<Object> set = new HashSet<>();
			Service ser = new Service();
			set = ser.getDeptInfo("sk7684"); // get department info

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			if (!set.contains(dept_no)) {
				if (set.contains(dept_id)) {
					department = dl.getDepartment(company, dept_id);
					department.setDeptName(dept_name);
					department.setDeptNo(dept_no);
					department.setCompany(company);
					department.setLocation(location);
					department = dl.updateDepartment(department); // update the department

				} else {
					return Response.ok(Constants.NO_DEPT_ID).build();
				}

			} else {
				return Response.ok(Constants.DEPT_ID_EXIST).build();
			}

			return Response.ok(gson.toJson(department)).build();
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_UPDATE_DEPT).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close updateDepartment method

	/**
	 * deleteDepartment method deletes the department record with the requested
	 * dept_id and Returns the dept_id deleted.In case of error returns the
	 * appropriate error message
	 * 
	 * @param company
	 * @param dept_id
	 * @return
	 */
	@Path("department")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDepartment(@DefaultValue("sk7684") @QueryParam("company") String company,
			@QueryParam("dept_id") int dept_id) {
		try {

			dl = new DataLayer(Constants.DEVELOPMENT);
			HashSet<Object> set = new HashSet<>();
			Service ser = new Service();
			set = ser.getDeptInfo("sk7684");
			if (set.contains(dept_id)) { // check if dept_id exist
				List<Employee> employees = dl.getAllEmployee(company);
				if (!employees.isEmpty()) {
					for (Employee e : employees) {
						if (e.getDeptId() == dept_id) {
							List<Timecard> timecards = dl.getAllTimecard(e.getId());
							if (!timecards.isEmpty()) {
								for (Timecard tm : timecards) {
									dl.deleteTimecard(tm.getId());
								}
								dl.deleteEmployee(e.getId()); // Delete the given Employee
							}
						}
					}
				}

				int count = dl.deleteDepartment(company, dept_id);// delete the record
				String success = "{\"success\":\"" + "Department " + dept_id + " from sk7684 deleted" + "\"}";

				return Response.ok(success).build();

			} else {
				return Response.ok(Constants.NO_DEPT_ID).build();
			}
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_DELETE_DEPT).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally

	}// close deleteDepartment method

	/**
	 * getEmployee method returns the requested Employee as a JSON String. and
	 * return the error message if there's a problem in getting employee
	 * 
	 * @param emp_id
	 * @return
	 */
	@Path("employee")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployee(@QueryParam("emp_id") int emp_id) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			Employee employee = dl.getEmployee(emp_id);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

			if (employee == null)
				return Response.ok(Constants.ERROR_GET_EMP).build();
			else
				return Response.ok(gson.toJson(employee)).build();

		} catch (Exception e) {
			return Response.ok(Constants.ERROR_GET_EMP).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close getEmployee method

	/**
	 * getAllEmployees method returns the requested list of Employees. and the
	 * default value is sk7684. Test cases are handled with the appropriate error
	 * message
	 * 
	 * @param company
	 * @return
	 */
	@Path("employees")
	@GET
	@Produces(MediaType.APPLICATION_JSON)

	public Response getAllEmployees(@DefaultValue("sk7684") @QueryParam("company") String company) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			List<Employee> employees = dl.getAllEmployee(company);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

			if (employees.size() == 0)
				return Response.ok(Constants.ERROR_GET_EMP).build();
			else
				return Response.ok(gson.toJson(employees)).build();
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_GET_EMP).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close getAllEmployees method

	/**
	 * addEmployee method adds the new employee details in the database and returns
	 * the new Employee as a JSON String. empl takes the the input in the JSON
	 * String. All the given validations are handled with appropriate error message
	 * 
	 * @param empl
	 * @return
	 */
	@Path("employee")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response addEmployee(Empl empl) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

			HashSet<Object> set = new HashSet<>();
			Service ser = new Service();
			boolean isValidHireDate = false;
			set = ser.getDeptInfo("sk7684");// gets the department info
			// HashSet<Object> setEmpId = new HashSet<>();
			// setEmpId=ser.getEmpDetail("sk7684");
			// validate the HireDate
			isValidHireDate = isThisValidDate(empl.getHire_date().trim(), "yyyy-MM-dd");
			if (isValidHireDate == false || empl.getHire_date().length() > 10) {
				return Response.ok(Constants.INVALID_DATE_FORMAT).build();
			}

			if (set.contains(empl.getDept_id())) { // validate for Dept_id
				set = ser.getEmplInfo("sk7684");
				if (set.contains(empl.getMng_id()) || empl.getMng_id() == 0) { // validate for Mng_id

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date parsedDate = dateFormat.parse(empl.getHire_date());
					java.sql.Date sqlHire_date = new java.sql.Date(parsedDate.getTime());

					// formating current date in sql format
					java.sql.Date sqlCurrentDate = new java.sql.Date(new java.util.Date().getTime());

					if ((sqlHire_date.before(sqlCurrentDate) || sqlHire_date.equals(sqlCurrentDate))) {

						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
						String day = simpleDateFormat.format(sqlHire_date); // get the day of the week

						// check for hire_day is not Saturday or Sunday
						if (!day.equalsIgnoreCase("Saturday") && !day.equalsIgnoreCase("Sunday")) {

							Employee emp = new Employee(empl.getEmp_name(), empl.getEmp_no(), sqlHire_date,
									empl.getJob(), empl.getSalary(), empl.getDept_id(), empl.getMng_id());

							emp = dl.insertEmployee(emp); // Insert an Employee in database
							return Response.ok(gson.toJson(emp)).build();
						} else {
							return Response.ok(Constants.ERROR_HIRE_DATE).build();
						}
					} else {
						return Response.ok(Constants.INVALID_HIRE_DATE).build();
					}
				} else {
					return Response.ok(Constants.NO_MANAGER_ID).build();
				}
			} else {
				return Response.ok(Constants.NO_DEPT_ID).build();
			}
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_INSERT_EMP).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close addEmployee method

	/**
	 * updateEmployee method updates the employee details in database and return the
	 * updated Employee as a JSON String.All the given validations are handles with
	 * the appropriate error messages
	 * 
	 * @param emp_id
	 * @param emp_name
	 * @param emp_no
	 * @param hire_date
	 * @param job
	 * @param salary
	 * @param dept_id
	 * @param mng_id
	 * @return
	 */
	@Path("employee")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateEmployee(@FormParam("emp_id") Integer emp_id, @FormParam("emp_name") String emp_name,
			@FormParam("emp_no") String emp_no, @FormParam("hire_date") String hire_date, @FormParam("job") String job,
			@FormParam("salary") Double salary, @FormParam("dept_id") Integer dept_id,
			@FormParam("mng_id") Integer mng_id) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);

			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

			HashSet<Object> set = new HashSet<>();
			Service ser = new Service();
			boolean isValidHireDate = false;
			set = ser.getDeptInfo("sk7684");// get the department info

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parsedDate = dateFormat.parse(hire_date);

			// convert into sql date format
			java.sql.Date sqlHire_date = new java.sql.Date(parsedDate.getTime());
			java.sql.Date sqlCurrentDate = new java.sql.Date(new java.util.Date().getTime());

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
			String day = simpleDateFormat.format(sqlHire_date);// get the day of a week

			// validate the hire_date
			isValidHireDate = isThisValidDate(hire_date.trim(), "yyyy-MM-dd");
			if (isValidHireDate == false || hire_date.length() > 10) {
				return Response.ok(Constants.INVALID_DATE_FORMAT).build();
			}
			if ((int) emp_id == (int) mng_id) {
				return Response.ok(Constants.INVALID_MNG_EMPL).build();
			}

			if (set.contains(dept_id)) { // validate for Dept_id
				set = ser.getEmplInfo("sk7684"); // get employee info

				if (set.contains(mng_id) || mng_id == 0) { // validate for Mng_id

					if ((sqlHire_date.before(sqlCurrentDate) || sqlHire_date.equals(sqlCurrentDate))) {
						// check for hire_day is not Saturday or Sunday
						if (!day.equalsIgnoreCase("Saturday") && !day.equalsIgnoreCase("Sunday")) {

							if (set.contains(emp_id)) { // check for emp_id

								Employee employee = dl.getEmployee(emp_id);
								employee.setSalary(salary);
								employee.setEmpName(emp_name);
								employee.setEmpNo(emp_no);
								employee.setHireDate(sqlHire_date);
								employee.setDeptId(dept_id);
								employee.setJob(job);
								employee.setMngId(mng_id);
								employee = dl.updateEmployee(employee); // update Employee in database
								return Response.ok(gson.toJson(employee)).build();
							} else {
								return Response.ok(Constants.NO_EMP_ID).build();
							}

						} else {
							return Response.ok(Constants.ERROR_HIRE_DATE).build();
						}

					} else {
						return Response.ok(Constants.INVALID_HIRE_DATE).build();
					}

				} else {
					return Response.ok(Constants.NO_MANAGER_ID).build();
				}

			} else {
				return Response.ok(Constants.NO_DEPT_ID).build();
			}

		} catch (Exception e) {
			return Response.ok(Constants.ERROR_UPDATE_EMP).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close updateEmployee method

	/**
	 * deleteEmployee method delete the record in employee with requested emp_id and
	 * return the emp_id deleted as a JSON string and also return error with the
	 * appropriate message
	 * 
	 * @param emp_id
	 * @return
	 */
	@Path("employee")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEmployee(@QueryParam("emp_id") int emp_id) {
		try {

			dl = new DataLayer(Constants.DEVELOPMENT);
			HashSet<Object> set = new HashSet<>();
			Service ser = new Service();
			set = ser.getEmplInfo("sk7684"); // get the employee info which includes emp_id
			// HashSet<Object> setForTimeCardId = new HashSet<>();

			if (set.contains(emp_id)) { // check for emp_id to delete

				List<Timecard> timecards = dl.getAllTimecard(emp_id);

				for (Timecard tm : timecards) {
					dl.deleteTimecard(tm.getId());
				}
				int deletedEmp = dl.deleteEmployee(emp_id); // Delete the given Employee
				String success = "{\"success\":\"" + "Employee " + emp_id + " deleted" + "\"}";
				return Response.ok(success).build();

			} else {
				return Response.ok(Constants.NO_EMP_ID).build();
			}
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_DELETE_EMP).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close deleteEmployee method

	/**
	 * getTimecard method returns the requested Timecard as a JSON String. and
	 * throws the exception in case of error
	 * 
	 * @param timecard_id
	 * @return
	 */
	@Path("timecard")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimecard(@QueryParam("timecard_id") int timecard_id) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			Timecard timecard = dl.getTimecard(timecard_id); // Get the requested Timecard

			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			if (timecard == null)
				return Response.ok(Constants.ERROR_GET_TIMECARD).build();
			else
				return Response.ok(gson.toJson(timecard)).build();

		} catch (Exception e) {
			return Response.ok(Constants.ERROR_GET_TIMECARD).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close getTimecard method

	/**
	 * getAllTimecards method returns the requested list of Timecards. emp_id is
	 * passed as Input in QueryParam
	 * 
	 * @param emp_id
	 * @return
	 */
	@Path("timecards")
	@GET
	@Produces(MediaType.APPLICATION_JSON)

	public Response getAllTimecards(@QueryParam("emp_id") int emp_id) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			List<Timecard> timecards = dl.getAllTimecard(emp_id); // Get all Timecards for a given Employee

			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			if (timecards.size() == 0)
				return Response.ok(Constants.ERROR_GET_TIMECARD).build();
			else
				return Response.ok(gson.toJson(timecards)).build();
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_GET_TIMECARD).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close getAllTimecards method

	/**
	 * addTimecard method insert a Timecard and return the new Timecard as a JSON
	 * String. TimeCard accepts the input as JSON string. All the input validations
	 * are handled with the appropriate error messages.
	 * 
	 * @param timeCard
	 * @return
	 */
	@Path("timecard")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response addTimecard(TimeCard timeCard) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);

			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			HashSet<Object> set = new HashSet<>();
			HashSet<Object> setForDate = new HashSet<>();
			Service ser = new Service();
			boolean isValidStartDate = false;
			boolean isValidEndDate = false;

			set = ser.getEmplInfo("sk7684");// get the employee info

			setForDate = ser.validStartDate(timeCard.getEmp_id()); // gets the start_time for that employee
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			// convert string stat_time into timestamp format
			Timestamp start_timeStamp = ser.getTimeStamp(timeCard.getStart_time());
			Timestamp end_timeStamp = ser.getTimeStamp(timeCard.getEnd_time());

			java.sql.Date sql_start_timeStamp = ser.getSqlDate(timeCard.getStart_time());
			java.sql.Date sql_end_timeStamp = ser.getSqlDate(timeCard.getEnd_time());

			// get startHour,end_hour,end_min and end_sec info of a given date
			map = ser.getCal(sql_start_timeStamp, sql_end_timeStamp);

			String startDay = ser.getDay(sql_start_timeStamp);// get the start day
			String endDay = ser.getDay(sql_end_timeStamp); // get the end day

			// calculating one week before date
			Date sevenDay = new Date(System.currentTimeMillis() - 7L * 24 * 3600 * 1000);

			// formating one week before date in sql format
			java.sql.Date sqlSevenDay = new java.sql.Date(sevenDay.getTime());
			java.sql.Date sqlCurrentDate = ser.getCurrentDay();

			// validate start date and time
			isValidStartDate = isThisValidDate(timeCard.getStart_time().trim(), "yyyy-MM-dd HH:mm:ss");
			// validate end date and time
			isValidEndDate = isThisValidDate(timeCard.getEnd_time().trim(), "yyyy-MM-dd HH:mm:ss");
			if (isValidStartDate == false)
				return Response.ok(Constants.INVALID_START_TIME_FORMAT).build();
			if (isValidEndDate == false)
				return Response.ok(Constants.INVALID_END_TIME_FORMAT).build();

			if (set.contains(timeCard.getEmp_id())) { // check for emp_id

				if ((sql_start_timeStamp.toString().equals(sqlCurrentDate.toString())
						|| start_timeStamp.after(sqlSevenDay))
						&& start_timeStamp.getTime() <= new java.util.Date().getTime()) {

					if ((end_timeStamp.getTime() - start_timeStamp.getTime()) > 3.6e+6
							&& sql_start_timeStamp.toString().equals(sql_end_timeStamp.toString())) {

						if (!startDay.equalsIgnoreCase("Saturday") && !startDay.equalsIgnoreCase("Sunday")
								&& !endDay.equalsIgnoreCase("Saturday") && !endDay.equalsIgnoreCase("Sunday")) {

							if (map.get("startHour") >= 6 && map.get("end_hour") <= 18 && map.get("end_min") >= 0
									&& map.get("end_sec") >= 0) {

								// check the start_time for that employee
								if (!setForDate.contains(sql_start_timeStamp.toString())) {

									Timecard tc = new Timecard(start_timeStamp, end_timeStamp, timeCard.getEmp_id());
									tc = dl.insertTimecard(tc); // Insert a Timecard in database
									return Response.ok(gson.toJson(tc)).build();
								} else {
									return Response.ok(Constants.NO_SAME_START_TIME_EMP).build();
								}
							} else {
								return Response.ok(Constants.NOT_BETWEEN_THE_HOURS).build();
							}
						} else {
							return Response.ok(Constants.INVALID_START_END_DAY).build();
						}
					} else {
						return Response.ok(Constants.ERROR_IN_END_TIME).build();
					}
				} else {
					return Response.ok(Constants.ERROR_IN_START_TIME).build();
				}
			} else {
				return Response.ok(Constants.NO_EMP_ID).build();
			}
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_INSERT_TIMECARD).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close addTimecard method

	/**
	 * updateTimecard method updated the timecard details in the database and
	 * returns the updated Timecard as a JSON String. Inputs is passed as as
	 * FormParams.All the validations are handled with the appropriate error
	 * messages.
	 * 
	 * @param timecard_id
	 * @param emp_id
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	@Path("timecard")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateTimecard(@FormParam("timecard_id") Integer timecard_id, @FormParam("emp_id") Integer emp_id,
			@FormParam("start_time") String start_time, @FormParam("end_time") String end_time) {
		try {
			dl = new DataLayer(Constants.DEVELOPMENT);
			/*
			 * GsonBuilder builder = new GsonBuilder(); Gson gson = builder.create();
			 */
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			HashSet<Object> set = new HashSet<>();
			HashSet<Object> setForDate = new HashSet<>();
			HashSet<Object> setForTimeCardId = new HashSet<>();
			boolean isValidStartDate = false;
			boolean isValidEndDate = false;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			Service ser = new Service();

			set = ser.getEmplInfo("sk7684"); // get the employee info
			setForDate = ser.validStartDate(emp_id); // gets the start_time for that employee
			setForTimeCardId = ser.getTimeCardInfo(emp_id); // gets the timecard_id for that employee if it exists

			Timestamp start_timeStamp = ser.getTimeStamp(start_time);
			Timestamp end_timeStamp = ser.getTimeStamp(end_time);
			java.sql.Date sql_start_timeStamp = ser.getSqlDate(start_time);
			java.sql.Date sql_end_timeStamp = ser.getSqlDate(end_time);

			// long timeDifference = ser.getMilliSeconds(start_time, end_time);
			map = ser.getCal(sql_start_timeStamp, sql_end_timeStamp);

			// get the start_day and end_day from the given date
			String startDay = ser.getDay(sql_start_timeStamp);
			String endDay = ser.getDay(sql_end_timeStamp);

			// calculating one week before date
			Date sevenDay = new Date(System.currentTimeMillis() - 7L * 24 * 3600 * 1000);

			// formate one week before date in sql date format
			java.sql.Date sqlSevenDay = new java.sql.Date(sevenDay.getTime());
			java.sql.Date sqlCurrentDate = ser.getCurrentDay();

			// validate start date and time
			isValidStartDate = isThisValidDate(start_time.trim(), "yyyy-MM-dd HH:mm:ss");
			// validate end date and time
			isValidEndDate = isThisValidDate(end_time.trim(), "yyyy-MM-dd HH:mm:ss");

			if (isValidStartDate == false)
				return Response.ok(Constants.INVALID_START_TIME_FORMAT).build();
			if (isValidEndDate == false)
				return Response.ok(Constants.INVALID_END_TIME_FORMAT).build();

			if (set.contains(emp_id)) { // emp_id must exist in database

				if ((sql_start_timeStamp.toString().equals(sqlCurrentDate.toString())
						|| start_timeStamp.after(sqlSevenDay))
						&& start_timeStamp.getTime() <= new java.util.Date().getTime()) {

					if ((end_timeStamp.getTime() - start_timeStamp.getTime()) > 3.6e+6
							&& sql_start_timeStamp.toString().equals(sql_end_timeStamp.toString())) {

						if (!startDay.equalsIgnoreCase("Saturday") && !startDay.equalsIgnoreCase("Sunday")
								&& !endDay.equalsIgnoreCase("Saturday") && !endDay.equalsIgnoreCase("Sunday")) {

							if (map.get("startHour") >= 6 && map.get("end_hour") <= 18 && map.get("end_min") >= 0
									&& map.get("end_sec") >= 0) {
								// check the start_time for that employee
								if (!setForDate.contains(sql_start_timeStamp.toString())) {

									// check if timecard_id exists in database
									if (setForTimeCardId.contains(timecard_id)) {

										Timecard timecard = dl.getTimecard(timecard_id);

										timecard.setStartTime(start_timeStamp);
										timecard.setEndTime(end_timeStamp);
										timecard.setEmpId(emp_id);
										timecard = dl.updateTimecard(timecard);// Update a given Timecard
										return Response.ok(gson.toJson(timecard)).build();

									} else {
										return Response.ok(Constants.NO_TIMECARD_ID).build();
									}
								} else {
									return Response.ok(Constants.NO_SAME_START_TIME_EMP).build();
								}
							} else {
								return Response.ok(Constants.NOT_BETWEEN_THE_HOURS).build();
							}
						} else {
							return Response.ok(Constants.INVALID_START_END_DAY).build();
						}
					} else {
						return Response.ok(Constants.ERROR_IN_END_TIME).build();
					}
				} else {
					return Response.ok(Constants.ERROR_IN_START_TIME).build();
				}
			} else {
				return Response.ok(Constants.NO_EMP_ID).build();
			}
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_UPDATE_TIMECARD).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	}// close updateTimecard method

	/**
	 * deleteTimecard method Delete the given Timecard from the database and return
	 * the timecard_id deleted. Throws exceptions in case of any error with the
	 * appropriate error message
	 * 
	 * @param timecard_id
	 * @return
	 */
	@Path("timecard")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTimecard(@QueryParam("timecard_id") int timecard_id) {
		try {

			dl = new DataLayer(Constants.DEVELOPMENT);
			Timecard timecard = dl.getTimecard(timecard_id);

			if (timecard != null) { // check for timecard_id before deleting

				int deletedTimecard = dl.deleteTimecard(timecard_id); // Delete the given Timecard
				String success = "{\"success\":\"" + "Timecard " + timecard_id + " deleted" + "\"}";
				return Response.ok(success).build();

			} else {
				return Response.ok(Constants.NO_TIMECARD_ID).build();
			}
		} catch (Exception e) {
			return Response.ok(Constants.ERROR_DELETE_TIMECARD).build();
		} // close catch block
		finally {
			dl.close();
		} // close finally block

	} // close deleteTimecard method

}// close Service class
