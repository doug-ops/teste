/**
 * Date create 18/02/2024.
 */
package com.manager.systems.web.financial.cashier.close.preview.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterLandscapePageEvent;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserPreviewHeaderDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyItemDTO;
import com.manager.systems.web.financial.cashier.close.preview.dto.CashingCloseUserWeekMovementCompanyItemProductDTO;
import com.manager.systems.web.financial.cashier.close.preview.service.CashingClosePreviewService;

public class CashingClosePreviewServiceImpl implements CashingClosePreviewService {
	
	public CashingClosePreviewServiceImpl(final Connection connection) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public byte[] generatePDFCashierClosingAnalitic(final CashingCloseUserPreviewHeaderDTO header, final List<CashingCloseUserWeekMovementCompanyDTO> items) throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		final float[] widths = new float[] {5, 15, 10, 10, 10, 10, 10, 10};
		final PdfPTable tableHeader = CreatePDF.createTable(widths.length, widths);
		tableHeader.setTotalWidth(680);
		tableHeader.setLockedWidth(true);

		
		/** Title */
		CreatePDF.addCell(tableHeader,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_CASHIER+
				com.manager.systems.common.utils.ConstantDataManager.SPACE+
				com.manager.systems.common.utils.ConstantDataManager.LABEL_FROM+
				com.manager.systems.common.utils.ConstantDataManager.SPACE+
			    com.manager.systems.common.utils.ConstantDataManager.LABEL_CLOSING+
			    com.manager.systems.common.utils.ConstantDataManager.SPACE+
			    com.manager.systems.common.utils.ConstantDataManager.LABEL_PREVIEW,
				Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);

