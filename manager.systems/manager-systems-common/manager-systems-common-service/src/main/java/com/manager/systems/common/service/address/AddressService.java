package com.manager.systems.common.service.address;

import java.util.List;
import java.util.Map;

import com.manager.systems.common.vo.Combobox;

public interface AddressService 
{
	Map<String, Combobox> getAllStatesMap() throws Exception;
	List<Combobox> getAllStates() throws Exception;
	List<Combobox> getAllCountry() throws Exception;
}