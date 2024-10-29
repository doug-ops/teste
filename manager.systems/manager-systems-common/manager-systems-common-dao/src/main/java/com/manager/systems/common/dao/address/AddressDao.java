package com.manager.systems.common.dao.address;

import java.util.List;
import java.util.Map;

import com.manager.systems.common.vo.Combobox;

public interface AddressDao 
{
	Map<String, Combobox> getAllStatesMap() throws Exception;
	List<Combobox> getAllStates() throws Exception;
	List<Combobox> getAllCountry() throws Exception;
}