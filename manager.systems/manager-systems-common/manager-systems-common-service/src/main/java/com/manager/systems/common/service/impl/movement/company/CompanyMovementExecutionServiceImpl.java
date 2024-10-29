/**
 * Date create 09/04/2020.
 */
package com.manager.systems.common.service.impl.movement.company;

import java.sql.Connection;

import com.manager.systems.common.dao.impl.movement.company.CompanyMovementExecutionDaoImpl;
import com.manager.systems.common.dao.movement.company.CompanyMovementExecutionDao;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionDTO;
import com.manager.systems.common.dto.movement.company.CompanyMovementExecutionItemDTO;
import com.manager.systems.common.service.movement.company.CompanyMovementExecutionService;


public class CompanyMovementExecutionServiceImpl implements CompanyMovementExecutionService
{
private CompanyMovementExecutionDao companyMovementExecutionDao;
	
	public CompanyMovementExecutionServiceImpl(final Connection connection) 
	{
		super();
		this.companyMovementExecutionDao = new CompanyMovementExecutionDaoImpl(connection);
	}
	
	@Override
	public void get(final CompanyMovementExecutionDTO companyMovementExecution) throws Exception {
		 this.companyMovementExecutionDao.get(companyMovementExecution);
	}

	@Override
	public void getAll(final CompanyMovementExecutionDTO companyMovementExecution) throws Exception {
		 this.companyMovementExecutionDao.getAll(companyMovementExecution);
	}
	
	@Override
	public boolean updateCompanyMovementExecution(final CompanyMovementExecutionItemDTO companyMovementExecutionItem) throws Exception {
		return this.companyMovementExecutionDao.updateCompanyMovementExecution(companyMovementExecutionItem);
	}
	
	@Override
	public boolean updateMovementMotive(final long companyId, final boolean isExecution) throws Exception {
		return this.companyMovementExecutionDao.updateMovementMotive(companyId, false);
	}
}