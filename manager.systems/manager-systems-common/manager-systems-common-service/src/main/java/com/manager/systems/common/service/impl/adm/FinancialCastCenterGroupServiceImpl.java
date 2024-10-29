package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.FinancialCastCenterGroupDao;
import com.manager.systems.common.dao.impl.adm.FinancialCastCenterGroupDaoImpl;
import com.manager.systems.common.dto.adm.FinancialCastCenterGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialCastCenterGroupDTO;
import com.manager.systems.common.service.adm.FinancialCastCenterGroupService;
import com.manager.systems.common.vo.Combobox;

public class FinancialCastCenterGroupServiceImpl implements FinancialCastCenterGroupService 
{
	private FinancialCastCenterGroupDao FinancialCastCenterGroupDao;
	
	public FinancialCastCenterGroupServiceImpl(final Connection connection) 
	{
		super();
		this.FinancialCastCenterGroupDao = new FinancialCastCenterGroupDaoImpl(connection);
	}

	@Override
	public boolean save(final FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception 
	{
		return this.FinancialCastCenterGroupDao.save(FinancialCastCenterGroup);
	}

	@Override
	public boolean inactive(final FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception 
	{
		return this.FinancialCastCenterGroupDao.inactive(FinancialCastCenterGroup);
	}

	@Override
	public void get(final FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception
	{
		this.FinancialCastCenterGroupDao.get(FinancialCastCenterGroup);

	}

	@Override
	public void getAll(final ReportFinancialCastCenterGroupDTO reportFinancialCastCenterGroup) throws Exception 
	{
		this.FinancialCastCenterGroupDao.getAll(reportFinancialCastCenterGroup);
	}
	
	@Override
	public List<Combobox> getAllCombobox(final ReportFinancialCastCenterGroupDTO reportFinancialCastCenterGroup) throws Exception 
	{
		return this.FinancialCastCenterGroupDao.getAllCombobox(reportFinancialCastCenterGroup);
	}
}