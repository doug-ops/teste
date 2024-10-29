package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.UserCompanyDao;
import com.manager.systems.common.dao.impl.adm.UserCompanyDaoImpl;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.UserCompnayDTO;
import com.manager.systems.common.service.adm.UserCompanyService;

public class UserCompanyServiceImpl implements UserCompanyService 
{
	private UserCompanyDao userCompanyDao;
	
	public UserCompanyServiceImpl(final Connection connection) 
	{
		super();
		this.userCompanyDao = new UserCompanyDaoImpl(connection);
	}

	@Override
	public boolean save(final UserCompnayDTO userCompnay) throws Exception 
	{
		return this.userCompanyDao.save(userCompnay);
	}

	@Override
	public boolean inactive(final UserCompnayDTO userCompnay) throws Exception 
	{
		return this.userCompanyDao.inactive(userCompnay);
	}

	@Override
	public List<CompanyDTO> get(final long userId) throws Exception 
	{
		return this.userCompanyDao.get(userId);
	}
	@Override
	public List<CompanyDTO> getDataMovementExecutionCompany(final String usersId) throws Exception 
	{
		return this.userCompanyDao.getDataMovementExecutionCompany(usersId);
	}

	@Override
	public List<UserCompnayDTO> getUsersCompany(final long companyId) throws Exception {
		return this.userCompanyDao.getUsersCompany(companyId);
	}
}