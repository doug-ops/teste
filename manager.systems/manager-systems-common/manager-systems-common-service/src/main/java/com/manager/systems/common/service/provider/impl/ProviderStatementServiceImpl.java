/*
 * Date create 28/06/2023.
 */
package com.manager.systems.common.service.provider.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.dao.provider.ProviderStatementDao;
import com.manager.systems.common.dao.provider.impl.ProviderStatementDaoImpl;
import com.manager.systems.common.dto.provider.ProviderStatemenReportDTO;
import com.manager.systems.common.dto.provider.ProviderStatementItemDTO;
import com.manager.systems.common.service.provider.ProviderStatementService;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterPortablePageEvent;
import com.manager.systems.common.utils.StringUtils;

public class ProviderStatementServiceImpl implements ProviderStatementService {

	private ProviderStatementDao providerStatementDao;
	
	public ProviderStatementServiceImpl(final Connection connection) {
		super();
		this.providerStatementDao = new ProviderStatementDaoImpl(connection);
	}
	
	@Override
	public void getProviderStatement(final ProviderStatemenReportDTO report) throws Exception {
		this.providerStatementDao.getProviderStatement(report);
	}
	
	@Override
	public byte[] processPdfReport(final ProviderStatemenReportDTO report) throws Exception 
	{
		final Document document = CreatePDF.createPDFDocument(PageSize.A4, 10, 10, 60, 40);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        final PdfPTable tableHeader = new PdfPTable(1);
    	tableHeader.setTotalWidth(680);
    	tableHeader.setLockedWidth(true);
 
    	final StringBuilder pdfTitle = new StringBuilder();
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_PROVIDER_STATEMENT);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.TRACO);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_PERIOD.toUpperCase());
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(ConstantDataManager.LABEL_FROM);   
    	pdfTitle.append(ConstantDataManager.SPACE);
    	pdfTitle.append(report.getDateFromString());
    	pdfTitle.append(ConstantDataManager.SPACE);
    	pdfTitle.append(ConstantDataManager.LABEL_TO);    	
    	pdfTitle.append(ConstantDataManager.SPACE);
    	pdfTitle.append(report.getDateToString());
    	    	   	
    	PdfPCell cell = new PdfPCell(new Phrase(pdfTitle.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(1);
		cell.setBorderWidth(0);				
    	tableHeader.addCell(cell);
    	
//    	for(final Map.Entry<Integer, BankAccountStatementCompanyDTO> entryCompany : report.getCompanys().entrySet()) {
//        	final StringBuilder pdfSubtitleCompanys = new StringBuilder();
//    		pdfSubtitleCompanys.append(ConstantDataManager.LABEL_COMPANY + ConstantDataManager.DOIS_PONTOS + ConstantDataManager.SPACE + entryCompany.getValue().getCompanyDescription() + ConstantDataManager.VIRGULA_STRING + ConstantDataManager.SPACE + ConstantDataManager.LABEL_ACCOUNTS + ConstantDataManager.DOIS_PONTOS + ConstantDataManager.SPACE);
//    		
//    		int countAccount = 0;
//    		for(Map.Entry<Integer, BankAccountStatementAccountDTO> entryAccount : entryCompany.getValue().getAccounts().entrySet()) {
//    			if(countAccount > 0) {
//    				pdfSubtitleCompanys.append(ConstantDataManager.VIRGULA_STRING + ConstantDataManager.SPACE);
//    			}
//    			pdfSubtitleCompanys.append(entryAccount.getValue().getBanAccountDescription());    			
//    			countAccount++;
//    		}
//    		
//    		cell = new PdfPCell(new Phrase(pdfSubtitleCompanys.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
//    		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//    		cell.setColspan(1);
//    		cell.setBorderWidth(0);				
//        	tableHeader.addCell(cell);
//    	}
    	    	
    	final StringBuilder pdfSubtitle2 = new StringBuilder();
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE);    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_CREDIT);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.REAL); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(StringUtils.formatDecimalValue(report.getTotalCredit()));
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_DEBIT);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.REAL); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(StringUtils.formatDecimalValue(report.getTotalDebit()));    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.REAL); 
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(StringUtils.formatDecimalValue(report.getTotalBalance()));   
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE); 
        cell = new PdfPCell(new Phrase(pdfSubtitle2.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(1);
		cell.setBorderWidth(0);				
    	tableHeader.addCell(cell);
        
        final HeaderFooterPortablePageEvent event = new HeaderFooterPortablePageEvent(tableHeader);
        writer.setPageEvent(event);
        
        document.open();
        PdfPTable table = null;
        
        final float[] widths = new float[] {15,15,15,15,15,15,15,15};
        
       	double balance = 0; 
        table = CreatePDF.createTable(widths.length, widths);	      
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_MOVEMENT_DATE, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DOCUMENT, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_MOVEMENT_TYPE, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DESCRIPTION, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
       	CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_VALUE), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
       	CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
       	
       	CreatePDF.addCell(table, report.getDateFromString(), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
       	CreatePDF.addCell(table, StringUtils.formatDecimalValue(balance), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
       	
       	for(final ProviderStatementItemDTO item : report.getItens())
		{ 
    		CreatePDF.addCell(table, item.getDocumentDate(), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);
        	CreatePDF.addCell(table, String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
        	CreatePDF.addCell(table, item.getTypeMovementDescription(), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
        	CreatePDF.addCell(table, item.getMovementDescription(), Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
           	CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
           	balance = (balance + item.getDocumentValue());
        	CreatePDF.addCell(table, StringUtils.formatDecimalValue(balance), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
		}
       	
       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 0);
       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
       	CreatePDF.addCell(table, StringUtils.formatDecimalValue(balance), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
       	
//       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
//       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_MOVEMENT_EXTRACT_COMPANY, Element.ALIGN_CENTER, 8, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
       	
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1);
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_CREDIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
//        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_DEBIT, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
//       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
       
//       	final List<BankAccountStatementAccountDTO> accountsFull = new ArrayList<>(); 
//       	for(final Map.Entry<Integer, BankAccountStatementCompanyDTO> entry : report.getCompanys().entrySet())
//		{   
//       		for(final Map.Entry<Integer, BankAccountStatementAccountDTO> entryAccount : entry.getValue().getAccounts().entrySet()) {
//       			accountsFull.add(entryAccount.getValue());
//       		}
//		}
//       	final List<BankAccountStatementAccountDTO> accounts = accountsFull.stream()
//                .collect(Collectors.groupingBy(BankAccountStatementAccountDTO -> BankAccountStatementAccountDTO.getBankAccountId()))
//                .entrySet().stream()
//                .map(e -> e.getValue().stream()
//                    .reduce((f1,f2) -> new BankAccountStatementAccountDTO(f1.getBankAccountId(),f1.getBanAccountDescription(),f1.getTotalDebit()+f2.getTotalDebit(), f1.getTotalCredit() + f2.getTotalCredit(), f1.getTotalBalance() + f2.getTotalBalance())))
//                    .map(f -> f.get())
//                    .collect(Collectors.toList());
//                 	
//           	for(final BankAccountStatementAccountDTO account : accounts) {
//            	CreatePDF.addCell(table, (account.getBanAccountDescription() + 
//            			com.manager.systems.common.utils.ConstantDataManager.SPACE +
//            			com.manager.systems.common.utils.ConstantDataManager.PARENTESES_LEFT +
//            			account.getBankAccountId() +
//            			com.manager.systems.common.utils.ConstantDataManager.PARENTESES_RIGHT), Element.ALIGN_LEFT, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);        	
//               	CreatePDF.addCell(table, StringUtils.formatDecimalValue(account.getTotalCredit()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//               	CreatePDF.addCell(table, StringUtils.formatDecimalValue(account.getTotalDebit()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
//               	CreatePDF.addCell(table, StringUtils.formatDecimalValue(account.getTotalBalance()), Element.ALIGN_RIGHT, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);           		
//           	}           	
           	
        document.add(table);
        document.close();        
        return baos.toByteArray();
	}
}