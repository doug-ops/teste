/**Create Date 08/09/2022*/
package com.manager.systems.web.movements.service;

import com.manager.systems.web.movements.dto.dre.DreReportDTO;
import com.manager.systems.web.movements.dto.dre.DreReportFilterDTO;

public interface DreService 
{
	DreReportDTO getAllDre(DreReportFilterDTO dre) throws Exception;
	byte[] generatePDFDre(DreReportDTO dre) throws Exception;

}