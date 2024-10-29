/*
 * Date create 02/01/2024
 */
package com.manager.systems.web.financial.cash.flow.grouping.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Map;
import java.util.stream.Collectors;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterLandscapePageEvent;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.financial.cash.flow.grouping.dao.CashFlowGroupingDao;
import com.manager.systems.web.financial.cash.flow.grouping.dao.impl.CashFlowGroupingDaoImpl;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingFilterDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingItemDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingKeyDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialGroupingReportDTO;
import com.manager.systems.web.financial.cash.flow.grouping.dto.CashFlowFinancialSubGroupingItemDTO;
import com.manager.systems.web.financial.cash.flow.grouping.service.CashFlowGroupingService;

public class CashFlowGroupingServiceImpl implements CashFlowGroupingService {
	
	private CashFlowGroupingDao cashFlowGroupingDao;
	
	public CashFlowGroupingServiceImpl(final Connection connection) {
		this.cashFlowGroupingDao = new CashFlowGroupingDaoImpl(connection);
	}

	@Override
	public CashFlowFinancialGroupingReportDTO getCashFlowGroupingReport(final CashFlowFinancialGroupingFilterDTO filter) throws Exception {
		return this.cashFlowGroupingDao.getCashFlowFinancialGroupingReport(filter);
	}

