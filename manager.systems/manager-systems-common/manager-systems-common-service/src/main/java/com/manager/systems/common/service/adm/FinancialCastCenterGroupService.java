package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.FinancialCastCenterGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialCastCenterGroupDTO;
import com.manager.systems.common.vo.Combobox;

public interface FinancialCastCenterGroupService 
{
	boolean save(FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception;
	boolean inactive(FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception;
	void get(FinancialCastCenterGroupDTO FinancialCastCenterGroup) throws Exception;
	void getAll(ReportFinancialCastCenterGroupDTO reportFinancialCastCenterGroup) throws Exception;
	List<Combobox> getAllCombobox(ReportFinancialCastCenterGroupDTO reportFinancialCastCenterGroup) throws Exception;
}