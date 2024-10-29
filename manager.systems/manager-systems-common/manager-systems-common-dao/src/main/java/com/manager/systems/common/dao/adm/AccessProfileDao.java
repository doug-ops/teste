package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.AccessProfileDTO;
import com.manager.systems.common.dto.adm.ReportAccessProfileDTO;
import com.manager.systems.common.vo.Combobox;

public interface AccessProfileDao 
{
	boolean save(AccessProfileDTO accessProfile) throws Exception;
	boolean inactive(AccessProfileDTO accessProfile) throws Exception;
	void get(AccessProfileDTO accessProfile) throws Exception;
	void getAll(ReportAccessProfileDTO reportAccessProfile) throws Exception;
	List<Combobox> getAllCombobox(ReportAccessProfileDTO reportAccessProfile) throws Exception;
	boolean saveConfig(AccessProfileDTO accessProfile) throws Exception;
}