package com.manager.systems.web.financial.dao;

import com.manager.systems.web.financial.dto.FinancialDocumentDTO;

public interface FinancialDocumentDao 
{
	void get(FinancialDocumentDTO financialDocument) throws Exception;
	boolean saveTransfer(FinancialDocumentDTO financialDocument) throws Exception;
	boolean change(FinancialDocumentDTO financialDocument) throws Exception;
	boolean inactive(FinancialDocumentDTO financialDocument) throws Exception;
	boolean ungroup(FinancialDocumentDTO financialDocument) throws Exception;
	boolean groupParent(FinancialDocumentDTO financialDocument) throws Exception;
	boolean changeStatus(FinancialDocumentDTO financialDocument) throws Exception;
	boolean saveNewDocument(FinancialDocumentDTO financialDocument) throws Exception;
	boolean saveDocumentExpense(FinancialDocumentDTO financialDocument) throws Exception;
	boolean rollbackMovement(FinancialDocumentDTO financialDocument) throws Exception;
}