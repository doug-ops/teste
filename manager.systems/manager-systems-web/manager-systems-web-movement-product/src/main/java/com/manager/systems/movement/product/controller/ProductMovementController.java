package com.manager.systems.movement.product.controller;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.dao.FactoryConnection;
import com.manager.systems.common.dto.CompanyLastMovementExecutedDTO;
import com.manager.systems.common.dto.adm.CompanyDTO;
import com.manager.systems.common.dto.adm.ProductDTO;
import com.manager.systems.common.dto.adm.ReportProductDTO;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;
import com.manager.systems.common.service.adm.ProductService;
import com.manager.systems.common.service.adm.UserCompanyService;
import com.manager.systems.common.service.impl.adm.ProductServiceImpl;
import com.manager.systems.common.service.impl.adm.UserCompanyServiceImpl;
import com.manager.systems.common.service.impl.movement.product.MovementProductServiceImpl;
import com.manager.systems.common.service.movement.product.MovementProductService;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.Combobox;
import com.manager.systems.model.admin.User;
import com.manager.systems.movement.product.utils.ConstantDataManager;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value=ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER)
public class ProductMovementController extends BaseController
{
	private static final Log log = LogFactory.getLog(ProductMovementController.class);
	
	@GetMapping(value = ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD)
	public String open(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER + ConstantDataManager.COMMON_CONTROLLER_MAINTENANCE_OPEN_METHOD);
		
		String result = ConstantDataManager.PRODUCT_MOVEMENT_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
		Connection connection = null;
		
		try
		{ 		
			final String selectedCompany = request.getParameter(ConstantDataManager.SELECTED_COMPANY);
			
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			if(!user.hasPermission("MENU_OFF_LINE", "MENU_OFF_LINE_MOVIMENTO_PRODUTO")) {
				result = ConstantDataManager.RESULT_REDIRECT_HOME;
				throw new Exception("Usuário: " + user.getName() + " sem permissão para acesso ao menu Movimento Produto.");
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final UserCompanyService userCompanyService = new UserCompanyServiceImpl(connection);
			final List<CompanyDTO> companyFilter = userCompanyService.get(user.getId());
			final List<CompanyDTO> companys = companyFilter.parallelStream().filter(x -> !x.isProcessMovementAutomatic()).collect(Collectors.toList());
			
			request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_COMPANYS, companys);
			model.addAttribute(ConstantDataManager.SELECTED_COMPANY, selectedCompany);
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
	
	@PostMapping(value = ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_GETALL_PRODUCTS_COMPANY)
	public ResponseEntity<String> getAllProductsByCompany(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER + ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_GETALL_PRODUCTS_COMPANY);
		
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
			
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if(!StringUtils.isLong(companyParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final ProductService productService = new ProductServiceImpl(connection);
			final ReportProductDTO filter = new ReportProductDTO();
			filter.setCompanyId(companyParameter);
			filter.setInactive(com.manager.systems.common.utils.ConstantDataManager.ZERO_STRING);
			filter.setUser(user.getId());
			final List<Combobox> products = productService.getAllAutocompleteByCompany(filter);
			
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_PRODUCTS, products);
			
			final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
			final CompanyLastMovementExecutedDTO lastMovementExecuted = movementProductService.companyLastMovementExecuted(Long.valueOf(filter.getCompanyId()), user.getId());
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_LAST_MOVEMENT_COMPANY, lastMovementExecuted);

			status = true;
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
	
	@PostMapping(value = ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_GET_DATA_PRODUCT)
	public ResponseEntity<String> getDataProduct(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER + ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_GETALL_PRODUCTS_COMPANY);
		
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
			
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if(!StringUtils.isLong(companyParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}
			
			final String productParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_ID);
			if(!StringUtils.isLong(productParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_INVALID, null));
			}			
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final MovementProductDTO movementProduct = new MovementProductDTO();
			movementProduct.setCompanyId(Long.valueOf(companyParameter));
			movementProduct.setProductId(Long.valueOf(productParameter));
			
			final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
			
