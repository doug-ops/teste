package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.manager.systems.common.dao.adm.PersonBankAccountDao;
import com.manager.systems.common.dao.impl.adm.PersonBankAccountDaoImpl;
import com.manager.systems.common.dto.adm.PersonBankAccountDTO;
import com.manager.systems.common.dto.adm.PersonBankAccountPermissionDTO;
import com.manager.systems.common.service.adm.PersonBankAccountService;
import com.manager.systems.common.vo.Combobox;

public class PersonBankAccountServiceImpl implements PersonBankAccountService {

	private PersonBankAccountDao personBankAccountDao;

	public PersonBankAccountServiceImpl(final Connection connection) {
		super();
		this.personBankAccountDao = new PersonBankAccountDaoImpl(connection);
	}

	@Override
	public boolean save(final PersonBankAccountDTO item) throws Exception {
		return this.personBankAccountDao.save(item);
	}

	@Override
	public boolean delete(final PersonBankAccountDTO item) throws Exception {
		return this.personBankAccountDao.delete(item);
	}

	@Override
	public List<PersonBankAccountDTO> getAll(final PersonBankAccountDTO filter) throws Exception {
		return this.personBankAccountDao.getAll(filter);
	}

	@Override
	public List<Combobox> getAllBankAccountByPerson(final PersonBankAccountDTO filter) throws Exception {
		return this.personBankAccountDao.getAllBankAccountByPerson(filter);
	}

	@Override
	public Map<Long, List<Combobox>> getAllBankAccountByCompanys(final String companys) throws Exception {
		return this.personBankAccountDao.getAllBankAccountByCompanys(companys);
	}

	@Override
	public boolean saveBankAccountPermission(final PersonBankAccountPermissionDTO item) throws Exception {
		return this.personBankAccountDao.saveBankAccountPermission(item);
	}

	@Override
	public boolean inactiveAllBankAccountPermission(final PersonBankAccountPermissionDTO item) throws Exception {
		return this.personBankAccountDao.inactiveAllBankAccountPermission(item);
	}

	@Override
	public List<PersonBankAccountPermissionDTO> getAllBankAccountPermissionsByUser(final long userId) throws Exception {
		return this.personBankAccountDao.getAllBankAccountPermissionsByUser(userId);
	}
}