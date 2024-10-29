package com.manager.systems.common.dto.provider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderStatemenReportDTO {

	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String providers;
	private String groupBy;
	private String typeDocument;
	private double initialProviderAvaliable;
	private double finalProviderAvaliable;
   	private double totalDebit;
   	private double totalCredit;
	private byte[] pdf;
	private long providerId;
	private String providerDescription;
	private String userChildrensParent;
	private List<ProviderStatementItemDTO> itens = new ArrayList<ProviderStatementItemDTO>();
	private Map<Long, ProviderStatementItemDTO> providersMap = new TreeMap<Long, ProviderStatementItemDTO>();
	
	public ProviderStatemenReportDTO() {
		super();
	}
	
	public final String getDateFromString() {
		return StringUtils.formatDate(this.dateFrom, StringUtils.DATE_PATTERN_DD_MM_YYYY);
	}
	
	public final String getDateToString() {
		return StringUtils.formatDate(this.dateTo, StringUtils.DATE_PATTERN_DD_MM_YYYY);
	}
	
	public final void addItem(final ProviderStatementItemDTO item)
	{
		this.finalProviderAvaliable += item.getDocumentValue();			
		item.setFinalProviderAvaliable(this.finalProviderAvaliable);
		
		this.itens.add(item);
		
		if(item.getDocumentValue()<0)
		{
			sumDebit(item.getDocumentValue());				
		}
		else 
		{
			sumCredit(item.getDocumentValue());					
		}
		addItemProvider(item);
	}
	
	public final void sumDebit(final double totalDebit) {
		this.totalDebit += totalDebit;
	}

	public final void sumCredit(final double totalCredit) {
		this.totalCredit += totalCredit;
	}

	public final double getTotalBalance()
	{
		return (this.initialProviderAvaliable + this.totalDebit + this.totalCredit);
	}
	
	public final byte[] getPdf() {
		return this.pdf;
	}
	
	public void setPdf(final byte[] pdf) {
		this.pdf = pdf;
	}
	
	public void addItemProvider(final ProviderStatementItemDTO item) {
		ProviderStatementItemDTO provider = this.providersMap.get(item.getProviderId());
		if(provider==null) {
			provider = new ProviderStatementItemDTO();
			provider.setProviderId(item.getProviderId());
			provider.setProviderDescription(item.getProviderDescription());
		}
		provider.addDocumentValue(item.getDocumentValue());
		this.providersMap.put(item.getProviderId(), provider);
		provider.getItens().add(item);
	}
}