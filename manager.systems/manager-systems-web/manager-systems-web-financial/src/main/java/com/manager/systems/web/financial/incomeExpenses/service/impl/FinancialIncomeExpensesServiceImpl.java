/*
 * Date create 24/08/2023.
 */
package com.manager.systems.web.financial.incomeExpenses.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.manager.systems.web.financial.incomeExpenses.dao.FinancialIncomeExpensesDao;
import com.manager.systems.web.financial.incomeExpenses.dao.impl.FinancialIncomeExpensesDaoImpl;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesCompanyDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesCompanyDateDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesCompanyWeekDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesDateDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFilterDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFinancialGroupDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFinancialGroupDateDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFinancialGroupWeekDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFinancialSubGroupDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFinancialSubGroupDateDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesFinancialSubGroupWeekDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesItemDetailDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesProviderDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesProviderDateDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesProviderWeekDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesReportDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesTransfMonthDTO;
import com.manager.systems.web.financial.incomeExpenses.dto.FinancialIncomeExpensesWeekDTO;
import com.manager.systems.web.financial.incomeExpenses.service.FinancialIncomeExpensesService;

public class FinancialIncomeExpensesServiceImpl implements FinancialIncomeExpensesService {

	private FinancialIncomeExpensesDao companyFinancialMovementDao;

	public FinancialIncomeExpensesServiceImpl(final Connection connection) {
		super();
		this.companyFinancialMovementDao = new FinancialIncomeExpensesDaoImpl(connection);
	}

	@Override
	public FinancialIncomeExpensesReportDTO getFinancialMovements(final FinancialIncomeExpensesFilterDTO filter)
			throws Exception {
		return this.companyFinancialMovementDao.getFinancialMovements(filter);
	}

	@Override
	public byte[] processPdfReport(final FinancialIncomeExpensesReportDTO report) throws Exception {

		final String[] accounts = !StringUtils.isNull(report.getBankAccountNames()) ? report.getBankAccountNames().split(ConstantDataManager.VIRGULA_STRING) : new String[0];
		//final Document document = CreatePDF.createPDFDocument(PageSize.A4, 5, 5, (accounts.length > 3 ? 60 : 40), 25);
		final Document document = CreatePDF.createPDFDocument(PageSize.A4, 5, 5, 20, 25);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		float[] widths = new float[] { 10, 36, 12, 12, 12, 12, 12, 12, 12 };
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
    		typeReport = "Somente Total";
    	} else if(report.getGroupBy() == 1) {
    		typeReport = "Por loja destino";			
		} else if(report.getGroupBy() == 2) {
			typeReport = "Por Data";		
		} else if(report.getGroupBy() == 3) {
			typeReport = "Por Loja Destino e Data";		
		} else if(report.getGroupBy() == 4) {
			typeReport = "Por Semana";			
		} else if(report.getGroupBy() == 5) {
			typeReport = "Por Loja Destino e Semana";		
		} else if(report.getGroupBy() == 6) {
			typeReport = "Por Loja Origem";		
		} else if(report.getGroupBy() == 7) {
			typeReport = "Por Loja Origem e Semana";		
		} else if(report.getGroupBy() == 8) {
			typeReport = "Por Fornecedor";		
		} else if(report.getGroupBy() == 9) {
			typeReport = "Por Fornecedor e Data";		
		} else if(report.getGroupBy() == 10) {
			typeReport = "Por Fornecedor e Semana";		
		} else if(report.getGroupBy() == 11) {
			typeReport = "Transferência Mensal";		
		} else if(report.getGroupBy() == 12) {
			typeReport = "Por Grupo Financeiro";		
		} else if(report.getGroupBy() == 13) {
			typeReport = "Por Grupo Financeiro e Data";		
		} else if(report.getGroupBy() == 14) {
			typeReport = "Por Grupo Financeiro e Semana";		
		} else if(report.getGroupBy() == 15) {
			typeReport = "Por SubGrupo Financeiro";		
		} else if(report.getGroupBy() == 16) {
			typeReport = "Por SubGrupo Financeiro e Data";		
		} else if(report.getGroupBy() == 17) {
			typeReport = "Por SubGrupo Financeiro e Semana";		
		}
    	
    	pdfTitle.append(typeReport.toUpperCase());
    	pdfTitle.append(ConstantDataManager.SPACE + ConstantDataManager.TRACO + ConstantDataManager.SPACE );
    	
