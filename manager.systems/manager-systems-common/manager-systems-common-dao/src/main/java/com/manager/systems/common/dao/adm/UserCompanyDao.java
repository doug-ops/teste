package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.UserCompnayDTO;

public interface UserCompanyDao 
{
	boolean save(UserCompnayDTO userCompnay) throws Exception;
	boolean inactive(UserCompnayDTO userCompnay) throws Exception;
	List<CompanyDTO> get(long userId) throws Exception;
	List<CompanyDTO> getDataMovementExecutionCompany(String usersId) throws Exception;
	List<UserCompnayDTO> getUsersCompany(long companyId) throws Exception;
}