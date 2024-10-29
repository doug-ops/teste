package com.manager.systems.movement.product.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.FinancialTransferGroupDTO;
import com.manager.systems.common.dto.adm.ReportFinancialTransferGroupDTO;
import com.manager.systems.common.dto.movement.company.MovementCompany;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.adm.FinancialTransferGroupService;
import com.manager.systems.common.service.adm.UserCompanyService;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialTransferGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.UserCompanyServiceImpl;
import com.manager.systems.common.service.impl.movement.product.MovementProductServiceImpl;
import com.manager.systems.common.service.movement.product.MovementProductService;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.model.admin.User;
import com.manager.systems.movement.product.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value = ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER)
public class CompanyMovementController extends BaseController {

	private static final Log log = LogFactory.getLog(CompanyMovementController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception {
		
		log.info(ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.COMPANY_MOVEMENT_OPEN_METHOD_RESULT;

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;

		Connection connection = null;

		try {
			final String selectedCompany = request.getParameter(ConstantDataManager.SELECTED_COMPANY);
			
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_OFF_LINE", "MENU_OFF_LINE_PROCESSA_EMPRESA")) {
				result = ConstantDataManager.RESULT_REDIRECT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Processa Empresa.");
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final UserCompanyService userCompanyService = new UserCompanyServiceImpl(connection);
			
			final List<CompanyDTO> companyFilter = userCompanyService.get(user.getId());
			
			final List<CompanyDTO> companys = companyFilter.parallelStream().filter(x -> !x.isProcessMovementAutomatic()).collect(Collectors.toList());
			
			request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_COMPANYS, companys);
			model.addAttribute(ConstantDataManager.SELECTED_COMPANY, selectedCompany);

		} catch (final Exception e) {
			e.printStackTrace();
			message = e.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, message);
		return result;
	}

	@PostMapping(value = ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER_GET_DATA_COMPANY)
	public ResponseEntity<String> getDataCompany(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER + ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER_GET_DATA_COMPANY);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if (!StringUtils.isLong(companyParameter)) {
				throw new Exception(this.messages.get(
						com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMMON_ADMN_INVALID_TABLE,
						null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final MovementCompany movementCompany = new MovementCompany();
			movementCompany.setCompanyId(Long.valueOf(companyParameter));
			movementCompany.setOffline(true);
			final MovementProductService service = new MovementProductServiceImpl(connection);
			service.getMovementsCompany(movementCompany);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_MOVEMENT_COMPANY, movementCompany);

			final FinancialTransferGroupService financialTransferGroupService = new FinancialTransferGroupServiceImpl(connection);
			final ReportFinancialTransferGroupDTO reportFinancialTransfer = new ReportFinancialTransferGroupDTO();
			reportFinancialTransfer.setCompanyId(Long.valueOf(companyParameter));
			reportFinancialTransfer.setExpense(4); //Despesa variavel
			reportFinancialTransfer.setUserOperation(user.getId());
			
			final FinancialTransferGroupDTO financialTransferGroup = financialTransferGroupService.getGroupFinancialTransfer(reportFinancialTransfer);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_FINANTIAL_GROUP_TRANSFER,financialTransferGroup);			
			status = true;
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER_PREVIEW_PROCESS_MOVEMENT)
	public ResponseEntity<String> previewProcessMovement(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER + ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER_PREVIEW_PROCESS_MOVEMENT);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;
		Connection connectionExpensev = null;

		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if (!StringUtils.isLong(companyParameter)) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}

