package com.manager.systems.common.service.adm;

import java.util.List;
import java.util.Map;

import com.manager.systems.common.dto.adm.PersonBankAccountDTO;
import com.manager.systems.common.dto.adm.PersonBankAccountPermissionDTO;
import com.manager.systems.common.vo.Combobox;

public interface PersonBankAccountService
{
	boolean save(PersonBankAccountDTO item) throws Exception;
	boolean delete(PersonBankAccountDTO item) throws Exception;
	List<PersonBankAccountDTO> getAll(PersonBankAccountDTO filter) throws Exception;
	List<Combobox> getAllBankAccountByPerson(PersonBankAccountDTO filter) throws Exception;
	Map<Long, List<Combobox>> getAllBankAccountByCompanys(String companys) throws Exception;
	boolean saveBankAccountPermission(PersonBankAccountPermissionDTO item) throws Exception;
	boolean inactiveAllBankAccountPermission(PersonBankAccountPermissionDTO item) throws Exception;
	List<PersonBankAccountPermissionDTO> getAllBankAccountPermissionsByUser(long userId) throws Exception;
}