/**
 * Creation Date 04/02/2023.
 */
package com.manager.systems.common.service.impl.movement.company;

import java.sql.Connection;

import com.manager.systems.common.dao.movement.DocumentMovementIdDao;
import com.manager.systems.common.dao.movement.impl.DocumentMovementIdDaoImpl;
import com.manager.systems.common.dto.movement.document.DocumentMovementIdDTO;
import com.manager.systems.common.service.movement.DocumentMovementIdService;

public class DocumentMovementIdServiceImpl implements DocumentMovementIdService {

	private DocumentMovementIdDao documentMovementIdDao;
	
	public DocumentMovementIdServiceImpl(final Connection connection) 
	{
		super();
		this.documentMovementIdDao = new DocumentMovementIdDaoImpl(connection);
	}
	
	@Override
	public DocumentMovementIdDTO generateDocumentMovementId(final boolean generateDocumentParentId, final boolean generateDocumentId) throws Exception {
		return this.documentMovementIdDao.generateDocumentMovementId(generateDocumentParentId, generateDocumentId);
	}
	
	@Override
	public long generateDocumentTransactionId() throws Exception {
		return this.documentMovementIdDao.generateDocumentTransactionId();
	}
}
