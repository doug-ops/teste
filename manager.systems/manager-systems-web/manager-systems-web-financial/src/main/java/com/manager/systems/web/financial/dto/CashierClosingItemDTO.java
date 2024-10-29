package com.manager.systems.web.financial.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.vo.DocumentMovementStatus;
import com.manager.systems.common.vo.DocumentMovementType;

public class CashierClosingItemDTO implements Serializable
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
	private long documentTransferId;
	private String documentTransferString;
	private StringBuilder documentsKeys = new StringBuilder();
	private double totalResidueValue;
	private boolean paymentResidue;
	private long companyIdOld;

	private List<CashierClosingItemDTO> itens = new ArrayList<CashierClosingItemDTO>();
	private List<CashierClosingItemDTO> itensGroup = new ArrayList<CashierClosingItemDTO>();
	private Map<Long, CashierClosingItemDTO> groupCompanys = new TreeMap<Long, CashierClosingItemDTO>();

	private List<CashierClosingItemDTO> groupCompanyItens = new ArrayList<CashierClosingItemDTO>();

	public CashierClosingItemDTO() 
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
	
	public final List<CashierClosingItemDTO> getItens() 
	{
		return this.itens;
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

	public final void setTotalExpense(final double totalExpense) {
		this.totalExpense = totalExpense;
	}

	public final int getCountProducts() {
		return this.countProducts;
	}

	public final void setCountProducts(final int countProducts) {
		this.countProducts = countProducts;
	}

	public final long getDocumentTransferId() {
		return this.documentTransferId;
	}

	public final void setDocumentTransferId(final long documentTransferId) {
		this.documentTransferId = documentTransferId;
		if(this.documentTransferId > 0) {
			this.documentTransferString = String.valueOf("FECHADO");
		}else{
			this.documentTransferString = String.valueOf("ABERTO");
		}
	}
	
	public final String getDocumentTransferString() {
		return this.documentTransferString;
	}

	public final void setDocumentTransferString(final String documentTransferString) {
		this.documentTransferString = documentTransferString;
	}
	
	public final List<CashierClosingItemDTO> getGroupCompanyItens() 
	{
		return this.groupCompanyItens;
	}
	
	public final Map<Long, CashierClosingItemDTO> getGroupCompanys() 
	{
		return this.groupCompanys;
	}

	public final double getTotalResidueValue() {
		return this.totalResidueValue;
	}
	
	public final void sumTotalResidueValue(final double totalResidueValue) 
	{
		this.totalResidueValue += totalResidueValue;
	}
	

	public final boolean isPaymentResidue() {
		return this.paymentResidue;
	}

	public final void setPaymentResidue(final boolean paymentResidue) {
		this.paymentResidue = paymentResidue;
	}

	public final long getCompanyIdOld() {
		return this.companyIdOld;
	}

	public final void setCompanyIdOld(final long companyIdOld) {
		this.companyIdOld = companyIdOld;
	}

	public final void addItem(final CashierClosingItemDTO item)
	{  
		final String documentKey  = 
				   String.valueOf(item.getDocumentParentId())+
				   ConstantDataManager.UNDERSCORE +
				   //String.valueOf(item.getDocumentId())+
				   //ConstantDataManager.UNDERSCORE +
				   String.valueOf(item.getCompanyId()) +
				   ConstantDataManager.PONTO_VIRGULA_STRING;

		if(this.documentsKeys.indexOf(documentKey) == -1){
			this.documentsKeys.append(documentKey);
		}
		
		this.setDocumentTypeId(item.getDocumentTypeId()); 
		if(!ConstantDataManager.ZERO_STRING.equalsIgnoreCase(item.getDocumentNumber()))
		{
			this.setDocumentNumber(item.getDocumentNumber());			
		}
		if(item.paymentResidue) {
			this.sumDocumentValue(item.isCredit() ? item.getDocumentValue() : (item.getDocumentValue())); 
			this.sumPaymentValue(item.isCredit() ? item.getPaymentValue() : (item.getPaymentValue()));  
		}else {
			this.sumDocumentValue(item.isCredit() ? item.getDocumentValue() : (item.getDocumentValue()*-1)); 
			this.sumPaymentValue(item.isCredit() ? item.getPaymentValue() : (item.getPaymentValue()*-1));       
		}
		this.sumDocumentDiscount(item.getDocumentDiscount());  
		if(item.isPaymentResidue()) {
			
			this.sumTotalResidueValue(item.getDocumentValue());			
		}
		this.sumDocumentExtra(item.getDocumentExtra());    
		this.setDocumentNote(ConstantDataManager.BLANK);       
		this.setDocumentStatusId(item.getDocumentStatusId());
		this.setCredit(this.documentValue>=0);
		this.setCompanyId(item.getCompanyId());          
		this.setCompany(item.getCompany());            
		this.setProviderId(item.getProviderId());         
		this.setProvider(ConstantDataManager.TRACO);           
		this.setBankAccountId(item.getBankAccountId());      
		this.setBankAccount(item.getBankAccount());        
		this.setPaymentData(item.getPaymentData());        
		this.setPaymentExpiryData(item.getPaymentExpiryData());  
		this.setPaymentUserId(item.getPaymentUserId());      
		//clone.setPaymentUser(this.paymentUser);        
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
		this.setDocumentTransferId(item.getDocumentTransferId());
		this.setPaymentResidue(item.paymentResidue);
		this.itens.add(item);
		
		CashierClosingItemDTO valueCompany = null;
		long keyDocument = item.getDocumentParentId();
		if(keyDocument>0)
		{
			valueCompany = this.groupCompanys.get(keyDocument);
			if(valueCompany==null)
			{
				valueCompany = item.createParent();
			}
			valueCompany.addGroupCompany(item);			
		}
		else
		{
			keyDocument = item.getId();
		}
		this.groupCompanys.put(keyDocument, valueCompany);
	
	
	}
	

	public final void addGroupCompany(final CashierClosingItemDTO item)
	{  
		
		this.setDocumentTypeId(item.getDocumentTypeId()); 
		if(!ConstantDataManager.ZERO_STRING.equalsIgnoreCase(item.getDocumentNumber()))
		{
			this.setDocumentNumber(item.getDocumentNumber());			
		}
		if(item.paymentResidue) {
			this.sumDocumentValue(item.isCredit() ? item.getDocumentValue() : (item.getDocumentValue())); 
			this.sumPaymentValue(item.isCredit() ? item.getPaymentValue() : (item.getPaymentValue()));  
		}else {
			this.sumDocumentValue(item.isCredit() ? item.getDocumentValue() : (item.getDocumentValue()*-1)); 
			this.sumPaymentValue(item.isCredit() ? item.getPaymentValue() : (item.getPaymentValue()*-1));       
		}  
		this.sumDocumentDiscount(item.getDocumentDiscount());   
		if(item.isPaymentResidue()) {
			this.sumTotalResidueValue(item.getDocumentValue());			
		}
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
		//clone.setPaymentUser(this.paymentUser);        
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
		this.setDocumentTransferId(item.getDocumentTransferId());
		this.setPaymentResidue(item.paymentResidue);
		this.groupCompanyItens.add(item);
}
	public CashierClosingItemDTO createParent()
	{
		final CashierClosingItemDTO clone = new CashierClosingItemDTO();
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
		final Map<String, List<CashierClosingItemDTO>> group = this.itens.stream()
				.collect(Collectors.groupingBy(CashierClosingItemDTO::getKey));
		
		for (final Map.Entry<String, List<CashierClosingItemDTO>> entry : group.entrySet()) {
			CashierClosingItemDTO item = null;
			for(final CashierClosingItemDTO itemList : entry.getValue()) {
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
					}
				}
				
			}
			this.itensGroup.add(item);
			//Todo esse cara esta com as colunas codigo do produto, valor in , valor out, e a sobra no totalValue
		}
		
		Collections.sort(this.itensGroup, new Comparator<CashierClosingItemDTO>()
		{
			@Override
			public int compare(final CashierClosingItemDTO o1, final CashierClosingItemDTO o2)
			{
				return o1.getKey().compareTo(o2.getKey());
			}
		});
	}
}