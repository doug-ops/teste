package com.manager.systems.movement.product.bravo.controller;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.manager.systems.common.dto.adm.CompanyIntegrationSystemDTO;
import com.manager.systems.common.dto.adm.IntegrationSystemsDTO;
import com.manager.systems.common.dto.adm.ProductDTO;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;
import com.manager.systems.common.service.IntegrationSystemsService;
import com.manager.systems.common.service.adm.CompanyService;
import com.manager.systems.common.service.adm.ProductService;
import com.manager.systems.common.service.impl.IntegrationSystemsServiceImpl;
import com.manager.systems.common.service.impl.adm.CompanyServiceImpl;
import com.manager.systems.common.service.impl.adm.ProductServiceImpl;
import com.manager.systems.common.service.impl.movement.product.MovementProductServiceImpl;
import com.manager.systems.common.service.movement.product.MovementProductService;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.ApplicationTypes;
import com.manager.systems.common.vo.ChangeData;
import com.manager.systems.common.vo.ObjectType;
import com.manager.systems.model.admin.User;
import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesJsonDTO;
import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesJsonItemDTO;
import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesRoomItemDTO;
import com.manager.systems.movement.product.bravo.dto.BravoDTO;
import com.manager.systems.movement.product.bravo.dto.BravoMachineDTO;
import com.manager.systems.movement.product.bravo.service.BillingBravoService;
import com.manager.systems.movement.product.bravo.service.impl.BillingBravoServiceImpl;
import com.manager.systems.movement.product.bravo.utils.ConstantDataManager;
import com.manager.systems.movement.product.bravo.utils.http.HttpURLConnection;
import com.manager.systems.web.common.controller.BaseController;

@Controller
@RequestMapping(value=ConstantDataManager.BRAVO_MOVEMENT_CONTROLLER)
public class BravoController extends BaseController
{	
	@PostMapping(value = ConstantDataManager.BRAVO_MOVEMENT_CONTROLLER_GET_DATA_MOVEMENT)
	public ResponseEntity<String> getMovement(final HttpServletRequest request) throws Exception
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
			
