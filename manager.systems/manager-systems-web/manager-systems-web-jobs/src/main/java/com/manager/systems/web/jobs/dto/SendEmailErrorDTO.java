/*
 * Date create 23/08/2022.
 */
package com.manager.systems.web.jobs.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.manager.systems.common.vo.ChangeData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailErrorDTO implements Serializable {
	private static final long serialVersionUID = 1436569188715762627L;

	private long id;
	private String processDescription;
	private String errorNote;
	private String email;
	private boolean hasSend;
	private LocalDateTime sendDate;
	private boolean inactive;
	private ChangeData changeData;
}