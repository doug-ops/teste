package com.manager.systems.movement.product.bravo.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.manager.systems.movement.product.bravo.dao.BillingBravoDao;
import com.manager.systems.movement.product.bravo.dao.impl.BillingBravoDaoImpl;
import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesJsonDTO;
import com.manager.systems.movement.product.bravo.dto.BillingBravoGamesRoomItemDTO;
import com.manager.systems.movement.product.bravo.dto.BravoDTO;
import com.manager.systems.movement.product.bravo.dto.BravoMachineDTO;
import com.manager.systems.movement.product.bravo.service.BillingBravoService;
import com.manager.systems.movement.product.bravo.utils.ConstantDataManager;
import com.manager.systems.movement.product.bravo.utils.http.HttpURLConnection;
import com.manager.systems.movement.product.bravo.utils.http.HttpsURL;

public class BillingBravoServiceImpl implements BillingBravoService {
	
	private Gson gson = null;
	private BillingBravoDao billingBravoDao;
	
	public BillingBravoServiceImpl() {
		super();
		this.gson = new Gson(); 
	}

	public BillingBravoServiceImpl(final Connection connection) {
		super();
		this.billingBravoDao = new BillingBravoDaoImpl(connection);
	}
	
	@Override
	public void login(final BravoDTO bravo, final HttpURLConnection http) throws Exception {		
		final String loginPage = http.getPageContent(ConstantDataManager.LOGIN_BRAVO_URL);
		final String postParams = HttpURLConnection.getFormParams(loginPage, bravo.getUsername(), bravo.getPassword());
		
		final HttpsURL httpsURLVO = new HttpsURL();
		httpsURLVO.setHost(ConstantDataManager.HOST_BRAVO_URL);
		httpsURLVO.setReferer(ConstantDataManager.LOGIN_BRAVO_URL);
		httpsURLVO.setUrl(ConstantDataManager.LOGIN_BRAVO_URL);
		httpsURLVO.setParams(postParams);		
		http.sendPost(httpsURLVO);
		
		//Get TOKEN
		String html = http.getPageContent(ConstantDataManager.MACHINES_BRAVO_URL);		
		final Document documentHtmlToken = Jsoup.parse(html);
		final List<Element> forms = documentHtmlToken.getElementsByTag(ConstantDataManager.FORM);
		
		loopToken:
		for (final Element element : forms) {
			final Elements inputElements = element.getElementsByTag(ConstantDataManager.INPUT);
			for (final Element inputElement : inputElements) 
			{
				final String key = inputElement.attr(ConstantDataManager.NAME);
				final String value = inputElement.attr(ConstantDataManager.VALUE);

				if (key.equals(ConstantDataManager.PARAM_CSR_TOKEN))
				{
					bravo.setToken(value);
					break loopToken;
				}				
			}			
		}
	}

	@Override
	public BillingBravoGamesJsonDTO getMachines(final BravoDTO bravo, final HttpURLConnection http) throws Exception {
		final StringBuilder urlMachinesContent = new StringBuilder();
		urlMachinesContent.append("https://billing.bravo.games/ajax/machines?");
		urlMachinesContent.append("csrfmiddlewaretoken="+bravo.getToken());
		urlMachinesContent.append("&fields=cb%2Cid%2Ctitle%2Cperiod%2CexpirationDate%2CmoneyIn%2CmoneyOut%2ClastMoneyInOutUpdate%2Ccontract%2Cusers");
		urlMachinesContent.append("&_search=false");
		urlMachinesContent.append("&nd=1505758111387");
		urlMachinesContent.append("&rows=1000&page=1&sidx=id&sord=desc");
		
		final String jsonMachines = http.getPageContent(urlMachinesContent.toString());		
		final BillingBravoGamesJsonDTO machines = this.gson.fromJson(jsonMachines, BillingBravoGamesJsonDTO.class);
		return machines;
	}

