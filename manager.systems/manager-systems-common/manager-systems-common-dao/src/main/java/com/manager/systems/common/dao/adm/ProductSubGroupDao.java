package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.ProductSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductSubGroupDTO;
import com.manager.systems.common.vo.Combobox;

public interface ProductSubGroupDao 
{
	boolean save(ProductSubGroupDTO productSubGroup) throws Exception;
	boolean inactive(ProductSubGroupDTO productSubGroup) throws Exception;
	void get(ProductSubGroupDTO productSubGroup) throws Exception;
	void getAll(ReportProductSubGroupDTO reportProductSubGroup) throws Exception;
	List<Combobox> getAllCombobox(ReportProductSubGroupDTO reportProductSubGroup) throws Exception;
}