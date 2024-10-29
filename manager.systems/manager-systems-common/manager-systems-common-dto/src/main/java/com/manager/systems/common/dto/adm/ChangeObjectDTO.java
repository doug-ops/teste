package com.manager.systems.common.dto.adm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import  com.google.gson.Gson;  
import  com.google.gson.reflect.TypeToken;  

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeObjectDTO implements Serializable
{
	private static final long serialVersionUID = -3987913989528926680L;
	
	private int id;
	private String changeObjectName;
	private String changeDate;
	private String changeFields;
	private long changeUser;
	private String changeUserName;

	private Map<String, ChangeObjectItemDTO> fildes = new TreeMap<>();
	
	public void convertJsontOdMap(final String jsonOld, final String jsonNew){
		
		final Gson  gson = new Gson();
		final Map<String, String> userDataOld =  gson.fromJson(jsonOld,  new  TypeToken<HashMap<String, String>>() {  
        }.getType()); 
		

		final Map<String, String> userDataNew =  gson.fromJson(jsonNew,  new  TypeToken<HashMap<String, String>>() {  
        }.getType()); 

		for(final Map.Entry<String, String> entry : userDataOld.entrySet()) {
			final ChangeObjectItemDTO item = new ChangeObjectItemDTO();
			item.setValueOld(entry.getValue());
			this.fildes.put(entry.getKey(), item);
		}
		
		final StringBuilder changes = new StringBuilder();
		int countChange = 0;
		
		for(final Map.Entry<String, String> entry : userDataNew.entrySet()) {
			
			ChangeObjectItemDTO item = this.fildes.get(entry.getKey());
			if(item == null) {
				item = new ChangeObjectItemDTO();
			}
			item.setValueNew(entry.getValue());
			item.verifyChangeValue();
			if(item.isChange()) {
				countChange++;
				if(countChange > 1) {
					changes.append(", ");
				}
				changes.append(entry.getKey());				
			}				
			this.fildes.put(entry.getKey(), item);
		}
		
		this.changeFields = changes.toString();
	}

}