package com.manager.systems.portal.controller;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.manager.systems.common.dao.FactoryConnection;
import com.manager.systems.common.dto.dashboard.DashboardCompanyExecutionWeekDTO;
import com.manager.systems.common.dto.dashboard.MovementPendingDTO;
import com.manager.systems.common.service.dashboard.DashboardCompanyExecutionWeekService;
import com.manager.systems.common.service.dashboard.DashboardService;
import com.manager.systems.common.service.impl.dashboard.DashboardCompanyExecutionWeekServiceImpl;
import com.manager.systems.common.service.impl.dashboard.DashboardServiceImpl;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.model.admin.User;
import com.manager.systems.portal.utils.ConstantDataManager;
import com.manager.systems.web.adm.exception.AdminException;
import com.manager.systems.web.common.controller.BaseController;
import com.manager.systems.web.movements.exception.MovementException;

@Controller
@RequestMapping(value = ConstantDataManager.DASHBOARD_CONTROLLER)
public class DashboardController extends BaseController {
	private static final Logger logger = LogManager.getLogger(DashboardController.class.getName());

	@PostMapping(value = ConstantDataManager.DASHBOARD_PENDING_ITENS)
	public ResponseEntity<String> pendingItens(final HttpServletRequest request) throws Exception {
		logger.debug(ConstantDataManager.DASHBOARD_PENDING_ITENS);
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;

		Connection connection = null;
		Connection connectionOldSystem = null;

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

			connectionOldSystem = FactoryConnection.getConnectionRetaguarda();
			if (connectionOldSystem == null) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}

			final List<MovementPendingDTO> itensOld = new ArrayList<MovementPendingDTO>();
			final DashboardService dashboardService = new DashboardServiceImpl();
			itensOld.addAll(dashboardService.getMovementPendingOldSystem(connectionOldSystem));

