package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.FinancialSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialSubGroupDTO;
import com.manager.systems.common.vo.Combobox;

public interface FinancialSubGroupService 
{
	boolean save(FinancialSubGroupDTO financialSubGroup) throws Exception;
	boolean inactive(FinancialSubGroupDTO financialSubGroup) throws Exception;
	void get(FinancialSubGroupDTO financialSubGroup) throws Exception;
	void getAll(ReportFinancialSubGroupDTO reportFinancialSubGroup) throws Exception;
	List<Combobox> getAllCombobox(ReportFinancialSubGroupDTO reportFinancialSubGroup) throws Exception;
	boolean inactiveAllByGroup(FinancialSubGroupDTO financialSubGroup) throws Exception;
	void getMaxId(final FinancialSubGroupDTO financialSubGroup) throws Exception;
}