	@Override
	public byte[] processPdfReport(final CashFlowFinancialGroupingReportDTO report) throws Exception {
		//final String[] accounts = !StringUtils.isNull(report.getBankAccountNames()) ? report.getBankAccountNames().split(ConstantDataManager.VIRGULA_STRING) : new String[0];
		final Document document = CreatePDF.createPDFDocument(new RectangleReadOnly(842,595), 5, 5, 40, 25);
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
    	
    	pdfTitle.append(ConstantDataManager.SPACE + ConstantDataManager.TRACO + ConstantDataManager.SPACE );
    	
    	pdfTitle.append(report.getPeriod());
    	
    	CreatePDF.addCell(tableHeader, pdfTitle.toString(), Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
    	
    	final StringBuilder pdfTitleCompany = new StringBuilder();
    	pdfTitleCompany.append(ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY.toUpperCase());
    	pdfTitleCompany.append(ConstantDataManager.DOIS_PONTOS);
    	pdfTitleCompany.append(ConstantDataManager.SPACE);
    	pdfTitleCompany.append(report.getCompanysDescriptionFilter());
    	
    	CreatePDF.addCell(tableHeader, pdfTitleCompany.toString(), Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
    	
    	if(report.getGroupingType() == 1) {
        	final StringBuilder pdfTitleUsers = new StringBuilder();
        	pdfTitleUsers.append(ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.LABEL_OPERETORS.toUpperCase());
        	pdfTitleUsers.append(ConstantDataManager.DOIS_PONTOS);
        	pdfTitleUsers.append(report.getUsersDescriptionFilter());
        	
        	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
        	CreatePDF.addCell(tableHeader, pdfTitleUsers.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);    		
    	}

		final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();
		
		widths = new float[(report.getKeysGroupingType().size() * 2) + 4];
		for(int i = 0; i < widths.length; i++) {
			widths[i] = 10;
		}
		
		final PdfPTable table = CreatePDF.createTable(widths.length, widths);
								
		if(report.getTypeDocuments().contains(0)) {
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH_CATEGORY, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			for(final Map.Entry<Long, String> entryCategories : report.getKeysGroupingType().entrySet()) {
				CreatePDF.addCellBackgroundColor(table, entryCategories.getValue(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			}
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
			for(int i = 0; i < report.getKeysGroupingType().size(); i++) {
				CreatePDF.addCellBackgroundColor(table, "Previsto", Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, "Realizado", Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}		
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_MONTH_BEFORE), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
			for(int i = 0; i < report.getKeysGroupingType().size(); i++) {
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(0), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(0), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}				
		}
		
		if(report.getTypeDocuments().contains(1)) {
			this.addNewBlankLine(table, widths.length);
			
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH_CATEGORY, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			for(final Map.Entry<Long, String> entryCategories : report.getKeysGroupingType().entrySet()) {
				CreatePDF.addCellBackgroundColor(table, entryCategories.getValue(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			}
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
			for(int i = 0; i < report.getKeysGroupingType().size(); i++) {
				CreatePDF.addCell(table, "Previsto", Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, "Realizado", Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
			}
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_INCOME), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
			for(Map.Entry<Long, String> entry : report.getKeysGroupingType().entrySet()) {
				
			
				final double incomeExpected = report.getIncomeItems().values().parallelStream().map(m -> m.getIncomeItems().values().parallelStream().filter(f -> f.getKey() == entry.getKey()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d.getIncomeExpected()).sum()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d).sum();						
				final double incomeFulfilled = report.getIncomeItems().values().parallelStream().map(m -> m.getIncomeItems().values().parallelStream().filter(f -> f.getKey() == entry.getKey()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d.getIncomeFulfilled()).sum()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d).sum();
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(incomeExpected), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(incomeFulfilled), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}	
			
			for(Map.Entry<String, CashFlowFinancialGroupingItemDTO> entry : report.getIncomeItems().entrySet()) {
				CreatePDF.addCell(table, entry.getValue().getGrouping() + ConstantDataManager.SPACE + entry.getValue().getGroupingDescription(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
				for(Map.Entry<Long, CashFlowFinancialGroupingKeyDTO> entryItem : entry.getValue().getIncomeItems().entrySet()) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getIncomeExpected()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getIncomeFulfilled()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				}	
				
				if(report.isAnalitic()) {
					for(Map.Entry<String, CashFlowFinancialSubGroupingItemDTO> entrySub : entry.getValue().getIncomeItemsSubGroups().entrySet()) {
						CreatePDF.addCell(table, "            " + entrySub.getValue().getSubGrouping() + ConstantDataManager.SPACE + entrySub.getValue().getSubGroupingDescription(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
						for(Map.Entry<Long, CashFlowFinancialGroupingKeyDTO> entryItem : entrySub.getValue().getIncomeItems().entrySet()) {
							CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getIncomeExpected()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
							CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getIncomeFulfilled()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
						}	
					}				
				}
			}
		}
		
		if(report.getTypeDocuments().contains(2)) {
			this.addNewBlankLine(table, widths.length);
			
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH_CATEGORY, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			for(final Map.Entry<Long, String> entryCategories : report.getKeysGroupingType().entrySet()) {
				CreatePDF.addCellBackgroundColor(table, entryCategories.getValue(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			}
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
			for(int i = 0; i < report.getKeysGroupingType().size(); i++) {
				CreatePDF.addCell(table, "Previsto", Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, "Realizado", Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
			}
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_PAYMENTS), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
			for(Map.Entry<Long, String> entry : report.getKeysGroupingType().entrySet()) {
				
			
				final double paymentExpected = report.getPaymentItems().values().parallelStream().map(m -> m.getPaymentItems().values().parallelStream().filter(f -> f.getKey() == entry.getKey()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d.getPaymentExpected()).sum()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d).sum();						
				final double paymentFulfilled = report.getPaymentItems().values().parallelStream().map(m -> m.getPaymentItems().values().parallelStream().filter(f -> f.getKey() == entry.getKey()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d.getPaymentFulfilled()).sum()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d).sum();
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(paymentExpected), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(paymentFulfilled), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}	
			
			for(Map.Entry<String, CashFlowFinancialGroupingItemDTO> entry : report.getPaymentItems().entrySet()) {
				CreatePDF.addCell(table, entry.getValue().getGrouping() + ConstantDataManager.SPACE + entry.getValue().getGroupingDescription(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
				for(Map.Entry<Long, CashFlowFinancialGroupingKeyDTO> entryItem : entry.getValue().getPaymentItems().entrySet()) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getPaymentExpected()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getPaymentFulfilled()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				}	
				
				if(report.isAnalitic()) {
					for(Map.Entry<String, CashFlowFinancialSubGroupingItemDTO> entrySub : entry.getValue().getPaymentItemsSubGroups().entrySet()) {
						CreatePDF.addCell(table, "            " + entrySub.getValue().getSubGrouping() + ConstantDataManager.SPACE + entrySub.getValue().getSubGroupingDescription(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
						for(Map.Entry<Long, CashFlowFinancialGroupingKeyDTO> entryItem : entrySub.getValue().getPaymentItems().entrySet()) {
							CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getIncomeExpected()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
							CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getIncomeFulfilled()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
						}	
					}				
				}
			}
		}
		
		if(report.getTypeDocuments().contains(4)) {
			this.addNewBlankLine(table, widths.length);
			
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH_CATEGORY, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			for(final Map.Entry<Long, String> entryCategories : report.getKeysGroupingType().entrySet()) {
				CreatePDF.addCellBackgroundColor(table, entryCategories.getValue(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
			}
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
			for(int i = 0; i < report.getKeysGroupingType().size(); i++) {
				CreatePDF.addCell(table, "Cr\u00e9dito", Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, "D\u00e9bito", Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
			}
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_TRANSFER), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
			for(Map.Entry<Long, String> entry : report.getKeysGroupingType().entrySet()) {
				
				final double transferCredit = report.getTransferItems().values().parallelStream().map(m -> m.getTransferItems().values().parallelStream().filter(f -> f.getKey() == entry.getKey()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d.getTransferCredit()).sum()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d).sum();						
				final double transferDebit = report.getTransferItems().values().parallelStream().map(m -> m.getTransferItems().values().parallelStream().filter(f -> f.getKey() == entry.getKey()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d.getTransferDebit()).sum()).collect(Collectors.toList()).parallelStream().mapToDouble(d -> d).sum();						

				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(transferCredit), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(transferDebit), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}	
			
			for(Map.Entry<String, CashFlowFinancialGroupingItemDTO> entry : report.getTransferItems().entrySet()) {
				CreatePDF.addCell(table, entry.getValue().getGrouping() + ConstantDataManager.SPACE + entry.getValue().getGroupingDescription(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
				for(Map.Entry<Long, CashFlowFinancialGroupingKeyDTO> entryItem : entry.getValue().getTransferItems().entrySet()) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getTransferCredit()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getTransferDebit()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				}	
				
				if(report.isAnalitic()) {
					for(Map.Entry<String, CashFlowFinancialSubGroupingItemDTO> entrySub : entry.getValue().getTransferItemsSubGroups().entrySet()) {
						CreatePDF.addCell(table, "            " + entrySub.getValue().getSubGrouping() + ConstantDataManager.SPACE + entrySub.getValue().getSubGroupingDescription(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
						for(Map.Entry<Long, CashFlowFinancialGroupingKeyDTO> entryItem : entrySub.getValue().getTransferItems().entrySet()) {
							CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getIncomeExpected()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
							CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryItem.getValue().getIncomeFulfilled()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
						}	
					}				
				}
			}
		}
						
		document.add(table);
		document.close();
		return baos.toByteArray();
	}
	
	private void addNewBlankLine(final PdfPTable table, final int size) {
		CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, size, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
	}
}