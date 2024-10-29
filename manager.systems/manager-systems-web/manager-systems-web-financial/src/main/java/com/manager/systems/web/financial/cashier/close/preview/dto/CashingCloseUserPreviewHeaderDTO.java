/*
 * Date create 07/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.dto;

import java.io.Serializable;
import java.util.List;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.vo.Combobox;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashingCloseUserPreviewHeaderDTO implements Serializable {

	private static final long serialVersionUID = 8886843840777094040L;
	
	private int weekYear;
	private String weekYearDescription;
	private List<Combobox> userChildrensParent;
	private List<Combobox> companys;
	
	
	public String getUsersSelected() {
		final StringBuilder result = new StringBuilder();
		if(this.userChildrensParent != null && this.userChildrensParent.size() > 0) {
			
			result.append("Operador(es): ");
			int count = 0;
			for(final Combobox item : this.userChildrensParent) {
				if(count > 0) {
					result.append(ConstantDataManager.VIRGULA_STRING);
					result.append(ConstantDataManager.SPACE);
				}
				result.append(item.getValue()); 
				result.append(ConstantDataManager.SPACE);
				result.append(ConstantDataManager.PARENTESES_LEFT);
				result.append(item.getKey());
				result.append(ConstantDataManager.PARENTESES_RIGHT);
				
				count++;
			}
		}
		
		return result.toString();
	}
	
	public String getCompanysSelected() {
		final StringBuilder result = new StringBuilder();
		if(this.companys != null && this.companys.size() > 0) {
			result.append("Empresa(s): ");
			int count = 0;
			for(final Combobox item : this.companys) {
				if(count > 0) {
					result.append(ConstantDataManager.VIRGULA_STRING);
					result.append(ConstantDataManager.SPACE);
				}
				result.append(item.getValue()); 
				result.append(ConstantDataManager.SPACE);
				result.append(ConstantDataManager.PARENTESES_LEFT);
				result.append(item.getKey());
				result.append(ConstantDataManager.PARENTESES_RIGHT);
				
				count++;
			}
		}
		
		return result.toString();
	}
}