package com.manager.systems.common.service.movement.company;

import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionDTO;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionItemDTO;

public interface CompanyMovementExecutionService 
{	
	void get(CompanyMovementExecutionDTO companyMovementExecution) throws Exception;
	void getAll(CompanyMovementExecutionDTO companyMovementExecution) throws Exception;
	boolean updateCompanyMovementExecution(CompanyMovementExecutionItemDTO companyMovementExecution) throws Exception;
	boolean updateMovementMotive(long companyId, boolean isExecution) throws Exception;
}