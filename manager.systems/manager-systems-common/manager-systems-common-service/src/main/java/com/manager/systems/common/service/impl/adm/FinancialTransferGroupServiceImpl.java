package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;

import com.manager.systems.common.dao.adm.FinancialTransferGroupDao;
import com.manager.systems.common.dao.impl.adm.FinancialTransferGroupDaoImpl;
import com.manager.systems.common.dto.adm.FinancialTransferGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialTransferGroupDTO;
import com.manager.systems.common.service.adm.FinancialTransferGroupService;

public class FinancialTransferGroupServiceImpl implements FinancialTransferGroupService 
{
	private FinancialTransferGroupDao financialTransferGroupDao;
	
	public FinancialTransferGroupServiceImpl(final Connection connection) 
	{
		super();
		this.financialTransferGroupDao = new FinancialTransferGroupDaoImpl(connection);
	}

	@Override
	public boolean save(final FinancialTransferGroupDTO financialTransfer) throws Exception 
	{
		return this.financialTransferGroupDao.save(financialTransfer);
	}

	@Override
	public boolean inactive(final FinancialTransferGroupDTO financialTransfer) throws Exception 
	{
		return this.financialTransferGroupDao.inactive(financialTransfer);
	}

	@Override
	public void get(final FinancialTransferGroupDTO financialTransfer) throws Exception 
	{
		this.financialTransferGroupDao.get(financialTransfer);
		this.financialTransferGroupDao.getProductsGroup(financialTransfer);
	}

	@Override
	public void getAll(final ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception 
	{
		this.financialTransferGroupDao.getAll(reportFinancialTransfer);
	}

	@Override
	public boolean clone(final FinancialTransferGroupDTO financialTransfer) throws Exception
	{
		return this.financialTransferGroupDao.clone(financialTransfer);
	}
	
	@Override
	public void getFinancialTransfer(final ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception 
	{
		this.financialTransferGroupDao.getFinancialTransfer(reportFinancialTransfer);
	}

	@Override
	public FinancialTransferGroupDTO getGroupFinancialTransfer(final ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception {
		return this.financialTransferGroupDao.getGroupFinancialTransfer(reportFinancialTransfer);
	}

	@Override
	public boolean saveGroupFinancialTransfer(final ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception {
		return this.financialTransferGroupDao.saveGroupFinancialTransfer(reportFinancialTransfer);
	}
}