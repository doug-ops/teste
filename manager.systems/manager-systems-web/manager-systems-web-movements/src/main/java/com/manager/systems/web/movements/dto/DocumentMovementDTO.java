package com.manager.systems.web.movements.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import com.manager.systems.common.utils.StringUtils;

public class DocumentMovementDTO 
{
	private long companyId;
	private long providerId;
	private int bankAccountId;
	private String financialGroupId;
	private String financialSubGroupId;
	private String documentType;
	private String documentStatus;
	private String documentNumber;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private int filterBy;
	private boolean credit;
	private boolean debit;
	private boolean financial;
	private boolean open;
	private boolean close;
	private boolean removed;
	private boolean transfer;
	private boolean transferHab;
	private Map<Long, DocumentMovementItemDTO> itens = new TreeMap<Long, DocumentMovementItemDTO>();
	private double totalCredit;
	private double totalDebit;
	private double totalSaldo;
	private int countItens;
	private long userOperation;	
	private String companysId;
	private String documentsParentId;
	private boolean analitic;
	private int productGroupId;
	private int productSubGroupId;
	private long id;
	private String name;
	private String userChildrensParent;
	private int financialCostCenterId;
	private boolean launch;
	private boolean inputMovement;
	private boolean outputMovement;
	private boolean duplicateMovement;
	
	public DocumentMovementDTO() 
	{
		super();
	}

	public final long getCompanyId()
	{
		return this.companyId;
	}

	public final void setCompanyId(final long companyId) 
	{
		this.companyId = companyId;
	}

	public final long getProviderId()
	{
		return this.providerId;
	}

	public final void setProviderId(final long providerId)
	{
		this.providerId = providerId;
	}

	public final int getBankAccountId() 
	{
		return this.bankAccountId;
	}

	public final void setBankAccountId(final int bankAccountId) 
	{
		this.bankAccountId = bankAccountId;
	}

	public final String getFinancialGroupId()
	{
		return this.financialGroupId;
	}

	public final void setFinancialGroupId(final String financialGroupId) 
	{
		this.financialGroupId = financialGroupId;
	}

	public final String getFinancialSubGroupId()
	{
		return this.financialSubGroupId;
	}

	public final void setFinancialSubGroupId(final String financialSubGroupId)
	{
		this.financialSubGroupId = financialSubGroupId;
	}

	public final String getDocumentType() 
	{
		return this.documentType;
	}

	public final void setDocumentType(final String documentType) 
	{
		this.documentType = documentType;
	}

	public final String getDocumentStatus() 
	{
		return this.documentStatus;
	}

	public final void setDocumentStatus(final String documentStatus) 
	{
		this.documentStatus = documentStatus;
	}
	
	public final String getDocumentNumber() 
	{
		return this.documentNumber;
	}

	public final void setDocumentNumber(final String documentNumber)
	{
		this.documentNumber = documentNumber;
	}

	public final LocalDateTime getDateFrom()
	{
		return this.dateFrom;
	}

	public final void setDateFrom(final LocalDateTime dateFrom) 
	{
		this.dateFrom = dateFrom;
	}

	public final LocalDateTime getDateTo()
	{
		return this.dateTo;
	}

	public final void setDateTo(final LocalDateTime dateTo) 
	{
		this.dateTo = dateTo;
	}

	public final int getFilterBy()
	{
		return this.filterBy;
	}

	public final void setFilterBy(final int filterBy)
	{
		this.filterBy = filterBy;
	}

	public final boolean isCredit() 
	{
		return this.credit;
	}

	public final void setCredit(final boolean credit) 
	{
		this.credit = credit;
	}

	public final boolean isDebit()
	{
		return this.debit;
	}

	public final void setDebit(final boolean debit) 
	{
		this.debit = debit;
	}

	public final boolean isFinancial() 
	{
		return this.financial;
	}

	public final void setFinancial(final boolean financial) 
	{
		this.financial = financial;
	}

	public final boolean isOpen() 
	{
		return this.open;
	}

	public final void setOpen(final boolean open) 
	{
		this.open = open;
	}

	public final boolean isClose() 
	{
		return this.close;
	}

	public final void setClose(final boolean close)
	{
		this.close = close;
	}

	public final boolean isRemoved() 
	{
		return this.removed;
	}

	public final void setRemoved(final boolean removed)
	{
		this.removed = removed;
	}

	public final double getTotalCredit() 
	{
		return this.totalCredit;
	}

	public final double getTotalDebit() 
	{
		return this.totalDebit;
	}

	public final double getTotalSaldo()
	{
		return this.totalSaldo;
	}
	
	public final void calculateSaldo()
	{
		this.totalSaldo = (this.totalCredit - this.totalDebit);
	}

	public final boolean isTransfer() {
		return this.transfer;
	}

