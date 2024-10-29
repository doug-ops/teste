/*
 * Date create 28/06/2023.
 */
package com.manager.systems.common.service.provider;

import com.manager.systems.common.dto.provider.ProviderStatemenReportDTO;

public interface ProviderStatementService {
	void getProviderStatement(ProviderStatemenReportDTO report) throws Exception;
	byte[] processPdfReport(ProviderStatemenReportDTO report) throws Exception;
}