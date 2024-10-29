package com.manager.systems.web.movements.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.manager.systems.common.dto.adm.ReportBankAccountDTO;
import com.manager.systems.common.dto.adm.ReportFinancialSubGroupDTO;
import com.manager.systems.common.dto.adm.ReportPersonDTO;
import com.manager.systems.common.dto.adm.ReportProductGroupDTO;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.service.adm.BankAccountService;
import com.manager.systems.common.service.adm.FinancialGroupService;
import com.manager.systems.common.service.adm.FinancialSubGroupService;
import com.manager.systems.common.service.adm.PersonService;
import com.manager.systems.common.service.adm.ProductGroupService;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.financialCostCenter.FinancialCostCenterService;
import com.manager.systems.common.service.financialCostCenter.impl.FinancialCostCenterServiceImpl;
import com.manager.systems.common.service.impl.adm.BankAccountServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.FinancialSubGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.PersonServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductGroupServiceImpl;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.service.impl.movement.product.MovementProductServiceImpl;
import com.manager.systems.common.service.movement.product.MovementProductService;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.movements.dto.DocumentMovementDTO;
import com.manager.systems.web.movements.dto.DocumentMovementGroup;
import com.manager.systems.web.movements.dto.DocumentMovementGroupFilter;
import com.manager.systems.web.movements.dto.DocumentMovementItemDTO;
import com.manager.systems.web.movements.exception.MovementException;
import com.manager.systems.web.movements.service.DocumentMovementService;
import com.manager.systems.web.movements.service.impl.DocumentMovementServiceImpl;
import com.manager.systems.web.movements.utils.ConstantDataManager;

