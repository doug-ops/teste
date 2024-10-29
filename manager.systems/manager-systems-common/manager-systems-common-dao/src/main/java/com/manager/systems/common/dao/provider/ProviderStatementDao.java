/*
 * Create Date 28/06/2023
 */
package com.manager.systems.common.dao.provider;

import com.manager.systems.common.dto.provider.ProviderStatemenReportDTO;

public interface ProviderStatementDao {
	void getProviderStatement(ProviderStatemenReportDTO report) throws Exception;
}