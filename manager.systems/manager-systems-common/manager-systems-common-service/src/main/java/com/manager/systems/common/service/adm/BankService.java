package com.manager.systems.common.service.adm;

import java.util.List;

import com.manager.systems.common.vo.Combobox;

public interface BankService 
{
	List<Combobox> getAllCombobox() throws Exception;
}