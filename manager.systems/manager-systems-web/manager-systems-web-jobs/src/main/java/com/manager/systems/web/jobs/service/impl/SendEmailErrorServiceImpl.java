/*
 * Date create 24/08/2022.
 */
package com.manager.systems.web.jobs.service.impl;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import com.manager.systems.web.jobs.dao.SendEmailErrorDao;
import com.manager.systems.web.jobs.dao.impl.SendEmailErrorDaoImpl;
import com.manager.systems.web.jobs.dto.SendEmailErrorDTO;
import com.manager.systems.web.jobs.service.SendEmailErrorService;

public class SendEmailErrorServiceImpl implements SendEmailErrorService {
	
	private SendEmailErrorDao sendEmailErrorDao;
	
	public SendEmailErrorServiceImpl(final Connection connection) {
		super();
		this.sendEmailErrorDao = new SendEmailErrorDaoImpl(connection);
	}
	@Override
	public List<SendEmailErrorDTO> getErrorSendEmail() throws Exception {
		return this.sendEmailErrorDao.getErrorSendEmail();
	}
	
	@Override
	public boolean updateEmailStatusSend(long id, LocalDateTime date, boolean hasSend) throws Exception {
		return this.sendEmailErrorDao.updateEmailStatusSend(id, date, hasSend);
	}
}