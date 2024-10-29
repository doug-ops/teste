package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.ProductGroupDao;
import com.manager.systems.common.dao.impl.adm.ProductGroupDaoImpl;
import com.manager.systems.common.dto.adm.ProductGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductGroupDTO;
import com.manager.systems.common.service.adm.ProductGroupService;
import com.manager.systems.common.vo.Combobox;

public class ProductGroupServiceImpl implements ProductGroupService 
{
	private ProductGroupDao productGroupDao;
	
	public ProductGroupServiceImpl(final Connection connection) 
	{
		super();
		this.productGroupDao = new ProductGroupDaoImpl(connection);
	}

	@Override
	public boolean save(final ProductGroupDTO productGroup) throws Exception 
	{
		return this.productGroupDao.save(productGroup);
	}

	@Override
	public boolean inactive(final ProductGroupDTO productGroup) throws Exception 
	{
		return this.productGroupDao.inactive(productGroup);
	}

	@Override
	public void get(final ProductGroupDTO productGroup) throws Exception
	{
		this.productGroupDao.get(productGroup);

	}

	@Override
	public void getAll(final ReportProductGroupDTO reportProductGroup) throws Exception 
	{
		this.productGroupDao.getAll(reportProductGroup);
	}
	
	@Override
	public List<Combobox> getAllCombobox(final ReportProductGroupDTO reportProductGroup) throws Exception 
	{
		return this.productGroupDao.getAllCombobox(reportProductGroup);
	}
}