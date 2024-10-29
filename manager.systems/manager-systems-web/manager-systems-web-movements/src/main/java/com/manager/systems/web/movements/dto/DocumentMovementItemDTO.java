package com.manager.systems.web.movements.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.common.vo.DocumentMovementStatus;
import com.manager.systems.common.vo.DocumentMovementType;

public class DocumentMovementItemDTO implements Serializable
{
	private static final long serialVersionUID = 8376214560768445645L;

	private long id;
	private long documentId;
	private long documentParentId;
	private int documentTypeId;
	private String documentType;
	private String documentNumber;
	private double documentValue;
	private double paymentValue;	
	private double documentDiscount;
	private double documentExtra;
	private boolean documentResidue;
	private String documentNote;
	private int documentStatusId;
	private String documentStatus;
	private boolean credit;
	private String creditString;
	private long companyId;
	private String company;
	private long providerId;
	private String provider;
	private int bankAccountId;
	private String bankAccount;
	private String paymentData;
	private String paymentExpiryData;
	private long paymentUserId;
	private int paymentStatus;
	private String paymentUser;
	private String financialGroupId;
	private String financialGroup;
	private String financialSubGroupId;
	private String financialSubGroup;
	private boolean inactive;
	private long createUser;
	private long changeUser;
	private String createDate;
	private String changeDate;
	private boolean isInput;
	private long movementProductInitial;
	private long movementProductOutFinal;
	private long movementProductBalance;
	private String movementProductId;
	private long documentMovementProductBalance;
	private long productId;
	private String productDescription;
	private boolean isProductMovement;
	private String key;
	private double inputValue;
	private double outputValue;
	private double totalValue;
	private String transferProvider;
	private long transferdocumentId;
	private double transferdocumentValue;
	private long inputDocumentId;
	private long outputDocumentId;
	private double totalInput;
	private double totalOutput;
	private double totalExpense;
	private int countProducts;
	private String bankAccountOriginDescription;
	private String providerDescription;
	private boolean isDocumentTransfer;
	private boolean isAutomaticTransfer;

	private List<DocumentMovementItemDTO> itens = new ArrayList<DocumentMovementItemDTO>();
	private List<DocumentMovementItemDTO> itensGroup = new ArrayList<DocumentMovementItemDTO>();
	
	public DocumentMovementItemDTO() 
	{
		super();
	}

	public final long getId()
	{
		return this.id;
	}

	public final void setId(final long id)
	{
		this.id = id;
	}

	public final long getDocumentId()
	{
		return this.documentId;
	}

	public final void setDocumentId(final long documentId)
	{
		this.documentId = documentId;
	}

	public final long getDocumentParentId()
	{
		return this.documentParentId;
	}

	public final void setDocumentParentId(final long documentParentId)
	{
		this.documentParentId = documentParentId;
	}

	public final int getDocumentTypeId()
	{
		return this.documentTypeId;
	}

	public final void setDocumentTypeId(final int documentTypeId) 
	{
		this.documentType = DocumentMovementType.valueOfType(documentTypeId).getDescription();
		this.documentTypeId = documentTypeId;
	}

	public final String getDocumentType()
	{
		return this.documentType;
	}

	public final void setDocumentType(final String documentType)
	{
		this.documentType = documentType;
	}

	public final String getDocumentNumber() 
	{
		return this.documentNumber;
	}

	public final void setDocumentNumber(final String documentNumber)
	{
		this.documentNumber = documentNumber;
	}

	public final double getDocumentValue() 
	{
		return this.documentValue;
	}

	public final void setDocumentValue(final double documentValue) 
	{
		this.documentValue = documentValue;
	}
	
	public final void sumDocumentValue(final double documentValue)
	{
		this.documentValue += documentValue;
	}
	
	public final double getPaymentValue() 
	{
		return this.paymentValue;
	}
	
	public final void setPaymentValue(final double paymentValue) 
	{
		this.paymentValue = paymentValue;
	}
	