			movementProductService.get(movementProduct);
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_PRODUCT, movementProduct);
						
			status = true;
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
	
	@PostMapping(value = ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_SAVE_DATA_PRODUCT)
	public ResponseEntity<String> saveDataProduct(final HttpServletRequest request) throws Exception
	{
		log.info(ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER + ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_SAVE_DATA_PRODUCT);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		try
		{
			final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			if(user==null || user.getId()==0)
			{
				throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			}
			
			final String movementIdParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_MOVEMENT_ID);
			if(!StringUtils.isLong(movementIdParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_MOVEMENT_ID_INVALID, null));
			}
			
			final String companyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if(!StringUtils.isLong(companyParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}
			
			final String productParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_ID);
			if(!StringUtils.isLong(productParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_INVALID, null));
			}			
			
			final String initialInputParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_INITIAL_INPUT);
			if(!StringUtils.isLong(initialInputParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_INITIAL_INPUT_INVALID, null));
			}
			final String finalInputParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_FINAL_INPUT);
			if(!StringUtils.isLong(finalInputParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_FINAL_INPUT_INVALID, null));
			}
			final String initialOutputParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_INITIAL_OUTPUT);
			if(!StringUtils.isLong(initialOutputParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_INITIAL_OUTPUT_INVALID, null));
			}
			final String finalOutputParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_FINAL_OUTPUT);
			if(!StringUtils.isLong(finalOutputParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_FINAL_OUTPUT_INVALID, null));
			}
			
			if(Long.parseLong(initialInputParameter)>Long.parseLong(finalInputParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_FINAL_INPUT_LESS_THAN_INVALID, new String[] {initialInputParameter}));				
			}
			if(Long.parseLong(initialOutputParameter)>Long.parseLong(finalOutputParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_FINAL_OUTPUT_LESS_THANINVALID, new String[] {initialOutputParameter}));				
			}
			
			final MovementProductDTO movementProduct = new MovementProductDTO();
			movementProduct.setMovementId(Long.parseLong(movementIdParameter));
			movementProduct.setCompanyId(Long.parseLong(companyParameter));
			movementProduct.setProductId(Long.parseLong(productParameter));
			movementProduct.setInitialInput(Long.parseLong(initialInputParameter));
			movementProduct.setFinalInput(Long.parseLong(finalInputParameter));
			movementProduct.setInitialOutput(Long.parseLong(initialOutputParameter));
			movementProduct.setFinalOutput(Long.parseLong(finalOutputParameter));
			movementProduct.setCompanyDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
			movementProduct.setMovementType(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_MOVEMENT_CLOSED);
			movementProduct.setInactive(false);
			movementProduct.setChangeUser(user.getId());
			movementProduct.setReadingDate(LocalDateTime.now(ZoneId.of(com.manager.systems.web.common.utils.ConstantDataManager.TIMEZONE_SAO_PAULO)));
			movementProduct.setOffline(true);
			
			final String enableClockParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_ENABLE_CLOCK);
			if(StringUtils.isLong(enableClockParameter) && com.manager.systems.common.utils.ConstantDataManager.TRUE_INT.equalsIgnoreCase(enableClockParameter))
			{
				final String initialClockParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_INITIAL_CLOCK);
				final String finalClockParameter = request.getParameter(ConstantDataManager.PARAMETER_PRODUCT_FINAL_CLOCK);
				if(!StringUtils.isLong(initialClockParameter))
				{
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_INITIAL_CLOCK_INVALID, null));
				}
				if(!StringUtils.isLong(finalClockParameter))
				{
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_FINAL_CLOCK_INVALID, null));
				}				
				if(Long.parseLong(initialClockParameter)>Long.parseLong(finalClockParameter))
				{
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_PRODUCT_FINAL_CLOCK_LESS_THAN_INVALID, new String[] {initialClockParameter}));				
				}
				
				movementProduct.setInitialClock(Long.parseLong(initialClockParameter));
				movementProduct.setFinalClock(Long.parseLong(finalClockParameter));
			}		
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
						
			final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
			status = movementProductService.save(movementProduct);
			
			if(!status)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
			}
			
			final String companyDestinyParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID_DESTINY);
			if(StringUtils.isLong(companyDestinyParameter)) {
				ProductService productService = new ProductServiceImpl(connection);
				ProductDTO filterProduct = new ProductDTO();
				filterProduct.setId(productParameter);
				productService.get(filterProduct);
				filterProduct.setCompanyId(companyDestinyParameter);
				productService.save(filterProduct);				
			}
			
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
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
	
	@GetMapping(value = ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_GETL_MOVEMENT_PRODUCT_OFF_SYSTEM_OLD_BY_COMPANY)
	public ResponseEntity<String> getMovementProductOffSystemOldByCompany(@PathVariable(name = ConstantDataManager.PARAMETER_COMPANY_ID) final Long companyId) throws Exception
	{
		log.info(ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER + ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_GETL_MOVEMENT_PRODUCT_OFF_SYSTEM_OLD_BY_COMPANY);
		
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		try
		{
			if(companyId==0)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			ProductService productService = new ProductServiceImpl(connection);
			final List<ProductDTO> products = productService.getAllProductsByCompany(companyId);
			
			boolean hasFilter = false;
			final StringBuilder filter = new StringBuilder();
			filter.append("select "); 
			filter.append("f.codigo_produto id, f.et_inicial initial_input, max(f.et_final) final_input , f.sa_inicial initial_output, max(f.sa_final) final_output, max(f.data) as reading_date "); 
			filter.append("from "); 
			filter.append("tb_bin_turno_fechamento f with (nolock) "); 
			filter.append("inner join "); 
			filter.append("(");
			if(products!=null && products.size()>0) {
				hasFilter = true;
				int count = 0;
				for (final ProductDTO item : products) 
				{
					if(count>0) {
						filter.append("union all ");
					}
					filter.append("select "+item.getId()+" codigo_produto, "+item.getInputMovement()+" et_inicial, "+item.getOutputMovement()+" sa_inicial ");
					count++;
				}
			}	
			filter.append(") x on f.codigo_produto=x.codigo_produto and f.et_inicial=x.et_inicial and f.et_inicial=x.et_inicial "); 
			filter.append("group by f.codigo_produto, f.et_inicial, f.sa_inicial" );
			
			final List<MovementProductDTO> movements = new ArrayList<>();
			if(hasFilter) {
				if(connection!=null)
				{
					connection.close();
				}
				connection = null;
				
				connection = FactoryConnection.getConnectionRetaguarda();				
				if(connection==null) {
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
				}
				productService = new ProductServiceImpl(connection);
				movements.addAll(productService.getMovmentProductsSystemOld(filter.toString()));				
			}		
			
			if(movements.size()>0) {
				if(connection!=null)
				{
					connection.close();
				}
				connection = null;
				
				connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
				if(connection==null)
				{
					throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
				}
				
				final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
				productService = new ProductServiceImpl(connection);
				for (final MovementProductDTO movementProduct : movements) 
				{
					movementProduct.setOffline(true);
					movementProduct.setCompanyId(companyId);
					movementProduct.setMovementId(0);								
					movementProduct.setCompanyDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
					movementProduct.setMovementType(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_MOVEMENT_CLOSED);
					movementProduct.setInactive(false);
					movementProduct.setChangeUser(1);
					movementProduct.setReadingDate(movementProduct.getReadingDate());
									
					status = movementProductService.save(movementProduct);
					if(!status)
					{
						throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
					}
					final ProductDTO product = new ProductDTO();
					product.setId(String.valueOf(movementProduct.getProductId()));
					productService.get(product);
					product.setInputMovement(String.valueOf(movementProduct.getInitialInput()));
					product.setOutputMovement(String.valueOf(movementProduct.getInitialOutput()));
					productService.save(product);
				}
			}
			
			
			result.put(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_PRODUCTS, movements);
			status = true;
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
	
	@GetMapping(value = ConstantDataManager.PRODUCT_WITHDRAWAL_MOVEMENT_CONTROLLER)
	public String productWithdrawalMovement(final HttpServletRequest request, final Model model) throws Exception 
	{
		log.info(ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER + ConstantDataManager.PRODUCT_MOVEMENT_CONTROLLER_GETL_MOVEMENT_PRODUCT_OFF_SYSTEM_OLD_BY_COMPANY);
		
		String result = ConstantDataManager.PRODUCT_WITHDRAWA_MOVEMENT_OPEN_METHOD_RESULT;
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		
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
			
			final UserCompanyService userCompanyService = new UserCompanyServiceImpl(connection);
			final List<CompanyDTO> companys = userCompanyService.get(user.getId());
			request.setAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_COMPANYS, companys);
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
}
