package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.ProductDTO;
import com.manager.systems.common.dto.adm.ReportProductDTO;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;
import com.manager.systems.common.vo.Combobox;

public interface ProductService 
{
	boolean save(ProductDTO product) throws Exception;
	boolean saveBravo(ProductDTO product) throws Exception;
	boolean inactive(ProductDTO product) throws Exception;
	void get(ProductDTO product) throws Exception;
	void getAll(ReportProductDTO reportProduct) throws Exception;
	List<Combobox> getAllAutocomplete(ReportProductDTO reportProduct) throws Exception;
	List<Combobox> getAllAutocompleteByCompany(ReportProductDTO reportProduct) throws Exception;
	List<ProductDTO> getAllProductsByCompany(long companyId) throws Exception;
	List<MovementProductDTO> getMovmentProductsSystemOld(String query) throws Exception;
	void getAllProducts(ReportProductDTO reportProduct) throws Exception;
	boolean transferProductsToCompany(int companyOrigin, int companyDestiny) throws Exception;

}