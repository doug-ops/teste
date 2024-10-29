/*
 * Date create 18/09/2023.
 */
package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.CompanyIntegrationSystemDTO;

public interface CompanyDao {
	List<CompanyIntegrationSystemDTO> getCompanyIntegrationSystem(long companyId) throws Exception;
}