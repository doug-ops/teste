package com.manager.systems.common.dao.adm;

import java.util.List;
import com.manager.systems.common.dto.adm.FinancialSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialSubGroupDTO;
import com.manager.systems.common.vo.Combobox;

public interface FinancialSubGroupDao 
{
	boolean save(FinancialSubGroupDTO financialSubGroup) throws Exception;
	boolean inactive(FinancialSubGroupDTO financialSubGroup) throws Exception;
	void get(FinancialSubGroupDTO financialSubGroup) throws Exception;
	void getAll(ReportFinancialSubGroupDTO reportFinancialSubGroup) throws Exception;
	public List<Combobox> getAllCombobox(final ReportFinancialSubGroupDTO reportFinancialSubGroup) throws Exception;
	boolean inactiveAllByGroup(FinancialSubGroupDTO financialSubGroup) throws Exception;
	void getMaxId(final FinancialSubGroupDTO financialSubGroup) throws Exception;
}