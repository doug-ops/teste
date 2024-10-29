package com.manager.systems.web.common.controller;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.web.common.cep.viacep.ClienteWs;
import com.manager.systems.web.common.cep.viacep.Endereco;
import com.manager.systems.web.common.exception.WebCommonException;
import com.manager.systems.web.common.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.ADDRESS_MAINTENANCE_CONTROLLER)
public class AddressMaintenanceController extends BaseController
{			
	@SuppressWarnings("unchecked")
	@RequestMapping(value=ConstantDataManager.ADDRESS_MAINTENANCE_CONTROLLER_SEARCH_BY_ZIP_CODE, method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<String> searchByZipCode(final HttpServletRequest request) throws Exception
	{
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		try
		{
			String addressZipCodeParameter = request.getParameter(ConstantDataManager.PARAMETER_ZIP_CODE);
			addressZipCodeParameter = StringUtils.replaceNonDigits(addressZipCodeParameter);
			if(!StringUtils.isLong(addressZipCodeParameter) || addressZipCodeParameter.length()!=8)
			{
				throw new Exception(this.messages.get(ConstantDataManager.MESSAGE_COMMON_INVALID_ZIP_CODE, null));
			}
			final Endereco address = ClienteWs.getEnderecoPorCep(addressZipCodeParameter);
			if(address==null)
			{
				throw new Exception(this.messages.get(ConstantDataManager.MESSAGE_COMMON_INVALID_ZIP_CODE, null));
			}
			final Map<String, Combobox> states = (Map<String, Combobox>) request.getSession().getAttribute(ConstantDataManager.PARAMETER_ADDRESS_STATES);
			if(states!=null && states.containsKey(address.getUf()))
			{
				address.setEstadoIbge(states.get(address.getUf()).getKey());
			}
			result.put(ConstantDataManager.PARAMETER_ADDRESS, address);
			
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, true);
		}
		catch (final WebCommonException ex)
		{
			ex.printStackTrace();
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, false);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, ex.getMessage());
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, false);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, ex.getMessage());
		}
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
}