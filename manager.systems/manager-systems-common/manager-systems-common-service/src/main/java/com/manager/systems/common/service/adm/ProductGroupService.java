package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.ProductGroupDTO;
import com.manager.systems.common.dto.adm.ReportProductGroupDTO;
import com.manager.systems.common.vo.Combobox;

public interface ProductGroupService 
{
	boolean save(ProductGroupDTO productGroup) throws Exception;
	boolean inactive(ProductGroupDTO productGroup) throws Exception;
	void get(ProductGroupDTO productGroup) throws Exception;
	void getAll(ReportProductGroupDTO reportProductGroup) throws Exception;
	List<Combobox> getAllCombobox(ReportProductGroupDTO reportProductGroup) throws Exception;
}