			final List<MovementPendingDTO> itens = new ArrayList<MovementPendingDTO>();
			itens.addAll(dashboardService.getMovementPending(connection));
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT_OLD, itensOld);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_REPORT, itens);
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
			if (connectionOldSystem != null) {
				connectionOldSystem.close();
			}
		}
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.STATUS_DESCRIPTION, status);
		result.put(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_DESCRIPTION, message);
		final String json = gson.toJson(result);
		return ResponseEntity.ok(json);
	}

	@PostMapping(value = ConstantDataManager.DASHBOARD_CONTROLLER_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception {

		String result = ConstantDataManager.DASHBOARD_COMPANY_EXECUTION_WEEK_CONTROLLER_OPEN_METHOD_RESULT;

		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;

		Connection connection = null;

		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
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

	@PostMapping(value = ConstantDataManager.FILTER_DASHBOARD_COMPANY_EXECUTION_WEEK_CONTROLLER)
	public ResponseEntity<String> filterDashboardCompanyExecutionWeek(final HttpServletRequest request,
			final Model model) throws Exception {
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

			final DashboardCompanyExecutionWeekDTO report = this.processFilter(request, connection);

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

	public DashboardCompanyExecutionWeekDTO processFilter(final HttpServletRequest request, final Connection connection)
			throws Exception {
		final DashboardCompanyExecutionWeekDTO dashboardCompanyExecutionWeek = new DashboardCompanyExecutionWeekDTO();
		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_DATE);

			if (StringUtils.isNull(dateParameter)) {
				LocalDateTime dateFrom = LocalDateTime
						.now(ZoneId.of(com.manager.systems.web.common.utils.ConstantDataManager.TIMEZONE_SAO_PAULO));
				dateFrom = dateFrom.plusDays(-45);

				LocalDateTime dateTo = LocalDateTime
						.now(ZoneId.of(com.manager.systems.web.common.utils.ConstantDataManager.TIMEZONE_SAO_PAULO));
				dateTo = dateTo.plusDays(15);

				String dateFromParameter = StringUtils.formatDate(dateFrom, StringUtils.DATE_PATTERN_DD_MM_YYYY);
				dashboardCompanyExecutionWeek.setDateFrom(dateFrom);
				dashboardCompanyExecutionWeek.setDateFromString(dateFromParameter);

				String dateToParameter = StringUtils.formatDate(dateTo, StringUtils.DATE_PATTERN_DD_MM_YYYY);
				dashboardCompanyExecutionWeek.setDateTo(dateTo);
				dashboardCompanyExecutionWeek.setDateToString(dateToParameter);
			}

			if (!StringUtils.isNull(dateParameter)) {
				final String[] dateArray = dateParameter
						.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
				dashboardCompanyExecutionWeek.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime(
						(dateArray[0].trim() + com.manager.systems.common.utils.ConstantDataManager.SPACE
								+ com.manager.systems.common.utils.ConstantDataManager.HOUR_ZERO),
						StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));

				String dateFromParameter = StringUtils.formatDate(dashboardCompanyExecutionWeek.getDateFrom(),
						StringUtils.DATE_PATTERN_DD_MM_YYYY);
				dashboardCompanyExecutionWeek.setDateFromString(dateFromParameter);

				dashboardCompanyExecutionWeek.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime(
						(dateArray[1].trim() + com.manager.systems.common.utils.ConstantDataManager.SPACE
								+ com.manager.systems.common.utils.ConstantDataManager.HOUR_FINAL),
						StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));

				String dateToParameter = StringUtils.formatDate(dashboardCompanyExecutionWeek.getDateTo(),
						StringUtils.DATE_PATTERN_DD_MM_YYYY);
				dashboardCompanyExecutionWeek.setDateToString(dateToParameter);
			}

			final String[] userChildrensParentParameter = request
					.getParameterValues(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			if (userChildrensParentParameter == null || userChildrensParentParameter.length == 0) {
				dashboardCompanyExecutionWeek.setUserChildrensParent(String.valueOf(user.getId()));
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
				dashboardCompanyExecutionWeek.setUserChildrensParent(userChildrensParentFilters.toString());
			}
			dashboardCompanyExecutionWeek.setUser(user.getId());

			final DashboardCompanyExecutionWeekService dashboardCompanyExecutionWeekService = new DashboardCompanyExecutionWeekServiceImpl(
					connection);
			dashboardCompanyExecutionWeekService.getAll(dashboardCompanyExecutionWeek);

		} catch (final Exception ex) {
			throw ex;
		}
		return dashboardCompanyExecutionWeek;
	}

	@PostMapping(value = ConstantDataManager.FILTER_DASHBOARD_MOVEMENT_CONTROLLER)
	public ResponseEntity<String> filterDashboardMovementGroups(final HttpServletRequest request,
			@RequestParam(value = ConstantDataManager.PARAMETER_TYPE) String type) throws Exception {
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

			final Map<String, Double> data = this.processFilterDashboard(request, type, connection);

			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_DATA, data);
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

	public Map<String, Double> processFilterDashboard(final HttpServletRequest request, final String type,
			final Connection connection) throws Exception {
		Map<String, Double> result = null;
		final DashboardCompanyExecutionWeekDTO dashboardCompanyExecutionWeek = new DashboardCompanyExecutionWeekDTO();

		try {
			final User user = (User) request.getSession()
					.getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if (user == null) {
				throw new LoginException(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}

			final String dateParameter = request.getParameter(ConstantDataManager.PARAMETER_FILTER_DATE);

			if (StringUtils.isNull(dateParameter)) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_INVALID_DATE, null));
			}

			final String[] dateArray = dateParameter.split(com.manager.systems.common.utils.ConstantDataManager.TRACO);
			if (dateArray != null && dateArray.length == 2) {
				final String[] dateHourArrayFrom = dateArray[0].trim()
						.split(com.manager.systems.common.utils.ConstantDataManager.SPACE);

				if (dateHourArrayFrom != null && dateHourArrayFrom.length == 2) {
					dashboardCompanyExecutionWeek.setDateFrom(StringUtils.convertStringDateHoraToLocalDateTime(
							(dateHourArrayFrom[0].trim() + com.manager.systems.common.utils.ConstantDataManager.SPACE
									+ dateHourArrayFrom[1]
									+ com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
									+ com.manager.systems.common.utils.ConstantDataManager.ZERO_ZERO),
							StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
				}

				final String[] dateHourArrayTo = dateArray[1].trim()
						.split(com.manager.systems.common.utils.ConstantDataManager.SPACE);

				if (dateHourArrayTo != null && dateHourArrayTo.length == 2) {
					dashboardCompanyExecutionWeek.setDateTo(StringUtils.convertStringDateHoraToLocalDateTime(
							(dateHourArrayTo[0].trim() + com.manager.systems.common.utils.ConstantDataManager.SPACE
									+ dateHourArrayTo[1]
									+ com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
									+ com.manager.systems.common.utils.ConstantDataManager.FIVE_NINE),
							StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
				}
			}

			final String[] userChildrensParentParameter = request
					.getParameterValues(ConstantDataManager.PARAMETER_USER_CHIDRENS_PARENT_COMBOBOX);
			if (userChildrensParentParameter == null || userChildrensParentParameter.length == 0) {
				dashboardCompanyExecutionWeek.setUserChildrensParent(String.valueOf(user.getId()));
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
				dashboardCompanyExecutionWeek.setUserChildrensParent(userChildrensParentFilters.toString());
			}
			dashboardCompanyExecutionWeek.setUser(user.getId());

			final String typeDashboardParameter = type;

			if (StringUtils.isNull(typeDashboardParameter)) {
				throw new Exception(this.messages
						.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_TYPE_INVALID, null));
			}

			switch (typeDashboardParameter) {
			case ConstantDataManager.CHART_TYPE_MOVEMENT_GROUPS:
				dashboardCompanyExecutionWeek.setTypeDashboard(1);
				break;
			case ConstantDataManager.CHART_TYPE_MOVEMENT_SUBGROUPS:
				dashboardCompanyExecutionWeek.setTypeDashboard(2);
				break;
			case ConstantDataManager.CHART_TYPE_TOP_COMPANY:
				dashboardCompanyExecutionWeek.setTypeDashboard(3);
				break;
			case ConstantDataManager.CHART_TYPE_AVERAGE_TOP_PRODUCT_COMPANY:
				dashboardCompanyExecutionWeek.setTypeDashboard(4);
				break;
			case ConstantDataManager.CHART_TYPE_AVERAGE_TOP_COMPANY:
				dashboardCompanyExecutionWeek.setTypeDashboard(5);
				break;
			case ConstantDataManager.CHART_TYPE_COMPANY_EXECUTION:
				dashboardCompanyExecutionWeek.setTypeDashboard(6);
				break;
			case ConstantDataManager.CHART_TYPE_PRODUCT_EXECUTION:
				dashboardCompanyExecutionWeek.setTypeDashboard(7);
				break;
			default:
				break;
			}

			final DashboardCompanyExecutionWeekService dashboardService = new DashboardCompanyExecutionWeekServiceImpl(
					connection);
			result = dashboardService.getChartMovements(dashboardCompanyExecutionWeek);
		} catch (final Exception ex) {
			throw ex;
		}
		return result;
	}
}