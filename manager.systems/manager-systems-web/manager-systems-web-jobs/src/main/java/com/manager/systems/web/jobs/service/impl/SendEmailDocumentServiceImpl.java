/*
 * Date create 30/03/2021.
 */
package com.manager.systems.web.jobs.service.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.web.jobs.dao.SendEmailDocumentDao;
import com.manager.systems.web.jobs.dao.impl.SendEmailDocumentDaoImpl;
import com.manager.systems.web.jobs.dto.SendEmailDocumentDTO;
import com.manager.systems.web.jobs.service.SendEmailDocumentService;

public class SendEmailDocumentServiceImpl implements SendEmailDocumentService {
	
	private SendEmailDocumentDao sendEmailDocumentDao;
	
	public SendEmailDocumentServiceImpl(final Connection connection) {
		super();
		this.sendEmailDocumentDao = new SendEmailDocumentDaoImpl(connection);
	}

	@Override
	public List<SendEmailDocumentDTO> getDocumentsToSendEmail() throws Exception {
		return this.sendEmailDocumentDao.getDocumentsToSendEmail();
	}

	@Override
	public boolean updateEmailStatusSend(final long documentParentId, final long companyId) throws Exception {
		return this.sendEmailDocumentDao.updateEmailStatusSend(documentParentId, companyId);
	}
}