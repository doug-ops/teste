/*
 * Date create 24/08/2022.
 */
package com.manager.systems.web.jobs.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.manager.systems.web.jobs.dto.SendEmailErrorDTO;

public interface SendEmailErrorDao {
	List<SendEmailErrorDTO> getErrorSendEmail() throws Exception;
	boolean updateEmailStatusSend(long id, LocalDateTime date, boolean hasSend) throws Exception;
}