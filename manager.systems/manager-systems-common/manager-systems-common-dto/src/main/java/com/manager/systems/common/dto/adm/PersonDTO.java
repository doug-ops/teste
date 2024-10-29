package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ObjectType;

public class PersonDTO implements Serializable {
	private static final long serialVersionUID = 6168049158997855242L;

	private String personId;
	private String name;
	private String aliasName;
	private String personType;
	private String objectType;
	private String cpfCnpj;
	private String rg;
	private String ie;
	private boolean inactive;
	private String email;
	private String site;
	private String addressZipCode;
	private String addressZipCodeSide;
	private String addressStreet;
	private String addressNumber;
	private String addressComplement;
	private String addressDistrict;
	private String addressCityIbge;
	private String addressCityDescription;
	private String addressStateIbge;
	private String addressStateDescription;
	private String addressCountryIbge;
	private String addressGia;
	private long userChange;
	private String userChangeName;
	private long userCreate;
	private String userCreateName;
	private Calendar creationDate;
	private Calendar changeDate;
	private String creationDateString;
	private String changeDateString;
	private int accessProfileId;
	private String accessDataUser;
	private String accessDataPassword;
	private Calendar expirationDate;
	private Calendar lastAccessDate;
	private boolean negativeClose;
	private boolean movementAutomatic;
	private long bankAccount;
	private String bankAccountDescription;
	private long creditProvider;
	private String creditProviderDescription;
	private long debitProvider;
	private String debitProviderDescription;
	private String financialGroup;
	private String financialGroupDescription;
	private String financialSubGroup;
	private String financialSubGroupDescription;
	private int registerType;
	private ReportPersonDTO reportPerson;
	private List<CompanyDTO> companys = new ArrayList<CompanyDTO>();
	private List<PersonBankAccountDTO> bankAccountOthers = new ArrayList<PersonBankAccountDTO>();
	private List<IntegrationSystemsDTO> integrationSystemsValues = new ArrayList<IntegrationSystemsDTO>();
	private List<UserDTO> users = new ArrayList<UserDTO>();
	private List<UserCompnayDTO> usersCompany = new ArrayList<UserCompnayDTO>();
	private String userLastAcessDateString;
	private Calendar userExitAcessDate;
	private String userExitAcessDateString;
	private int personTypeId;
	private int establishmentTypeId;
	private boolean blocked;
	private long mainCompanyId;
	private String personTypeDescription;
	private String establishmentTypeDescription;
	private int countProducts;
	private int clientId;
	private int qtdeCompanys;
	private String attachedUsers;
	private List<PersonBankAccountPermissionDTO> banksAccountCompanysPermission;
	private double cashClosingMaxDiscount;
	private int financialCostCenterId;
	
	public PersonDTO() {
		super();
	}

	public final String getPersonId() {
		return this.personId;
	}

