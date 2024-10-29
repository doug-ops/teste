package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.GameTypeDao;
import com.manager.systems.common.dao.impl.adm.GameTypeDaoImpl;
import com.manager.systems.common.dto.adm.GameTypeDTO;
import com.manager.systems.common.service.adm.GameTypeService;
import com.manager.systems.common.vo.Combobox;

public class GameTypeServiceImpl implements GameTypeService 
{
	private GameTypeDao gameTypeDao;
	
	public GameTypeServiceImpl(final Connection connection) 
	{
		super();
		this.gameTypeDao = new GameTypeDaoImpl(connection);
	}

	@Override

	public List<Combobox> getAllCombobox() throws Exception 
	{
		return this.gameTypeDao.getAllCombobox();
	}
	
	@Override
	public Integer save(final GameTypeDTO gameType) throws Exception 
	{
		return this.gameTypeDao.save(gameType);
	}
}