			final String companyIdParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANY_ID);
			if(!StringUtils.isLong(companyIdParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}			
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final ProductService productService = new ProductServiceImpl(connection);
			final CompanyService companyService = new CompanyServiceImpl(connection);
			
			final List<ProductDTO> productsCompany = productService.getAllProductsByCompany(Long.valueOf(companyIdParameter));
			final List<CompanyIntegrationSystemDTO> companyIntegrationsSystem = companyService.getCompanyIntegrationSystem(Long.valueOf(companyIdParameter));
			
			final List<ProductDTO> products = new ArrayList<>();
			
			if(productsCompany!=null && productsCompany.size()>0) {
				int countProductsCompany = productsCompany.size();
				
				final HttpURLConnection http = new HttpURLConnection();		
				CookieHandler.setDefault(new CookieManager());
				
				final BravoDTO bravo = new BravoDTO();
				if(user.getId() == 7 || user.getId() == 43) {
					bravo.setUsername(ConstantDataManager.USERNAME_BRAVO_BJ_SINC);
					bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO_BJ_SINC);				
				} else if(user.getId() == 27  || user.getId() == 46) {
					bravo.setUsername(ConstantDataManager.USERNAME_BRAVO_BJ_POA_SINC);
					bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO_BJ_POA_SINC);			
				} else {
					bravo.setUsername(ConstantDataManager.USERNAME_BRAVO);
					bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO);				
				}

				final BillingBravoService billingBravoService = new BillingBravoServiceImpl();
				billingBravoService.login(bravo, http);
							
				final List<BillingBravoGamesRoomItemDTO> allRooms = billingBravoService.getRooms(bravo, http);	
				
				List<BillingBravoGamesRoomItemDTO> rooms = null;
				for(final CompanyIntegrationSystemDTO companyIntegrationSystem : companyIntegrationsSystem) {
					if(companyIntegrationSystem != null && !StringUtils.isNull(companyIntegrationSystem.getBravoRoomId())) {
						if(rooms == null) {
							rooms = new ArrayList<>();
						}
						rooms.addAll(allRooms.parallelStream().filter(e -> e.getUrlMachinesRoom().toUpperCase().indexOf("/"+companyIntegrationSystem.getBravoRoomId()+"/") > -1).collect(Collectors.toList()));
					} else if(companyIntegrationSystem != null && !StringUtils.isNull(companyIntegrationSystem.getCompanyDescription())) {
						if(rooms == null) {
							rooms = new ArrayList<>();
						}
						rooms.addAll(allRooms.parallelStream().filter(e -> e.getRoomName().toUpperCase().indexOf(companyIntegrationSystem.getCompanyDescription().toUpperCase()) > -1).collect(Collectors.toList()));
					}
				}
				
				if(rooms == null || rooms.size() == 0) {
					rooms = allRooms;
				}
			
				for (final BillingBravoGamesRoomItemDTO room : rooms) {
					BillingBravoGamesJsonDTO movements = null;
					try {
						movements = billingBravoService.getMovements(room, bravo, http);
					} catch (final Exception e) {
						// TODO: handle exception
					}
					
					if(movements == null) {
						continue;
					}
					
					
					for (final BillingBravoGamesJsonItemDTO machine : movements.getRows()) {
						final BravoMachineDTO machineData =  machine.getMachine();
						
						if(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ACUMULADO_DISPLAY_BRAZIL.equalsIgnoreCase(machineData.getMachineDescription()) ||
						   com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_SUN_BRAZIL_M.equalsIgnoreCase(machineData.getMachineDescription())) {
							continue;
						}

						for(final ProductDTO item : productsCompany) {
							if(Long.parseLong(item.getId())==machineData.getMachineId()) {							
								final ProductDTO product = new ProductDTO();
								product.setId(String.valueOf(machineData.getMachineId()));
								product.setDescription(machineData.getMachineDescription().toUpperCase());					
								product.setCompanyId(companyIdParameter);
								product.setInputMovement(String.valueOf(machineData.getInputMovement()) + com.manager.systems.web.common.utils.ConstantDataManager.TWO_ZERO_STRING);
								product.setOutputMovement(String.valueOf(machineData.getOutputMovement())+ com.manager.systems.web.common.utils.ConstantDataManager.TWO_ZERO_STRING);
								product.setClockMovement(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
								products.add(product);
								break;
							}
						}
						
						if(countProductsCompany == products.size()) {
							break;
						}
					}
					if(countProductsCompany == products.size()) {
						break;
					}
				}
			}
			
			if(products != null && products.size() > 0) {
				for (final ProductDTO productBravo : products) {	
					
					final MovementProductDTO movementProduct = new MovementProductDTO();
					movementProduct.setProductId(Long.parseLong(productBravo.getId()));
					movementProduct.setCompanyId(Long.parseLong(productBravo.getCompanyId()));
					movementProduct.setFinalInput(Long.parseLong(productBravo.getInputMovement()));
					movementProduct.setFinalOutput(Long.parseLong(productBravo.getOutputMovement()));
					movementProduct.setReadingDate(LocalDateTime.now(ZoneId.of(com.manager.systems.web.common.utils.ConstantDataManager.TIMEZONE_SAO_PAULO)));
					movementProduct.setCompanyDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
					movementProduct.setMovementType(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_MOVEMENT_CLOSED);
					movementProduct.setOffline(true);

					final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
					status = movementProductService.saveSynchronize(movementProduct);
						
					if(!status)
					{
						throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
					}					
				}
				
				message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.BRAVO_SYNCHONYZE_DATA_SUCCESS, null);
				status = true;	
			} else {
				message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.BRAVO_SYNCHONYZE_DATA_NOT_FOUND, null);
				status = true;	
			}
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
	
	@PostMapping(value = ConstantDataManager.BRAVO_MOVEMENT_CONTROLLER_GET_DATA_MOVEMENT_COMPANYS)
	public ResponseEntity<String> getMovementCompanys(final HttpServletRequest request) throws Exception
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
			
			final String companysIdParameter = request.getParameter(ConstantDataManager.PARAMETER_COMPANYS_ID);
			if(StringUtils.isNull(companysIdParameter))
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.MESSAGE_COMPANY_INVALID, null));
			}			
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final ProductService productService = new ProductServiceImpl(connection);
			final CompanyService companyService = new CompanyServiceImpl(connection);
			
			final List<ProductDTO> products = new ArrayList<>();
			
			final String[] companysArray = companysIdParameter.split(com.manager.systems.common.utils.ConstantDataManager.VIRGULA_STRING);
			if(companysArray != null && companysArray.length > 0) {
				
				final HttpURLConnection http = new HttpURLConnection();		
				CookieHandler.setDefault(new CookieManager());
				
				final BravoDTO bravo = new BravoDTO();
				if(user.getId() == 7 || user.getId() == 43) {
					bravo.setUsername(ConstantDataManager.USERNAME_BRAVO_BJ_SINC);
					bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO_BJ_SINC);				
				} else if(user.getId() == 27) {
					bravo.setUsername(ConstantDataManager.USERNAME_BRAVO_BJ_POA_SINC);
					bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO_BJ_POA_SINC);			
				} else {
					bravo.setUsername(ConstantDataManager.USERNAME_BRAVO);
					bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO);				
				}

				final BillingBravoService billingBravoService = new BillingBravoServiceImpl();
				billingBravoService.login(bravo, http);
							
				final List<BillingBravoGamesRoomItemDTO> allRooms = billingBravoService.getRooms(bravo, http);	
				
				for (final String companyIdParameter : companysArray) {
					final List<ProductDTO> productsCompany = productService.getAllProductsByCompany(Long.valueOf(companyIdParameter));
					final List<CompanyIntegrationSystemDTO> companyIntegrationsSystem = companyService.getCompanyIntegrationSystem(Long.valueOf(companyIdParameter));
					
					if(productsCompany!=null && productsCompany.size()>0) {
						final int countProductsCompany = productsCompany.size();
												
						List<BillingBravoGamesRoomItemDTO> rooms = new ArrayList<>();
						for(final CompanyIntegrationSystemDTO companyIntegrationSystem : companyIntegrationsSystem) {
							if(companyIntegrationSystem != null && !StringUtils.isNull(companyIntegrationSystem.getBravoRoomId())) {
								if(rooms == null) {
									rooms = new ArrayList<>();
								}
								rooms.addAll(allRooms.parallelStream().filter(e -> e.getUrlMachinesRoom().toUpperCase().indexOf("/"+companyIntegrationSystem.getBravoRoomId()+"/") > -1).collect(Collectors.toList()));
							} else if(companyIntegrationSystem != null && !StringUtils.isNull(companyIntegrationSystem.getCompanyDescription())) {
								if(rooms == null) {
									rooms = new ArrayList<>();
								}
								rooms.addAll(allRooms.parallelStream().filter(e -> e.getRoomName().toUpperCase().indexOf(companyIntegrationSystem.getCompanyDescription().toUpperCase()) > -1).collect(Collectors.toList()));
							}
						}
					
						for (final BillingBravoGamesRoomItemDTO room : rooms) {
							BillingBravoGamesJsonDTO movements = null;
							try {
								movements = billingBravoService.getMovements(room, bravo, http);
							} catch (final Exception e) {
								// TODO: handle exception
							}
							
							if(movements == null) {
								continue;
							}
							
							
							for (final BillingBravoGamesJsonItemDTO machine : movements.getRows()) {
								final BravoMachineDTO machineData =  machine.getMachine();
								
								if(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ACUMULADO_DISPLAY_BRAZIL.equalsIgnoreCase(machineData.getMachineDescription()) ||
								   com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_SUN_BRAZIL_M.equalsIgnoreCase(machineData.getMachineDescription())) {
									continue;
								}

								for(final ProductDTO item : productsCompany) {
									if(Long.parseLong(item.getId())==machineData.getMachineId()) {							
										final ProductDTO product = new ProductDTO();
										product.setId(String.valueOf(machineData.getMachineId()));
										product.setDescription(machineData.getMachineDescription().toUpperCase());					
										product.setCompanyId(companyIdParameter);
										product.setInputMovement(String.valueOf(machineData.getInputMovement()) + com.manager.systems.web.common.utils.ConstantDataManager.TWO_ZERO_STRING);
										product.setOutputMovement(String.valueOf(machineData.getOutputMovement())+ com.manager.systems.web.common.utils.ConstantDataManager.TWO_ZERO_STRING);
										product.setClockMovement(com.manager.systems.web.common.utils.ConstantDataManager.ZERO_STRING);
										products.add(product);
										break;
									}
								}
								
								if(countProductsCompany == products.size()) {
									break;
								}
							}
							if(countProductsCompany == products.size()) {
								break;
							}
						}
					}
				}
			}
			
			if(products != null && products.size() > 0) {
				for (final ProductDTO productBravo : products) {	
					
					final MovementProductDTO movementProduct = new MovementProductDTO();
					movementProduct.setProductId(Long.parseLong(productBravo.getId()));
					movementProduct.setCompanyId(Long.parseLong(productBravo.getCompanyId()));
					movementProduct.setFinalInput(Long.parseLong(productBravo.getInputMovement()));
					movementProduct.setFinalOutput(Long.parseLong(productBravo.getOutputMovement()));
					movementProduct.setReadingDate(LocalDateTime.now(ZoneId.of(com.manager.systems.web.common.utils.ConstantDataManager.TIMEZONE_SAO_PAULO)));
					movementProduct.setCompanyDescription(com.manager.systems.common.utils.ConstantDataManager.BLANK);
					movementProduct.setMovementType(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_MOVEMENT_CLOSED);
					movementProduct.setOffline(true);

					final MovementProductService movementProductService = new MovementProductServiceImpl(connection);
					status = movementProductService.saveSynchronize(movementProduct);
						
					if(!status)
					{
						throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_ERROR, null));
					}					
				}
				
				message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.BRAVO_SYNCHONYZE_DATA_SUCCESS, null);
				status = true;	
			} else {
				message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.BRAVO_SYNCHONYZE_DATA_NOT_FOUND, null);
				status = true;	
			}
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
	
	@PostMapping(value = ConstantDataManager.BRAVO_MOVEMENT_CONTROLLER_SYNCHRONIZE_PRODUCT)
	public ResponseEntity<String> sincronizeProduct(final HttpServletRequest request) throws Exception
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
			
			final HttpURLConnection http = new HttpURLConnection();		
			CookieHandler.setDefault(new CookieManager());
			
			final BravoDTO bravo = new BravoDTO();
			if(user.getId() == 7 || user.getId() == 43) {
				bravo.setUsername(ConstantDataManager.USERNAME_BRAVO_BJ_SINC);
				bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO_BJ_SINC);				
			} else if(user.getId() == 27) {
				bravo.setUsername(ConstantDataManager.USERNAME_BRAVO_BJ_POA_SINC);
				bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO_BJ_POA_SINC);			
			} else {
				bravo.setUsername(ConstantDataManager.USERNAME_BRAVO);
				bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO);				
			}

			final BillingBravoService billingBravoService = new BillingBravoServiceImpl();
			billingBravoService.login(bravo, http);		
			
			final List<ProductDTO> products = new ArrayList<>();			
			final List<BillingBravoGamesRoomItemDTO> rooms = billingBravoService.getRooms(bravo, http);
					
			for (final BillingBravoGamesRoomItemDTO room : rooms) {
				
				BillingBravoGamesJsonDTO movements = null;
				try {
					movements = billingBravoService.getMovements(room, bravo, http);
				} catch (final Exception e) {
					// TODO: handle exception
				}
				
				if(movements == null) {
					continue;
				}
				
				for (final BillingBravoGamesJsonItemDTO machine : movements.getRows()) {
					final BravoMachineDTO machineData =  machine.getMachine();
					
					if(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ACUMULADO_DISPLAY_BRAZIL.equalsIgnoreCase(machineData.getMachineDescription()) ||
							   com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_SUN_BRAZIL_M.equalsIgnoreCase(machineData.getMachineDescription())) {
								continue;
					}
			
					final boolean isBJM = machineData.getContract().contains("BJM");
					final boolean isSPA = machineData.getContract().contains("SPA");
					
					final ProductDTO product = new ProductDTO();
					product.setId(String.valueOf(machineData.getMachineId()));
					product.setDescription(machineData.getMachineDescription().toUpperCase());
					product.setGroupId("6");
					product.setSubGroupId("2");
					product.setCostPrice("0.01");
					product.setSalePrice("0.01");
					product.setInputMovement("0");
					product.setOutputMovement("0");
					product.setClockMovement("0");
					product.setConversionFactor("0.01");
					if(isSPA) {
						product.setCompanyId("142");						
					} else if(isBJM) {
						product.setCompanyId("17");
					}
					product.setInactive(false);
					final ChangeData changeData = new ChangeData();
					changeData.setUserChange(user.getId());
					changeData.setChangeDate(Calendar.getInstance(tz).getTime());
					product.setChangeData(changeData);
					product.addIntegrationSystemsValue((StringUtils.replaceNonDigits(room.getUrlMachinesRoom())+com.manager.systems.common.utils.ConstantDataManager.UNDERSCORE+product.getId()), "3");
					product.validateFormSave();
					
					if(isBJM || isSPA && !StringUtils.isNull(product.getId())) {
						products.add(product);						
					}
				}
			}					
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			final ProductService productService = new ProductServiceImpl(connection);	
			
			for (final ProductDTO productBravo : products) {
				final boolean isSaved = productService.saveBravo(productBravo);			
				if(isSaved) {
					final IntegrationSystemsService integrationSystemsService = new IntegrationSystemsServiceImpl(connection);
					final IntegrationSystemsDTO integrationSystem = new IntegrationSystemsDTO();
					integrationSystem.setObjectId(productBravo.getId());
					integrationSystem.setObjectType(ObjectType.PRODUCT.getType());
					integrationSystem.setInactive(true);
					integrationSystem.setUserChange(productBravo.getChangeData().getUserChange());
					integrationSystem.setChangeDate(Calendar.getInstance(tz));
					integrationSystemsService.delete(integrationSystem);
					
					if(productBravo.getIntegrationSystemsValues()!=null && productBravo.getIntegrationSystemsValues().size()>0)
					{			
						for (final IntegrationSystemsDTO item : productBravo.getIntegrationSystemsValues()) 
						{
							integrationSystem.setInactive(false);
							integrationSystem.setObjectType(ObjectType.PRODUCT.getType());
							integrationSystemsService.save(item);	
						}
					}	
				}
			}
			
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_SAVE_DATA_SUCCESS, null);
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
	
	@GetMapping(value = ConstantDataManager.BRAVO_MOVEMENT_CONTROLLER_GET_DATA_ROOM)
	public ResponseEntity<String> sincronizeRoom(final HttpServletRequest request) throws Exception
	{
		final Map<String, Object> result = new TreeMap<String, Object>();
		final Gson gson = new Gson();
		
		String message = com.manager.systems.web.common.utils.ConstantDataManager.BLANK;
		boolean status = false;
		
		Connection connection = null;
		
		try
		{
			final User user = new User();
			user.setId(8);
			//final User user = (User) request.getSession().getAttribute(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_USER);
			//if(user==null)
			//{
			//	throw new LoginException(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.LOGIN_ACCESS_EXPIRED, null));
			//}	
				
			final HttpURLConnection http = new HttpURLConnection();		
			CookieHandler.setDefault(new CookieManager());
			
			final BravoDTO bravo = new BravoDTO();
			if(user.getId() == 7 || user.getId() == 43) {
				bravo.setUsername(ConstantDataManager.USERNAME_BRAVO_BJ_SINC);
				bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO_BJ_SINC);				
			} else if(user.getId() == 27) {
				bravo.setUsername(ConstantDataManager.USERNAME_BRAVO_BJ_POA_SINC);
				bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO_BJ_POA_SINC);			
			} else {
				bravo.setUsername(ConstantDataManager.USERNAME_BRAVO);
				bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO);				
			}

			BillingBravoService billingBravoService = new BillingBravoServiceImpl();
			billingBravoService.login(bravo, http);
							
			final List<BillingBravoGamesRoomItemDTO> rooms = billingBravoService.getRooms(bravo, http);	
					
			for (final BillingBravoGamesRoomItemDTO room : rooms) {
				BillingBravoGamesJsonDTO movements = null;
				try {
					movements = billingBravoService.getMovements(room, bravo, http);
				} catch (final Exception e) {
					// TODO: handle exception
				}
				
				if(movements == null) {
					continue;
				}
				
				
				for (final BillingBravoGamesJsonItemDTO machine : movements.getRows()) {
					final BravoMachineDTO machineData =  machine.getMachine();
					
					final String roomIdString = room.getRoomIdString();
					if(StringUtils.isLong(roomIdString)) {
						room.setRoomId(Long.valueOf(roomIdString));
						machineData.setRoomId(room.getRoomId());						
					}
					else {
						continue;
					}
					
					if(com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_ACUMULADO_DISPLAY_BRAZIL.equalsIgnoreCase(machineData.getMachineDescription()) ||
					   com.manager.systems.web.common.utils.ConstantDataManager.PARAMETER_SUN_BRAZIL_M.equalsIgnoreCase(machineData.getMachineDescription())) {
						continue;
					}
					
					room.addProduct(machineData);
				}
			}
			
			
			connection = this.connectionFactory.getConnection(ApplicationTypes.PORTAL);
			if(connection==null)
			{
				throw new Exception(this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.GLOBAL_CONNECTION_ERROR, null));
			}
			
			billingBravoService = new BillingBravoServiceImpl(connection);
			
			for (final BillingBravoGamesRoomItemDTO room : rooms) {
				billingBravoService.saveRoom(room);
				if(room.getProducts() != null && room.getProducts().size() > 0) {
					for (final BravoMachineDTO product : room.getProducts()) {
						billingBravoService.saveRoomItem(product);
					}					
				}
			}
			message = this.messages.get(com.manager.systems.web.common.utils.ConstantDataManager.BRAVO_SYNCHONYZE_DATA_SUCCESS, null);
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
	
