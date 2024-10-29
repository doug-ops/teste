/**Create 01/08/2022*/
package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.ChangeObjectDTO;
import com.manager.systems.common.dto.adm.LogSystemDTO;
import com.manager.systems.common.dto.adm.ReportLogSystemDTO;

public interface LogSystemService 
{
	List<LogSystemDTO> get(ReportLogSystemDTO reportLog) throws Exception;
	List<ChangeObjectDTO> getAll(ReportLogSystemDTO reportLog) throws Exception;
	byte[] generatePDFLogSystem(List<ChangeObjectDTO> changeObject, boolean analitic) throws Exception;
}