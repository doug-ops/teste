package com.manager.systems.common.service.financialCostCenter.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.financialCostCenter.FinancialCostCenterDao;
import com.manager.systems.common.dao.impl.financialCostCenter.FinancialCostCenterDaoImpl;
import com.manager.systems.common.dto.financialCostCenter.FinancialCostCenterDTO;
import com.manager.systems.common.service.financialCostCenter.FinancialCostCenterService;
import com.manager.systems.common.vo.Combobox;

public class FinancialCostCenterServiceImpl implements FinancialCostCenterService 
{
	private FinancialCostCenterDao financialCostCenterDao;
	
	public FinancialCostCenterServiceImpl(final Connection connection) 
	{
		super();
		this.financialCostCenterDao = new FinancialCostCenterDaoImpl(connection);
	}

	@Override

	public List<Combobox> getAllCombobox() throws Exception 
	{
		return this.financialCostCenterDao.getAllCombobox();
	}
	
	@Override
	public Integer save(final FinancialCostCenterDTO costCenter) throws Exception 
	{
		return this.financialCostCenterDao.save(costCenter);
	}
}