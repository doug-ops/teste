package com.manager.systems.common.service.movement.product;

import java.util.List;

import com.manager.systems.common.dto.CompanyLastMovementExecutedDTO;
import com.manager.systems.common.dto.movement.company.MovementCompany;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;

public interface MovementProductService 
{
	void get(MovementProductDTO movementProduct) throws Exception;
	boolean save(MovementProductDTO movementProduct) throws Exception;
	void getMovementsCompany(MovementCompany movementCompany) throws Exception;
	void previewProcessMovement(PreviewMovementCompanyDTO movementCompany) throws Exception;
	long processMovement(PreviewMovementCompanyDTO movementCompany) throws Exception;
	PreviewMovementCompanyDTO reportCompanyMovement(long documentParentId, long companyId) throws Exception;
	byte[] generatePDFMovementCompany(PreviewMovementCompanyDTO preview) throws Exception;
	boolean saveSynchronize(MovementProductDTO movementProduct) throws Exception;
	List<Long> hasNoMovementProduct(long companyId, long user) throws Exception;
	CompanyLastMovementExecutedDTO companyLastMovementExecuted(long companyId, long user) throws Exception;
}