/**
 * Creation Date 02/04/2023.
 */
package com.manager.systems.common.dto.movement.document;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentMovementIdDTO implements Serializable {
	private static final long serialVersionUID = 6992533571264107926L;
	
	private long documentParentId;
	private long documentId;
}
