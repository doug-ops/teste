package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.FinancialGroupDao;
import com.manager.systems.common.dao.impl.adm.FinancialGroupDaoImpl;
import com.manager.systems.common.dto.adm.FinancialGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialGroupDTO;
import com.manager.systems.common.service.adm.FinancialGroupService;
import com.manager.systems.common.vo.Combobox;

public class FinancialGroupServiceImpl implements FinancialGroupService 
{
	private FinancialGroupDao financialGroupDao;
	
	public FinancialGroupServiceImpl(final Connection connection) 
	{
		super();
		this.financialGroupDao = new FinancialGroupDaoImpl(connection);
	}

	@Override
	public boolean save(final FinancialGroupDTO financialGroup) throws Exception 
	{
		return this.financialGroupDao.save(financialGroup);
	}

	@Override
	public boolean inactive(final FinancialGroupDTO financialGroup) throws Exception 
	{
		return this.financialGroupDao.inactive(financialGroup);
	}

	@Override
	public void get(final FinancialGroupDTO financialGroup) throws Exception
	{
		this.financialGroupDao.get(financialGroup);

	}

	@Override
	public void getAll(final ReportFinancialGroupDTO reportFinancialGroup) throws Exception 
	{
		this.financialGroupDao.getAll(reportFinancialGroup);
	}

	@Override
	public List<Combobox> getAllCombobox() throws Exception 
	{
		return this.financialGroupDao.getAllCombobox();
	}
	
	@Override
	public String getMaxId() throws Exception
	{
		return this.financialGroupDao.getMaxId();
	}
}