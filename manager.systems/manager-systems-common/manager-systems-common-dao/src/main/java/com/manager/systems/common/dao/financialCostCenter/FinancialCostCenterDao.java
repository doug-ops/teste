package com.manager.systems.common.dao.financialCostCenter;

import java.util.List;

import com.manager.systems.common.dto.financialCostCenter.FinancialCostCenterDTO;
import com.manager.systems.common.vo.Combobox;

public interface FinancialCostCenterDao 
{
	List<Combobox> getAllCombobox() throws Exception;
	Integer save(FinancialCostCenterDTO financialCostCenter) throws Exception;
}