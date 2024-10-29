package com.manager.systems.web.movements.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentMovementGroupType implements Comparable<DocumentMovementGroupType> {

	private long id;
	private long documentParentId;
	private long documentId;
	private long companyId;
	private String companyDescription;
	private double documentValue;
	private String documentNote;
	private String paymentDate;
	private int documentType;
	private long order;
	private long reportsTo;
	private List<DocumentMovementGroupType> items;

	public void addItem(DocumentMovementGroupType item) {
		if (this.items == null) {
			items = new ArrayList<DocumentMovementGroupType>();
		}
		this.items.add(item);
	}

	public void sumDocumentValue(final double documentValue) {
		this.documentValue += documentValue;
	}

	@Override
	public int compareTo(final DocumentMovementGroupType o) {
		return Comparator.comparing(DocumentMovementGroupType::getCompanyId)
				.thenComparing(DocumentMovementGroupType::getCompanyId).compare(this, o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.documentType);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DocumentMovementGroupType other = (DocumentMovementGroupType) obj;
		return Objects.equals(this.documentType, other.documentType);
	}
}