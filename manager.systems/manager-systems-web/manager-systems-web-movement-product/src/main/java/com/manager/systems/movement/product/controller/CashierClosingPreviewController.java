/*
 * Crete Date 08/10/2022
 */
package com.manager.systems.movement.product.controller;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.manager.systems.model.admin.User;
import com.manager.systems.movement.product.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value=ConstantDataManager.CASHIER_CLOSING_PREVIEW_CONTROLLER)
public class CashierClosingPreviewController extends BaseController
{
	
	private static final Log log = LogFactory.getLog(CashierClosingPreviewController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.CASHIER_CLOSING_PREVIEW_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.CASHIER_CLOSING_PREVIEW_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
			
		try
		{

			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);				
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_OFF_LINE", "MENU_OFF_LINE_FECHAMENTO_CAIXA")) {
				result = ConstantDataManager.RESULT_REDIRECT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Fechamento de Caixa.");
			}
			
			String frameUrl = null;
			if(ConstantDataManager.LOOPBACK_IP.equalsIgnoreCase(request.getLocalAddr())) {
				frameUrl = request.getScheme() + "://" + request.getLocalAddr()  + ":" +  request.getLocalPort()+"/ROOT/cashierClosingPreview/cashier-closing/open?is_offline=S&username=" + user.getAccessData().getUsername() + "&password=" + user.getAccessData().getPassword();
			} else {
				//frameUrl = "http://ec2-52-90-85-165.compute-1.amazonaws.com:8080/cashierClosingPreview/cashier-closing/open?is_offline=S&username=" + user.getAccessData().getUsername() + "&password=" + user.getAccessData().getPassword();
				frameUrl = "http://54.89.222.132:8080/cashierClosingPreview/cashier-closing/open?is_offline=S&username=" + user.getAccessData().getUsername() + "&password=" + user.getAccessData().getPassword();
			}
			
			request.setAttribute(ConstantDataManager.PARAMETER_FRAME_URL, frameUrl);
			
		}
		catch(final Exception e) 
		{
			e.printStackTrace();
			message = e.getMessage();
		}
		finally 
		{

		}
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, message);
		return result;
	}
}