	public final void setTransfer(final boolean transfer) {
		this.transfer = transfer;
	}

	public final Map<Long, DocumentMovementItemDTO> getItens() 
	{
		return this.itens;
	}

	public final boolean isTransferHab() {
		return this.transferHab;
	}

	public final void setTransferHab(final boolean transferHab) {
		this.transferHab = transferHab;
	}
	
	public final long getUserOperation() {
		return this.userOperation;
	}

	public final void setUserOperation(final long userOperation) {
		this.userOperation = userOperation;
	}
	
	public final boolean isAnalitic() {
		return analitic;
	}

	public final void setAnalitic(final boolean analitic) {
		this.analitic = analitic;
	}
	
	public final String getCompanysId() {
		return this.companysId;
	}

	public final void setCompanysId(final String companysId) {
		this.companysId = companysId;
	}

	public final String getDocumentsParentId() {
		return this.documentsParentId;
	}

	public final void setDocumentsParentId(final String documentsParentId) {
		this.documentsParentId = documentsParentId;
	}
	
	public final int getProductGroupId() {
		return this.productGroupId;
	}

	public final void setProductGroupId(final int productGroupId) {
		this.productGroupId = productGroupId;
	}

	public final int getProductSubGroupId() {
		return this.productSubGroupId;
	}

	public final void setProductSubGroupId(final int productSubGroupId) {
		this.productSubGroupId = productSubGroupId;
	}
	
	public final long getId() 
	{
		return this.id;
	}

	public final void setId(final long id) 
	{
		this.id = id;
	}

	public final String getName() 
	{
		return this.name;
	}

	public final void setName(final String name) 
	{
		this.name = name;
	}
	
	public final String getUserChildrensParent() 
	{
		return this.userChildrensParent;
	}

	public final void setUserChildrensParent(final String userChildrensParent) 
	{
		this.userChildrensParent = userChildrensParent;
	}
	
	public final int getFinancialCostCenterId() {
		return this.financialCostCenterId;
	}

	public final void setFinancialCostCenterId(final int financialCostCenterId) {
		this.financialCostCenterId = financialCostCenterId;
	}
	
	public final void setLaunch(final boolean launch) {
		this.launch = launch;
	}
	
	public final boolean getLaunch() {
		return this.launch;
	}

	public final void setInputMovement(final boolean inputMovement) {
		this.inputMovement = inputMovement;
	}
	
	public final boolean getInputMovement() {
		return this.inputMovement;
	}

	public final void setOutputMovement(final boolean outputMovement) {
		this.outputMovement = outputMovement;
	}
	
	public final boolean getOutputMovement() {
		return this.outputMovement;
	}

	public final void setDuplicateMovement(final boolean duplicateMovement) {
		this.duplicateMovement = duplicateMovement;
	}
	
	public final boolean getDuplicateMovement() {
		return this.duplicateMovement;
	}

	public final void addItem(final DocumentMovementItemDTO item) 
	{
		if(item.isCredit())
		{
			this.totalCredit+=item.getDocumentValue();
		}
		else
		{
			this.totalDebit+=item.getDocumentValue();
		}
		this.countItens=this.countItens+1;
		
		DocumentMovementItemDTO value = null;
		long key = item.getDocumentParentId();
		if(key>0)
		{
			value = this.itens.get(key);
			if(value==null)
			{
				value = item.createParent();
			}
			value.addItem(item);			
		}
		else
		{
			key = item.getId();
		}
		
		if(value != null) {
			this.itens.put(key, value);
		}		
	}
	
	public final MovementWekendDTO convetToGroupWeek() throws Exception {
		
		final MovementWekendDTO movementWekend = new MovementWekendDTO(); 		
		movementWekend.prepareMapDate(this.dateFrom, this.dateTo);
		
		for(final Map.Entry<Long, DocumentMovementItemDTO> entry : this.itens.entrySet()) {
			movementWekend.addItem(entry.getValue());
		}
		return movementWekend;
	}
	
	public final MovementDocumentCompanyDTO convetGroupCompany() throws Exception {
		
		final MovementDocumentCompanyDTO movementDocumentCompany = new MovementDocumentCompanyDTO(); 
		movementDocumentCompany.setDataFrom(StringUtils.formatDate(dateFrom, StringUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS));
		movementDocumentCompany.setDataTo(StringUtils.formatDate(dateTo, StringUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS));
		for(final Map.Entry<Long, DocumentMovementItemDTO> entry : this.itens.entrySet()) {
			movementDocumentCompany.addItem(entry.getValue());
			double documentValue = entry.getValue().getDocumentValue();
			movementDocumentCompany.sumTotal(entry.getValue().isCredit() ? documentValue : (documentValue < 0 ? documentValue : (documentValue * -1)));
		}
		return movementDocumentCompany;
	}
}