/*
 * Date create 13/12/2021.
 */
package com.manager.systems.common.service.bankAccount.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
//import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.dao.bankAccount.BankAccountStatementDao;
import com.manager.systems.common.dao.bankAccount.impl.BankAccountStatementDaoImpl;
import com.manager.systems.common.dto.bankAccount.BankAccountStatemenReportDTO;
import com.manager.systems.common.dto.bankAccount.BankAccountStatementAccountDTO;
import com.manager.systems.common.dto.bankAccount.BankAccountStatementCompanyDTO;
import com.manager.systems.common.dto.bankAccount.BankAccountStatementFilterDTO;
import com.manager.systems.common.dto.bankAccount.BankAccountStatementItemDTO;
import com.manager.systems.common.service.bankAccount.BankAccountStatementService;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterPortablePageEvent;
import com.manager.systems.common.utils.SendEmail;
import com.manager.systems.common.utils.StringUtils;

public class BankAccountStatementServiceImpl implements BankAccountStatementService {

	private BankAccountStatementDao bankAccountStatementDao;
	
	public BankAccountStatementServiceImpl(final Connection connection) {
		super();
		this.bankAccountStatementDao = new BankAccountStatementDaoImpl(connection);
	}
	
	@Override
	public void getBankAccountStatement(final BankAccountStatemenReportDTO report) throws Exception {
		this.bankAccountStatementDao.getBankAccountStatement(report);
	}
	
	@Override
	public void sendEmailBankAccountStatement() throws Exception {
		final ZonedDateTime actualDate = ZonedDateTime.now(ZoneId.of(ConstantDataManager.TIMEZONE_SAO_PAULO));
		final LocalDateTime dateFrom = actualDate.toLocalDateTime().withHour(0).withMinute(0).withSecond(0).minusDays(2);
		final LocalDateTime dateTo = actualDate.toLocalDateTime().withHour(23).withMinute(59).withSecond(59).minusDays(2);

		final BankAccountStatemenReportDTO report = new BankAccountStatemenReportDTO();	
		report.setDateFrom(dateFrom);
		report.setDateTo(dateTo);
		report.setOperation(4);
		this.bankAccountStatementDao.getBankAccountStatementFilter(report);
		
		final List<BankAccountStatemenReportDTO> bankAccountstatements = new ArrayList<>();
		
		if(report.getFilterMap().size()>0) {
			for (Map.Entry<Long, BankAccountStatementFilterDTO> entry : report.getFilterMap().entrySet()) {
				final BankAccountStatementFilterDTO bankAccountFilter = entry.getValue();
				final String email = bankAccountFilter.getEmail();

				if(bankAccountFilter.getBanksAccount().size()>0) {
					final StringBuilder accounts = new StringBuilder();
					int count = 0;
					for (final long bankAccount : bankAccountFilter.getBanksAccount()) {
						if(count>0) {
							accounts.append(",");
						}
						accounts.append(bankAccount);
						count++;
					}
					
					final StringBuilder persons = new StringBuilder();
					count = 0;
					for (final long person : bankAccountFilter.getPersons()) {
						if(count>0) {
							persons.append(",");
						}
						persons.append(person);
						count++;
					}
					
					final BankAccountStatemenReportDTO reportItem = new BankAccountStatemenReportDTO();		
					reportItem.setDateFrom(dateFrom);
					reportItem.setDateTo(dateTo);					
					reportItem.setBankAccounts(accounts.toString());
					reportItem.setPersons(persons.toString());
					reportItem.setEmail(email);
					//Operation = 1(Analitico) setOperation=2(Sintetico)
					reportItem.setOperation(2);
					this.bankAccountStatementDao.getBankAccountStatement(reportItem);
					bankAccountstatements.add(reportItem);
				}
			}
		}
	
		if(bankAccountstatements.size()>0) {
			for (final BankAccountStatemenReportDTO statementItem : bankAccountstatements) {
				final byte[] pdf = this.processPdfReport(statementItem);
				statementItem.setPdf(pdf);
			}
			
			for (final BankAccountStatemenReportDTO pdfItem : bankAccountstatements) {
				if(pdfItem.getPdf()!=null) {
					final String title = com.manager.systems.common.utils.ConstantDataManager.LABEL_BANK_ACCOUNT_STATEMENT
							+ com.manager.systems.common.utils.ConstantDataManager.SPACE
							+ com.manager.systems.common.utils.ConstantDataManager.TRACO
							+ com.manager.systems.common.utils.ConstantDataManager.SPACE
							+ com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE
							+ com.manager.systems.common.utils.ConstantDataManager.SPACE + pdfItem.getDateToString();
					final String body = com.manager.systems.common.utils.ConstantDataManager.LABEL_BANK_ACCOUNT_STATEMENT;
				 
					if(StringUtils.isValidEmailArray(pdfItem.getEmail())) {
						String emailTo = pdfItem.getEmail();
						if(emailTo.length() > 0) {
							emailTo +=";";
						}else {
							emailTo = StringUtils.isValidEmailArray(pdfItem.getEmail()) ? "" : pdfItem.getEmail();
						}
						
						final Properties props = new Properties();
						props.put("mail.from", "reportes35904@gmail.com");
						props.put("mail.username", "6bb8d7803d4f281dbe82952664286432");
						props.put("mail.password", "6bcb1d62b814f136f93cac5eee5b11b4");
						props.put("habilita.envio.email", "true");
	
						props.put("mail.smtp.host", "in-v3.mailjet.com");
						props.put("mail.smtp.auth", "true");
						props.put("mail.smtp.port", "587");
						props.setProperty("mail.smtp.socketFactory.port", "465");
						props.put("mail.smtp.ssl.trust", "*");
						props.put("mail.smtp.startssl.enable", "false");
	
						SendEmail.sendEmailAttachment(emailTo, body, title, pdfItem.getPdf(), props);
					}
				}
			}
		}
	}
	
