/*
 * Date create 30/03/2021.
 */
package com.manager.systems.web.jobs.dto;

import java.io.Serializable;

import com.manager.systems.common.vo.ChangeData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailDocumentDTO implements Serializable {
	private static final long serialVersionUID = 1436569188715762627L;

	private long id;
	private String email;
	private long documentParentId;
	private long companyId;
	private boolean isSend;
	private boolean inactive;
	private ChangeData changeData;
}