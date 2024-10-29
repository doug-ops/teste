/**
 * Creation Date 04/02/2023.
 */
package com.manager.systems.common.service.movement;

import com.manager.systems.common.dto.movement.document.DocumentMovementIdDTO;

public interface DocumentMovementIdService {
	DocumentMovementIdDTO generateDocumentMovementId(boolean generateDocumentParentId, boolean generateDocumentId) throws Exception;
	long generateDocumentTransactionId() throws Exception;
}