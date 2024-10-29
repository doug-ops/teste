package com.manager.systems.common.dao.adm;

import com.manager.systems.common.dto.adm.ProductCompanyDTO;
import com.manager.systems.common.dto.adm.ReportProductCompanyDTO;

public interface ProductCompanyDao 
{
	boolean save(ProductCompanyDTO productCompany) throws Exception;
	boolean inactive(ProductCompanyDTO productCompany) throws Exception;
	boolean delete(ProductCompanyDTO productCompany) throws Exception;
	void get(ProductCompanyDTO productCompany) throws Exception;
	void getAll(ReportProductCompanyDTO reportProductCompany) throws Exception;
}