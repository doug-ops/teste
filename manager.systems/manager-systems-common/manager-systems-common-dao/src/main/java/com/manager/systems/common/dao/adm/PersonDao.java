package com.manager.systems.common.dao.adm;

import java.util.List;

import com.manager.systems.common.dto.adm.PersonDTO;
import com.manager.systems.common.dto.adm.ReportPersonDTO;
import com.manager.systems.common.vo.Combobox;

public interface PersonDao 
{
	boolean save(PersonDTO person) throws Exception;
	boolean inactive(PersonDTO person) throws Exception;
	void get(PersonDTO person) throws Exception;
	void getAll(ReportPersonDTO reportPerson) throws Exception;
	List<Combobox> getAllAutocomplete(ReportPersonDTO reportPerson) throws Exception;
	List<Combobox> getAllCombobox(ReportPersonDTO reportPerson) throws Exception;
	void getAllReport(ReportPersonDTO reportPerson) throws Exception;
}