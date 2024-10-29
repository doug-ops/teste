package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.FinancialSubGroupDao;
import com.manager.systems.common.dao.impl.adm.FinancialSubGroupDaoImpl;
import com.manager.systems.common.dto.adm.FinancialSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialSubGroupDTO;
import com.manager.systems.common.service.adm.FinancialSubGroupService;
import com.manager.systems.common.vo.Combobox;

public class FinancialSubGroupServiceImpl implements FinancialSubGroupService 
{
	private FinancialSubGroupDao financialSubGroupDao;
	
	public FinancialSubGroupServiceImpl(final Connection connection) 
	{
		super();
		this.financialSubGroupDao = new FinancialSubGroupDaoImpl(connection);
	}

	@Override
	public final boolean save(final FinancialSubGroupDTO financialSubGroup) throws Exception 
	{
		return this.financialSubGroupDao.save(financialSubGroup);
	}

	@Override
	public final boolean inactive(final FinancialSubGroupDTO financialSubGroup) throws Exception 
	{
		return this.financialSubGroupDao.inactive(financialSubGroup);
	}

	@Override
	public final void get(final FinancialSubGroupDTO financialSubGroup) throws Exception
	{
		this.financialSubGroupDao.get(financialSubGroup);
	}

	@Override
	public final void getAll(final ReportFinancialSubGroupDTO reportFinancialSubGroup) throws Exception 
	{
		this.financialSubGroupDao.getAll(reportFinancialSubGroup);
	}

	@Override
	public final List<Combobox> getAllCombobox(final ReportFinancialSubGroupDTO reportFinancialSubGroup) throws Exception
	{
		return this.financialSubGroupDao.getAllCombobox(reportFinancialSubGroup);
	}

	@Override
	public final boolean inactiveAllByGroup(final FinancialSubGroupDTO financialSubGroup) throws Exception {
		return this.financialSubGroupDao.inactiveAllByGroup(financialSubGroup);
	}
	
	@Override
	public final void getMaxId(final FinancialSubGroupDTO financialSubGroup) throws Exception
	{
		this.financialSubGroupDao.getMaxId(financialSubGroup);
	}
}