	public final void setPersonId(final String personId) {
		this.personId = personId;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getAliasName() {
		return this.aliasName;
	}

	public final void setAliasName(final String aliasName) {
		this.aliasName = aliasName;
	}

	public final String getPersonType() {
		return this.personType;
	}

	public final void setPersonType(final String personType) {
		this.personType = personType;
	}

	public final String getObjectType() {
		return this.objectType;
	}

	public final void setObjectType(final String objectType) {
		this.objectType = objectType;
	}

	public final String getCpfCnpj() {
		return this.cpfCnpj;
	}

	public final void setCpfCnpj(String cpfCnpj) {
		if (!StringUtils.isNull(cpfCnpj)) {
			cpfCnpj = StringUtils.replaceNonDigits(cpfCnpj);
		}
		this.cpfCnpj = cpfCnpj;
	}

	public final String getRg() {
		return this.rg;
	}

	public final void setRg(final String rg) {
		this.rg = rg;
	}

	public final String getIe() {
		return this.ie;
	}

	public final void setIe(final String ie) {
		this.ie = ie;
	}

	public final boolean isInactive() {
		return this.inactive;
	}

	public final int getInactive() {
		return this.inactive ? 0 : 1;
	}

	public final void setInactive(final boolean inactive) {
		this.inactive = inactive;
	}

	public final String getEmail() {
		return this.email;
	}

	public final void setEmail(final String email) {
		this.email = email;
	}

	public final String getAddressZipCode() {
		return this.addressZipCode;
	}

	public final void setAddressZipCode(String addressZipCode) {
		if (!StringUtils.isNull(addressZipCode)) {
			addressZipCode = StringUtils.replaceNonDigits(addressZipCode);
		}
		this.addressZipCode = addressZipCode;
	}

	public final String getAddressZipCodeSide() {
		return this.addressZipCodeSide;
	}

	public final void setAddressZipCodeSide(final String addressZipCodeSide) {
		if (StringUtils.isNull(addressZipCodeSide)) {
			this.addressZipCodeSide = "A";
		} else if (addressZipCodeSide.length() > 1) {
			this.addressZipCodeSide = ConstantDataManager.BLANK;
		} else {
			this.addressZipCodeSide = addressZipCodeSide;
		}
	}

	public final String getAddressStreet() {
		return this.addressStreet;
	}

	public final void setAddressStreet(final String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public final String getAddressNumber() {
		return this.addressNumber;
	}

	public final void setAddressNumber(final String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public final String getAddressComplement() {
		return this.addressComplement;
	}

	public final void setAddressComplement(final String addressComplement) {
		this.addressComplement = addressComplement;
	}

	public final String getAddressDistrict() {
		return this.addressDistrict;
	}

	public final void setAddressDistrict(final String addressDistrict) {
		this.addressDistrict = addressDistrict;
	}

	public final String getAddressCityIbge() {
		return this.addressCityIbge;
	}

	public final void setAddressCityIbge(final String addressCityIbge) {
		this.addressCityIbge = addressCityIbge;
	}

	public final String getAddressCityDescription() {
		return this.addressCityDescription;
	}

	public final void setAddressCityDescription(final String addressCityDescription) {
		this.addressCityDescription = addressCityDescription;
	}

	public final String getAddressStateIbge() {
		return this.addressStateIbge;
	}

	public final void setAddressStateIbge(final String addressStateIbge) {
		this.addressStateIbge = addressStateIbge;
	}

	public final String getAddressStateDescription() {
		return this.addressStateDescription;
	}

	public final void setAddressStateDescription(final String addressStateDescription) {
		this.addressStateDescription = addressStateDescription;
	}

	public final String getAddressCountryIbge() {
		return this.addressCountryIbge;
	}

	public final void setAddressCountryIbge(final String addressCountryIbge) {
		this.addressCountryIbge = addressCountryIbge;
	}

	public final String getAddressGia() {
		return this.addressGia;
	}

	public final void setAddressGia(final String addressGia) {
		this.addressGia = addressGia;
	}

	public final long getUserCreate() {
		return this.userCreate;
	}

	public final void setUserCreate(final long userCreate) {
		this.userCreate = userCreate;
	}

	public final String getUserCreateName() {
		return this.userCreateName;
	}

	public final void seUserCreatetName(final String userCreateName) {
		this.userCreateName = userCreateName;
	}

	public final long getUserChange() {
		return this.userChange;
	}

	public final void setUserChange(final long userChange) {
		this.userChange = userChange;
	}

	public final String getUserChangeName() {
		return this.userChangeName;
	}

	public final void setUserChangeName(final String userChangeName) {
		this.userChangeName = userChangeName;
	}

	public final Calendar getChangeDate() {
		return this.changeDate;
	}

	public final void setChangeDate(final Calendar changeDate) {
		this.changeDate = changeDate;
		if (this.changeDate != null) {
			this.changeDateString = StringUtils.formatDate(this.changeDate.getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS);
		}
	}

	public final int getAccessProfileId() {
		return this.accessProfileId;
	}

	public final void setAccessProfileId(final int accessProfileId) {
		this.accessProfileId = accessProfileId;
	}

	public final boolean isNegativeClose() {
		return this.negativeClose;
	}

	public final void setNegativeClose(final boolean negativeClose) {
		this.negativeClose = negativeClose;
	}

	public boolean isMovementAutomatic() {
		return this.movementAutomatic;
	}

	public void setMovementAutomatic(final boolean movementAutomatic) {
		this.movementAutomatic = movementAutomatic;
	}

	public final ReportPersonDTO getReportPerson() {
		return this.reportPerson;
	}

	public final void setReportPerson(final ReportPersonDTO reportPerson) {
		this.reportPerson = reportPerson;
	}

	public final void addReportItem(final PersonDTO item) {
		this.getReportPerson().addItem(item);
	}

	public final String getAccessDataUser() {
		return this.accessDataUser;
	}

	public final void setAccessDataUser(final String accessDataUser) {
		this.accessDataUser = accessDataUser;
	}

	public final String getAccessDataPassword() {
		return this.accessDataPassword;
	}

	public final void setAccessDataPassword(final String accessDataPassword) {
		this.accessDataPassword = accessDataPassword;
	}

	public final Calendar getExpirationDate() {
		return this.expirationDate;
	}

	public final void setExpirationDate(final Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	public final Calendar getLastAccessDate() {
		return this.lastAccessDate;
	}

	public final void setLastAccessDate(final Calendar lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
		this.userLastAcessDateString = StringUtils.formatDate(this.lastAccessDate.getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS);
	}
	
	public final Calendar getUserExitAcessDate() {
		return this.userExitAcessDate;
	}

	public final  void setUserExitAcessDate(final Calendar userExitAcessDate) {
		this.userExitAcessDate = userExitAcessDate;
		if(userExitAcessDate == null) {
			this.userExitAcessDateString = "-";

		}
		this.userExitAcessDateString = StringUtils.formatDate(this.userExitAcessDate.getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM);
		
	}

	public final String getSite() {
		return this.site;
	}

	public final void setSite(final String site) {
		this.site = site;
	}

	public final Calendar getCreationDate() {
		return this.creationDate;
	}

	public final void setCreationDate(final Calendar creationDate) {
		this.creationDate = creationDate;
		if (this.creationDate != null) {
			this.creationDateString = StringUtils.formatDate(this.creationDate.getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS);
		}
	}

	public final String getCreationDateString() {
		return this.creationDateString;
	}

	public final void setCreationDateString(final String creationDateString) {
		this.creationDateString = creationDateString;
	}

	public final String getChangeDateString() {
		return this.changeDateString;
	}

	public final void setChangeDateString(final String changeDateString) {
		this.changeDateString = changeDateString;
	}

	public final void setUserCreateName(final String userCreateName) {
		this.userCreateName = userCreateName;
	}

	public final long getBankAccount() {
		return this.bankAccount;
	}

	public final void setBankAccount(final long bankAccount) {
		this.bankAccount = bankAccount;
	}

	public final String getBankAccountDescription() {
		return this.bankAccountDescription;
	}

	public final void setBankAccountDescription(final String bankAccountDescription) {
		this.bankAccountDescription = bankAccountDescription;
	}

	public long getCreditProvider() {
		return this.creditProvider;
	}

	public void setCreditProvider(final long creditProvider) {
		this.creditProvider = creditProvider;
	}

	public String getCreditProviderDescription() {
		return this.creditProviderDescription;
	}

	public void setCreditProviderDescription(final String creditProviderDescription) {
		this.creditProviderDescription = creditProviderDescription;
	}

	public long getDebitProvider() {
		return this.debitProvider;
	}

	public void setDebitProvider(final long debitProvider) {
		this.debitProvider = debitProvider;
	}

	public String getDebitProviderDescription() {
		return this.debitProviderDescription;
	}

	public void setDebitProviderDescription(final String debitProviderDescription) {
		this.debitProviderDescription = debitProviderDescription;
	}

	public final List<IntegrationSystemsDTO> getIntegrationSystemsValues() {
		return this.integrationSystemsValues;
	}

	public final String getFinancialGroup() {
		return this.financialGroup;
	}

	public final void setFinancialGroup(final String financialGroup) {
		this.financialGroup = financialGroup;
	}

	public final String getFinancialGroupDescription() {
		return this.financialGroupDescription;
	}

	public final void setFinancialGroupDescription(final String financialGroupDescription) {
		this.financialGroupDescription = financialGroupDescription;
	}

	public final String getFinancialSubGroup() {
		return this.financialSubGroup;
	}

	public final void setFinancialSubGroup(final String financialSubGroup) {
		this.financialSubGroup = financialSubGroup;
	}

	public final String getFinancialSubGroupDescription() {
		return this.financialSubGroupDescription;
	}

	public final void setFinancialSubGroupDescription(final String financialSubGroupDescription) {
		this.financialSubGroupDescription = financialSubGroupDescription;
	}

	public final List<CompanyDTO> getCompanys() {
		return this.companys;
	}

	public final void addCompnay(final CompanyDTO company) {
		this.companys.add(company);
	}

	public final void setCompanys(final List<CompanyDTO> companys) {
		this.companys = companys;
	}

	public final List<PersonBankAccountDTO> getBankAccountOthers() {
		return this.bankAccountOthers;
	}
	
	public final void addBankAccountOthers(final PersonBankAccountDTO item)
	{
		this.bankAccountOthers.add(item);
	}

	public final void setBankAccountOthers(final List<PersonBankAccountDTO> bankAccountOthers) {
		this.bankAccountOthers = bankAccountOthers;
	}

	public final int getRegisterType() {
		return this.registerType;
	}

	public final void setRegisterType(final int registerType) {
		this.registerType = registerType;
	}
	
	public final String getUserLastAcessDateString() {
		return this.userLastAcessDateString;
	}
	
	public final  void setUserLastAcessDateString(final String userLastAcessDateString) {
		this.userLastAcessDateString = userLastAcessDateString;
	}	
	public final String getUserExitAcessDateString() {
		return this.userExitAcessDateString;
	}
	
	public final  void setUserExitAcessDateString(final String userExitAcessDateString) {
		this.userExitAcessDateString = userExitAcessDateString;
	}

	public final int getPersonTypeId() {
		return this.personTypeId;
	}

	public final void setPersonTypeId(final int personTypeId) {
		this.personTypeId = personTypeId;
	}

	public final int getEstablishmentTypeId() {
		return this.establishmentTypeId;
	}

	public final void setEstablishmentTypeId(final int establishmentTypeId) {
		this.establishmentTypeId = establishmentTypeId;
	}
	
	public final List<UserDTO> getUsers() {
		return this.users;
	}

	public final void addUserParent(final UserDTO user) {
		this.users.add(user);
	}

	public final void setUsers(final List<UserDTO> users) {
		this.users = users;
	}

	public final int getBlocked() {
		return this.blocked ? 1 : 0;
	}

	public final void setBlocked(final boolean blocked) {
		this.blocked = blocked;
	}

	public final long getMainCompanyId() {
		return this.mainCompanyId;
	}

	public final void setMainCompanyId(final long mainCompanyId) {
		this.mainCompanyId = mainCompanyId;
	}

	public final String getPersonTypeDescription() {
		return this.personTypeDescription;
	}

	public final void setPersonTypeDescription(final String personTypeDescription) {
		this.personTypeDescription = personTypeDescription;
	}

	public final String getEstablishmentTypeDescription() {
		return this.establishmentTypeDescription;
	}

	public final void setEstablishmentTypeDescription(final String establishmentTypeDescription) {
		this.establishmentTypeDescription = establishmentTypeDescription;
	}

	public final int getCountProducts() {
		return this.countProducts;
	}

	public final void setCountProducts(final int countProducts) {
		this.countProducts = countProducts;
	}

	public void setIntegrationSystemsValues(List<IntegrationSystemsDTO> integrationSystemsValues) {
		this.integrationSystemsValues = integrationSystemsValues;
	}
	
	public final List<UserCompnayDTO> getUsersCompany() {
		return this.usersCompany;
	}

	public final void setUsersCompany(final List<UserCompnayDTO> usersCompany) {
		this.usersCompany = usersCompany;
	}
	
	public final void addUserCompany(final UserCompnayDTO userCompnay) {
		this.usersCompany.add(userCompnay);
	}

	public final int getClientId() {
		return this.clientId;
	}

	public final void setClientId(final int clientId) {
		this.clientId = clientId;
	}

	public final int getQtdeCompanys() {
		return this.qtdeCompanys;
	}

	public final void setQtdeCompanys(final int qtdeCompanys) {
		this.qtdeCompanys = qtdeCompanys;
	}

	public final String getAttachedUsers() {
		return this.attachedUsers;
	}

	public final void setAttachedUsers(final String attachedUsers) {
		this.attachedUsers = attachedUsers;
	}
	

	public final List<PersonBankAccountPermissionDTO> getBanksAccountCompanysPermission() {
		return this.banksAccountCompanysPermission;
	}

	public final void setBanksAccountCompanysPermission(final List<PersonBankAccountPermissionDTO> banksAccountCompanysPermission) {
		this.banksAccountCompanysPermission = banksAccountCompanysPermission;
	}
	
    public final void addPersonBankAccountPermission(final PersonBankAccountPermissionDTO permisson) {
    	if(this.banksAccountCompanysPermission == null) {
    		this.banksAccountCompanysPermission = new ArrayList<>();
    	}
    	
    	this.banksAccountCompanysPermission.add(permisson);
    }

	public final double getCashClosingMaxDiscount() {
		return this.cashClosingMaxDiscount;
	}

	public final void setCashClosingMaxDiscount(final double cashClosingMaxDiscount) {
		this.cashClosingMaxDiscount = cashClosingMaxDiscount;
	}
	
	public final int getFinancialCostCenterId() {
		return this.financialCostCenterId;
	}

	public final void setFinancialCostCenterId(final int financialCostCenterId) {
		this.financialCostCenterId = financialCostCenterId;
	}

	public final List<UserCompnayDTO> getUserCompanys() {
		final List<UserCompnayDTO> itens = new ArrayList<UserCompnayDTO>();
		for (final CompanyDTO company : this.companys) {
			final UserCompnayDTO item = new UserCompnayDTO();
			item.setUserId(Long.valueOf(this.personId));
			item.setCompanyId(company.getId());
			item.setInactive(false);
			item.setUserChange(this.userChange);
			itens.add(item);
		}
		return itens;
	}
	
	public final List<UserCompnayDTO> getUsersCompanys() {
		final List<UserCompnayDTO> itens = new ArrayList<UserCompnayDTO>();
		for (final UserCompnayDTO user : this.usersCompany) {
			final UserCompnayDTO item = new UserCompnayDTO();
			item.setUserId(user.getUserId());
			item.setCompanyId(user.getCompanyId());
			item.setInactive(false);
			item.setUserChange(this.userChange);
			itens.add(item);
		}
		return itens;
	}
	
	public final List<UserParentDTO> getUserParents() {
		final List<UserParentDTO> itens = new ArrayList<UserParentDTO>();
		for (final UserDTO user : this.users) {
			final UserParentDTO item = new UserParentDTO();
			item.setUserParentId(user.getUserOperation());
			item.setUserId(user.getId());
			item.setInactive(false);
			item.setUserChange(this.userChange);
			itens.add(item);
		}
		return itens;
	}
	
	public final void addIntegrationSystemsValue(final String legacyId, final String integrationSystemId) {
		final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
		integrationSystem.setObjectId(this.personId);
		integrationSystem.setObjectType(this.objectType);
		integrationSystem.setInactive(false);
		integrationSystem.setIntegrationSystemId(Integer.valueOf(integrationSystemId));
		integrationSystem.setLegacyId(legacyId);
		integrationSystem.setUserChange(this.userChange);
		integrationSystem.setChangeDate(this.changeDate);
		this.integrationSystemsValues.add(integrationSystem);
	}

	public void validateFormDetail() throws Exception {
		ObjectType objectTypeResult = null;
		if (!StringUtils.isNull(this.objectType)) {
			objectTypeResult = ObjectType.valueOfType(objectType);
		}

		if (objectTypeResult == null) {
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_OBJECT_TYPE, this.personId));
		}

		if (!StringUtils.isLong(this.personId)) {
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_ID, this.personId));
		}
	}

