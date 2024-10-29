package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.ProductSubGroupDao;
import com.manager.systems.common.dao.impl.adm.ProductSubGroupDaoImpl;
import com.manager.systems.common.dto.adm.ProductSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductSubGroupDTO;
import com.manager.systems.common.service.adm.ProductSubGroupService;
import com.manager.systems.common.vo.Combobox;

public class ProductSubGroupServiceImpl implements ProductSubGroupService 
{
	private ProductSubGroupDao productSubGroupDao;
	
	public ProductSubGroupServiceImpl(final Connection connection) 
	{
		super();
		this.productSubGroupDao = new ProductSubGroupDaoImpl(connection);
	}

	@Override
	public boolean save(final ProductSubGroupDTO productSubGroup) throws Exception 
	{
		return this.productSubGroupDao.save(productSubGroup);
	}

	@Override
	public boolean inactive(final ProductSubGroupDTO productSubGroup) throws Exception 
	{
		return this.productSubGroupDao.inactive(productSubGroup);
	}

	@Override
	public void get(final ProductSubGroupDTO productSubGroup) throws Exception
	{
		this.productSubGroupDao.get(productSubGroup);
	}

	@Override
	public void getAll(final ReportProductSubGroupDTO reportProductSubGroup) throws Exception 
	{
		this.productSubGroupDao.getAll(reportProductSubGroup);
	}

	@Override
	public List<Combobox> getAllCombobox(final ReportProductSubGroupDTO reportProductSubGroup) throws Exception 
	{
		return this.productSubGroupDao.getAllCombobox(reportProductSubGroup);
	}
}