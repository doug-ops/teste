/*
 * Date create 30/03/2021.
 */
package com.manager.systems.web.jobs.service;

import java.util.List;

import com.manager.systems.web.jobs.dto.SendEmailDocumentDTO;

public interface SendEmailDocumentService {
	List<SendEmailDocumentDTO> getDocumentsToSendEmail() throws Exception;
	boolean updateEmailStatusSend(long documentParentId, long companyId) throws Exception;
}