		CreatePDF.addCell(tableHeader, header.getWeekYearDescription(),
				Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
		
		CreatePDF.addCell(tableHeader, header.getUsersSelected() + ConstantDataManager.SPACE + header.getCompanysSelected(), Element.ALIGN_LEFT,
				8, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0, 0, 0, 0);

		final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();

		final PdfPTable table = CreatePDF.createTable(widths.length, widths);
		/** Launch title */
	
        double totalMovement = 0;        
        double totalPendingBefore = 0;
        double totalStore = 0;
        double totalDiscount = 0;
        double totalPay = 0;
        double totalPendingAfter = 0;
				
		
		for (final CashingCloseUserWeekMovementCompanyDTO item : items) {
			
	        totalMovement += item.getMovementValue();
	        totalPendingBefore += item.getPendingMovementValue();
	        totalStore += item.getTotalCompany();
	        totalDiscount += item.getDiscountTotal();
	        totalPay += item.getPaymentTotal();
	        totalPendingAfter += item.getPendingMovementAfterValue();
	        
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY), Element.ALIGN_CENTER, 2,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_MOVEMENTS), Element.ALIGN_CENTER, 1,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
				
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_PENDENT_BEFORE), Element.ALIGN_CENTER, 1,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_STORE), Element.ALIGN_CENTER, 1,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_DISCOUNT), Element.ALIGN_CENTER, 1,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_PAY), Element.ALIGN_CENTER, 1,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_PENDENT_ACTUAL), Element.ALIGN_CENTER, 1,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1, BaseColor.LIGHT_GRAY);
	        
			CreatePDF.addCell(table, 
					item.getCompanyDescription()+ ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT+item.getCompanyId()+ConstantDataManager.PARENTESES_RIGHT, 
					Element.ALIGN_LEFT, 2, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getMovementValue()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);
			
			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getPendingMovementValue()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);
		
			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getTotalCompany()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getDiscountTotal()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);
			
			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getPaymentTotal()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);
			
			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getPendingMovementAfterValue()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 0f, 1f);
			
			//Label Product
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_PRODUCT), 
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_INPUT_PERIOD), 
					Element.ALIGN_CENTER, 2, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_OUTPUT_PERIOD), 
					Element.ALIGN_CENTER, 2, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
		
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_SOBRA), 
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_INPUT_DOCUMENT), 
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_OUTPUT_DOCUMENT),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			
			for (final CashingCloseUserWeekMovementCompanyItemProductDTO itemProd : item.getProducts()) {
				CreatePDF.addCell(table, 
						String.valueOf(itemProd.getProductId()), 
						Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, 
						StringUtils.formatDecimalValue(itemProd.getInputValue()), 
						Element.ALIGN_CENTER, 2, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
				
				CreatePDF.addCell(table, 
						StringUtils.formatDecimalValue(itemProd.getOutputValue()), 
						Element.ALIGN_CENTER, 2, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
			
				CreatePDF.addCell(table, 
						StringUtils.formatDecimalValue(itemProd.getBalance()),
						Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
				
				CreatePDF.addCell(table, 
						String.valueOf(itemProd.getInputDocumentMovementId()), 
						Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
				
				CreatePDF.addCell(table, 
						String.valueOf(itemProd.getOutputDocumentMovementId()), 
						Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f);
			}				
			
			//Label Transf
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_DOCUMENT), 
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_TYPE), 
					Element.ALIGN_CENTER, 4, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_VALUE), 
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
		
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_INITIAL_DATE), 
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table, 
					StringUtils.capitalizeAllFirstLetter(ConstantDataManager.LABEL_FINAL_DATE), 
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			for (final CashingCloseUserWeekMovementCompanyItemDTO itemTransf : item.getItems()) {
				CreatePDF.addCell(table, 
						String.valueOf(itemTransf.getDocumentMovementId()), 
						Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, 
						itemTransf.getDocumentNote(),
						Element.ALIGN_CENTER, 4, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
				
				CreatePDF.addCell(table, 
						StringUtils.formatDecimalValue(itemTransf.getDocumentValue()), 
						Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
			
				CreatePDF.addCell(table, 
						itemTransf.getDateFrom(), 
						Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
				
				CreatePDF.addCell(table, 
						itemTransf.getDateTo(), 
						Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f);
			}
			
		
			
			//break line
			CreatePDF.addCell(table, 
					ConstantDataManager.SPACE,
					Element.ALIGN_CENTER, 8, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 0f, 0f, 0f);
		}
	
		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalMovement), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
			
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalPendingBefore), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalStore), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);

		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalDiscount), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalPay), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalPendingAfter), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1, BaseColor.LIGHT_GRAY);
		
		document.add(table);
		document.close();
		return baos.toByteArray();
	}

	@Override
	public byte[] generatePDFCashierClosingSintetic(final CashingCloseUserPreviewHeaderDTO header, final List<CashingCloseUserWeekMovementCompanyDTO> items) throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		final float[] widths = new float[] {5, 15, 10, 10, 10, 10, 10, 10};
		final PdfPTable tableHeader = CreatePDF.createTable(widths.length, widths);
		tableHeader.setTotalWidth(680);
		tableHeader.setLockedWidth(true);

		
		/** Title */
		CreatePDF.addCell(tableHeader,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_CASHIER+
				com.manager.systems.common.utils.ConstantDataManager.SPACE+
				com.manager.systems.common.utils.ConstantDataManager.LABEL_FROM+
				com.manager.systems.common.utils.ConstantDataManager.SPACE+
			    com.manager.systems.common.utils.ConstantDataManager.LABEL_CLOSING+
			    com.manager.systems.common.utils.ConstantDataManager.SPACE+
			    com.manager.systems.common.utils.ConstantDataManager.LABEL_PREVIEW,
				Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);

		CreatePDF.addCell(tableHeader, header.getWeekYearDescription(),
				Element.ALIGN_RIGHT, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
		
		CreatePDF.addCell(tableHeader, header.getUsersSelected() + ConstantDataManager.SPACE + header.getCompanysSelected(), Element.ALIGN_LEFT,
				8, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_7, 0, 0, 0, 0);

		final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();

		final PdfPTable table = CreatePDF.createTable(widths.length, widths);
		/** Launch title */
	
        double totalMovement = 0;        
        double totalPendingBefore = 0;
        double totalStore = 0;
        double totalDiscount = 0;
        double totalPay = 0;
        double totalPendingAfter = 0;
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY), Element.ALIGN_CENTER, 2,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_MOVEMENTS), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
			
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_PENDENT_BEFORE), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_STORE), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_DISCOUNT), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_PAY), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_PENDENT_ACTUAL), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1, BaseColor.LIGHT_GRAY);
		
		
		for (final CashingCloseUserWeekMovementCompanyDTO item : items) {
			
	        totalMovement += item.getMovementValue();
	        totalPendingBefore += item.getPendingMovementValue();
	        totalStore += item.getTotalCompany();
	        totalDiscount += item.getDiscountTotal();
	        totalPay += item.getPaymentTotal();
	        totalPendingAfter += item.getPendingMovementAfterValue();
	        
			CreatePDF.addCell(table, 
					item.getCompanyDescription()+ ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT+item.getCompanyId()+ConstantDataManager.PARENTESES_RIGHT, 
					Element.ALIGN_LEFT, 2, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getMovementValue()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
			
			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getPendingMovementValue()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
		
			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getTotalCompany()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getDiscountTotal()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
			
			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getPaymentTotal()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
			
			CreatePDF.addCell(table, 
					StringUtils.formatDecimalValue(item.getPendingMovementAfterValue()),
					Element.ALIGN_CENTER, 1, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f);
		}
	
		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_CENTER, 2,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalMovement), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1, BaseColor.LIGHT_GRAY);
			
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalPendingBefore), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalStore), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1, BaseColor.LIGHT_GRAY);

		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalDiscount), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalPay), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1, BaseColor.LIGHT_GRAY);
		
		CreatePDF.addCellBackgroundColor(table,
				StringUtils.formatDecimalValue(totalPendingAfter), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1, BaseColor.LIGHT_GRAY);
		
		document.add(table);
		document.close();
		return baos.toByteArray();
	}
}