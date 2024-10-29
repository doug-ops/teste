package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.vo.Combobox;

public interface BankDao 
{
	List<Combobox> getAllCombobox() throws Exception;
}