	public void validateFormSave(final String objectType) throws Exception {
		ObjectType objectTypeResult = null;
		if (!StringUtils.isNull(this.objectType)) {
			objectTypeResult = ObjectType.valueOfType(objectType);
		}

		if (objectTypeResult == null) {
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_OBJECT_TYPE, this.personId));
		}

		if (!StringUtils.isLong(this.personId)) {
			throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_ID, ConstantDataManager.BLANK));
		} else if (StringUtils.isNull(this.name)) {
			if (ObjectType.COMPANY.getType().equalsIgnoreCase(objectType) || ObjectType.PROVIDER.getType().equalsIgnoreCase(objectType)) {
				throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_SOCIAL_NAME, ConstantDataManager.BLANK));
			} else if (ObjectType.USER.getType().equalsIgnoreCase(objectType)) {
				throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_NAME, this.name));
			}
		} else if (!StringUtils.isNull(this.email)) {
			final String[] emails = this.email.split(ConstantDataManager.PONTO_VIRGULA_STRING);
			for(final String emailItem : emails) {
				final boolean isValid = StringUtils.isValidEmailAddress(emailItem);
				if (!isValid) {
					throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_EMAIL, emailItem));
				}				
			}
		}

		if (ObjectType.USER.getType().equalsIgnoreCase(objectType) && (StringUtils.isNull(this.accessDataUser) || StringUtils.isNull(this.accessDataPassword))) {
			throw new Exception(ConstantDataManager.MESSAGE_INVALID_DATA_ACCESS);
		}

		if (StringUtils.isLong(this.addressZipCode)) {
			if (this.addressZipCode.length() != 8) {
				throw new Exception(String.format(ConstantDataManager.MESSAGE_INVALID_CEP, this.addressZipCode));
			}
		}
	}
}