@Controller
@RequestMapping(value = ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER)
public class DocumentMovementController extends BaseController {
	private static final Log log = LogFactory.getLog(DocumentMovementController.class);

	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception {
		log.info(ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER
				+ ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);

		String result = ConstantDataManager.DOCUMENT_MOVEMENT_OPEN_METHOD_RESULT;

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;

		Connection connection = null;

		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_MOVIMENTOS", "MENU_MOVIMENTOS_CONSULTA_DOCUMENTOS")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Consulta Documentos.");
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);

			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			final List<Combobox> bankAccounts = bankAccountService.getAllCombobox(reportBankAccount);
			request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNTS, bankAccounts);

			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);
			final List<Combobox> financialGroups = financialGroupService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_GROUPS, financialGroups);

			final ProductGroupService productGroupService = new ProductGroupServiceImpl(connection);
			final ReportProductGroupDTO reportProductGroup = new ReportProductGroupDTO();
			reportProductGroup.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportProductGroup.setInactive(com.manager.systems.common.utils.ConstantDataManager.FALSE_INT);
			final List<Combobox> productGroups = productGroupService.getAllCombobox(reportProductGroup);
			request.setAttribute(ConstantDataManager.PARAMETER_PRODUCT_GROUPS, productGroups);

			final UserParentService userParentService = new UserParentServiceImpl(connection);
			long userId = user.getId();
			final List<Combobox> userChidrensParent = userParentService.getAllParentCombobox(userId);
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChidrensParent);

			final FinancialCostCenterService financialCostCenterService = new FinancialCostCenterServiceImpl(
					connection);
			final List<Combobox> financialCostCenters = financialCostCenterService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTERS, financialCostCenters);

			final PersonService companyService = new PersonServiceImpl(connection);
			final ReportPersonDTO reportPerson = new ReportPersonDTO();
			reportPerson.setPersonIdFrom(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setPersonIdTo(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setInactive(com.manager.systems.common.utils.ConstantDataManager.FALSE_INT);
			reportPerson.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setObjectType(ObjectType.COMPANY.getType());
			reportPerson.setUserOperation(user.getId());
			final List<Combobox> companies = companyService.getAllCombobox(reportPerson);
			request.setAttribute(ConstantDataManager.PARAMETER_COMPANIES, companies);

			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS,
					com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_DOCUMENT_MOVEMENT);
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
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_GROUP_METHOD)
	public String openGroup(final HttpServletRequest request, final Model model) throws Exception {
		log.info(ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_GROUP_METHOD);

		String result = ConstantDataManager.DOCUMENT_MOVEMENT_OPEN_GROUP_METHOD_RESULT;

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;

		Connection connection = null;

		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_MOVIMENTOS", "MENU_MOVIMENTOS_DOCUMENTOS_AGRUPADOS")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Documentos Agrupados.");
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final BankAccountService bankAccountService = new BankAccountServiceImpl(connection);

			final ReportBankAccountDTO reportBankAccount = new ReportBankAccountDTO();
			reportBankAccount.setUserOperation(user.getId());
			final List<Combobox> bankAccounts = bankAccountService.getAllCombobox(reportBankAccount);
			request.setAttribute(ConstantDataManager.PARAMETER_BANK_ACCOUNTS, bankAccounts);

			final FinancialGroupService financialGroupService = new FinancialGroupServiceImpl(connection);
			final List<Combobox> financialGroups = financialGroupService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_GROUPS, financialGroups);

			final ProductGroupService productGroupService = new ProductGroupServiceImpl(connection);
			final ReportProductGroupDTO reportProductGroup = new ReportProductGroupDTO();
			reportProductGroup.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportProductGroup.setInactive(com.manager.systems.common.utils.ConstantDataManager.FALSE_INT);
			final List<Combobox> productGroups = productGroupService.getAllCombobox(reportProductGroup);
			request.setAttribute(ConstantDataManager.PARAMETER_PRODUCT_GROUPS, productGroups);

			final UserParentService userParentService = new UserParentServiceImpl(connection);
			long userId = user.getId();
			final List<Combobox> userChidrensParent = userParentService.getAllParentCombobox(userId);
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChidrensParent);

			final FinancialCostCenterService financialCostCenterService = new FinancialCostCenterServiceImpl(
					connection);
			final List<Combobox> financialCostCenters = financialCostCenterService.getAllCombobox();
			request.setAttribute(ConstantDataManager.PARAMETER_FINANCIAL_COST_CENTERS, financialCostCenters);

			final PersonService companyService = new PersonServiceImpl(connection);
			final ReportPersonDTO reportPerson = new ReportPersonDTO();
			reportPerson.setPersonIdFrom(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setPersonIdTo(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setInactive(com.manager.systems.common.utils.ConstantDataManager.FALSE_INT);
			reportPerson.setDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			reportPerson.setObjectType(ObjectType.COMPANY.getType());
			reportPerson.setUserOperation(user.getId());
			final List<Combobox> companies = companyService.getAllCombobox(reportPerson);
			request.setAttribute(ConstantDataManager.PARAMETER_COMPANIES, companies);

			request.setAttribute(ConstantDataManager.PARAMETER_OBJECT_TYPE_ALIAS,
					com.manager.systems.common.utils.ConstantDataManager.OBJECT_TYPE_DOCUMENT_MOVEMENT);
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

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception {
		log.info(ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER
				+ ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD);

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

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final DocumentMovementDTO report = this.processFilter(request, connection);
			report.calculateSaldo();

			for (final Map.Entry<Long, DocumentMovementItemDTO> entry : report.getItens().entrySet()) {
				if (entry.getValue() != null) {
					entry.getValue().processGroupMovements();
				}
			}

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, report);
			status = true;
		} catch (final MovementException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
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
	
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_GROUP_METHOD)
	public ResponseEntity<String> filterGroup(final HttpServletRequest request) throws Exception {
		log.info(ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_GROUP_METHOD);

		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new GsonBuilder().serializeNulls().create();

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

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final DocumentMovementGroup report = this.processFilterGroup(request, connection);

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, report);
			status = true;
		} catch (final MovementException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
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
		//System.out.println(json);
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER_DETAIL_DOCUMENT_METHOD)
	public ResponseEntity<String> detailDocument(final HttpServletRequest request) throws Exception {
		log.info(ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER
				+ ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER_DETAIL_DOCUMENT_METHOD);

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

			final String documentIdParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_ID);
			final String documentParentIdParameter = request
					.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID);
			final String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE);

			if (!StringUtils.isLong(documentIdParameter)) {
				throw new MovementException(this.messages.get(
						com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_DOCUMENT_INVALID,
						null));
			}
			if (!StringUtils.isLong(documentParentIdParameter)) {
				throw new MovementException(this.messages.get(
						com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_GROUP_DOCUMENT_INVALID,
						null));
			}
			if (!StringUtils.isLong(documentTypeParameter)) {
				throw new MovementException(this.messages.get(
						com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_FINANCIAL_DOCUMENT_TYPE_INVALID,
						null));
			}

			final DocumentMovementItemDTO document = new DocumentMovementItemDTO();
			document.setDocumentId(Long.parseLong(documentIdParameter));
			document.setDocumentParentId(Long.parseLong(documentParentIdParameter));
			document.setDocumentTypeId(Integer.parseInt(documentTypeParameter));
			document.setChangeUser(user.getId());

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final DocumentMovementService documentMovementService = new DocumentMovementServiceImpl(connection);
			documentMovementService.getDocumentMovementItem(document);

			if (!StringUtils.isNull(document.getFinancialGroupId())) {
				final ReportFinancialSubGroupDTO reportFinancialSubGroup = new ReportFinancialSubGroupDTO();
				reportFinancialSubGroup.setFinancialSubGroupIdFrom(
						com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
				reportFinancialSubGroup
						.setFinancialSubGroupIdTo(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
				reportFinancialSubGroup.setFinancialGroupIds(String.valueOf(document.getFinancialGroupId()));
				reportFinancialSubGroup
						.setInactive(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
				reportFinancialSubGroup.setDescription(com.manager.systems.web.common.utils.ConstantDataManager.BLANK);

				final FinancialSubGroupService financialSubGroupService = new FinancialSubGroupServiceImpl(connection);
				final List<Combobox> financialSubGroups = financialSubGroupService
						.getAllCombobox(reportFinancialSubGroup);
				result.put(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUPS, financialSubGroups);
			}

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ITEM, document);
			status = true;
		} catch (final MovementException ex) {
			ex.printStackTrace();
			status = false;
			message = ex.getMessage();
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

	private ResponseEntity<byte[]> reportPDFDocumentCompanyMovement(final Connection connection,
			final Long documentParentId, final Long companyId) throws Exception {

		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;

		try {
			if (companyId == 0) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}

			if (documentParentId == 0) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_MOVEMENT_INVALID, null));
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

		}
		final ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfFile, headers, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_REPORT_PDF_METHOD, method = { RequestMethod.GET,
			RequestMethod.POST })
	public ResponseEntity<byte[]> reportPdf(final HttpServletRequest request) throws Exception {
		log.info(ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER
				+ ConstantDataManager.COMMON_CONTROLLER_REPORT_PDF_METHOD);

		ResponseEntity<byte[]> response = null;

		Connection connection = null;
		final PreviewMovementCompanyDTO previewMovementCompany = new PreviewMovementCompanyDTO();

		try {
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if (StringUtils.isLong(companyParameter)) {
				previewMovementCompany.setCompnayId(Long.valueOf(companyParameter));
			}
			final String documentParentIdParameter = request
					.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_PARENT_ID);
			if (StringUtils.isLong(documentParentIdParameter)) {
				previewMovementCompany.setDocumentParentId(Long.valueOf(documentParentIdParameter));
			}

			if (previewMovementCompany.getCompnayId() == 0) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}

			if (previewMovementCompany.getDocumentParentId() == 0) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_MOVEMENT_INVALID, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			response = this.reportPDFDocumentCompanyMovement(connection, previewMovementCompany.getDocumentParentId(),
					previewMovementCompany.getCompnayId());
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return response;
	}

	private DocumentMovementDTO processFilter(final HttpServletRequest request, final Connection connection)
			throws Exception {
		final DocumentMovementDTO documentMovement = new DocumentMovementDTO();
		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			documentMovement.setUserOperation(user.getId());

			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if (StringUtils.isLong(companyParameter)) {
				documentMovement.setCompanyId(Long.valueOf(companyParameter));
			}
			final String providerParameter = request.getParameter(ConstantDataManager.PARAMETER_PROVIDER_ID);
			if (StringUtils.isLong(providerParameter)) {
				documentMovement.setProviderId(Long.valueOf(providerParameter));
			}
			final String financialGroupParameter = request
					.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_GROUP_ID);
			if (!StringUtils.isNull(financialGroupParameter)) {
				documentMovement.setFinancialGroupId(financialGroupParameter);
			}
			final String[] financialSubGroupParameter = request
					.getParameterValues(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUP_ID);
			if (financialSubGroupParameter != null && financialSubGroupParameter.length > 0) {
				final StringBuilder financialSubGroupFilters = new StringBuilder();
				int count = 0;
				for (final String subGroupItem : financialSubGroupParameter) {
					if (count > 0) {
						financialSubGroupFilters
								.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					financialSubGroupFilters.append(com.manager.systems.common.utils.ConstantDataManager.SIMPLE_ASPAS
							+ subGroupItem + com.manager.systems.common.utils.ConstantDataManager.SIMPLE_ASPAS);
					count++;
				}
				documentMovement.setFinancialSubGroupId(financialSubGroupFilters.toString());
			}
			final String bankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ID);
			if (StringUtils.isLong(bankAccountParameter)) {
				documentMovement.setBankAccountId(Integer.valueOf(bankAccountParameter));
			}
			final String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE);
			documentMovement.setDocumentType(documentTypeParameter);
			final String documentNumberParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_NUMBER);
			documentMovement.setDocumentNumber(documentNumberParameter);
			final String filterDateParameter = request.getParameter(ConstantDataManager.PARAMETER_FILTER_DATE);
			documentMovement.setFilterBy(Integer.valueOf(filterDateParameter));
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);

			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			documentMovement.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime(
					(dateArray[0].trim() + com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
							+ com.manager.systems.common.utils.ConstantDataManager.ZERO_ZERO),
					StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			documentMovement.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime(
					(dateArray[1].trim() + com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
							+ com.manager.systems.common.utils.ConstantDataManager.FIVE_NINE),
					StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			final String[] movementTypeParameterArray = request
					.getParameterValues(ConstantDataManager.PARAMETER_MOVEMENT_TYPE_ID);
			if (movementTypeParameterArray != null && movementTypeParameterArray.length > 0) {
				for (final String movementType : movementTypeParameterArray) {
					if (StringUtils.isLong(movementType)) {

						final int movementTypeCode = Integer.parseInt(movementType);

						// 0 - Debito,
						// 1 - Credito,
						// 2 - Baixado,
						// 3 - Aberto,
						// 4 - Transferencias,
						// 5 - Lancamentos,
						// 6 - Input,
						// 7 - Output,
						// 8 - Duplicatas Movimentos,
						// 9 - Excluidos,
						// 10 - HAB
						switch (movementTypeCode) {
						case 0:
							documentMovement.setDebit(true);
							break;
						case 1:
							documentMovement.setCredit(true);
							break;
						case 2:
							documentMovement.setClose(true);
							break;
						case 3:
							documentMovement.setOpen(true);
							break;
						case 4:
							documentMovement.setTransfer(true);
							break;
						case 5:
							documentMovement.setLaunch(true);
							break;
						case 6:
							documentMovement.setInputMovement(true);
							break;
						case 7:
							documentMovement.setOutputMovement(true);
							break;
						case 8:
							documentMovement.setDuplicateMovement(true);
							break;
						case 9:
							documentMovement.setRemoved(true);
							break;
						case 10:
							documentMovement.setTransferHab(true);
							break;
						default:
							break;
						}
					}
				}
			}

			final String productGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_GROUP_ID);
			if (StringUtils.isLong(productGroupParameter)) {
				documentMovement.setProductGroupId(Integer.valueOf(productGroupParameter));
			}
			final String productSubGroupParameter = request
					.getParameter(ConstantDataManager.PARAMETER_PRODUCT_SUB_GROUP_ID);
			if (StringUtils.isLong(productSubGroupParameter)) {
				documentMovement.setProductSubGroupId(Integer.valueOf(productSubGroupParameter));
			}

			final String[] userChildrensParentParameter = request
					.getParameterValues(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			if (userChildrensParentParameter == null || userChildrensParentParameter.length == 0) {
				documentMovement.setUserChildrensParent(String.valueOf(user.getId()));
			} else {
				final StringBuilder userChildrensParentFilters = new StringBuilder();
				int count = 0;
				for (final String userParent : userChildrensParentParameter) {
					if (count > 0) {
						userChildrensParentFilters
								.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					userChildrensParentFilters.append(userParent);
					count++;
				}
				documentMovement.setUserChildrensParent(userChildrensParentFilters.toString());
			}
			final String financialCostCenterId = request
					.getParameter(ConstantDataManager.PARAMETER_FILTER_FINANCIAL_COST_CENTER_ID);
			if (!StringUtils.isNull(financialCostCenterId)) {
				documentMovement.setFinancialCostCenterId(Integer.parseInt(financialCostCenterId));
			}

			final DocumentMovementService documentMovementService = new DocumentMovementServiceImpl(connection);
			documentMovementService.getAllDocumentMovement(documentMovement);
		} catch (final Exception ex) {
			throw ex;
		}
		return documentMovement;
	}
	
	private DocumentMovementGroup processFilterGroup(final HttpServletRequest request, final Connection connection) throws Exception {
		
		DocumentMovementGroup movementGroup = null;
		
		try {
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			final DocumentMovementGroupFilter filter = new DocumentMovementGroupFilter();
			filter.setUserOperation(user.getId());

			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if (StringUtils.isLong(companyParameter)) {
				filter.setCompanyId(Long.valueOf(companyParameter));
			}
			final String providerParameter = request.getParameter(ConstantDataManager.PARAMETER_PROVIDER_ID);
			if (StringUtils.isLong(providerParameter)) {
				filter.setProviderId(Long.valueOf(providerParameter));
			}
			final String financialGroupParameter = request
					.getParameter(ConstantDataManager.PARAMETER_FINANCIAL_GROUP_ID);
			if (!StringUtils.isNull(financialGroupParameter)) {
				filter.setFinancialGroupId(financialGroupParameter);
			}
			final String[] financialSubGroupParameter = request
					.getParameterValues(ConstantDataManager.PARAMETER_FINANCIAL_SUB_GROUP_ID);
			if (financialSubGroupParameter != null && financialSubGroupParameter.length > 0) {
				final StringBuilder financialSubGroupFilters = new StringBuilder();
				int count = 0;
				for (final String subGroupItem : financialSubGroupParameter) {
					if(!StringUtils.isNull(subGroupItem)) {
						if (count > 0) {
							financialSubGroupFilters
									.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
						}
						financialSubGroupFilters.append(com.manager.systems.common.utils.ConstantDataManager.SIMPLE_ASPAS
								+ subGroupItem + com.manager.systems.common.utils.ConstantDataManager.SIMPLE_ASPAS);
						count++;						
					}
				}
				if(!StringUtils.isNull(financialSubGroupFilters.toString())) {
					filter.setFinancialSubGroupId(financialSubGroupFilters.toString());					
				}
			}
			final String bankAccountParameter = request.getParameter(ConstantDataManager.PARAMETER_BANK_ACCOUNT_ID);
			if (StringUtils.isLong(bankAccountParameter)) {
				filter.setBankAccountId(Integer.valueOf(bankAccountParameter));
			}
			final String documentTypeParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_TYPE);
			filter.setDocumentType(documentTypeParameter);
			final String documentNumberParameter = request.getParameter(ConstantDataManager.PARAMETER_DOCUMENT_NUMBER);
			filter.setDocumentNumber(documentNumberParameter);
			final String filterDateParameter = request.getParameter(ConstantDataManager.PARAMETER_FILTER_DATE);
			filter.setFilterBy(Integer.valueOf(filterDateParameter));
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);

			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			filter.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime(
					(dateArray[0].trim() + com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
							+ com.manager.systems.common.utils.ConstantDataManager.ZERO_ZERO),
					StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			filter.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime(
					(dateArray[1].trim() + com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
							+ com.manager.systems.common.utils.ConstantDataManager.FIVE_NINE),
					StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			final String[] movementTypeParameterArray = request
					.getParameterValues(ConstantDataManager.PARAMETER_MOVEMENT_TYPE_ID);
			if (movementTypeParameterArray != null && movementTypeParameterArray.length > 0) {
				for (final String movementType : movementTypeParameterArray) {
					if (StringUtils.isLong(movementType)) {

						final int movementTypeCode = Integer.parseInt(movementType);

						// 0 - Debito,
						// 1 - Credito,
						// 2 - Baixado,
						// 3 - Aberto,
						// 4 - Transferencias,
						// 5 - Lancamentos,
						// 6 - Input,
						// 7 - Output,
						// 8 - Duplicatas Movimentos,
						// 9 - Excluidos,
						// 10 - HAB
						switch (movementTypeCode) {
						case 0:
							filter.setDebit(true);
							break;
						case 1:
							filter.setCredit(true);
							break;
						case 2:
							filter.setClose(true);
							break;
						case 3:
							filter.setOpen(true);
							break;
						case 4:
							filter.setTransfer(true);
							break;
						case 5:
							filter.setLaunch(true);
							break;
						case 6:
							filter.setInputMovement(true);
							break;
						case 7:
							filter.setOutputMovement(true);
							break;
						case 8:
							filter.setDuplicateMovement(true);
							break;
						case 9:
							filter.setRemoved(true);
							break;
						case 10:
							filter.setTransferHab(true);
							break;
						default:
							break;
						}
					}
				}
			}

			final String productGroupParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_GROUP_ID);
			if (StringUtils.isLong(productGroupParameter)) {
				filter.setProductGroupId(Integer.valueOf(productGroupParameter));
			}
			final String productSubGroupParameter = request
					.getParameter(ConstantDataManager.PARAMETER_PRODUCT_SUB_GROUP_ID);
			if (StringUtils.isLong(productSubGroupParameter)) {
				filter.setProductSubGroupId(Integer.valueOf(productSubGroupParameter));
			}

			final String[] userChildrensParentParameter = request
					.getParameterValues(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			if (userChildrensParentParameter == null || userChildrensParentParameter.length == 0) {
				filter.setUserChildrensParent(String.valueOf(user.getId()));
			} else {
				final StringBuilder userChildrensParentFilters = new StringBuilder();
				int count = 0;
				for (final String userParent : userChildrensParentParameter) {
					if (count > 0) {
						userChildrensParentFilters
								.append(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
					}
					userChildrensParentFilters.append(userParent);
					count++;
				}
				filter.setUserChildrensParent(userChildrensParentFilters.toString());
			}
			final String financialCostCenterId = request
					.getParameter(ConstantDataManager.PARAMETER_FILTER_FINANCIAL_COST_CENTER_ID);
			if (!StringUtils.isNull(financialCostCenterId)) {
				filter.setFinancialCostCenterId(Integer.parseInt(financialCostCenterId));
			}

			final DocumentMovementService documentMovementService = new DocumentMovementServiceImpl(connection);
			movementGroup = documentMovementService.getDocumentMovementGroup(filter);
		} catch (final Exception ex) {
			throw ex;
		}
		return movementGroup;
	}

	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD)
	public ResponseEntity<byte[]> filterReport(final HttpServletRequest request, final Long documentParentId,
			final Long companyId) throws Exception {

		log.info(ConstantDataManager.DOCUMENT_MOVEMENT_CONTROLLER
				+ ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD);

		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;

		Connection connection = null;

		try {

			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final DocumentMovementDTO report = this.processReportPdf(request, connection);

			String analiticParameter = request.getParameter(ConstantDataManager.PARAMETER_ANALITIC);
			if (!StringUtils.isLong(analiticParameter)) {
				analiticParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}
			if (Integer.parseInt(analiticParameter) == 0) {
				analiticParameter = com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING;
			}

			final DocumentMovementService documentMovementsReportService = new DocumentMovementServiceImpl(connection);

			if (Integer.parseInt(analiticParameter) == 3 ? true : false) {
				pdfFile = documentMovementsReportService.generatePDFMovementDocumentsWeek(report.convetToGroupWeek());
			} else if (Integer.parseInt(analiticParameter) == 4 ? true : false) {
				pdfFile = documentMovementsReportService
						.generatePDFMovementDocumentsGroupCompany(report.convetGroupCompany());

			} else {
				report.setAnalitic(Integer.parseInt(analiticParameter) == 0 ? false : true);
				report.calculateSaldo();

				pdfFile = documentMovementsReportService.generatePDFMovementDocuments(report);
				report.getDocumentNumber();
			}

			final StringBuilder filename = new StringBuilder();
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_REPORT);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY);
			filename.append(report.getCompanyId());
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

		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}

		final ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfFile, headers, HttpStatus.OK);
		return response;
	}

	private DocumentMovementDTO processReportPdf(final HttpServletRequest request, final Connection connection)
			throws Exception {
		final DocumentMovementDTO documentMovement = new DocumentMovementDTO();
		try {
			final String companysIdParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANYS_ID);
			if (!StringUtils.isNull(companysIdParameter)) {
				documentMovement.setCompanysId(companysIdParameter);
			}
			final String documentsParentIdParameter = request
					.getParameter(ConstantDataManager.PARAMETER_DOCUMENTS_PARENT_ID);
			if (!StringUtils.isNull(documentsParentIdParameter)) {
				documentMovement.setDocumentsParentId(documentsParentIdParameter);
			}

			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);
			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			documentMovement.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime(
					(dateArray[0].trim() + com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
							+ com.manager.systems.common.utils.ConstantDataManager.ZERO_ZERO),
					StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			documentMovement.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime(
					(dateArray[1].trim() + com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
							+ com.manager.systems.common.utils.ConstantDataManager.FIVE_NINE),
					StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));

			final DocumentMovementService documentMovementReportService = new DocumentMovementServiceImpl(connection);
			documentMovementReportService.getAllDocumentMovementReport(documentMovement);
		} catch (final Exception ex) {
			throw ex;
		}
		return documentMovement;
	}
}
