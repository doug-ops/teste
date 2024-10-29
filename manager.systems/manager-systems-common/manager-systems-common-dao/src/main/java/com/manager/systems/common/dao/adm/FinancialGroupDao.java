package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.FinancialGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialGroupDTO;
import com.manager.systems.common.vo.Combobox;

public interface FinancialGroupDao 
{
	boolean save(FinancialGroupDTO financialGroup) throws Exception;
	boolean inactive(FinancialGroupDTO financialGroup) throws Exception;
	void get(FinancialGroupDTO financialGroup) throws Exception;
	void getAll(ReportFinancialGroupDTO reportFinancialGroup) throws Exception;
	List<Combobox> getAllCombobox() throws Exception;
	String getMaxId() throws Exception;
}