			final String movementId = request.getParameter(ConstantDataManager.PARAMETER_MOVEMENT_ID);
			if (StringUtils.isNull(movementId)) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_MOVEMENT_INVALID, null));
			}
			
			final List<ReportFinancialTransferGroupDTO> expenses = new ArrayList<>();
			
			final Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) { 
	            final String paramName = parameterNames.nextElement();
	            if(paramName.indexOf("valueTransfer_")>-1) {
	            	final String[] paramKeys = paramName.split(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
	            	if(paramKeys!=null && paramKeys.length == 3) {
		            	String paramValue = request.getParameter(paramName);
		            	if(!StringUtils.isNull(paramValue))
		    			{
		    				if(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_ZERO_ZERO.equalsIgnoreCase(paramValue))
		    				{
		    					paramValue = com.manager.systems.common.utils.ConstantDataManager.ZERO_VIRGULA_ZERO_ZERO;
		    				}
		    				paramValue = StringUtils.replaceNonDigits(paramValue);
		    				final StringBuilder resultMoeda = new StringBuilder(paramValue);
		    				if(resultMoeda.length()>2)
		    				{
		    					resultMoeda.insert(resultMoeda.length() - 2, com.manager.systems.common.utils.ConstantDataManager.PONTO);
		    				}						
		    				paramValue = resultMoeda.toString();
		    			}
		    			else
		    			{
		    				paramValue = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
		    			}
		            	final ReportFinancialTransferGroupDTO item = new ReportFinancialTransferGroupDTO();
		            	item.setCompanyId(Long.parseLong(companyParameter));
		            	item.setGroupId(Integer.parseInt(paramKeys[1]));
		            	item.setGroupItemId(Integer.parseInt(paramKeys[2]));
		            	item.setTransferValue(Double.parseDouble(paramValue));
		            	item.setInactive(0);
		            	item.setExpense(4);
		            	item.setUserOperation(user.getId());
		            	expenses.add(item);
	            	}
	            }
	        }
			
			if(expenses.size() > 0) {
				connectionExpensev = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
				if (connectionExpensev == null) {
					throw new Exception(this.messages
							.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
				}
				
				connectionExpensev.setAutoCommit(false);
				final FinancialTransferGroupService financialTransferGroupService = new FinancialTransferGroupServiceImpl(connectionExpensev);
				for (final ReportFinancialTransferGroupDTO reportFinancialTransferGroup : expenses) {
					financialTransferGroupService.saveGroupFinancialTransfer(reportFinancialTransferGroup);
				}
				connectionExpensev.commit();
			}	

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			long companyId = Long.valueOf(companyParameter);
			final MovementProductService movementProductService = new MovementProductServiceImpl(connection); 
			
			final PreviewMovementCompanyDTO preview = new PreviewMovementCompanyDTO();
			preview.setCompnayId(companyId);
			preview.setOffline(true);
			preview.setPreview(true);
			preview.setMovementsId(movementId);
			preview.setUserChange(user.getId());

			
			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);
			preview.setListBanckAccountIdOutDocumentParent(bankAccountService.getListBanckAccountIdOutDocumentParent());

			movementProductService.previewProcessMovement(preview);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_PREVIEW, preview);
			status = true;
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
			
			if(connectionExpensev != null) {
				connectionExpensev.rollback();
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (connectionExpensev != null) {
				connectionExpensev.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER_PROCESS_MOVEMENT)
	public ResponseEntity<String> processMovement(final HttpServletRequest request) throws Exception {
		
		log.info(ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER + ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER_PROCESS_MOVEMENT);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;
		PreviewMovementCompanyDTO preview = null;

		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if (!StringUtils.isLong(companyParameter)) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}

			final String movementId = request.getParameter(ConstantDataManager.PARAMETER_MOVEMENT_ID);
			if (StringUtils.isNull(movementId)) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_MOVEMENT_INVALID, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			connection.setAutoCommit(false);

			preview = new PreviewMovementCompanyDTO();
			preview.setCompnayId(Long.valueOf(companyParameter));
			preview.setOffline(true);
			preview.setPreview(false);
			preview.setMovementsId(movementId);
			preview.setUserChange(user.getId());

			final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
			final long documentParentId = movementProductService.processMovement(preview);
			result.put(ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID, documentParentId);
			message = this.messages.get(
					com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_PROCESSING_SUCCESS, null);
			status = true;

			connection.commit();
		} catch (final Exception ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}

	@GetMapping(value = ConstantDataManager.COMPANY_MOVEMENT_REPORT_PDF)
	public ResponseEntity<byte[]> reportCompanyMovementPdf(
			@PathVariable(name = ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID) final Long documentParentId,
			@PathVariable(name = ConstantDataManager.PARAMETER_COMPANY_ID) final Long companyId) throws Exception {
		
		log.info(ConstantDataManager.COMPANY_MOVEMENT_CONTROLLER + ConstantDataManager.COMPANY_MOVEMENT_REPORT_PDF);
		
		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;

		Connection connection = null;

		try {
			if (companyId == 0) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}

			if (documentParentId == 0) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_MOVEMENT_INVALID, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
			final PreviewMovementCompanyDTO previewMovementCompany = movementProductService
					.reportCompanyMovement(documentParentId, companyId);

			previewMovementCompany.calculateBalanceMovement();
			
			pdfFile = movementProductService.generatePDFMovementCompany(previewMovementCompany);

			final StringBuilder filename = new StringBuilder();
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_REPORT);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY);
			filename.append(previewMovementCompany.getCompnayId());
			filename.append(com.manager.systems.common.utils.ConstantDataManager.PONTO);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.EXTENSION_PDF);

			headers.setContentType(MediaType
					.parseMediaType(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_TYPE_PDF));
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_ORIGIN,
					"*");
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_METHODS,
					com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_GET
							+ com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA
							+ com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_POST
							+ com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA
							+ com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_PUT);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_ACCESS_CONTROL_ALLOW_HEADERS,
					com.manager.systems.web.common.utils.ConstantDataManager.CONTENT_TYPE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.CONTENT_DISPOSITION,
					com.manager.systems.web.common.utils.ConstantDataManager.INLINE
							+ com.manager.systems.common.utils.ConstantDataManager.PONTO_VIRGULA_STRING
							+ com.manager.systems.web.common.utils.ConstantDataManager.FILENAME
							+ com.manager.systems.common.utils.ConstantDataManager.IGUAL + filename.toString());
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_CACHE_CONTROL,
					com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_CACHE
							+ com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA
							+ com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_STORE
							+ com.manager.systems.web.common.utils.ConstantDataManager.VIRGULA
							+ com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_MUST_REVALIDATE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_PRAGMA,
					com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_NO_CACHE);
			headers.add(com.manager.systems.web.common.utils.ConstantDataManager.RESPONSE_EXPIRES,
					com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);

			headers.setContentLength(pdfFile.length);

		} catch (final Exception ex) {
			ex.printStackTrace();

		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		final ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfFile, headers, HttpStatus.OK);
		return response;
	}
}