	@Override
	public byte[] processPdfReport(final BankAccountStatemenReportDTO report) throws Exception 
	{
		final Document document = CreatePDF.createPDFDocument(PageSize.A4, 10, 10, 60, 40);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        final PdfPTable tableHeader = new PdfPTable(1);
    	tableHeader.setTotalWidth(680);
    	tableHeader.setLockedWidth(true);
 
    	final StringBuilder pdfTitle = new StringBuilder();
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_BANK_ACCOUNT_STATEMENT);
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
    	
    	for(final Map.Entry<Long, BankAccountStatementCompanyDTO> entryCompany : report.getCompanys().entrySet()) {
        	final StringBuilder pdfSubtitleCompanys = new StringBuilder();
    		pdfSubtitleCompanys.append(ConstantDataManager.LABEL_COMPANY + ConstantDataManager.DOIS_PONTOS + ConstantDataManager.SPACE + entryCompany.getValue().getCompanyDescription() + ConstantDataManager.VIRGULA_STRING + ConstantDataManager.SPACE + ConstantDataManager.LABEL_ACCOUNTS + ConstantDataManager.DOIS_PONTOS + ConstantDataManager.SPACE);
    		
    		int countAccount = 0;
    		for(Map.Entry<Long, BankAccountStatementAccountDTO> entryAccount : entryCompany.getValue().getAccounts().entrySet()) {
    			if(countAccount > 0) {
    				pdfSubtitleCompanys.append(ConstantDataManager.VIRGULA_STRING + ConstantDataManager.SPACE);
    			}
    			pdfSubtitleCompanys.append(entryAccount.getValue().getBanAccountDescription());    			
    			countAccount++;
    		}
    		
    		cell = new PdfPCell(new Phrase(pdfSubtitleCompanys.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
    		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    		cell.setColspan(1);
    		cell.setBorderWidth(0);				
        	tableHeader.addCell(cell);
    	}
    	    	
    	final StringBuilder pdfSubtitle2 = new StringBuilder();
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.PIPE);    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_INITIAL_BALANCE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.REAL);    	
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfSubtitle2.append(StringUtils.formatDecimalValue(report.getInitialBankAccountAvaliable()));
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
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
    	pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_FINAL_BALANCE);
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
        
       	double balance = report.getInitialBankAccountAvaliable(); 
        table = CreatePDF.createTable(widths.length, widths);	      
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_MOVEMENT_DATE, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DOCUMENT, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_MOVEMENT_TYPE, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DESCRIPTION, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
       	CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_VALUE), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 1, 1);
       	
       	CreatePDF.addCell(table, report.getDateFromString(), Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
        CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.BLANK, Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
       	CreatePDF.addCell(table, StringUtils.formatDecimalValue(balance), Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
       	
       	for(final BankAccountStatementItemDTO item : report.getItens())
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
       	CreatePDF.addCell(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_TOTAL, Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
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