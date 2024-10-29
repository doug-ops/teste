/*
 * Date create 28/10/2023.
 */
package com.manager.systems.web.financial.cash.flow.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterPortablePageEvent;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.financial.cash.flow.dao.CashFlowDao;
import com.manager.systems.web.financial.cash.flow.dao.impl.CashFlowDaoImpl;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowBankAccountTranferDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowFilterDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowIncomeExpectedDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowIncomeFulfilledDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowPaymentExpectedDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowPaymentFulfilledDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowPaymentFulfilledFinancialGroupDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowPaymentFulfilledFinancialSubGroupDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowReportDTO;
import com.manager.systems.web.financial.cash.flow.dto.CashFlowReportItemDTO;
import com.manager.systems.web.financial.cash.flow.service.CashFlowService;

public class CashFlowServiceImpl implements CashFlowService {

	private CashFlowDao cashFlowDao;
	
	public CashFlowServiceImpl(final Connection connection) {
		this.cashFlowDao = new CashFlowDaoImpl(connection);
	}
	
	@Override
	public CashFlowReportDTO getCashFlowReport(final CashFlowFilterDTO filter) throws Exception {
		return this.cashFlowDao.getCashFlowReport(filter);
	}
	
	@Override
	public byte[] processPdfReport(final CashFlowReportDTO report) throws Exception {

		//final String[] accounts = !StringUtils.isNull(report.getBankAccountNames()) ? report.getBankAccountNames().split(ConstantDataManager.VIRGULA_STRING) : new String[0];
		final Document document = CreatePDF.createPDFDocument(PageSize.A4, 5, 5, report.getReportMarginTop(), 25);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		float[] widths = new float[] { 12, 30, 12, 12, 12, 12, 12, 12, 12 };
		final PdfPTable tableHeader = CreatePDF.createTable(9, widths);
		tableHeader.setTotalWidth(600);
		tableHeader.setLockedWidth(true);

		final StringBuilder pdfTitle = new StringBuilder();
		pdfTitle.append(ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE);
    	pdfTitle.append(report.getTitle());
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.TRACO);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	
    	String typeReport = "";
    	if(report.getGroupBy() == 0) {
    		typeReport = "Por Empresa Origem";
    	} else if(report.getGroupBy() == 1) {
    		typeReport = "Por Operador";			
		}
    	
    	pdfTitle.append(typeReport.toUpperCase());
    	pdfTitle.append(ConstantDataManager.SPACE + ConstantDataManager.TRACO + ConstantDataManager.SPACE );
    	