	public final void sumPaymentValue(final double paymentValue) 
	{
		this.paymentValue += paymentValue;
	}

	public final double getDocumentDiscount() 
	{
		return this.documentDiscount;
	}

	public final void setDocumentDiscount(final double documentDiscount) 
	{
		this.documentDiscount = documentDiscount;
	}
	
	public final void sumDocumentDiscount(final double documentDiscount) 
	{
		this.documentDiscount += documentDiscount;
	}

	public final double getDocumentExtra()
	{
		return this.documentExtra;
	}

	public final void setDocumentExtra(final double documentExtra) 
	{
		this.documentExtra = documentExtra;
	}
	
	public final void sumDocumentExtra(final double documentExtra) 
	{
		this.documentExtra += documentExtra;
	}

	public final boolean isDocumentResidue()
	{
		return this.documentResidue;
	}

	public final void setDocumentResidue(final boolean documentResidue)
	{
		this.documentResidue = documentResidue;
	}
	
	public final void setAutomaticTransfer(final boolean isAutomaticTransfer) {
		this.isAutomaticTransfer = isAutomaticTransfer;
	}
	
	public final boolean isAutomaticTransfer() {
		return this.isAutomaticTransfer;
	}

	public final String getDocumentNote()
	{
		return this.documentNote;
	}

	public final void setDocumentNote(final String documentNote)
	{
		this.documentNote = documentNote;
	}

	public final int getDocumentStatusId() 
	{
		return this.documentStatusId;
	}

	public final void setDocumentStatusId(final int documentStatusId) 
	{
		this.documentStatus = DocumentMovementStatus.valueOfType(documentStatusId).getDescription();
		this.documentStatusId = documentStatusId;
	}

	public final String getDocumentStatus() 
	{
		return this.documentStatus;
	}

	public final void setDocumentStatus(final String documentStatus)
	{
		this.documentStatus = documentStatus;
	}

	public final boolean isCredit()
	{
		return this.credit;
	}

	public final void setCredit(final boolean credit) 
	{
		if(this.credit)
		{
			this.creditString = ConstantDataManager.CREDIT_ALIAS;
		}
		else
		{
			this.creditString = ConstantDataManager.DEBIT_ALIAS;
		}
		this.credit = credit;
	}
	
	public String getCreditString() 
	{
		return this.creditString;
	}

