/*
 * Date create 21/06/2024.
 */
package com.manager.systems.common.service.bankAccount;

import java.util.List;

import com.manager.systems.common.vo.Combobox;

public interface BankAccountPermissionService {

	List<Combobox> getBankAccountPermissionsByUser(long userId) throws Exception;

}