    	pdfTitle.append(report.getPeriod());
    	
    	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
    	CreatePDF.addCell(tableHeader, pdfTitle.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
    	
    	final StringBuilder pdfTitleCompany = new StringBuilder();
    	pdfTitleCompany.append(ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANYS_PARENT_S.toUpperCase());
    	pdfTitleCompany.append(ConstantDataManager.DOIS_PONTOS);
    	pdfTitleCompany.append(report.getCompanysDescriptionFilter());
    	
    	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
    	CreatePDF.addCell(tableHeader, pdfTitleCompany.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
    	
    	if(report.getGroupBy() == 1) {
        	final StringBuilder pdfTitleUsers = new StringBuilder();
        	pdfTitleUsers.append(ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.LABEL_OPERETORS.toUpperCase());
        	pdfTitleUsers.append(ConstantDataManager.DOIS_PONTOS);
        	pdfTitleUsers.append(report.getUsersDescriptionFilter());
        	
        	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
        	CreatePDF.addCell(tableHeader, pdfTitleUsers.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);    		
    	}

		final HeaderFooterPortablePageEvent event = new HeaderFooterPortablePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();
		
		widths = new float[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 };
		
		final PdfPTable table = CreatePDF.createTable(widths.length, widths);
				
		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INC0ME_FULFILLED, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INC0ME_EXPECTED, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PAYMENT_FULFILLED, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PAYMENT_EXPECTED, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSFER_BANK_ACCOUNT_DEBIT, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSFER_BANK_ACCOUNT_CREDIT, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

		CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getIncomeFulfilled().getTotal()), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
		CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getIncomeExpected().getTotal()), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
		CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getPaymentFulfilled().getTotal()), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
		CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getPaymentExpected().getTotal()), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
		CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getBankAccountTranfer().getBankAccountTranferCredit().getTotal()), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
		CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getBankAccountTranfer().getBankAccountTranferDebit().getTotal()), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
		
		this.addNewBlankLine(table);
						
		if(report.isAnalitic()) {
			this.populateIncomeFulfilled(table, report.getIncomeFulfilled());	
			this.addNewBlankLine(table);
			this.populateIncomeExpected(table, report.getIncomeExpected());
			this.addNewBlankLine(table);
		} else {
			this.populateIncomeFulfilledSintetic(table, report.getIncomeFulfilled());
		}
		this.populatePaymentFulfilled(table, report.getPaymentFulfilled());
		if(report.isAnalitic()) {
			this.addNewBlankLine(table);	
			this.populatePaymentExpected(table, report.getPaymentExpected());
			this.addNewBlankLine(table);
			this.populateTransferSameBankAccount(table, report.getBankAccountTranfer());				
		}
		
		document.add(table);
		document.close();
		return baos.toByteArray();
	}
	
	private void addNewBlankLine(final PdfPTable table) {
		CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, 24, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
	}

	private void populateIncomeFulfilled(final PdfPTable table, final CashFlowIncomeFulfilledDTO incomeFulfilled) {

		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INC0ME_FULFILLED.replace("\n", ConstantDataManager.BLANK).toUpperCase(), Element.ALIGN_CENTER, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		//CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE.toUpperCase(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(incomeFulfilled.getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		
		if(incomeFulfilled.getItems() != null && incomeFulfilled.getItems().size() > 0) {
			
			final List<CashFlowReportItemDTO> transform = incomeFulfilled.getItems().parallelStream()
		            .collect(Collectors.groupingBy(foo -> foo.getBankAccountOriginId()))
		            .entrySet().stream()
		            .map(e -> e.getValue().parallelStream()
		                .reduce((f1,f2) -> CashFlowReportItemDTO.builder().bankAccountOriginId(f1.getBankAccountOriginId()).bankAccountOriginDescription(f1.getBankAccountOriginDescription()).paymentDate(f1.getPaymentDate()).documentValue(f1.getDocumentValue() + f2.getDocumentValue()).build()))
		                .map(f -> f.get())
		                .collect(Collectors.toList()).parallelStream().sorted(Comparator.comparing(CashFlowReportItemDTO::getBankAccountOriginDescription))
		    			.collect(Collectors.toList());
			
			for(final CashFlowReportItemDTO item : transform) {
				CreatePDF.addCell(table, item.getBankAccountOriginDescription(), Element.ALIGN_LEFT, 20, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
				//CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
			}			
		}
	}
	
	private void populateIncomeFulfilledSintetic(final PdfPTable table, final CashFlowIncomeFulfilledDTO incomeFulfilled) {

		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INC0ME_FULFILLED.replace("\n", ConstantDataManager.BLANK).toUpperCase(), Element.ALIGN_CENTER, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(incomeFulfilled.getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		
		if(incomeFulfilled.getItems() != null && incomeFulfilled.getItems().size() > 0) {
			final Map<String, Double> totalByCompany =  incomeFulfilled.getItems().stream().collect(Collectors.groupingBy(CashFlowReportItemDTO::getBankAccountOriginDescription, Collectors.summingDouble(CashFlowReportItemDTO::getDocumentValue)));
			
			for(final Map.Entry<String, Double> entry : totalByCompany.entrySet()) {
				CreatePDF.addCell(table, entry.getKey(), Element.ALIGN_LEFT, 20, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
			}			
		}
		
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 24, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 0f, 0f, 0f);
	}
	
	private void populateIncomeExpected(final PdfPTable table, final CashFlowIncomeExpectedDTO incomeExpected) {

		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INC0ME_EXPECTED.replace("\n", ConstantDataManager.BLANK).toUpperCase(), Element.ALIGN_CENTER, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		//CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE.toUpperCase(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(incomeExpected.getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		
		if(incomeExpected.getItems() != null && incomeExpected.getItems().size() > 0) {
			
			final List<CashFlowReportItemDTO> transform = incomeExpected.getItems().parallelStream()
		            .collect(Collectors.groupingBy(foo -> foo.getCompanyId()))
		            .entrySet().parallelStream()
		            .map(e -> e.getValue().stream()
		                .reduce((f1,f2) -> CashFlowReportItemDTO.builder().companyId(f1.getCompanyId()).companyDescription(f1.getCompanyDescription()).paymentDate(f1.getPaymentDate()).documentValue(f1.getDocumentValue() + f2.getDocumentValue()).build()))
		                .map(f -> f.get())
		                .collect(Collectors.toList()).parallelStream().sorted(Comparator.comparing(CashFlowReportItemDTO::getCompanyDescription))
		    			.collect(Collectors.toList());
			
			
			for(final CashFlowReportItemDTO item : transform) {
				CreatePDF.addCell(table, item.getCompanyDescription(), Element.ALIGN_LEFT, 20, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
				//CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
			}			
		}
	}
	
	private void populatePaymentFulfilled(final PdfPTable table, final CashFlowPaymentFulfilledDTO paymentFulfilled) {

		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PAYMENT_FULFILLED.replace("\n", ConstantDataManager.BLANK).toUpperCase(), Element.ALIGN_CENTER, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(paymentFulfilled.getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
				
		final Map<String, CashFlowPaymentFulfilledFinancialGroupDTO> groupSortedMap = paymentFulfilled.getItems() != null ? paymentFulfilled.getItems().entrySet().stream().sorted((e1,e2)->
        Double.valueOf(e1.getValue().getId().toUpperCase().trim()).compareTo(Double.valueOf(e2.getValue().getId().toUpperCase().trim())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)) : new TreeMap<>();
		
		for(final Map.Entry<String, CashFlowPaymentFulfilledFinancialGroupDTO> entryGroup :  groupSortedMap.entrySet()) {

			CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_LEFT, 24, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 0f);
			
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_GROUP.toUpperCase() + ConstantDataManager.DOIS_PONTOS + ConstantDataManager.SPACE + entryGroup.getValue().getDescription() + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + entryGroup.getValue().getId() + ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(entryGroup.getValue().getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			
			final Map<String, CashFlowPaymentFulfilledFinancialSubGroupDTO> subGroupSortedMap = entryGroup.getValue().getItems().entrySet().stream().sorted((e1,e2)->
	        e1.getValue().getDescription().toUpperCase().trim().compareTo(e2.getValue().getDescription().toUpperCase().trim()))
	        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

			for(final Map.Entry<String, CashFlowPaymentFulfilledFinancialSubGroupDTO> entrySub :  subGroupSortedMap.entrySet()){
				CreatePDF.addCellBackgroundColor(table, "     " + com.manager.systems.common.utils.ConstantDataManager.LABEL_SUB_GROUP.toUpperCase() + ConstantDataManager.DOIS_PONTOS + ConstantDataManager.SPACE + entrySub.getValue().getDescription() + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + entrySub.getValue().getId() + ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, CreatePDF.MOR_LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(entrySub.getValue().getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, CreatePDF.MOR_LIGHT_GRAY);

				if(entrySub.getValue().getItems() != null && entrySub.getValue().getItems().size() > 0) {
					if(paymentFulfilled.isAnalitic()) {
						final List<CashFlowReportItemDTO> sortedList = entrySub.getValue().getItems().parallelStream()
								  .sorted((o1, o2) -> o1.getProviderDescription().compareTo(o2.getProviderDescription())).collect(Collectors.toList());
							
						//int count = 0;
						for(final CashFlowReportItemDTO item : sortedList) {
//							if(count == 0) {
//								CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER.toUpperCase(), Element.ALIGN_CENTER, 16, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
//								CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE.toUpperCase(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
//								CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL.toUpperCase(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
//							}
							CreatePDF.addCell(table,item.getProviderOrDocumentNote(), Element.ALIGN_LEFT, 16, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
							CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
							CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
							
							//count++;
						}					
					}					
				}
			}
		}		
	}
	
	private void populatePaymentExpected(final PdfPTable table, final CashFlowPaymentExpectedDTO paymentExpected) {

		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PAYMENT_EXPECTED.replace("\n", ConstantDataManager.BLANK).toUpperCase(), Element.ALIGN_CENTER, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(paymentExpected.getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
		final Map<String, CashFlowPaymentFulfilledFinancialGroupDTO> groupSortedMap = paymentExpected.getItems() != null ? paymentExpected.getItems().entrySet().stream().sorted((e1,e2)->
        e1.getValue().getDescription().toUpperCase().trim().compareTo(e2.getValue().getDescription().toUpperCase().trim()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)) : new TreeMap<>();
		
		for(final Map.Entry<String, CashFlowPaymentFulfilledFinancialGroupDTO> entryGroup :  groupSortedMap.entrySet()) {

			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_GROUP.toUpperCase() + ConstantDataManager.DOIS_PONTOS + ConstantDataManager.SPACE + entryGroup.getValue().getDescription() + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + entryGroup.getValue().getId() + ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(entryGroup.getValue().getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			final Map<String, CashFlowPaymentFulfilledFinancialSubGroupDTO> subGroupSortedMap = entryGroup.getValue().getItems().entrySet().stream().sorted((e1,e2)->
	        e1.getValue().getDescription().toUpperCase().trim().compareTo(e2.getValue().getDescription().toUpperCase().trim()))
	        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

			for(final Map.Entry<String, CashFlowPaymentFulfilledFinancialSubGroupDTO> entrySub :  subGroupSortedMap.entrySet()){
				CreatePDF.addCellBackgroundColor(table, "     " + com.manager.systems.common.utils.ConstantDataManager.LABEL_SUB_GROUP.toUpperCase() + ConstantDataManager.DOIS_PONTOS + ConstantDataManager.SPACE + entrySub.getValue().getDescription() + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT + entrySub.getValue().getId() + ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, CreatePDF.MOR_LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(entrySub.getValue().getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, CreatePDF.MOR_LIGHT_GRAY);

				if(entrySub.getValue().getItems() != null && entrySub.getValue().getItems().size() > 0) {
					final List<CashFlowReportItemDTO> sortedList = entrySub.getValue().getItems().parallelStream()
							  .sorted((o1, o2) -> o1.getProviderDescription().compareTo(o2.getProviderDescription())).collect(Collectors.toList());
						
					//int count = 0;
					for(final CashFlowReportItemDTO item : sortedList) {
						//if(count == 0) {
						//	CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER.toUpperCase(), Element.ALIGN_CENTER, 16, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						//	CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE.toUpperCase(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						//	CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL.toUpperCase(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						//}
						CreatePDF.addCell(table,item.getProviderOrDocumentNote(), Element.ALIGN_LEFT, 16, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
						CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
						CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
						
						//count++;
					}	
				}
			}
		}		
	}
	
	private void populateTransferSameBankAccount(final PdfPTable table, final CashFlowBankAccountTranferDTO tranfers) {

		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSFER_BANK_ACCOUNT.replace("\n", ConstantDataManager.BLANK).toUpperCase(), Element.ALIGN_CENTER, 20, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(tranfers.getTotal()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		
		if(tranfers.getItems() != null && tranfers.getItems().size() > 0) {
			
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BANK_ACCCOUNT.toUpperCase(), Element.ALIGN_CENTER, 16, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE.toUpperCase(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL.toUpperCase(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);

			for(final CashFlowReportItemDTO item : tranfers.getItems()) {
				CreatePDF.addCell(table, item.getBankAccountDescription(), Element.ALIGN_LEFT, 16, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
			}			
		}
	}
}