	public final long getCompanyId() 
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId) 
	{
		this.companyId = companyId;
	}

	public final String getCompany() 
	{
		return this.company;
	}

	public final void setCompany(final String company)
	{
		this.company = company;
	}

	public final long getProviderId() 
	{
		return this.providerId;
	}

	public final void setProviderId(final long providerId) 
	{
		this.providerId = providerId;
	}

	public final String getProvider() 
	{
		return this.provider;
	}

	public final void setProvider(final String provider) 
	{
		this.provider = provider;
	}

	public final int getBankAccountId() 
	{
		return this.bankAccountId;
	}

	public final void setBankAccountId(final int bankAccountId)
	{
		this.bankAccountId = bankAccountId;
	}

	public final String getBankAccount() 
	{
		return this.bankAccount;
	}

	public final void setBankAccount(final String bankAccount) 
	{
		this.bankAccount = bankAccount;
	}

	public final String getPaymentData() 
	{
		return this.paymentData;
	}

	public final void setPaymentData(final String paymentData) 
	{
		this.paymentData = paymentData;
	}

	public final String getPaymentExpiryData()
	{
		return this.paymentExpiryData;
	}

	public final void setPaymentExpiryData(final String paymentExpiryData)
	{
		this.paymentExpiryData = paymentExpiryData;
	}

	public final long getPaymentUserId() 
	{
		return this.paymentUserId;
	}

	public final void setPaymentUserId(final long paymentUserId)
	{
		this.paymentUserId = paymentUserId;
	}

	public final String getPaymentUser()
	{
		return this.paymentUser;
	}

	public final void setPaymentUser(final String paymentUser)
	{
		this.paymentUser = paymentUser;
	}
	
	public final int getPaymentStatus() 
	{
		return this.paymentStatus;
	}
	
	public final void setPaymentStatus(final int paymentStatus) 
	{
		this.paymentStatus = paymentStatus;
	}

	public final String getFinancialGroupId() 
	{
		return this.financialGroupId;
	}

	public final void setFinancialGroupId(final String financialGroupId)
	{
		this.financialGroupId = financialGroupId;
	}

	public final String getFinancialGroup() 
	{
		return this.financialGroup;
	}

	public final void setFinancialGroup(final String financialGroup)
	{
		this.financialGroup = financialGroup;
	}

	public final String getFinancialSubGroupId()
	{
		return this.financialSubGroupId;
	}

	public final void setFinancialSubGroupId(final String financialSubGroupId) 
	{
		this.financialSubGroupId = financialSubGroupId;
	}

	public final String getFinancialSubGroup() 
	{
		return this.financialSubGroup;
	}

	public final void setFinancialSubGroup(final String financialSubGroup) 
	{
		this.financialSubGroup = financialSubGroup;
	}

	public final boolean isInactive() 
	{
		return this.inactive;
	}

	public final void setInactive(final boolean inactive)
	{
		this.inactive = inactive;
	}

	public final long getCreateUser() 
	{
		return this.createUser;
	}

	public final void setCreateUser(final long createUser) 
	{
		this.createUser = createUser;
	}

	public final long getChangeUser() 
	{
		return this.changeUser;
	}

	public final void setChangeUser(final long changeUser)
	{
		this.changeUser = changeUser;
	}

	public final String getCreateDate()
	{
		return this.createDate;
	}

	public final void setCreateDate(final String createDate)
	{
		this.createDate = createDate.toString().substring(0, 10);
	}

	public final String getChangeDate() 
	{
		return this.changeDate;
	}

	public final void setChangeDate(final String changeDate)
	{
		this.changeDate = changeDate;
	}
	
	public final boolean isInput() {
		return this.isInput;
	}

	public final void setInput(final boolean isInput) {
		this.isInput = isInput;
	}

	public final long getMovementProductInitial() {
		return this.movementProductInitial;
	}

	public final void setMovementProductInitial(final long movementProductInitial) {
		this.movementProductInitial = movementProductInitial;
	}

	public final long getMovementProductOutFinal() {
		return this.movementProductOutFinal;
	}

	public final void setMovementProductOutFinal(final long movementProductOutFinal) {
		this.movementProductOutFinal = movementProductOutFinal;
	}

	public final long getMovementProductBalance() {
		return this.movementProductBalance;
	}

	public final void setMovementProductBalance(final long movementProductBalance) {
		this.movementProductBalance = movementProductBalance;
	} 

	public final String getMovementProductId() {
		return this.movementProductId;
	}

	public final void setMovementProductId(final String movementProductId) {
		this.movementProductId = movementProductId;
	}
	
	public final long getDocumentMovementProductBalance() {
		return this.documentMovementProductBalance;
	}

	public final void setDocumentMovementProductBalance(final long documentMovementProductBalance) {
		this.documentMovementProductBalance = documentMovementProductBalance;
	}

	public final long getProductId() {
		return this.productId;
	}

	public final void setProductId(final long productId) {
		this.productId = productId;
	}

	public final String getProductDescription() {
		return this.productDescription;
	}

	public final void setProductDescription(final String productDescription) {
		this.productDescription = productDescription;
	}

	public final boolean isProductMovement() {
		return this.isProductMovement;
	}

	public final void setProductMovement(final boolean isProductMovement) {
		this.isProductMovement = isProductMovement;
	}

	public final String getKey() {
		return this.key;
	}

	public final void setKey(final String key) {
		this.key = key;
	}

	public final double getInputValue() {
		return inputValue;
	}

	public final void setInputValue(double inputValue) {
		this.inputValue = inputValue;
	}

	public final double getOutputValue() {
		return outputValue;
	}

	public final void setOutputValue(double outputValue) {
		this.outputValue = outputValue;
	}

	public final double getTotalValue() {
		return totalValue;
	}

	public final void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}
	
	public String getTransferProvider() {
		return this.transferProvider;
	}

	public void setTransferProvider(final String transferProvider) {
		this.transferProvider = transferProvider;
	}
	
	public final long getTransferdocumentId() {
		return this.transferdocumentId;
	}

	public final void setTransferdocumentId(final long transferdocumentId) {
		this.transferdocumentId = transferdocumentId;
	}

	public final double getTransferdocumentValue() {
		return this.transferdocumentValue;
	}

	public final void setTransferdocumentValue(final double transferdocumentValue) {
		this.transferdocumentValue = transferdocumentValue;
	}
	
	public final long getInputDocumentId() {
		return this.inputDocumentId;
	}

	public final void setInputDocumentId(final long inputDocumentId) {
		this.inputDocumentId = inputDocumentId;
	}

	public final long getOutputDocumentId() {
		return this.outputDocumentId;
	}

	public final void setOutputDocumentId(final long outputDocumentId) {
		this.outputDocumentId = outputDocumentId;
	}
	
	public final double getTotalInput() {
		return this.totalInput;
	}

	public final void setTotalInput(final double totalInput) {
		this.totalInput = totalInput;
	}

	public final double getTotalOutput() {
		return this.totalOutput;
	}

	public final void setTotalOutput(final double totalOutput) {
		this.totalOutput = totalOutput;
	}

	public final double getTotalExpense() {
		return this.totalExpense;
	}

	public final void setTotalExpense(double totalExpense) {
		this.totalExpense = totalExpense;
	}

	public final int getCountProducts() {
		return this.countProducts;
	}

	public final void setCountProducts(final int countProducts) {
		this.countProducts = countProducts;
	}

	public final String getBankAccountOriginDescription() {
		return this.bankAccountOriginDescription;
	}

	public final void setBankAccountOriginDescription(final String bankAccountOriginDescription) {
		this.bankAccountOriginDescription = bankAccountOriginDescription;
	}
	
	public final String getProviderDescription() {
		return this.providerDescription;
	}

	public final void setProviderDescription(final String providerDescription) {
		this.providerDescription = providerDescription;
	}
	
	public final void setCreditString(String creditString) {
		this.creditString = creditString;
	}

	public final List<DocumentMovementItemDTO> getItens() 
	{
		return this.itens;
	}
	

	public final boolean isDocumentTransfer() {
		return this.isDocumentTransfer;
	}

	public void setDocumentTransfer(final boolean isDocumentTransfer) {
		this.isDocumentTransfer = isDocumentTransfer;
	}

	public final void addItem(final DocumentMovementItemDTO item)
	{  
		this.setDocumentTypeId(item.getDocumentTypeId()); 
		if(!ConstantDataManager.ZERO_STRING.equalsIgnoreCase(item.getDocumentNumber()))
		{
			this.setDocumentNumber(item.getDocumentNumber());			
		}
		this.sumDocumentValue(item.isCredit() ? item.getDocumentValue() : (item.getDocumentValue()*-1)); 
		this.sumPaymentValue(item.getPaymentValue());       
		this.sumDocumentDiscount(item.getDocumentDiscount());   
		this.sumDocumentExtra(item.getDocumentExtra());    
		this.setDocumentNote(ConstantDataManager.BLANK);       
		this.setDocumentStatusId(item.getDocumentStatusId());
		this.setCredit(this.documentValue>=0);
		this.setCompanyId(item.getCompanyId());          
		this.setCompany(item.getCompany());            
		this.setProviderId(item.getProviderId());         
		this.setProvider(ConstantDataManager.TRACO);           
		this.setBankAccountId(item.getBankAccountId());      
		this.setBankAccount(ConstantDataManager.TRACO);        
		this.setPaymentData(item.getPaymentData());        
		this.setPaymentExpiryData(item.getPaymentExpiryData());  
		this.setPaymentUserId(item.getPaymentUserId());             
		this.setFinancialGroupId(item.getFinancialGroupId());   
		this.setFinancialGroup(ConstantDataManager.TRACO);     
		this.setFinancialSubGroupId(item.getFinancialGroupId());
		this.setFinancialSubGroup(ConstantDataManager.TRACO);  
		this.setInactive(item.isInactive());           
		this.setCreateUser(item.getCreateUser());         
		this.setChangeUser(item.getChangeUser());         
		this.setCreateDate(item.getCreateDate());         
		this.setChangeDate(item.getChangeDate());
		this.setProductId(item.getProductId());
		this.setProductDescription(item.getProductDescription());
		this.setProductMovement(item.isProductMovement());
		this.setInputValue(item.getInputValue());
		this.setOutputValue(item.getOutputValue());
		this.setTotalValue(item.getTotalValue());
		this.setKey(item.getKey());
		if(!this.isDocumentTransfer) {
			this.setDocumentTransfer(item.isDocumentTransfer);			
		}
		if(!this.isAutomaticTransfer) {
			this.setAutomaticTransfer(item.isAutomaticTransfer);			
		}
		this.itens.add(item);
	}
	
	public DocumentMovementItemDTO createParent()
	{
		final DocumentMovementItemDTO clone = new DocumentMovementItemDTO();
		clone.setId(0);
		clone.setDocumentId(this.documentParentId);
		clone.setDocumentParentId(this.documentParentId);   
		return clone;
	}
	
	public void populateDataDocumentNote() {
		if(this.documentNote!=null) {
			final String[] documentNoteArray = this.documentNote.split(ConstantDataManager.BARRA);
			if(documentNoteArray.length==3) {
				for (final String documentItem : documentNoteArray) {
					final String[] keyValueArray = documentItem.split(ConstantDataManager.DOIS_PONTOS);
					if(keyValueArray.length==2) {
						if(ConstantDataManager.INPUT_INITIAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.movementProductInitial = Long.parseLong(keyValueArray[1]);
							this.isInput = true;
						}
						else if(ConstantDataManager.INPUT_FINAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.movementProductOutFinal = Long.parseLong(keyValueArray[1]);
							this.isInput = true;
						}
						else if(ConstantDataManager.OUTPUT_INITIAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.movementProductInitial = Long.parseLong(keyValueArray[1]);
							this.isInput = false;
						}
						else if(ConstantDataManager.OUTPUT_FINAL_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.movementProductOutFinal = Long.parseLong(keyValueArray[1]);
							this.isInput = false;
						}
						else if(ConstantDataManager.PRODUCT_DOCUMENT_NOTE_ALIAS.equalsIgnoreCase(keyValueArray[0])) {
							this.movementProductId = String.valueOf(keyValueArray[1]);
							this.isInput = false;
						}	
					}
					else {
							this.movementProductId = documentNote;
							this.isInput = false;
					}
				}
			}
		}
		movementProductBalance = this.movementProductOutFinal - this.movementProductInitial;
	}	
	
	public void processGroupMovements() {
		final Map<String, List<DocumentMovementItemDTO>> group = this.itens.stream()
				.collect(Collectors.groupingBy(DocumentMovementItemDTO::getKey));
		
		for (final Map.Entry<String, List<DocumentMovementItemDTO>> entry : group.entrySet()) {
			DocumentMovementItemDTO item = null;
			for(final DocumentMovementItemDTO itemList : entry.getValue()) {
				if(item == null) {
					item = itemList;
					
					if(itemList.productId>0) {
						if(itemList.isInput) {
							this.countProducts++;
							this.totalInput += itemList.getDocumentValue();
							item.setInputValue(itemList.getDocumentValue());
							item.setTotalValue(item.getInputValue() - item.getOutputValue());
							item.setInputDocumentId(itemList.getDocumentId());
							item.setDocumentValue(item.getTotalValue());
						}
						else {
							this.totalOutput += itemList.getDocumentValue();
							item.setOutputValue(itemList.getDocumentValue());
							item.setTotalValue(item.getInputValue() - item.getOutputValue());
							item.setOutputDocumentId(itemList.getDocumentId());
							item.setDocumentValue(item.getTotalValue());
						}						
					}
					else {
						this.totalExpense += itemList.getDocumentValue();
						item.setTotalValue(itemList.getDocumentValue());
						
						this.bankAccountOriginDescription = ConstantDataManager.TRACO;
						
						if(itemList.getDocumentTypeId() == 3) {
							this.bankAccountOriginDescription = (StringUtils.isNull(itemList.getProviderDescription()) ? "" : itemList.getProviderDescription())  + " / " + itemList.getDocumentNote();	
						} else if(!StringUtils.isNull(itemList.getBankAccountOriginDescription())){
						    this.bankAccountOriginDescription = itemList.getBankAccountOriginDescription();						    
						} else if(!StringUtils.isNull(itemList.getProductDescription())) {
						    this.bankAccountOriginDescription = itemList.getProviderDescription();
						}
						
						if(itemList.getDocumentNote() != null && itemList.getDocumentNote().indexOf("TRANSF. FEC. CAIXA >> ") > -1) {
							this.bankAccountOriginDescription = "TRANSF. FEC. CAIXA >> " + itemList.getPaymentData();
						} 
						
					}
				}
				else {
					if(itemList.productId>0) {
						if(itemList.isInput) {
							this.countProducts++;
							this.totalInput += itemList.getDocumentValue();
							item.setInputValue(itemList.getDocumentValue());
							item.setTotalValue(item.getInputValue() - item.getOutputValue());
							item.setInputDocumentId(itemList.getDocumentId());
							item.setDocumentValue(item.getTotalValue());
						}
						else {
							this.totalOutput += itemList.getDocumentValue();
							item.setOutputValue(itemList.getDocumentValue());
							item.setTotalValue(item.getInputValue() - item.getOutputValue());
							item.setOutputDocumentId(itemList.getDocumentId());
							item.setDocumentValue(item.getTotalValue());
						}						
					}
					else {
						this.totalExpense += itemList.getDocumentValue();
						item.setTotalValue(itemList.getDocumentValue());
						
						this.bankAccountOriginDescription = ConstantDataManager.TRACO;
						
						if(itemList.getDocumentTypeId() == 3) {
							this.bankAccountOriginDescription = (StringUtils.isNull(itemList.getProviderDescription()) ? "" : itemList.getProviderDescription())  + " / " + itemList.getDocumentNote();	
						} else if(!StringUtils.isNull(itemList.getBankAccountOriginDescription())){
						    this.bankAccountOriginDescription = itemList.getBankAccountOriginDescription();						    
						} else if(!StringUtils.isNull(itemList.getProductDescription())) {
						    this.bankAccountOriginDescription = itemList.getProviderDescription();
						}
						
						if(itemList.getDocumentNote() != null && itemList.getDocumentNote().indexOf("TRANSF. FEC. CAIXA >> ") > -1) {
							this.bankAccountOriginDescription = "TRANSF. FEC. CAIXA >> " + itemList.getPaymentData();
						} 
					}
				}
				
			}
			this.itensGroup.add(item);
			//Todo esse cara esta com as colunas codigo do produto, valor in , valor out, e a sobra no totalValue
		}
		
		Collections.sort(this.itensGroup, new Comparator<DocumentMovementItemDTO>()
		{
			@Override
			public int compare(final DocumentMovementItemDTO o1, final DocumentMovementItemDTO o2)
			{
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		if(this.countProducts > 0) {
		    	this.bankAccountOriginDescription = "MOV. PROD.";
		}
	}
}