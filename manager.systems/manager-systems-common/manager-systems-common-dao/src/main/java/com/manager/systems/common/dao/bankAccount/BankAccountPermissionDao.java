/*
 * Creation Date 21/06/2024.
 */
package com.manager.systems.common.dao.bankAccount;

import java.util.List;

import com.manager.systems.common.vo.Combobox;

public interface BankAccountPermissionDao {
	
	List<Combobox> getBankAccountPermissionsByUser(long userId) throws Exception;
	
}
