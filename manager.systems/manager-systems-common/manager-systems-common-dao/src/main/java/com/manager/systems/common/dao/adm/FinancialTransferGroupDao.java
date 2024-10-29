package com.manager.systems.common.dao.adm;

import com.manager.systems.common.dto.adm.FinancialTransferGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialTransferGroupDTO;

public interface FinancialTransferGroupDao 
{
	boolean save(FinancialTransferGroupDTO financialTransfer) throws Exception;
	boolean inactive(FinancialTransferGroupDTO financialTransfer) throws Exception;
	void get(FinancialTransferGroupDTO financialTransfer) throws Exception;
	void getProductsGroup(FinancialTransferGroupDTO financialTransfer) throws Exception;
	void getAll(ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception;
	boolean clone(FinancialTransferGroupDTO financialTransfer) throws Exception;
	void getFinancialTransfer(ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception;
	FinancialTransferGroupDTO getGroupFinancialTransfer(ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception;
	boolean saveGroupFinancialTransfer(ReportFinancialTransferGroupDTO reportFinancialTransfer) throws Exception;
} 