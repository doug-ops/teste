package com.manager.systems.web.movements.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.ConstantDataManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentMovementGroup implements Serializable {

	private static final long serialVersionUID = 4587809877710227516L;
	
	private double input;
	private double output;
	private double inputOutputBalance;
	private double expensive;
	private double credit;
	private double debit;
	private int countProducts;
	private double balance;
	
	private Map<Long, DocumentMovementGroupItem> movements = new TreeMap<Long, DocumentMovementGroupItem>();
	private List<DocumentMovementGroupItem> items;
	
	private int countGroupType;
	
	public void addItem(final DocumentMovementGroupItem item, final boolean isProductMovement, final long productId, final boolean isCredit, final int documentType) {
		
		final long companyKey = item.getCompanyId();
		DocumentMovementGroupItem groupCompany = this.movements.get(companyKey);
		if(groupCompany == null) {
			groupCompany = new  DocumentMovementGroupItem();
			groupCompany.setId(companyKey);			
			groupCompany.setCompanyId(companyKey);
			groupCompany.setCompanyDescription(item.getCompanyDescription());
			groupCompany.setInitialDate(item.getInitialDate());
			groupCompany.setFinalDate(item.getFinalDate());
			groupCompany.setDocumentNote("MOVIMENTO LOJA");
		}
		
		final String groupTypeKey = companyKey + ConstantDataManager.UNDERSCORE + item.getDocumentParentId();
		DocumentMovementGroupItem groupType = groupCompany.getItem(groupTypeKey);
		if(groupType == null) {
			groupType = new DocumentMovementGroupItem();
			groupType.setId(item.getDocumentParentId());
			groupType.setCompanyId(item.getCompanyId());
			groupType.setCompanyDescription(item.getCompanyDescription());
			groupType.setInitialDate(item.getInitialDate());
			groupType.setFinalDate(item.getFinalDate());
			if(documentType == 1) {
				groupType.setDocumentNote("MOVIMENTO PRODUTO");				
			} else {
				groupType.setDocumentNote(item.getDocumentNote());	
			}
			groupType.setParentId(groupCompany.getId());
		}
		
		item.setParentId(groupType.getId());
		groupCompany.sumDocumentValue(item.getDocumentValue());
		groupType.sumDocumentValue(item.getDocumentValue());
		
		final double documentValuePositive = (item.getDocumentValue() < 0 ? item.getDocumentValue() * -1 : item.getDocumentValue());
		
		if(isCredit) {
			groupCompany.sumCredit(documentValuePositive);
		} else {
			groupCompany.sumDebit(documentValuePositive);
		}
		
		if(isProductMovement) {
			item.sumCountProduct(productId);
			groupType.sumCountProduct(productId);
			groupCompany.sumCountProduct(productId);
			
			if(isCredit) {
				item.sumInputValue(documentValuePositive);
				groupType.sumInputValue(documentValuePositive);
				groupCompany.sumInputValue(documentValuePositive);
			} else {
				item.sumOutputValue(documentValuePositive);
				groupType.sumOutputValue(documentValuePositive);
				groupCompany.sumOutputValue(documentValuePositive);
			}
			
			item.calculateInputOutputBalance();
			groupType.calculateInputOutputBalance();
			groupCompany.calculateInputOutputBalance();
		}
		else if(documentType == 1) {
			item.sumExpensiveValue(documentValuePositive);
			groupType.sumExpensiveValue(documentValuePositive);
			groupCompany.sumExpensiveValue(documentValuePositive);
		}
		
		groupType.addItem(item);
		
		groupType.populateInitialDate(item.getInitialDateLong(), item.getInitialDate());
		groupType.populateFinalDate(item.getFinalDateLong(), item.getFinalDate());
		
		groupCompany.addItemMap(groupTypeKey, groupType);
		
		groupCompany.populateInitialDate(item.getInitialDateLong(), item.getInitialDate());
		groupCompany.populateFinalDate(item.getFinalDateLong(), item.getFinalDate());
		
		this.movements.put(companyKey, groupCompany);
	}
	
	public void convertMapToList() {
		//this.items = new ArrayList<DocumentMovementGroupCompany>(this.movements.values());
		//this.items.forEach(item -> item.convertMapToList());
		//this.movements = null;
		
		this.items = new ArrayList<DocumentMovementGroupItem>();
		
		this.movements.values().parallelStream().forEach(e -> {
			this.countProducts += e.getCountProducts();
			this.input += e.getInput();
			this.output += e.getOutput();
			this.inputOutputBalance = (this.input - this.output);
			this.expensive += e.getExpensive();
			this.credit += e.getCredit();
			this.debit += e.getDebit();
			this.balance = (this.credit - this.debit);
			
			this.items.addAll(e.convertDataToList());
			this.items.add(e);
			e.cleanMap();
		});
		
		this.items.sort(Comparator.comparing(DocumentMovementGroupItem::getId));
		this.movements = null;
	}
}
