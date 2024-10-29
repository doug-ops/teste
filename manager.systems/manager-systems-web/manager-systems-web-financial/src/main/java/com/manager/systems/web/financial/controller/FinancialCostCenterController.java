package com.manager.systems.web.financial.controller;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.financial.exception.FinancialException;
import com.manager.systems.web.financial.utils.ConstantDataManager;

@Controller
@RequestMapping(value=ConstantDataManager.COST_CENTER_CONTROLLER)
public class FinancialCostCenterController extends BaseController
{
	private static final Log log = LogFactory.getLog(FinancialCostCenterController.class);
	
	@PostMapping(value=ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD)
	public ResponseEntity<String> save(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.COST_CENTER_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_SAVE_METHOD);
		
		final Gson gson = new Gson();
		final Map<String, Object> result = new TreeMap<String, Object>();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		com.manager.systems.common.dto.financialCostCenter.FinancialCostCenterDTO financialCostCenter = null;
				
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			

			final String descriptionParameter = request.getParameter(ConstantDataManager.PARAMETER_DESCRIPTION);
						
			financialCostCenter = new com.manager.systems.common.dto.financialCostCenter.FinancialCostCenterDTO();
			financialCostCenter.setDescription(StringUtils.decodeString(descriptionParameter).toUpperCase());
			financialCostCenter.setInactive(false);
			final ChangeData changeData = new ChangeData();
			changeData.setUserChange(user.getId());
			changeData.setChangeDate(Calendar.getInstance(tz).getTime());
			financialCostCenter.setChangeData(changeData);
			
				
			final com.manager.systems.common.service.financialCostCenter.FinancialCostCenterService financialCostCenterService = new 
					com.manager.systems.common.service.financialCostCenter.impl.FinancialCostCenterServiceImpl(connection);	
			final Integer financialCostCenterId = financialCostCenterService.save(financialCostCenter);
			financialCostCenter.setId(financialCostCenterId);
						
			status = true;
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
		}
		catch (final FinancialException ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			status = false;
			message =  ex.getMessage();
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}	
		
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_DATA, financialCostCenter);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
}