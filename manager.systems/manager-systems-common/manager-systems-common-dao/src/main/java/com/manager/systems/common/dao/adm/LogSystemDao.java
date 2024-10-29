package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.ChangeObjectDTO;
import com.manager.systems.common.dto.adm.LogSystemDTO;
import com.manager.systems.common.dto.adm.ReportLogSystemDTO;

public interface LogSystemDao 
{
	List<LogSystemDTO> get(ReportLogSystemDTO reportLog) throws Exception;
	List<ChangeObjectDTO> getAll(ReportLogSystemDTO reportLog) throws Exception;
}