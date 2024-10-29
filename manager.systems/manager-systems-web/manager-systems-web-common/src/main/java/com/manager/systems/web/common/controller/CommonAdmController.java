package com.manager.systems.web.common.controller;

import java.sql.Connection;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.service.CommonService;
import com.manager.systems.common.service.impl.CommonServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.web.common.exception.WebCommonException;
import com.manager.systems.web.common.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.COMMON_ADM_CONTROLLER)
public class CommonAdmController extends BaseController
{			
	@RequestMapping(value=ConstantDataManager.COMMON_ADM_CONTROLLER_NEXT_CODE_METHOD, method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<String> nextCode(final HttpServletRequest request) throws Exception
	{
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		Connection connection = null;
		
		try
		{
			final String tableNameParameter = request.getParameter(ConstantDataManager.PARAMETER_TABLE_NAME);
			final String initialCodeParameter = request.getParameter(ConstantDataManager.PARAMETER_INITIAL_CODE);
			if(StringUtils.isNull(tableNameParameter))
			{
				throw new Exception(this.messages.get(ConstantDataManager.MESSAGE_COMMON_ADMN_INVALID_TABLE, null));				
			}
			if(!StringUtils.isLong(initialCodeParameter))
			{
				throw new Exception(this.messages.get(ConstantDataManager.MESSAGE_COMMON_ADMN_INVALID_INITIAL_CODE, null));				
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final CommonService commonService = new CommonServiceImpl(connection);
			final long nextCode = commonService.getNextCode(tableNameParameter, Long.valueOf(initialCodeParameter));
			result.put(ConstantDataManager.PARAMETER_NEXT_CODE, nextCode);
			
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
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
}