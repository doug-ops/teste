package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.ProductDao;
import com.manager.systems.common.dao.impl.adm.ProductDaoImpl;
import com.manager.systems.common.dto.adm.ProductDTO;
import com.manager.systems.common.dto.adm.ReportProductDTO;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;
import com.manager.systems.common.service.adm.ProductService;
import com.manager.systems.common.vo.Combobox;

public class ProductServiceImpl implements ProductService 
{
	private ProductDao productDao;
	
	public ProductServiceImpl(final Connection connection) 
	{
		super();
		this.productDao = new ProductDaoImpl(connection);
	}

	@Override
	public boolean save(final ProductDTO product) throws Exception
	{
		return this.productDao.save(product);
	}
	
	@Override
	public boolean saveBravo(final ProductDTO product) throws Exception
	{
		return this.productDao.saveBravo(product);
	}

	@Override
	public boolean inactive(final ProductDTO product) throws Exception
	{
		return this.productDao.inactive(product);
	}

	@Override
	public void get(final ProductDTO product) throws Exception
	{
		this.productDao.get(product);
	}

	@Override
	public void getAll(final ReportProductDTO reportProduct) throws Exception
	{
		this.productDao.getAll(reportProduct);
	}

	@Override
	public List<Combobox> getAllAutocomplete(final ReportProductDTO reportProduct) throws Exception 
	{
		return this.productDao.getAllAutocomplete(reportProduct);
	}

	@Override
	public List<Combobox> getAllAutocompleteByCompany(ReportProductDTO reportProduct) throws Exception
	{
		return this.productDao.getAllAutocompleteByCompany(reportProduct);
	}

	@Override
	public List<ProductDTO> getAllProductsByCompany(final long companyId) throws Exception {
		return this.productDao.getAllProductsByCompany(companyId);
	}

	@Override
	public List<MovementProductDTO> getMovmentProductsSystemOld(final String query) throws Exception {
		return this.productDao.getMovmentProductsSystemOld(query);
	}
	
	@Override
	public void getAllProducts(final ReportProductDTO reportProduct) throws Exception 
	{
		this.productDao.getAllProducts(reportProduct);
	}
	
	@Override
	public boolean transferProductsToCompany(final int companyOrigin, final int companyDestiny) throws Exception
	{
		return this.productDao.transferProductsToCompany(companyOrigin, companyDestiny);
	}
	
}