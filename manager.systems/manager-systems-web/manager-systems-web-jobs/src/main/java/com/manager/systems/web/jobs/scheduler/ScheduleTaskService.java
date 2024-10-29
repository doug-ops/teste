package com.manager.systems.web.jobs.scheduler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.service.JobService;
import com.manager.systems.web.jobs.service.impl.JobServiceImpl;
import com.manager.systems.web.jobs.tasks.GenerateCashingClosingMovementTask;
import com.manager.systems.web.jobs.tasks.ProcessWeekMovementTask;
import com.manager.systems.web.jobs.tasks.SendEmailBankAccountStatementTask;
import com.manager.systems.web.jobs.tasks.SendEmailErrorTask;
import com.manager.systems.web.jobs.tasks.SendEmailMovementTask;
import com.manager.systems.web.jobs.tasks.SincronizeProductMovementTask;
import com.manager.systems.web.jobs.utils.ConstantDataManager;

@Service
public class ScheduleTaskService {
	private static final Log log = LogFactory.getLog(ScheduleTaskService.class);
	private static TimeZone tz = TimeZone.getTimeZone(ConstantDataManager.TIMEZONE_SAO_PAULO);

	// Task Scheduler
	@Autowired
	private TaskScheduler scheduler;

	// A map for keeping scheduled tasks
	private Map<Integer, ScheduledFuture<?>> jobsMap = new HashMap<Integer, ScheduledFuture<?>>();

	public ScheduleTaskService() {
		super();
	}

	// Schedule Task to be executed every night at 00 or 12 am
	public void addTaskToScheduler(final int id, final Runnable task, final String cron) {
		final ScheduledFuture<?> scheduledTask = this.scheduler.schedule(task, new CronTrigger(cron, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
		this.jobsMap.put(id, scheduledTask);
	}

	// Remove scheduled task
	public void removeTaskFromScheduler(final int id) {
		final ScheduledFuture<?> scheduledTask = this.jobsMap.get(id);
		if (scheduledTask != null) {
			scheduledTask.cancel(true);
			this.jobsMap.put(id, null);
		}
	}

	// A context refresh event listener
	@EventListener({ ContextRefreshedEvent.class })
	void contextRefreshedEvent() {
		Connection connection = null;
		try {
			log.info(ConstantDataManager.GLOBAL_MSG_INITIALIZING_JOBS);
			connection = this.getConnection();
			if (connection == null) {
				throw new Exception(ConstantDataManager.GLOBAL_MSG_CONNECTION_ERROR);
			}
			final JobService jobService = new JobServiceImpl();
			final List<JobDTO> jobs = jobService.getAll(connection);
			log.info(String.format(ConstantDataManager.GLOBAL_MSG_FIND_JOBS, jobs.size()));
			if (jobs != null && jobs.size() > 0) {
				for (final JobDTO item : jobs) {
					if (ConstantDataManager.TASK_SINC_PRODUCT_MOVEMENT.equalsIgnoreCase(item.getDescription())) {
						log.info(ConstantDataManager.GLOBAL_MSG_SCHEDULLER + ConstantDataManager.BLANK + ConstantDataManager.TASK_SINC_PRODUCT_MOVEMENT);
						this.addTaskToScheduler(item.getId(), new SincronizeProductMovementTask(item), item.getSyncTimer());
					}
					else if (ConstantDataManager.TASK_SEND_EMAIL_MOVEMENT.equalsIgnoreCase(item.getDescription())) {
						log.info(ConstantDataManager.GLOBAL_MSG_SCHEDULLER + ConstantDataManager.BLANK + ConstantDataManager.TASK_SEND_EMAIL_MOVEMENT);
						this.addTaskToScheduler(item.getId(), new SendEmailMovementTask(item), item.getSyncTimer());
					}
					else if (ConstantDataManager.TASK_SEND_EMAIL_BANK_ACCOUNT_STATEMENT.equalsIgnoreCase(item.getDescription())) {
						log.info(ConstantDataManager.GLOBAL_MSG_SCHEDULLER + ConstantDataManager.BLANK + ConstantDataManager.TASK_SEND_EMAIL_BANK_ACCOUNT_STATEMENT);
						this.addTaskToScheduler(item.getId(), new SendEmailBankAccountStatementTask(item), item.getSyncTimer());
					}
					else if (ConstantDataManager.TASK_SEND_EMAIL_ERROR.equalsIgnoreCase(item.getDescription())) {
						log.info(ConstantDataManager.GLOBAL_MSG_SCHEDULLER + ConstantDataManager.BLANK + ConstantDataManager.TASK_SEND_EMAIL_ERROR);
						this.addTaskToScheduler(item.getId(), new SendEmailErrorTask(item), item.getSyncTimer());
					} else if (ConstantDataManager.TASK_PROCESS_NEXT_WEEK_MOVEMENT.equalsIgnoreCase(item.getDescription())) {
						log.info(ConstantDataManager.GLOBAL_MSG_SCHEDULLER + ConstantDataManager.BLANK + ConstantDataManager.TASK_PROCESS_NEXT_WEEK_MOVEMENT);
						this.addTaskToScheduler(item.getId(), new ProcessWeekMovementTask(item), item.getSyncTimer());
					} else if (ConstantDataManager.TASK_SINC_CASHING_CLOSING_MOVEMENT.equalsIgnoreCase(item.getDescription())) {
						log.info(ConstantDataManager.GLOBAL_MSG_SCHEDULLER + ConstantDataManager.BLANK + ConstantDataManager.TASK_SINC_CASHING_CLOSING_MOVEMENT);
						this.addTaskToScheduler(item.getId(), new GenerateCashingClosingMovementTask(item), item.getSyncTimer());
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
			log.error(StringUtils.formatDate(Calendar.getInstance(tz).getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS) + ConstantDataManager.SPACE + e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException e) {
					e.printStackTrace();
					log.error(StringUtils.formatDate(Calendar.getInstance(tz).getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS) + ConstantDataManager.SPACE + e.getMessage());
				}
			}
		}
	}

	public Connection getConnection() throws Exception {
		final Context initContext = new InitialContext();
		final Context envContext = (Context) initContext.lookup("java:/comp/env");
		final DataSource dataSource = (DataSource) envContext.lookup("jdbc/portal");
		return dataSource.getConnection();
	}
}