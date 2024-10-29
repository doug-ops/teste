package com.manager.systems.web.movements.dao;

import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.web.movements.dto.DocumentMovementDTO;
import com.manager.systems.web.movements.dto.DocumentMovementGroup;
import com.manager.systems.web.movements.dto.DocumentMovementGroupFilter;
import com.manager.systems.web.movements.dto.DocumentMovementItemDTO;

public interface DocumentMovementDao 
{
	void getAllDocumentMovement(DocumentMovementDTO documentMovement) throws Exception;
	void getDocumentMovement(PreviewMovementCompanyDTO previewMovementCompany) throws Exception;
	void getDocumentMovementItem(DocumentMovementItemDTO item) throws Exception;
	void getAllDocumentMovementReport(DocumentMovementDTO documentMovement) throws Exception;	
	DocumentMovementGroup getDocumentMovementGroup(DocumentMovementGroupFilter filter) throws Exception;
}