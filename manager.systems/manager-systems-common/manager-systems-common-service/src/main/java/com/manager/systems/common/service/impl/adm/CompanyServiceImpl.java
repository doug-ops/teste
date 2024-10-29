/*
 * Date create 18/09/2023.
 */
package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.CompanyDao;
import com.manager.systems.common.dao.impl.adm.CompanyDaoImpl;
import com.manager.systems.common.dto.adm.CompanyIntegrationSystemDTO;
import com.manager.systems.common.service.adm.CompanyService;

public class CompanyServiceImpl implements CompanyService {

	private CompanyDao companyDao;
	
	public CompanyServiceImpl(final Connection connection) {
		this.companyDao = new CompanyDaoImpl(connection);
	}
	
	@Override
	public List<CompanyIntegrationSystemDTO> getCompanyIntegrationSystem(long companyId) throws Exception {
		return this.companyDao.getCompanyIntegrationSystem(companyId);
	}
}
