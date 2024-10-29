/*
 * Date create 30/03/2021.
 */
package com.manager.systems.web.jobs.service;

import java.time.LocalDateTime;
import java.util.List;

import com.manager.systems.web.jobs.dto.SendEmailErrorDTO;

public interface SendEmailErrorService {
	List<SendEmailErrorDTO> getErrorSendEmail() throws Exception;
	boolean updateEmailStatusSend(long id, LocalDateTime date, boolean hasSend) throws Exception;
}
