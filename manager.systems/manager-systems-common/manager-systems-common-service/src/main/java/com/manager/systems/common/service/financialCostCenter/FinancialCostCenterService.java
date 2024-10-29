package com.manager.systems.common.service.financialCostCenter;

import java.util.List;

import com.manager.systems.common.dto.financialCostCenter.FinancialCostCenterDTO;
import com.manager.systems.common.vo.Combobox;

public interface FinancialCostCenterService 
{
	List<Combobox> getAllCombobox() throws Exception;
	Integer save(FinancialCostCenterDTO financialCostCenter) throws Exception;
}