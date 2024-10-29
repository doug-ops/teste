package com.manager.systems.web.movements.service;

import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.web.movements.dto.DocumentMovementDTO;
import com.manager.systems.web.movements.dto.DocumentMovementGroup;
import com.manager.systems.web.movements.dto.DocumentMovementGroupFilter;
import com.manager.systems.web.movements.dto.DocumentMovementItemDTO;
import com.manager.systems.web.movements.dto.MovementDocumentCompanyDTO;
import com.manager.systems.web.movements.dto.MovementWekendDTO;

public interface DocumentMovementService 
{
	void getAllDocumentMovement(DocumentMovementDTO documentMovement) throws Exception;
	void getDocumentMovement(PreviewMovementCompanyDTO previewMovementCompany) throws Exception;
	void getDocumentMovementItem(DocumentMovementItemDTO item) throws Exception;
	byte[] genetarePdfReportFromPreview(PreviewMovementCompanyDTO preview) throws Exception;
	void getAllDocumentMovementReport(DocumentMovementDTO documentMovement) throws Exception;
	byte[] generatePDFMovementDocuments(DocumentMovementDTO documents) throws Exception;
	byte[] generatePDFMovementDocumentsWeek(MovementWekendDTO documents) throws Exception;
	byte[] generatePDFMovementDocumentsGroupCompany(MovementDocumentCompanyDTO documents) throws Exception;
	DocumentMovementGroup getDocumentMovementGroup(DocumentMovementGroupFilter filter) throws Exception;
}