    	pdfTitle.append(report.getPeriod());
    	
    	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
    	CreatePDF.addCell(tableHeader, pdfTitle.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
    	
    	final StringBuilder pdfTitleCompany = new StringBuilder();
    	pdfTitleCompany.append(ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE + com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANYS_PARENT_S.toUpperCase());
    	pdfTitleCompany.append(ConstantDataManager.DOIS_PONTOS);
    	
    	int countCompany = 0;
    	
    	if(report.getCompanys() != null) {
        	for(Map.Entry<Long, String> entryCompany : report.getCompanys().entrySet()) {
        		
        		if(countCompany > 5) {
        			continue;
        		}
        		
        		if(countCompany > 0) {
            		pdfTitleCompany.append(ConstantDataManager.VIRGULA_STRING);    			
        		}
        		
        		pdfTitleCompany.append(ConstantDataManager.SPACE);
        		pdfTitleCompany.append(entryCompany.getValue());
        		pdfTitleCompany.append(ConstantDataManager.PARENTESES_LEFT);
        		pdfTitleCompany.append(entryCompany.getKey());
        		pdfTitleCompany.append(ConstantDataManager.PARENTESES_RIGHT);    
        		
        		if(countCompany == 5) {
        			pdfTitleCompany.append(ConstantDataManager.TRES_PONTOS);
        		}
        		countCompany++;
        	}    		
    	}
    	
    	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
    	CreatePDF.addCell(tableHeader, pdfTitleCompany.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
    	
    	final StringBuilder filtersReport = new StringBuilder();
    	filtersReport.append("Tipos Doc: ");
    	if(report.getIncomeExpense() == 0) {
    		filtersReport.append(StringUtils.capitalize(ConstantDataManager.LABEL_INCOMES));
    		filtersReport.append(ConstantDataManager.VIRGULA_STRING);
    		filtersReport.append(ConstantDataManager.SPACE);
    		filtersReport.append(StringUtils.capitalize(ConstantDataManager.LABEL_EXPENSES));
    	} else if(report.getIncomeExpense() == 1) {
    		filtersReport.append(StringUtils.capitalize(ConstantDataManager.LABEL_INCOMES));
    	} else if(report.getIncomeExpense() == 2) {
    		filtersReport.append(StringUtils.capitalize(ConstantDataManager.LABEL_EXPENSES));
    	}
    	
    	if(report.isExpenseHaab()) {
    		filtersReport.append(ConstantDataManager.VIRGULA_STRING);
    		filtersReport.append(ConstantDataManager.SPACE);
    		filtersReport.append(ConstantDataManager.LABEL_EXPENSES_HAB);
    	}
    	
    	if(report.isTransfCashingClose()) {
    		filtersReport.append(ConstantDataManager.VIRGULA_STRING);
    		filtersReport.append(ConstantDataManager.SPACE);
    		filtersReport.append(ConstantDataManager.LABEL_TRANSF_CASHING_CLOSE);
    	}
    	
    	if(!StringUtils.isNull(report.getBankAccountNames()) && accounts.length <= 2) {
    		filtersReport.append(ConstantDataManager.SPACE + ConstantDataManager.TRACO + ConstantDataManager.SPACE );
    		filtersReport.append(" Conta(s): " + report.getBankAccountNames());
    		
        	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
        	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE + filtersReport.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

    	} else {
    		filtersReport.append(ConstantDataManager.SPACE + ConstantDataManager.TRACO + ConstantDataManager.SPACE );
    		filtersReport.append(" Conta(s): ");
    		
    		
    		final StringBuilder filtersAnotherAccounts = new StringBuilder();
    		int countAccount = 1;
    		boolean hasBroke = false;
    		for (final String account : accounts) {
				if(countAccount < 3) {
					filtersReport.append(account);
				} else {
					filtersAnotherAccounts.append(account);
					hasBroke = true;
				}
    			countAccount++;
			}
    		
        	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
        	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE + filtersReport.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

    		
        	if(hasBroke){
            	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
            	CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE + ConstantDataManager.SPACE + ConstantDataManager.SPACE + filtersAnotherAccounts.toString(), Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);    		
        	}
    	}


		//final HeaderFooterPortablePageEvent event = new HeaderFooterPortablePageEvent(tableHeader);
		//writer.setPageEvent(event);

		document.open();
		
		CreatePDF.addCell(tableHeader, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
		document.add(tableHeader);
		
		if((report.hasTransfeSameCompany() && !report.isHasProfitDistribution()) || (!report.hasTransfeSameCompany() && report.isHasProfitDistribution())) {
			widths = new float[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 };
		} else if(report.hasTransfeSameCompany() && report.isHasProfitDistribution()) {
			widths = new float[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 };
		} else {
			widths = new float[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 };
		}
		
		final PdfPTable table = CreatePDF.createTable(widths.length, widths);
				
		if(!report.hasTransfeSameCompany()) {
				CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT : com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL : com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT : com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

				CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getIncome()), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getExpenses()), Element.ALIGN_CENTER, 4,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getBalance()), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);			
				
				CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
		} else if((report.hasTransfeSameCompany() && !report.isHasProfitDistribution()) || (!report.hasTransfeSameCompany() && report.isHasProfitDistribution())) {
			
			CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT : com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL : com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			if(report.hasTransfeSameCompany()) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}
			
			CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT : com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getIncome()), Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getExpenses()), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			
			if(report.hasTransfeSameCompany()) {
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getTransfInput()), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getTransfOutput()), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else {
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getProfitDistribution()), Element.ALIGN_CENTER, 6,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getBalance()), Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
		} else {
			CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT : com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL : com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, report.getGroupBy() == 11 ? com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT : com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getIncome()), Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getExpenses()), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getTransfInput()), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getTransfOutput()), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getProfitDistribution()), Element.ALIGN_CENTER, 6,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(report.getBalance()), Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			
			CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE), Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);
		}
				
		
		if(report.getGroupBy() == 0 && report.isAnalitc()) {
			final boolean withHeader = true;
			this.populateAnalic(table, report.getItems(), withHeader, report.hasTransfeSameCompany(), report.isHasProfitDistribution());
			
			CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_CENTER, 10, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 0f, 0f, 0f);
			CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(report.getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
		} else if(report.getGroupBy() == 1 && report.getItemsCompanyMap() != null) {
			this.populateDataCompanys(table, report.getItemsCompanyMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		} else if(report.getGroupBy() == 2 && report.getItemsDateMap() != null) {
			this.populateDataDate(table, report.getItemsDateMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		} else if(report.getGroupBy() == 3 && report.getItemsCompanyDateMap() != null) {
			this.populateDataCompanyDate(table, report.getItemsCompanyDateMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		} else if(report.getGroupBy() == 4 && report.getItemsWeekMap() != null) {
			this.populateDataWeek(table, report.getItemsWeekMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		} else if(report.getGroupBy() == 5 && report.getItemsCompanyWeekMap() != null) {
			this.populateDataCompanyWeek(table, report.getItemsCompanyWeekMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());	
		} else if(report.getGroupBy() == 6 && report.getItemsOriginCompanyMap() != null) {
			this.populateDataOriginCompanys(table, report.getItemsOriginCompanyMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());				
		} else if(report.getGroupBy() == 7 && report.getItemsOriginCompanyWeekMap() != null) {
			this.populateDataOriginCompanyWeek(table, report.getItemsOriginCompanyWeekMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		} else if(report.getGroupBy() == 8 && report.getItemsProviderMap() != null) {
			this.populateDataProviders(table, report.getItemsProviderMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());				
		} else if(report.getGroupBy() == 9 && report.getItemsProviderDateMap() != null) {
			this.populateDataProviderDate(table, report.getItemsProviderDateMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());				
		} else if(report.getGroupBy() == 10 && report.getItemsProviderWeekMap() != null) {
			this.populateDataProviderWeek(table, report.getItemsProviderWeekMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		} else if(report.getGroupBy() == 11 && report.getItemsTrasnfMonthMap() != null) {
			this.populateDataTrasnfMonth(table, report.getItemsTrasnfMonthMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		} else if(report.getGroupBy() == 12 && report.getItemsFinancialGroupMap() != null) {
			this.populateDataFinancialGroup(table, report.getItemsFinancialGroupMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());				
		} else if(report.getGroupBy() == 13 && report.getItemsFinancialGroupDateMap() != null) {
			this.populateDataFinancialGroupDate(table, report.getItemsFinancialGroupDateMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());				
		} else if(report.getGroupBy() == 14 && report.getItemsFinancialGroupWeekMap() != null) {
			this.populateDataFinancialGroupWeek(table, report.getItemsFinancialGroupWeekMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		} else if(report.getGroupBy() == 15 && report.getItemsFinancialSubGroupMap() != null) {
			this.populateDataFinancialSubGroup(table, report.getItemsFinancialSubGroupMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());				
		} else if(report.getGroupBy() == 16 && report.getItemsFinancialSubGroupDateMap() != null) {
			this.populateDataFinancialSubGroupDate(table, report.getItemsFinancialSubGroupDateMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());				
		} else if(report.getGroupBy() == 17 && report.getItemsFinancialSubGroupWeekMap() != null) {
			this.populateDataFinancialSubGroupWeek(table, report.getItemsFinancialSubGroupWeekMap(), report.isAnalitc(), report.hasTransfeSameCompany(), report.isHasProfitDistribution());			
		}
		
		document.add(table);
		document.close();
		return baos.toByteArray();
	}
	
	private void populateAnalic(final PdfPTable table, final List<FinancialIncomeExpensesItemDetailDTO> items, final boolean withHeader, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(withHeader) {
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE.toUpperCase(), Element.ALIGN_CENTER, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 1 : ((hasTransfeSameCompany && hasProfitDistribution) ? 2 : 1)), CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DOC.toUpperCase(), Element.ALIGN_CENTER, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 1 : ((hasTransfeSameCompany && hasProfitDistribution) ? 2 : 1)), CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TYPE.toUpperCase(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_CLIENT_PROVIDER.toUpperCase(), Element.ALIGN_CENTER, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 3 : ((hasTransfeSameCompany && hasProfitDistribution) ? 6 : 4)), CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_NOTE.toUpperCase(), Element.ALIGN_CENTER, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 4 : ((hasTransfeSameCompany && hasProfitDistribution) ? 7 : 5)), CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_VALUE.toUpperCase(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
		}	

		if(items != null) {
			for (final FinancialIncomeExpensesItemDetailDTO item : items) {
				
				CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 1 : ((hasTransfeSameCompany && hasProfitDistribution) ? 2 : 1)), CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);
				CreatePDF.addCell(table, String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 1 : ((hasTransfeSameCompany && hasProfitDistribution) ? 2 : 1)), CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);
				if(item.getMovementTypeId() == 1 && item.getDocumentDescription().indexOf(ConstantDataManager.LABEL_GROSS_PROFIT) > -1) {
					CreatePDF.addCell(table, ConstantDataManager.LABEL_GROSS_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);
				} else if(item.getMovementTypeId() == 1 && item.getDocumentType() == 2 && item.getDocumentValue() < 0 &&  item.getDocumentNote().indexOf(ConstantDataManager.LABEL_TRANSF_FEC_CAIXA_ORIGIN) > -1) {
					CreatePDF.addCell(table, ConstantDataManager.LABEL_INCOME_NEGATIVE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);				
				} else {
					if(item.isTransactionSameCompany()) {
						CreatePDF.addCell(table, item.getMovementTypeId() == 1 ? ConstantDataManager.LABEL_TRANSF_CREDITO : ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);				
					} else if(item.isProfitDistribution()) {
						CreatePDF.addCell(table, item.getMovementTypeId() == 1 ? ConstantDataManager.LABEL_PROFIT_DISTRIBUTION : ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);				
					} else {
						CreatePDF.addCell(table, item.getMovementTypeId() == 1 ? ConstantDataManager.LABEL_INCOME : ConstantDataManager.LABEL_EXPENSE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);										
					}
				}
				
				if(item.getMovementTypeId() == 1) {
					CreatePDF.addCell(table, item.getDocumentDescription(), Element.ALIGN_LEFT, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 3 : ((hasTransfeSameCompany && hasProfitDistribution) ? 6 : 4)), CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, item.getDocumentDescription(), Element.ALIGN_LEFT, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 3 : ((hasTransfeSameCompany && hasProfitDistribution) ? 6 : 4)), CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);	
				}
				
				CreatePDF.addCell(table, item.getDocumentNote(), Element.ALIGN_LEFT, ((!hasTransfeSameCompany && !hasProfitDistribution) ? 4 : ((hasTransfeSameCompany && hasProfitDistribution) ? 7 : 5)), CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getMovementTypeId() == 1 ? item.getDocumentValue() : (item.getDocumentValue() * -1)), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
			}			
		}
	}
	
	private void populateDataCompanys(final PdfPTable table, final Map<Long, FinancialIncomeExpensesCompanyDTO> unSortedNewMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		final Map<Object, FinancialIncomeExpensesCompanyDTO> itemsMap = unSortedNewMap.entrySet().stream().sorted((e1,e2)->
        e1.getValue().getCompanyDescription().toUpperCase().trim().compareTo(e2.getValue().getCompanyDescription().toUpperCase().trim()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}
		}
		for (final Map.Entry<Object, FinancialIncomeExpensesCompanyDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);									
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
					} else {
						CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
					}
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
						CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);						
					} else {
						CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					}
					
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
	}
	
	private void populateDataDate(final PdfPTable table, final Map<Long, FinancialIncomeExpensesDateDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);										
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		double totalBalance = 0d;
		
		for (final Map.Entry<Long, FinancialIncomeExpensesDateDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);

				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
								
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput - totalExpenses - totalTransfOutput + totalProfitDistribution);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {					
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataCompanyDate(final PdfPTable table, final Map<String, FinancialIncomeExpensesCompanyDateDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		double totalBalance = 0d;
		
		for (final Map.Entry<String, FinancialIncomeExpensesCompanyDateDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput - totalExpenses - totalTransfOutput + totalProfitDistribution);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataWeek(final PdfPTable table, final Map<Integer, FinancialIncomeExpensesWeekDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		double totalBalance = 0d;
		
		for (final Map.Entry<Integer, FinancialIncomeExpensesWeekDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, String.valueOf(entry.getValue().getWeekDescription()), Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, String.valueOf(entry.getValue().getWeekDescription()), Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, String.valueOf(entry.getValue().getWeekDescription()), Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}

				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, String.valueOf(entry.getValue().getWeekDescription()), Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput - totalExpenses - totalTransfOutput + totalProfitDistribution);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataCompanyWeek(final PdfPTable table, final Map<String, FinancialIncomeExpensesCompanyWeekDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				}
				else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;		
		double totalBalance = 0d;
		
		for (final Map.Entry<String, FinancialIncomeExpensesCompanyWeekDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {						
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput - totalExpenses - totalTransfOutput + totalProfitDistribution);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataOriginCompanys(final PdfPTable table, final Map<Long, FinancialIncomeExpensesCompanyDTO> unSortedNewMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		final Map<Object, FinancialIncomeExpensesCompanyDTO> itemsMap = unSortedNewMap.entrySet().stream().sorted((e1,e2)->
        e1.getValue().getCompanyDescription().toUpperCase().trim().compareTo(e2.getValue().getCompanyDescription().toUpperCase().trim()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			}
		}
		
		for (final Map.Entry<Object, FinancialIncomeExpensesCompanyDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			}
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
	}
	
	private void populateDataOriginCompanyWeek(final PdfPTable table, final Map<String, FinancialIncomeExpensesCompanyWeekDTO> unSortedNewMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		final Map<Object, FinancialIncomeExpensesCompanyWeekDTO> itemsMap = unSortedNewMap.entrySet().stream().sorted((e1,e2)->
        e1.getValue().getCompanyDescription().toUpperCase().trim().compareTo(e2.getValue().getCompanyDescription().toUpperCase().trim()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
		
		int countCompanys = itemsMap.size();
		if(!isAnalitic && countCompanys == 0) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);									
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		double totalBalance = 0d;
		
		
		double totalIncomeCompany = 0d;
		double totalExpensesCompany = 0d;
		double totalTransfInputCompany = 0d;
		double totalTransfOutputCompany = 0d;
		double totalProfitDistributionCompany = 0d;
		double totalBalanceCompany = 0d;
		
		long lasCompnayId = 0;
		for (final Map.Entry<Object, FinancialIncomeExpensesCompanyWeekDTO> entry : itemsMap.entrySet()) {
			
			if(!isAnalitic && lasCompnayId == 0) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);		
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
				} else {
					CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
				}
			}
			
			entry.getValue().calculateTotais();
			
			if(lasCompnayId != 0 && lasCompnayId != entry.getValue().getCompanyId()) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncomeCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpensesCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalanceCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);						
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);						
					} else {
						CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
					}
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncomeCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpensesCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInputCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutputCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistributionCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalanceCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);	
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncomeCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpensesCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInputCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutputCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistributionCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalanceCompany), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);	
				}				
				if(isAnalitic) {
					if(!hasTransfeSameCompany && !hasProfitDistribution) {
						CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);						
					} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
						CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);						
					} else {
						CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);						
					}
				}
				
				totalIncomeCompany = 0d;
				totalExpensesCompany = 0d;
				totalTransfInputCompany = 0d;
				totalTransfOutput = 0d;
				totalProfitDistributionCompany = 0d;
				totalBalanceCompany = 0d;
				
				if(!isAnalitic) {
					if(!hasTransfeSameCompany && !hasProfitDistribution) {
						CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f);
						CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);														
					} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
						CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f);
						
						if(hasTransfeSameCompany) {
							CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
						} else {
							CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						}
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						
						if(hasTransfeSameCompany) {
							CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
							CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
						} else {
							CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						}

						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);								
					} else {
						CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f);
						CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);								
					}
				}
			}
			
			if(isAnalitic && (lasCompnayId == 0 || lasCompnayId != entry.getValue().getCompanyId())) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);	
					} else {
						CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput - totalExpenses - totalTransfOutput - totalProfitDistribution);

			
			totalIncomeCompany += entry.getValue().getIncome();
			totalExpensesCompany += entry.getValue().getExpenses();
			totalTransfInputCompany += entry.getValue().getTransfInput();
			totalTransfOutputCompany += entry.getValue().getTransfOutput();
			totalProfitDistributionCompany += entry.getValue().getProfitDistribution();
			totalBalanceCompany = (totalIncomeCompany + totalTransfInputCompany - totalExpensesCompany - totalTransfOutputCompany - totalProfitDistributionCompany);
			
			if(isAnalitic) {
				boolean withHeader = false;
				if(lasCompnayId == 0 || lasCompnayId != entry.getValue().getCompanyId()) {
					withHeader = true;
				}
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);				
			}
			
			lasCompnayId = entry.getValue().getCompanyId();					
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataProviders(final PdfPTable table, final Map<String, FinancialIncomeExpensesProviderDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}
		}
		for (final Map.Entry<String, FinancialIncomeExpensesProviderDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);									
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);								
			} else {
				CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);								
			}
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
	}
	
	private void populateDataProviderDate(final PdfPTable table, final Map<String, FinancialIncomeExpensesProviderDateDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		double totalBalance = 0d;
		
		for (final Map.Entry<String, FinancialIncomeExpensesProviderDateDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				

				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}				
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput - totalExpenses - totalTransfOutput - totalProfitDistribution);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(!hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				if(!hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataProviderWeek(final PdfPTable table, final Map<String, FinancialIncomeExpensesProviderWeekDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		double totalBalance = 0d;
		
		for (final Map.Entry<String, FinancialIncomeExpensesProviderWeekDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getProviderDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput - totalExpenses - totalTransfOutput + totalProfitDistribution);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 8, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataTrasnfMonth(final PdfPTable table, final Map<Long, FinancialIncomeExpensesTransfMonthDTO> unSortedNewMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		final Map<Object, FinancialIncomeExpensesTransfMonthDTO> itemsMap = unSortedNewMap.entrySet().stream().sorted((e1,e2)->
        e1.getValue().getCompanyDescription().toUpperCase().trim().compareTo(e2.getValue().getCompanyDescription().toUpperCase().trim()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_ACCOUNT, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_ACCOUNT, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_ACCOUNT, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);	
				}	
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}

				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_ACCOUNT, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table,  com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			}
		}
		
		//TODO AUMENTAR COLUNA TIPO DO ANALITICO
		//MUDAR NO ANALITICO PARA LUCRO BRUTO
		//grupO FINANCEIRO  HAB - 06.000 E 06.003
		//IN OUT 01.000 01.001
		//       07.000 07.001
		
		for (final Map.Entry<Object, FinancialIncomeExpensesTransfMonthDTO> entry : itemsMap.entrySet()) {
			//entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_ACCOUNT, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_ACCOUNT, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_ACCOUNT, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					}
					else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_ACCOUNT, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_GROSS_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PERSONAL_EXPENSE_PRL, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_NET_PROFIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);

				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);				
			} else {
				CreatePDF.addCell(table, entry.getValue().getCompanyDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0f, 1f, 0f, 1f);				
			}
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
	}
	
	private void populateDataFinancialGroup(final PdfPTable table, final Map<String, FinancialIncomeExpensesFinancialGroupDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}
		}
		for (final Map.Entry<String, FinancialIncomeExpensesFinancialGroupDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);									
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);								
			} else {
				CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);								
			}
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
	}
	
	private void populateDataFinancialGroupDate(final PdfPTable table, final Map<String, FinancialIncomeExpensesFinancialGroupDateDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;		
		double totalBalance = 0d;
		
		for (final Map.Entry<String, FinancialIncomeExpensesFinancialGroupDateDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				

				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}				
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput + totalProfitDistribution - totalExpenses - totalTransfOutput);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataFinancialGroupWeek(final PdfPTable table, final Map<String, FinancialIncomeExpensesFinancialGroupWeekDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		
		double totalBalance = 0d;
		
		for (final Map.Entry<String, FinancialIncomeExpensesFinancialGroupWeekDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getFinancialGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput + totalProfitDistribution - totalExpenses - totalTransfOutput);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 8, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataFinancialSubGroup(final PdfPTable table, final Map<String, FinancialIncomeExpensesFinancialSubGroupDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			}
		}
		for (final Map.Entry<String, FinancialIncomeExpensesFinancialSubGroupDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);									
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);				
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);								
			} else {
				CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);								
			}
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
	}
	
	private void populateDataFinancialSubGroupDate(final PdfPTable table, final Map<String, FinancialIncomeExpensesFinancialSubGroupDateDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		double totalBalance = 0d;
		
		for (final Map.Entry<String, FinancialIncomeExpensesFinancialSubGroupDateDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					

					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				

				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}				
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getDate(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);			
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput + totalProfitDistribution - totalExpenses - totalTransfOutput);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
	
	private void populateDataFinancialSubGroupWeek(final PdfPTable table, final Map<String, FinancialIncomeExpensesFinancialSubGroupWeekDTO> itemsMap, final boolean isAnalitic, final boolean hasTransfeSameCompany, final boolean hasProfitDistribution) {
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				

				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
		
		double totalIncome = 0d;
		double totalExpenses = 0d;
		double totalTransfInput = 0d;
		double totalTransfOutput = 0d;
		double totalProfitDistribution = 0d;
		double totalBalance = 0d;
		
		for (final Map.Entry<String, FinancialIncomeExpensesFinancialSubGroupWeekDTO> entry : itemsMap.entrySet()) {
			entry.getValue().calculateTotais();
			
			if(isAnalitic) {
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);								
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					
					if(hasTransfeSameCompany) {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);						
					} else {
						CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					}
					
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				} else {
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_FINANCIAL_SUB_GROUP, Element.ALIGN_CENTER, 75, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_WEEK, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_INCOMES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPENSES, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_CREDITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TRANSF_DEBITO, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_PROFIT_DISTRIBUTION, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);			
				}
			}
			
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 3,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);				
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);					
				} else {
					CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				}
				
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			} else {
				CreatePDF.addCell(table, entry.getValue().getFinancialSubGroupDescription().toUpperCase(), Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, entry.getValue().getWeekDescription(), Element.ALIGN_CENTER, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getIncome()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getExpenses()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfInput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getTransfOutput()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getProfitDistribution()), Element.ALIGN_RIGHT, 2,  CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entry.getValue().getBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f);
			}
			
			totalIncome += entry.getValue().getIncome();
			totalExpenses += entry.getValue().getExpenses();
			totalTransfInput += entry.getValue().getTransfInput();
			totalTransfOutput += entry.getValue().getTransfOutput();
			totalProfitDistribution += entry.getValue().getProfitDistribution();
			totalBalance = (totalIncome + totalTransfInput + totalProfitDistribution - totalExpenses - totalTransfOutput);
			
			if(isAnalitic) {
				final boolean withHeader = true;
				this.populateAnalic(table, entry.getValue().getItems(), withHeader, hasTransfeSameCompany, hasProfitDistribution);
				
				if(!hasTransfeSameCompany && !hasProfitDistribution) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 12, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);					
				} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 15, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 21, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);
				}
			}
		}
		
		if(!isAnalitic) {
			if(!hasTransfeSameCompany && !hasProfitDistribution) {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);							
			} else if((hasTransfeSameCompany && !hasProfitDistribution) || (!hasTransfeSameCompany && hasProfitDistribution)) {
				if(hasTransfeSameCompany) {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);					
				} else {
					CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 8, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				}
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				
				if(hasTransfeSameCompany) {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);					
				} else {
					CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				}
				
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			} else {
				CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 0f);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalIncome), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalExpenses), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfInput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalTransfOutput), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalProfitDistribution), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalBalance), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);			
			}
		}
	}
}