/**.
 * Date Create 23/08/2019
 */

package com.manager.systems.common.service.impl.adm;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.common.dao.adm.BankAccountDao;
import com.manager.systems.common.dao.impl.adm.BankAccountDaoImpl;
import com.manager.systems.common.dto.adm.BankAccountDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.vo.Combobox;

public class BankAccountServiceImpl implements BankAccountService 
{
	private BankAccountDao bankAccountDao;
	
	public BankAccountServiceImpl(final Connection connection) 
	{
		super();
		this.bankAccountDao = new BankAccountDaoImpl(connection);
	}
	
	@Override
	public boolean save(final BankAccountDTO bankAccount) throws Exception 
	{
		return this.bankAccountDao.save(bankAccount);
	}

	@Override
	public boolean inactive(final BankAccountDTO bankAccount) throws Exception 
	{
		return this.bankAccountDao.inactive(bankAccount);
	}

	@Override
	public void get(final BankAccountDTO bankAccount) throws Exception 
	{
		this.bankAccountDao.get(bankAccount);
	}

	@Override
	public void getAll(final ReportBankAccountDTO reportBankAccount) throws Exception 
	{
		this.bankAccountDao.getAll(reportBankAccount);
	}

	@Override
	public List<Combobox> getAllCombobox(final ReportBankAccountDTO reportBankAccount) throws Exception 
	{
		return this.bankAccountDao.getAllCombobox(reportBankAccount);
	}

	@Override
	public int getCompanyIdByBankAccount(final int bankAccountId) throws Exception 
	{
		return this.bankAccountDao.getCompanyIdByBankAccount(bankAccountId);
	}

	@Override
	public List<Combobox> getAllAutocomplete(final ReportBankAccountDTO reportBankAccount) throws Exception 
	{
		return this.bankAccountDao.getAllAutocomplete(reportBankAccount);
	}

	@Override
	public List<Integer> getListBanckAccountIdOutDocumentParent() throws Exception {
		return this.bankAccountDao.getListBanckAccountIdOutDocumentParent();
	}
}