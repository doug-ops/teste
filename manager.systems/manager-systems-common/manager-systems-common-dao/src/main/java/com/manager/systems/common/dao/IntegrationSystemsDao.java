package com.manager.systems.common.dao;

import java.util.List;

import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.vo.Combobox;

public interface IntegrationSystemsDao
{
	List<Combobox> getAllCombobox() throws Exception;
	boolean save(IntegrationSystemsDTO integrationSystems) throws Exception;
	boolean delete(IntegrationSystemsDTO integrationSystems) throws Exception;
	List<IntegrationSystemsDTO> getAll(IntegrationSystemsDTO integrationSystems) throws Exception;
}