package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.AccessProfileDao;
import com.manager.systems.common.dao.impl.adm.AccessProfileDaoImpl;
import com.manager.systems.common.dto.adm.AccessProfileDTO;
import com.manager.systems.common.dto.adm.ReportAccessProfileDTO;
import com.manager.systems.common.service.adm.AccessProfileService;
import com.manager.systems.common.vo.Combobox;

public class AccessProfileServiceImpl implements AccessProfileService 
{
	private AccessProfileDao accessProfileDao;
	
	public AccessProfileServiceImpl(final Connection connection) 
	{
		super();
		this.accessProfileDao = new AccessProfileDaoImpl(connection);
	}

	@Override
	public boolean save(final AccessProfileDTO accessProfile) throws Exception 
	{
		return this.accessProfileDao.save(accessProfile);
	}

	@Override
	public boolean inactive(final AccessProfileDTO accessProfile) throws Exception 
	{
		return this.accessProfileDao.inactive(accessProfile);
	}

	@Override
	public void get(final AccessProfileDTO accessProfile) throws Exception 
	{
		this.accessProfileDao.get(accessProfile);
	}

	@Override
	public void getAll(final ReportAccessProfileDTO reportAccessProfile) throws Exception 
	{
		this.accessProfileDao.getAll(reportAccessProfile);
	}

	@Override
	public List<Combobox> getAllCombobox(ReportAccessProfileDTO reportAccessProfile) throws Exception
	{
		return this.accessProfileDao.getAllCombobox(reportAccessProfile);
	}

	@Override
	public boolean saveConfig(final AccessProfileDTO accessProfile) throws Exception {
		return this.accessProfileDao.saveConfig(accessProfile);
	}
}