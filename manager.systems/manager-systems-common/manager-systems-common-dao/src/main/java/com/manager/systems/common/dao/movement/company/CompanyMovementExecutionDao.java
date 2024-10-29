package com.manager.systems.common.dao.movement.company;

import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionDTO;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionItemDTO;

public interface CompanyMovementExecutionDao 
{
	void get(CompanyMovementExecutionDTO companyMovementExecution) throws Exception;
	void getAll(CompanyMovementExecutionDTO companyMovementExecution) throws Exception;
	boolean updateCompanyMovementExecution(CompanyMovementExecutionItemDTO companyMovementExecutionItem) throws Exception;
	boolean updateMovementMotive(long companyId,boolean isExecution) throws Exception;
}