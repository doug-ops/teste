package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;

import com.manager.systems.common.dao.adm.ProductCompanyDao;
import com.manager.systems.common.dao.impl.adm.ProductCompanyDaoImpl;
import com.manager.systems.common.dto.adm.ProductCompanyDTO;
import com.manager.systems.common.dto.adm.ReportProductCompanyDTO;
import com.manager.systems.common.service.adm.ProductCompanyService;

public class ProductCompanyServiceImpl implements ProductCompanyService 
{
	private ProductCompanyDao productCompanyDao;
	
	public ProductCompanyServiceImpl(final Connection connection) 
	{
		super();
		this.productCompanyDao = new ProductCompanyDaoImpl(connection);
	}

	@Override
	public boolean save(final ProductCompanyDTO productCompany) throws Exception 
	{
		return this.productCompanyDao.save(productCompany);
	}

	@Override
	public boolean inactive(final ProductCompanyDTO productCompany) throws Exception 
	{
		return this.productCompanyDao.inactive(productCompany);
	}

	@Override
	public boolean delete(final ProductCompanyDTO productCompany) throws Exception 
	{
		return this.productCompanyDao.delete(productCompany);
	}

	@Override
	public void get(final ProductCompanyDTO productCompany) throws Exception 
	{
		this.productCompanyDao.get(productCompany);
	}

	@Override
	public void getAll(final ReportProductCompanyDTO reportProductCompany) throws Exception 
	{
		this.productCompanyDao.getAll(reportProductCompany);
	}
}