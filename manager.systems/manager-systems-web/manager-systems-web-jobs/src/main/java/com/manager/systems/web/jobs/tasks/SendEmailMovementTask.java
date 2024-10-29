/*
 * Date create 30/03/2021.
 */
package com.manager.systems.web.jobs.tasks;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.service.impl.movement.product.MovementProductServiceImpl;
import com.manager.systems.common.service.movement.product.MovementProductService;
import com.manager.systems.common.utils.SendEmail;
import com.manager.systems.web.jobs.dao.FactoryConnection;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.dto.SendEmailDocumentDTO;
import com.manager.systems.web.jobs.service.SendEmailDocumentService;
import com.manager.systems.web.jobs.service.impl.SendEmailDocumentServiceImpl;
import com.manager.systems.web.jobs.utils.ConstantDataManager;

public class SendEmailMovementTask implements Runnable 
{
	private static final Log log = LogFactory.getLog(SendEmailMovementTask.class);

	private JobDTO job;
	
	public SendEmailMovementTask(final JobDTO job) 
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
				if(ConstantDataManager.TASK_SEND_EMAIL_MOVEMENT.equalsIgnoreCase(this.job.getDescription()))
				{
					log.info(ConstantDataManager.GLOBAL_MSG_INITIAL_SINC + ConstantDataManager.TASK_SEND_EMAIL_MOVEMENT);

					final List<SendEmailDocumentDTO> items = this.getDocumentsToSendEmail();
					
					log.info(ConstantDataManager.GLOBAL_MSG_SEND_EMAILS + ConstantDataManager.SPACE + items.size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SEND_EMAIL_MOVEMENT);
					
					if(items.size()>0)
					{
						connection = FactoryConnection.getConnectionPortal();
						final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
						final SendEmailDocumentService sendEmailService = new SendEmailDocumentServiceImpl(connection);
						
						for(final SendEmailDocumentDTO item : items) {

							final PreviewMovementCompanyDTO previewMovementCompany = movementProductService.reportCompanyMovement(item.getDocumentParentId(), item.getCompanyId());
							previewMovementCompany.calculateBalanceMovement();
						
							if (previewMovementCompany != null) {
								final byte[] pdfFile = movementProductService.generatePDFMovementCompany(previewMovementCompany);

								final String title = com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_PDF
										+ com.manager.systems.common.utils.ConstantDataManager.SPACE + previewMovementCompany.getCompnayId()
										+ com.manager.systems.common.utils.ConstantDataManager.SPACE + previewMovementCompany.getCompanyDescription();
								final String body = com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_PDF
										+ com.manager.systems.common.utils.ConstantDataManager.SPACE + previewMovementCompany.getCompnayId()
										+ com.manager.systems.common.utils.ConstantDataManager.SPACE + previewMovementCompany.getCompanyDescription();

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

								SendEmail.sendEmailAttachment(emailTo, body, title, pdfFile, props);
								
								sendEmailService.updateEmailStatusSend(item.getDocumentParentId(), item.getCompanyId());
							}							
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
	
	private List<SendEmailDocumentDTO> getDocumentsToSendEmail() throws Exception
	{
		final List<SendEmailDocumentDTO> items = new ArrayList<>();
		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			final SendEmailDocumentService service = new SendEmailDocumentServiceImpl(connection);
			items.addAll(service.getDocumentsToSendEmail());
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