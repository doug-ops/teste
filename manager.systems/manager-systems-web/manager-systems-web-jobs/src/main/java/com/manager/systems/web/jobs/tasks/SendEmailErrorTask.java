/*
 * Date create 24/08/2022.
 */
package com.manager.systems.web.jobs.tasks;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manager.systems.common.utils.SendEmail;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.jobs.dao.FactoryConnection;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.dto.SendEmailErrorDTO;
import com.manager.systems.web.jobs.service.SendEmailErrorService;
import com.manager.systems.web.jobs.service.impl.SendEmailErrorServiceImpl;
import com.manager.systems.web.jobs.utils.ConstantDataManager;

public class SendEmailErrorTask implements Runnable 
{
	private static final Log log = LogFactory.getLog(SendEmailErrorTask.class);

	private JobDTO job;
	
	public SendEmailErrorTask(final JobDTO job) 
	{
		super();
		this.job = job;
	}
	
	@Override
	public void run() 
	{
		try
		{
			this.handleJob();	
		}
		catch (final Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	private void handleJob() throws Exception {
		Connection connection = null;

		try
		{
			if(this.job!=null && this.job.getParentId()>0)
			{
				if(ConstantDataManager.TASK_SEND_EMAIL_ERROR.equalsIgnoreCase(this.job.getDescription()))
				{
					log.info(ConstantDataManager.GLOBAL_MSG_INITIAL_SINC + ConstantDataManager.TASK_SEND_EMAIL_ERROR);
					
					final List<SendEmailErrorDTO> items = this.getErrorSendEmail();

					log.info(ConstantDataManager.GLOBAL_MSG_SEND_EMAILS + ConstantDataManager.SPACE + items.size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SEND_EMAIL_ERROR);
					
					if(items.size()>0)
					{
						connection = FactoryConnection.getConnectionPortal();
						
						for(final SendEmailErrorDTO item : items) {
							
							final String title = com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_ERRO_EMAIL;
									
							final String body = "FUNCIONALIDADE: "+ item.getProcessDescription() +",\n" +
												"MENSAGEM DO ERRO: "+ item.getErrorNote() + ",\n" +
												"DATA DO ERRO: " + StringUtils.formatDate(item.getSendDate(),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS)+".";

								final String emailTo = item.getEmail();
								final Properties props = new Properties();
								props.put("mail.from", "reportes35904@gmail.com");
								props.put("mail.username", "6bb8d7803d4f281dbe82952664286432");
								props.put("mail.password", "6bcb1d62b814f136f93cac5eee5b11b4");
								props.put("habilita.envio.email", "true");

								props.put("mail.smtp.host", "in-v3.mailjet.com");
								props.put("mail.smtp.auth", "true");
								props.put("mail.smtp.port", "587");
								props.setProperty("mail.smtp.socketFactory.port", "465");
								props.put("mail.smtp.ssl.trust", "*");
								props.put("mail.smtp.startssl.enable", "false");

								SendEmail.sendEmailAttachment(emailTo, body, title, null, props);
								
								final SendEmailErrorService sendEmailErrorservice = new SendEmailErrorServiceImpl(connection);
								boolean hasSend = true;
								sendEmailErrorservice.updateEmailStatusSend(item.getId(), item.getSendDate(), hasSend);
						}
					}
				}
			}
		}
		catch (final Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
	}
	
	private List<SendEmailErrorDTO> getErrorSendEmail() throws Exception
	{
		final List<SendEmailErrorDTO> items = new ArrayList<>();
		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			final SendEmailErrorService service = new SendEmailErrorServiceImpl(connection);
			items.addAll(service.getErrorSendEmail());
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}		
		return items;
	}
}