//	public static void main(final String[] args) throws Exception{
//		
//		final HttpURLConnection http = new HttpURLConnection();		
//		CookieHandler.setDefault(new CookieManager());
//		
//		final BravoDTO bravo = new BravoDTO();
//		bravo.setUsername(ConstantDataManager.USERNAME_BRAVO);
//		bravo.setPassword(ConstantDataManager.PASSWORD_BRAVO);
//
//		final BillingBravoService billingBravoService = new BillingBravoServiceImpl();
//		billingBravoService.login(bravo, http);		
//		System.out.println(bravo.getToken());
//		
//		final BillingBravoGamesJsonDTO machines = billingBravoService.getMachines(bravo, http);
//		System.out.println(machines.toString());
//		
//		for (final BillingBravoGamesJsonItemDTO machine : machines.getRows()) {
//			final BravoMachineDTO machineData =  machine.getMachine();
//			System.out.println(machineData.toString());
//		}
//		
//		final List<BillingBravoGamesRoomItemDTO> rooms = billingBravoService.getRooms(bravo, http);
//		System.out.println(rooms.toString());
//		
//		for (final BillingBravoGamesRoomItemDTO room : rooms) {
//			final BillingBravoGamesJsonDTO movements = billingBravoService.getMovements(room, bravo, http);
//			System.out.println(movements.toString());
//		}
//	}
}