	@Override
	public List<BillingBravoGamesRoomItemDTO> getRooms(final BravoDTO bravo, final HttpURLConnection http) throws Exception {
		final List<BillingBravoGamesRoomItemDTO> rooms = new ArrayList<>();
		final StringBuilder urlRoomsContent = new StringBuilder();
		urlRoomsContent.append(ConstantDataManager.ROOMS_BRAVO_URL);
		final String htmlRooms = http.getPageContent(urlRoomsContent.toString());
		final Document documentHtmlRooms = Jsoup.parse(htmlRooms);
		final List<Element> tablesRooms = documentHtmlRooms.getElementsByTag(ConstantDataManager.TABLE);
		if(tablesRooms.size()>0) {
			for (final Element element : tablesRooms) {
				final List<Element> body = element.select(ConstantDataManager.TBODY);
				if(body.size()>0) {
					final List<Element> trs = body.get(0).select(ConstantDataManager.TR);
					if(trs.size()>0) {
						for (final Element tr : trs) {
							final List<Element> tds =  tr.select(ConstantDataManager.TD);
							int countTd = 0;
							final BillingBravoGamesRoomItemDTO room = new BillingBravoGamesRoomItemDTO(); 
							for (final Element td : tds) {
								switch (countTd) {
								case 0:
									if(td.childNodes()!=null && td.childNodes().size()>0) {
									room.setTitleQuantityOfflineMachine(td.childNode(0).attr("title"));									
									}
									break;
								case 1:
									if(td.childNodes()!=null && td.childNodes().size()>0) {
										if(td.childNode(0).childNodes()!=null && td.childNode(0).childNodes().size()>0) {
											room.setRoomName(td.childNode(0).childNode(0).toString());									
										}
									}
									break;
								case 2:
									if(td.childNodes()!=null && td.childNodes().size()>0) {
										if(td.childNode(0).childNodes()!=null && td.childNode(0).childNodes().size()>0) {
									room.setServerId(Integer.valueOf(td.childNode(0).childNode(0).toString()));									
										}
									}
									break;
								case 3:
									if(td.childNodes()!=null && td.childNodes().size()>0) {
										if(td.childNode(0).childNodes()!=null && td.childNode(0).childNodes().size()>0) {
											room.setUrlMachinesRoom(td.childNode(0).attr("href"));								
											room.setQuantityMachine(Integer.valueOf(td.childNode(0).childNode(0).toString()));									
										}
									}	
									break;
								default:
									break;
								}
								countTd++;
							}
							rooms.add(room);
						}
					}
				}
			}			
		}
		return rooms;
	}

	@Override
	public BillingBravoGamesJsonDTO getMovements(final BillingBravoGamesRoomItemDTO room, final BravoDTO bravo, final HttpURLConnection http) throws Exception {
		final StringBuilder urlMachinesContent = new StringBuilder();
		urlMachinesContent.append("https://billing.bravo.games/ajax/");
		urlMachinesContent.append(room.getUrlMachinesRoom());
		urlMachinesContent.append("?csrfmiddlewaretoken="+bravo.getToken());
		urlMachinesContent.append("&fields=cb%2Cid%2Ctitle%2Ccontract%2Cperiod%2CexpirationDate%2CgamesPlayed%2CmoneyIn%2CmoneyOut%2Crevenue%2CoutInPayout%2ClastMoneyInOutUpdate%2ClastCounters%2ClastSeen&_search=false&nd=1603066369167&rows=1000&page=1&sidx=id&sord=asc");
		final String jsonMovments = http.getPageContent(urlMachinesContent.toString());		
		final BillingBravoGamesJsonDTO movements = this.gson.fromJson(jsonMovments, BillingBravoGamesJsonDTO.class);
		return movements;
	}

	@Override
	public boolean saveRoom(final BillingBravoGamesRoomItemDTO room) throws Exception {
		return this.billingBravoDao.saveRoom(room);
	}

	@Override
	public boolean saveRoomItem(final BravoMachineDTO roomItem) throws Exception {
		return this.billingBravoDao.saveRoomItem(roomItem);
	}
}