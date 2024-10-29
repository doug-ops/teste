/*
 * Date create 23/03/2021.
 */
package com.manager.systems.web.jobs.dao;

import java.util.List;

import com.manager.systems.web.jobs.dto.SendEmailDocumentDTO;

public interface SendEmailDocumentDao {
	List<SendEmailDocumentDTO> getDocumentsToSendEmail() throws Exception;
	boolean updateEmailStatusSend(long documentParentId, long companyId) throws Exception;
}