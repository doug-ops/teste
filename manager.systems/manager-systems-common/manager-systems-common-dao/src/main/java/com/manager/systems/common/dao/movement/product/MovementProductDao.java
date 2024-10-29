package com.manager.systems.common.dao.movement.product;

import java.util.List;

import com.manager.systems.common.dto.CompanyLastMovementExecutedDTO;
import com.manager.systems.common.dto.movement.company.MovementCompany;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;

public interface MovementProductDao 
{
	
	void get(MovementProductDTO movementProduct) throws Exception;
	boolean save(MovementProductDTO movementProduct) throws Exception;
	void getMovementsCompany(MovementCompany movementCompany) throws Exception;
	void previewProcessMovement(PreviewMovementCompanyDTO movementCompany) throws Exception;
	long processMovement(PreviewMovementCompanyDTO movementCompany) throws Exception;
	PreviewMovementCompanyDTO reportCompanyMovement(Long documentParentId, Long companyId) throws Exception;
	boolean saveSynchronize(MovementProductDTO movementProduct) throws Exception;
	List<Long> hasNoMovementProduct(long companyId, long user) throws Exception;
	CompanyLastMovementExecutedDTO companyLastMovementExecuted(long companyId, long user) throws Exception;
}