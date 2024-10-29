/*
 * Date create 04/07/2023.
 */
package com.manager.systems.web.financial.cash.statement.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashStatemenResumeItemDTO implements Serializable{
	
	private static final long serialVersionUID = 4666986888974101487L;
	
	private int movementType;
	private int groupType;
	private int weekYear;
	private String description;
	private double value;
	private int order;
	private boolean credit;
	private List<CashStatementItemDTO> items;
	
	public void addItem(final CashStatementItemDTO item) {
		if(this.items == null) {
			this.items = new ArrayList<>();
		}
		
		if(this.order == 0) {
			this.movementType = item.getMovementTypeId();
			switch (this.movementType) {
			case 1:
				this.description = "Saldo anterior Pendencias (fiados)";
				this.credit = true;
				this.order = 1;
				break;
			case 2:
				this.description = "Duplicatas";
				this.credit = true;
				this.order = 2;
				break;
			case 3:
				this.description = "Despesas Loja";
				this.credit = true;
				this.order = 3;
				break;
			case 4:
				this.description = "Despesa Custo Software (HAB)";
				this.credit = false;
				this.order = 4;
				break;
			case 5:
				this.description = "Despesas";
				this.credit = false;
				this.order = 5;
				break;
			case 6:
				this.description = "Reforço/Transf Fundo Caixas";
				this.credit = false;
				this.order = 6;
				break;
			case 7:
				this.description = "Caixa";
				this.credit = false;
				this.order = 7;
				break;
			case 8:
				this.description = "Valor Meninos";
				this.credit = false;
				this.order = 8;
				break;
			case 9:
				this.description = "Leit. Pendentes Saldo Geral Atual";
				this.credit = false;
				this.order = 9;
				break;
			default:
				break;
			}
		}
		
		this.value += item.getDocumentValue();
		
		//this.items.add(item);
	}
}