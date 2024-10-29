package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.PersonDao;
import com.manager.systems.common.dao.impl.adm.PersonDaoImpl;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.PersonBankAccountDTO;
import com.manager.systems.common.dto.adm.PersonBankAccountPermissionDTO;
import com.manager.systems.common.dto.adm.PersonDTO;
import com.manager.systems.common.dto.adm.ReportPersonDTO;
import com.manager.systems.common.dto.adm.UserCompnayDTO;
import com.manager.systems.common.dto.adm.UserDTO;
import com.manager.systems.common.dto.adm.UserParentDTO;
import com.manager.systems.common.service.adm.PersonBankAccountService;
import com.manager.systems.common.service.adm.PersonService;
import com.manager.systems.common.service.adm.UserCompanyService;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.adm.UserService;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;

public class PersonServiceImpl implements PersonService {
	private PersonDao personDao;
	private UserCompanyService userCompanyService;
	private PersonBankAccountService personBankAccountService;
	private UserParentService userParentService;
	private UserService userService;

	public PersonServiceImpl(final Connection connection) {
		super();
		this.personDao = new PersonDaoImpl(connection);
		this.userCompanyService = new UserCompanyServiceImpl(connection);
		this.personBankAccountService = new PersonBankAccountServiceImpl(connection);
		this.userParentService = new UserParentServiceImpl(connection);
		this.userService = new UserServiceImpl(connection);
	}

	@Override
	public boolean save(final PersonDTO person) throws Exception {
		boolean isSaved = this.personDao.save(person);
		if (isSaved) {
			final long personId = Long.parseLong(person.getPersonId());
			if(ObjectType.USER.getType().equalsIgnoreCase(person.getObjectType())) {
				final UserCompnayDTO userCompnay = new UserCompnayDTO();
				userCompnay.setUserId(personId);
				userCompnay.setInactive(true);
				userCompnay.setUserChange(person.getUserChange());
				this.userCompanyService.inactive(userCompnay);

				for (final UserCompnayDTO item : person.getUserCompanys()) {
					this.userCompanyService.save(item);
				}
				
				final UserParentDTO userParent = new UserParentDTO();
				userParent.setUserParentId(personId);
				userParent.setInactive(true);
				userParent.setUserChange(person.getUserChange());				
				this.userParentService.inactive(userParent);
				
				for (final UserParentDTO item : person.getUserParents()) {
					this.userParentService.save(item);
				}				
				
				this.personBankAccountService.inactiveAllBankAccountPermission(PersonBankAccountPermissionDTO.builder().userId(personId).userChange(person.getUserChange()).inactive(true).build());
				if(person.getBanksAccountCompanysPermission() != null && person.getBanksAccountCompanysPermission().size() > 0) {
					for (final PersonBankAccountPermissionDTO item : person.getBanksAccountCompanysPermission()) {
						this.personBankAccountService.saveBankAccountPermission(item);
					}					
				}
				
				final User user = new User();
				user.setId(personId);
				user.setCashClosingMaxDiscount(person.getCashClosingMaxDiscount());
				this.userService.saveConfigUser(user);
				
			}
			else if(ObjectType.COMPANY.getType().equalsIgnoreCase(person.getObjectType()))
			{				
				final PersonBankAccountDTO bank = new PersonBankAccountDTO();
				bank.setObjectId(personId);
				bank.setObjectType(person.getObjectType());
				bank.setInactive(true);
				bank.setUserChange(person.getUserChange());
				this.personBankAccountService.delete(bank);

				for (final PersonBankAccountDTO item : person.getBankAccountOthers()) {
					item.setObjectId(personId);
					item.setObjectType(person.getObjectType());
					this.personBankAccountService.save(item);
				}
				
				final UserCompnayDTO userCompnay = new UserCompnayDTO();
				userCompnay.setCompanyId(personId);
				userCompnay.setInactive(true);
				userCompnay.setUserChange(person.getUserChange());
				this.userCompanyService.inactive(userCompnay);
				
				for (final UserCompnayDTO item : person.getUsersCompanys()) {
					this.userCompanyService.save(item);
				}
			}
		}
		return isSaved;
	}

	@Override
	public boolean inactive(final PersonDTO person) throws Exception {
		return this.personDao.inactive(person);
	}

	@Override
	public void get(final PersonDTO person) throws Exception {
		this.personDao.get(person);
		final long personId = Long.parseLong(person.getPersonId());
		if(ObjectType.USER.getType().equalsIgnoreCase(person.getObjectType())) {
			final List<CompanyDTO> companys = this.userCompanyService.get(personId);
			person.setCompanys(companys);			
			
			final List<UserDTO> users = this.userParentService.get(personId);
			person.setUsers(users);
			
			person.setBanksAccountCompanysPermission(this.personBankAccountService.getAllBankAccountPermissionsByUser(personId));
		}
		
		if(ObjectType.COMPANY.getType().equalsIgnoreCase(person.getObjectType())) {
			final PersonBankAccountDTO bankFilter = new PersonBankAccountDTO();
			bankFilter.setObjectId(personId);
			bankFilter.setObjectType(person.getObjectType());
			bankFilter.setInactive(false);
			final List<PersonBankAccountDTO> bankAccountOthers = this.personBankAccountService.getAll(bankFilter);
			person.setBankAccountOthers(bankAccountOthers);	
			
			final List<UserCompnayDTO> usersCompany = this.userCompanyService.getUsersCompany(personId);
			person.setUsersCompany(usersCompany);		
		}
	}

	@Override
	public void getAll(final ReportPersonDTO reportPerson) throws Exception {
		this.personDao.getAll(reportPerson);
	}

	@Override
	public List<Combobox> getAllAutocomplete(final ReportPersonDTO reportPerson) throws Exception {
		return this.personDao.getAllAutocomplete(reportPerson);
	}

	@Override
	public List<Combobox> getAllCombobox(final ReportPersonDTO reportPerson) throws Exception {
		return this.personDao.getAllCombobox(reportPerson);
	}

	@Override
	public void getAllReport(final ReportPersonDTO reportPerson) throws Exception {
		this.personDao.getAllReport(reportPerson);
	}
}