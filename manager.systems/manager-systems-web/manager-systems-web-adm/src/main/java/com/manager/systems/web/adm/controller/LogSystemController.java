package com.manager.systems.web.adm.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.ChangeObjectDTO;
import com.manager.systems.common.dto.adm.LogSystemDTO;
import com.manager.systems.common.dto.adm.ReportLogSystemDTO;
import com.manager.systems.common.service.adm.LogSystemService;
import com.manager.systems.common.service.adm.UserParentService;
import com.manager.systems.common.service.impl.adm.LogSystemServiceImpl;
import com.manager.systems.common.service.impl.adm.UserParentServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.web.adm.exception.AdminException;
import com.manager.systems.web.adm.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value = ConstantDataManager.LOG_SYSTEM_CONTROLLER)
public class LogSystemController extends BaseController {
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD, method = {RequestMethod.GET, RequestMethod.POST})
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		String result = ConstantDataManager.LOG_SYSTEM_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_RELATORIOS", "MENU_LOGS_SISTEMA_LISTAR")) {
				result = ConstantDataManager.RESULT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Log Sistema.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final UserParentService userParentService = new UserParentServiceImpl(connection);
			
			long userId = user.getId();
			final List<Combobox> userChildrensParent = userParentService.getAllParentCombobox(userId);
			request.setAttribute(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT, userChildrensParent);
			
		}
		catch(final Exception e) 
		{
			e.printStackTrace();
			message = e.getMessage();
		}
		finally 
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE, message);
		return result;
	}
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_DETAIL_METHOD, method=RequestMethod.POST)
	public ResponseEntity<String> detail(final HttpServletRequest request) throws Exception
	{
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
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
			final ReportLogSystemDTO reportLogSystem = new ReportLogSystemDTO();
		
			
			final String idParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			if(!StringUtils.isNull(idParameter))
			{
				reportLogSystem.setId(Integer.valueOf(idParameter));
			}
			final String changeDateParameter = request.getParameter(ConstantDataManager.PARAMETER_CHANGE_DATE);
			if(!StringUtils.isNull(changeDateParameter))
			{
				reportLogSystem.setChangeDate(changeDateParameter);
			}
			
			final LogSystemService logSystemtService = new LogSystemServiceImpl(connection);	
			final List<ChangeObjectDTO> itens = logSystemtService.getAll(reportLogSystem);

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ITENS, itens);
			status = true;
		}
		catch (final AdminException ex)
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
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_METHOD, method = RequestMethod.POST)
	public ResponseEntity<String> filter(final HttpServletRequest request) throws Exception {
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final List<LogSystemDTO> reportItems = this.processFilter(request, connection);

			status = true;
			result.put(ConstantDataManager.PARAMETER_ITEMS, reportItems);
			status = true;
		} catch (final AdminException ex) {
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

	public List<LogSystemDTO> processFilter(final HttpServletRequest request, final Connection connection) throws Exception {
		final ReportLogSystemDTO reportLogSystem = new ReportLogSystemDTO();
		final List<LogSystemDTO> items;
		try {
			
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			final String objectIdParameter = request.getParameter(ConstantDataManager.PARAMETER_OBJECT_ID);
			final String systemIdParameter = request.getParameter(ConstantDataManager.PARAMETER_SYSTEM_ID);
			
			reportLogSystem.setCountLogSystem(10);
			reportLogSystem.setObjectId(Long.valueOf(objectIdParameter));
			reportLogSystem.setSystemId(Integer.valueOf(systemIdParameter));

			final LogSystemService logSystemtService = new LogSystemServiceImpl(connection);	
			items = logSystemtService.get(reportLogSystem);
			 
		} catch (final Exception ex) {
			throw ex;
		}
		return items;
	}
	
	@PostMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_LOG_SYSTEM_METHOD)
	public ResponseEntity<String> filterLogSystem(final HttpServletRequest request) throws Exception {
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;

		try {			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final List<ChangeObjectDTO> reportItems = this.processFilterLogSystem(request, connection);
						
			status = true;
			result.put(ConstantDataManager.PARAMETER_ITEMS, reportItems);
			status = true;
		} catch (final AdminException ex) {
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

	public List<ChangeObjectDTO> processFilterLogSystem(final HttpServletRequest request, final Connection connection) throws Exception {
		final ReportLogSystemDTO reportLogSystem = new ReportLogSystemDTO();
		final List<ChangeObjectDTO> items;
		try {
			
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			reportLogSystem.setCountLogSystem(10);
			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);

			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			reportLogSystem.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[0].trim()+com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+com.manager.systems.common.utils.ConstantDataManager.ZERO_ZERO),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			reportLogSystem.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime((dateArray[1].trim()+com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS+com.manager.systems.common.utils.ConstantDataManager.FIVE_NINE),StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			
			final String userIdParameter = request.getParameter(ConstantDataManager.PARAMETER_USER_ID);
			if(StringUtils.isLong(userIdParameter))
			{
				reportLogSystem.setUserId(Long.valueOf(userIdParameter));
			}
			
			final String idParameter = request.getParameter(ConstantDataManager.PARAMETER_ID);
			if(!StringUtils.isNull(idParameter))
			{
				reportLogSystem.setId(Integer.valueOf(idParameter));
			}
			final String changeDateParameter = request.getParameter(ConstantDataManager.PARAMETER_CHANGE_DATE);
			if(!StringUtils.isNull(changeDateParameter))
			{
				reportLogSystem.setChangeDate(changeDateParameter);
			}
			
			final LogSystemService logSystemtService = new LogSystemServiceImpl(connection);	
			items = logSystemtService.getAll(reportLogSystem);
			 
		} catch (final Exception ex) {
			throw ex;
		}
		return items;
	}
	
	@RequestMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_FILTER_REPORT_METHOD, method = RequestMethod.POST)
	public ResponseEntity<byte[]> filterReport(final HttpServletRequest request, final Long documentParentId,
			final Long companyId) throws Exception {
		final HttpHeaders headers = new HttpHeaders();
		byte[] pdfFile = null;
		
		Connection connection = null;

		try {

			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
		
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if (connection == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			String analiticParameter = request.getParameter(ConstantDataManager.PARAMETER_ANALITIC);

			final List<ChangeObjectDTO> report = this.processFilterLogSystem(request, connection);

			boolean analitic = Integer.parseInt(analiticParameter)== 0 ? false : true;

			final LogSystemService logSystemtService = new LogSystemServiceImpl(connection);	
			pdfFile = logSystemtService.generatePDFLogSystem(report, analitic);

			final StringBuilder filename = new StringBuilder();
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_REPORT);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE);
			filename.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY);
			
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

	public ChangeObjectDTO processReportPdf(final HttpServletRequest request, final Connection connection)
			throws Exception {
		final ReportLogSystemDTO reportLogSystem = new ReportLogSystemDTO();
		try {
//			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
//			if(user==null)
//			{
//				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
//			}
//			documentMovement.setUserOperation(user.getId());
			

			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);
			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			reportLogSystem.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime(
					(dateArray[0].trim() + com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
							+ com.manager.systems.common.utils.ConstantDataManager.ZERO_ZERO),
					StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
			reportLogSystem.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime(
					(dateArray[1].trim() + com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
							+ com.manager.systems.common.utils.ConstantDataManager.FIVE_NINE),
					StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));

			
		} catch (final Exception ex) {
			throw ex;
		}
		return null;
	}
}