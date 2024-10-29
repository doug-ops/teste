package com.manager.systems.web.movements.dto;

import java.util.ArrayList;
//import java.util.Comparator;
import java.util.List;
import java.util.Map;
//import java.util.Objects;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMovementGroupItem /*implements Comparable<DocumentMovementGroupItem>*/ {

	private long id;
	private long documentParentId;
	private long documentId;
	private long companyId;
	private String companyDescription;
	private double documentValue;
	private String documentNote;
	private String initialDate;
	private long initialDateLong;
	private String finalDate;
	private long finalDateLong;
	private Long parentId;
	private double input;
	private double output;
	private double inputOutputBalance;
	private double expensive;
	private double credit;
	private double debit;
	private int countProducts;
	private Map<String, DocumentMovementGroupItem> itemsMap;
	private List<DocumentMovementGroupItem> items = new ArrayList<>();
	private List<Long> producsId = new ArrayList<>();

	public DocumentMovementGroupItem getItem(final String key) {
		return this.itemsMap != null ? this.itemsMap.get(key) : null;
	}
	
	public void addItemMap(final String key, final DocumentMovementGroupItem value) {
		if(this.itemsMap == null) {
			this.itemsMap = new TreeMap<String, DocumentMovementGroupItem>();
		}
		this.itemsMap.put(key, value);
	}
	
	public void addItem(final DocumentMovementGroupItem item) {
		this.items.add(item);
	}

	public void sumDocumentValue(final double documentValue) {
		this.documentValue += documentValue;
	}
	
	public void sumInputValue(final double documentValue) {
		this.input += documentValue;
	}
	
	public void sumOutputValue(final double documentValue) {
		this.output += documentValue;
	}
	
	public void calculateInputOutputBalance() {
		this.inputOutputBalance = (this.input - this.output);
	}
	
	public void sumCredit(final double documentValue) {
		this.credit += documentValue;
	}

	public void sumDebit(final double documentValue) {
		this.debit += documentValue;
	}
	
	public void sumExpensiveValue(final double documentValue) {
		this.expensive += documentValue;
	}
	
	public void populateInitialDate(final long itemInitialDateLong, final String itemInitialDate) {
		if(this.initialDateLong == 0) {
			this.initialDateLong = itemInitialDateLong;
			this.initialDate = itemInitialDate;
		} else if(this.initialDateLong < itemInitialDateLong) {
			this.initialDateLong = itemInitialDateLong;
			this.initialDate = itemInitialDate;
		}
	}
	
	public void populateFinalDate(final long itemFinalDateLong, final String itemFinalDate) {
		if(this.finalDateLong == 0) {
			this.finalDateLong = itemFinalDateLong;
			this.finalDate = itemFinalDate;
		} else if(this.finalDateLong < itemFinalDateLong) {
			this.finalDateLong = itemFinalDateLong;
			this.finalDate = itemFinalDate;
		}
	}
	
	public void sumCountProduct(final long productId) {
		if(productId > 0) {
			if (!this.producsId.contains(productId)) {
				this.producsId.add(productId);
				this.countProducts++;			
			}			
		}
	}
	
	public List<DocumentMovementGroupItem> getDocuments(){
		final List<DocumentMovementGroupItem> listResult = new ArrayList<DocumentMovementGroupItem>();
		listResult.addAll(this.getItems());
		cleanItemsList();
		
		return listResult;
	}
	
	public void cleanItemsList() {
		this.items = null;
	}
	
	public List<DocumentMovementGroupItem> convertMapToList() {
		final List<DocumentMovementGroupItem> listResult = new ArrayList<DocumentMovementGroupItem>(this.itemsMap.values());
		return listResult;
	}
	
	public void cleanMap() {
		this.itemsMap = null;
	}
	
	public List<DocumentMovementGroupItem> convertDataToList() {
		final List<DocumentMovementGroupItem> listResult = new ArrayList<DocumentMovementGroupItem>();
		
		this.itemsMap.values().parallelStream().forEach(e -> {
			listResult.addAll(e.getDocuments());
			listResult.add(e);
			e.cleanMap();
		});
		
		return listResult;
	}

//	@Override
//	public int compareTo(final DocumentMovementGroupItem o) {
//		return Comparator.comparing(DocumentMovementGroupItem::getCompanyId)
//				.thenComparing(DocumentMovementGroupItem::getCompanyId).compare(this, o);
//	}

//	@Override
//	public int hashCode() {
//		return Objects.hash(this.documentType);
//	}
//
//	@Override
//	public boolean equals(final Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		final DocumentMovementGroupItem other = (DocumentMovementGroupItem) obj;
//		return Objects.equals(this.documentType, other.documentType);
//	}
}