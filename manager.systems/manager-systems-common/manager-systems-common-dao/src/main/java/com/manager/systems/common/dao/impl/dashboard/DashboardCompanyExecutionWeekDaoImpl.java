/*
 * Create Date 14/03/2022
 */
package com.manager.systems.common.dao.impl.dashboard;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
//import java.util.Calendar;
import java.util.Date;
//import java.util.TimeZone;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.dao.dashboard.DashboardCompanyExecutionWeekDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.dashboard.DashboardCompanyExecutionWeekDTO;
import com.manager.systems.common.dto.dashboard.DashboardCompanyExecutionWeekItemDTO;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.OperationType;

public class DashboardCompanyExecutionWeekDaoImpl implements DashboardCompanyExecutionWeekDao {

	private Connection connection;
	public DashboardCompanyExecutionWeekDaoImpl(Connection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public void getAll(final DashboardCompanyExecutionWeekDTO dashboardCompanyExecutionWeek) throws Exception 
	{	
		CallableStatement statement = null;
		ResultSet resultSet = null;
		
		//final TimeZone tz = TimeZone.getTimeZone(com.manager.systems.common.utils.ConstantDataManager.TIMEZONE_SAO_PAULO);
		//final Calendar calendar = Calendar.getInstance(tz);
				
		final TemporalField temporalField = WeekFields.ISO.weekOfWeekBasedYear();				
		int initialWeekNumber = Integer.valueOf(
									String.valueOf(dashboardCompanyExecutionWeek.getDateFrom().getYear()) + 
									String.valueOf(dashboardCompanyExecutionWeek.getDateFrom().get(temporalField)));
		
		int finalWeekNumber = Integer.valueOf(
				String.valueOf(dashboardCompanyExecutionWeek.getDateTo().getYear()) + 
				String.valueOf(dashboardCompanyExecutionWeek.getDateTo().get(temporalField)));
		
		DashboardCompanyExecutionWeekItemDTO weekItem = new DashboardCompanyExecutionWeekItemDTO();
		weekItem.setWeekYear(initialWeekNumber);
		
		LocalDateTime initialDate = dashboardCompanyExecutionWeek.getDateFrom().with(DayOfWeek.MONDAY);
		LocalDateTime finalDate = initialDate.plusDays(6);
		
		ZonedDateTime zdt = (initialDate.atZone(ZoneId.of(ConstantDataManager.TIMEZONE_SAO_PAULO)));
		weekItem.setInitialDateWeek(Date.from(zdt.toInstant()));
		
		zdt = (finalDate.atZone(ZoneId.of(ConstantDataManager.TIMEZONE_SAO_PAULO)));
		weekItem.setFinalDateWeek(Date.from(zdt.toInstant()));
		
		dashboardCompanyExecutionWeek.getMapData().put(initialWeekNumber, weekItem);
		while(initialWeekNumber < finalWeekNumber) {
			initialDate = finalDate.plusDays(1);
			finalDate = initialDate.plusDays(6);
			
			initialWeekNumber = Integer.valueOf(
					String.valueOf(initialDate.getYear()) + 
					String.valueOf(initialDate.get(temporalField)));
			
			weekItem = new DashboardCompanyExecutionWeekItemDTO();
			weekItem.setWeekYear(initialWeekNumber);
				
			zdt = (initialDate.atZone(ZoneId.of(ConstantDataManager.TIMEZONE_SAO_PAULO)));
			weekItem.setInitialDateWeek(Date.from(zdt.toInstant()));
			
			zdt = (finalDate.atZone(ZoneId.of(ConstantDataManager.TIMEZONE_SAO_PAULO)));
			weekItem.setFinalDateWeek(Date.from(zdt.toInstant()));
			
			dashboardCompanyExecutionWeek.getMapData().put(initialWeekNumber, weekItem);
		}		
		
		//final String weekYearString = String.valueOf(calendar.get(Calendar.YEAR)) +  String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)-1);
		final int weekYear = Integer.parseInt(String.valueOf(LocalDate.now().getYear()) + StringUtils.padLeft(String.valueOf(LocalDate.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)), 2, ConstantDataManager.ZERO_STRING));
		dashboardCompanyExecutionWeek.setSelectedWeekYear(weekYear);

		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_dashboard_company_execution_week");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _date_from DATETIME,
		query.append("?, "); //03 - IN _date_to DATETIME,
		query.append("?"); //04 - _user_id BIGINT
		query.append(")"); 
		query.append("}");

		try {
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET_ALL.getType()); //01 - _operation INT,
			statement.setTimestamp(2,Timestamp.valueOf(dashboardCompanyExecutionWeek.getDateFrom())); //02 - IN _date_from DATETIME,
			statement.setTimestamp(3, Timestamp.valueOf(dashboardCompanyExecutionWeek.getDateTo())); //03 - IN _date_to DATETIME,
			statement.setString(4, dashboardCompanyExecutionWeek.getUserChildrensParent()); //04 - _users_id varchar(5000)
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				
				final int key = resultSet.getInt(DatabaseConstants.COLUMN_COUNT_WEEK_YEAR);
				
				DashboardCompanyExecutionWeekItemDTO item = dashboardCompanyExecutionWeek.getMapData().get(key);
				if(item == null) {
					item = new DashboardCompanyExecutionWeekItemDTO();
					item.setWeekYear(key);
					item.setInitialDateWeek(resultSet.getDate(DatabaseConstants.COLUMN_INITIAL_DATE_WEEK));
					item.setFinalDateWeek(resultSet.getDate(DatabaseConstants.COLUMN_FINAL_DATE_WEEK));
				}
				item.setCountMovement(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_MOVEMENET));
				item.setCountExecuted(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_EXECUTED));
				item.setCountNotExecuted(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_NOT_EXECUTED));
				item.setCountDisable(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_DISABLE));
				item.setCountDisableExecuted(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_DISABLE_EXECUTED));
				item.setCountNegativeExecuted(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_NEGATIVE_EXECUTED));
				item.setCountLowMovement(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_LOW_MOVEMENT));
				item.setCountOthers(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_OTHERS));
				item.setCountCompanyNotExpiry(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_COMPANY_NOT_EXPIRY));
				item.setCountNotInformed(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_NOT_INFORMED));
				item.setCountNegative(resultSet.getInt(DatabaseConstants.COLUMN_COUNT_NEGATIVE));

				if(item.getWeekYear() == weekYear) {
					item.setSelectedMovement(true);
				}
				
				//dashboardCompanyExecutionWeek.addItem(item);
				dashboardCompanyExecutionWeek.getMapData().put(item.getWeekYear(), item);
			}
		} 
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(resultSet!=null)
			{
				resultSet.close();
			}
			if(statement!=null)
			{
				statement.close();
			}
		}	
	}
		
	@Override
	public Map<String, Double> getChartMovements(final DashboardCompanyExecutionWeekDTO dashboardCompanyExecutionWeek) throws Exception 
	{
		final Map<String, Double> result = new TreeMap<String, Double>();
		CallableStatement statement = null;
		ResultSet resultSet = null;
														
		final StringBuilder query = new StringBuilder();
		query.append("{");
		query.append("call pr_dashboard");
		query.append("(");
		query.append("?, "); //01 - _operation INT,
		query.append("?, "); //02 - _date_from DATETIME,
		query.append("?, "); //03 - IN _date_to DATETIME,
		query.append("?  "); //04 - _user_id BIGINT
		query.append(")"); 
		query.append("}");

		try 
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, dashboardCompanyExecutionWeek.getTypeDashboard()); //01 - _operation INT,
			statement.setTimestamp(2, Timestamp.valueOf(dashboardCompanyExecutionWeek.getDateFrom())); //02 - IN _date_from DATETIME,
			statement.setTimestamp(3, Timestamp.valueOf(dashboardCompanyExecutionWeek.getDateTo())); //03 - IN _date_to DATETIME,
			statement.setString(4, dashboardCompanyExecutionWeek.getUserChildrensParent()); //04 - _users_id varchar(5000)
			resultSet = statement.executeQuery();
								
			if(dashboardCompanyExecutionWeek.getTypeDashboard() == 3 || 
			   dashboardCompanyExecutionWeek.getTypeDashboard() == 4 ||
			   dashboardCompanyExecutionWeek.getTypeDashboard() == 5)
			{
				while (resultSet.next()) 
				{				
					String key = resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION);  
					Double value = resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL);
					Double valueOthers = 0d;
					final Integer rownum = resultSet.getInt(DatabaseConstants.COLUMN_ROWNUM);
						
					if(rownum <= 5)
					{
						result.put(key, value);
					}
					else
					{
						key = "OUTROS";
						valueOthers += value;
						result.put(key, valueOthers);
					}					
				}
			}
			else
			{
				while (resultSet.next()) 
				{				
					final String key = resultSet.getString(DatabaseConstants.COLUMN_DESCRIPTION);  
					final Double value = resultSet.getDouble(DatabaseConstants.COLUMN_TOTAL);
									
					result.put(key, value);
				}
			}				
		} 
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(resultSet!=null)
			{
				resultSet.close();
			}
			if(statement!=null)
			{
				statement.close();
			}
		}
		return result;	
	}
}