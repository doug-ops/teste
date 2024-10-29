/**Create Date 08/09/2022*/
package com.manager.systems.web.movements.dao;

import com.manager.systems.web.movements.dto.dre.DreReportDTO;
import com.manager.systems.web.movements.dto.dre.DreReportFilterDTO;

public interface DreDao 
{
	DreReportDTO getAllDre(DreReportFilterDTO dre) throws Exception;
}