package com.manager.systems.web.movements.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentMovementGroupCompany implements Comparable<DocumentMovementGroupCompany> {
	
	private long id;
	private long companyId;
	private String companyDescription;
	private double documentValue;
	private String documentNote;
	private String paymentDate;
	private int documentType;
	private long reportsTo;
	private Map<Integer, DocumentMovementGroupItem> itemsMap = new TreeMap<Integer, DocumentMovementGroupItem>();
	private List<DocumentMovementGroupItem> items;
		
	public DocumentMovementGroupItem getItem(final int key) {
		return this.itemsMap.get(key);
	}
	
	public void addItem(final int key, final DocumentMovementGroupItem value) {
		this.itemsMap.put(key, value);
	}
	
	public void convertMapToList() {
		this.items = new ArrayList<DocumentMovementGroupItem>(this.itemsMap.values());
		this.itemsMap = null;
	}
	
	public void sumDocumentValue(final double documentValue) {
		this.documentValue +=  documentValue;
	}
	
//	public DocumentMovementGroupItem convertToItem() {
//		return DocumentMovementGroupItem.builder()
//				.id(this.id)
//				.documentParentId(0)
//				.documentId(0)
//				.companyId(this.companyId)
//				.companyDescription(this.companyDescription)
//				.documentValue(this.documentValue)
//				.documentNote(this.documentNote)
//				.paymentDate(this.paymentDate)
//				.documentType(this.documentType)
//				.reportsTo(this.reportsTo)
//				.build();
//	}

	
	@Override
	public int compareTo(final DocumentMovementGroupCompany o) {
		return Comparator.comparing(DocumentMovementGroupCompany::getCompanyId).thenComparing(DocumentMovementGroupCompany::getCompanyId).compare(this, o);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(companyId);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DocumentMovementGroupCompany other = (DocumentMovementGroupCompany) obj;
		return this.companyId == other.companyId;
	}
}