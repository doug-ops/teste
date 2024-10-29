/**.
 * Date Create 23/08/2019
 */

package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.BankAccountDTO;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.vo.Combobox;

public interface BankAccountDao 
{
	boolean save(BankAccountDTO bankAccount) throws Exception;
	boolean inactive(BankAccountDTO bankAccount) throws Exception;
	void get(BankAccountDTO bankAccount) throws Exception;
	void getAll(ReportBankAccountDTO reportBankAccount) throws Exception;
	List<Combobox> getAllCombobox(ReportBankAccountDTO reportBankAccount) throws Exception;
	List<Combobox> getAllAutocomplete(ReportBankAccountDTO reportBankAccount) throws Exception;
	int getCompanyIdByBankAccount(int bankAccountId) throws Exception;
	List<Integer> getListBanckAccountIdOutDocumentParent() throws Exception;
}