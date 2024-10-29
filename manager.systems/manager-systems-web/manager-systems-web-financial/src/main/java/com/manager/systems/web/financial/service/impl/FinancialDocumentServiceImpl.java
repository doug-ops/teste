package com.manager.systems.web.financial.service.impl;

import java.sql.Connection;

import com.manager.systems.web.financial.dao.FinancialDocumentDao;
import com.manager.systems.web.financial.dao.impl.FinancialDocumentDaoImpl;
import com.manager.systems.web.financial.dto.FinancialDocumentDTO;
import com.manager.systems.web.financial.service.FinancialDocumentService;

public class FinancialDocumentServiceImpl implements FinancialDocumentService
{
	private FinancialDocumentDao financialDocumentDao;
	
	public FinancialDocumentServiceImpl(final Connection connection) 
	{
		super();
		this.financialDocumentDao = new FinancialDocumentDaoImpl(connection);
	}

	@Override
	public void get(final FinancialDocumentDTO financialDocument) throws Exception 
	{
		this.financialDocumentDao.get(financialDocument);
	}

	@Override
	public boolean saveTransfer(final FinancialDocumentDTO financialDocument) throws Exception 
	{
		return this.financialDocumentDao.saveTransfer(financialDocument);
	}
	
	@Override
	public boolean inactive(final FinancialDocumentDTO financialDocument) throws Exception 
	{
		return this.financialDocumentDao.inactive(financialDocument);
	}
	
	@Override
	public boolean ungroup(final FinancialDocumentDTO financialDocument) throws Exception 
	{
		return this.financialDocumentDao.ungroup(financialDocument);
	}
	
	@Override
	public boolean changeStatus(final FinancialDocumentDTO financialDocument) throws Exception 
	{
		return this.financialDocumentDao.changeStatus(financialDocument);
	}

	@Override
	public boolean change(final FinancialDocumentDTO financialDocument) throws Exception
	{
		return this.financialDocumentDao.change(financialDocument);
	}

	@Override
	public boolean groupParent(final FinancialDocumentDTO financialDocument) throws Exception 
	{
		return this.financialDocumentDao.groupParent(financialDocument);
	}
	@Override
	public boolean saveNewDocument(final FinancialDocumentDTO financialDocument) throws Exception 
	{
		return this.financialDocumentDao.saveNewDocument(financialDocument);
	}
	
	@Override
	public boolean saveDocumentExpense(final FinancialDocumentDTO financialDocument) throws Exception 
	{
		return this.financialDocumentDao.saveDocumentExpense(financialDocument);
	}

	@Override
	public boolean rollbackMovement(final FinancialDocumentDTO financialDocument) throws Exception {
		return this.financialDocumentDao.rollbackMovement(financialDocument);
	}
}