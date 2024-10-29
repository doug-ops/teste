/*
 * Date create 18/08/2023.
 */
package com.manager.systems.web.financial.cashier.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterLandscapePageEvent;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.financial.cashier.dao.CashierMovementDao;
import com.manager.systems.web.financial.cashier.dao.impl.CashierMovementDaoImpl;
import com.manager.systems.web.financial.cashier.dto.CashierMovementReportDTO;
import com.manager.systems.web.financial.cashier.dto.CashierMovementReportFilterDTO;
import com.manager.systems.web.financial.cashier.dto.CashierMovementReportItemDTO;
import com.manager.systems.web.financial.cashier.service.CashierMovementService;

public class CashierMovementServiceImpl implements CashierMovementService {

	private CashierMovementDao cashierMovementDao;
	
	public CashierMovementServiceImpl(final Connection connection) 
	{
		super();
		this.cashierMovementDao = new CashierMovementDaoImpl(connection);
	}
	
	@Override
	public CashierMovementReportDTO getMovements(final CashierMovementReportFilterDTO filter) throws Exception {
		return this.cashierMovementDao.getMovements(filter);
	}

	@Override
	public byte[] generatePDFSintetic(final CashierMovementReportDTO report) throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        final PdfPTable tableHeader = new PdfPTable(1);
    	tableHeader.setTotalWidth(680);
    	tableHeader.setLockedWidth(true);
 
    	final StringBuilder pdfTitle = new StringBuilder();
    	pdfTitle.append(report.getTitle());
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.TRACO);
    	pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
    	pdfTitle.append(report.getPeriod());
    	    	   	
    	PdfPCell cell = new PdfPCell(new Phrase(pdfTitle.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(1);
		cell.setBorderWidth(0);				
    	tableHeader.addCell(cell);
    	
    	final BaseColor BASE_COLOR = new BaseColor(245, 247, 247);
    	final BaseColor LIGHT_RED = new BaseColor(255, 245, 245);
        
        final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
        writer.setPageEvent(event);
        
        document.open();
        
        
        final float[] widths = new float[] {15,2,15,2,15};
       	
        PdfPTable table = CreatePDF.createTable(widths.length, widths);
        
        
        //int countTotal = report.getMapItem().size();
        
        int countInnerTable = 0;
        int countLines = 0;
        for(final Map.Entry<String, Map<String, CashierMovementReportItemDTO>> mapEntryItem : report.getMapItem().entrySet()) {
        	
            final float[] widthsInner = new float[] {15,15};
           	
            final PdfPTable tableInner = CreatePDF.createTable(widthsInner.length, widthsInner);	
        	
            
    		if(countInnerTable == 1 || countInnerTable == 2) {
    			final PdfPCell cellSpace = new PdfPCell();
    			cellSpace.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cellSpace.setColspan(1);
    			cellSpace.setBorderWidthLeft(0);
    			cellSpace.setBorderWidthRight(0);
    			cellSpace.setBorderWidthTop(0);
    			cellSpace.setBorderWidthBottom(0);
        		table.addCell(cellSpace);
    		}
    		
    		boolean isAcumulated = false; 
    		
        	int countLinesInnerTable = 0;
    		final int countItems = mapEntryItem.getValue().size();
        	for(final Map.Entry<String, CashierMovementReportItemDTO> entryItem : mapEntryItem.getValue().entrySet()) {
        		final CashierMovementReportItemDTO item = entryItem.getValue();
        		
        		isAcumulated = item.isAcumulated();
        		if(countLinesInnerTable == 0) {
        			final StringBuilder weekDescription = new StringBuilder();
        			if(!isAcumulated) {
            			weekDescription.append(ConstantDataManager.WEEK_LABEL.toUpperCase());
            			weekDescription.append(ConstantDataManager.SPACE);
            			weekDescription.append(item.getWeekYearDescription());
            			weekDescription.append(ConstantDataManager.SPACE);
            			weekDescription.append(ConstantDataManager.TRACO);
            			weekDescription.append(ConstantDataManager.SPACE);
            			weekDescription.append(ConstantDataManager.LABEL_FROM);
            			weekDescription.append(ConstantDataManager.DOIS_PONTOS);
            			weekDescription.append(ConstantDataManager.SPACE);
            			weekDescription.append(item.getDateFromString());
            			weekDescription.append(ConstantDataManager.SPACE);
            			weekDescription.append(ConstantDataManager.LABEL_TO);
            			weekDescription.append(ConstantDataManager.DOIS_PONTOS);
            			weekDescription.append(ConstantDataManager.SPACE);
            			weekDescription.append(item.getDateToString());        				
        			} else {
        				weekDescription.append("CONSOLIDADO");
        				
        			}
        			
                	CreatePDF.addCell(tableInner, weekDescription.toString(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 0);
                	
                	CreatePDF.addCell(tableInner, "Creditos".toString(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 0);	
        		} else if(countLinesInnerTable == 4) {
                	CreatePDF.addCell(tableInner, "Debitos".toString(), Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 0);	
        		}
            	countLinesInnerTable++;
            	
            	double totalItem = item.getTotal();
            	Font font = CreatePDF.HELVETICA_NORMAL_ELEMENTO;
            	BaseColor color = BASE_COLOR;
            	switch (item.getMovementTypeId()) {
				case 1:
					color = LIGHT_RED;
					break;
				case 4:
					color = BaseColor.LIGHT_GRAY;
					font = CreatePDF.HELVETICA_BOLD_ELEMENTO;
					break;
				case 8:
					color = BaseColor.CYAN;
					break;
				case 9:
					color = BaseColor.YELLOW;
					break;
				case 10:
					color = BaseColor.LIGHT_GRAY;
					font = CreatePDF.HELVETICA_BOLD_ELEMENTO;
					break;
				case 11:
					color = BaseColor.WHITE;
					break;
				case 12:
					color = BaseColor.GREEN;
					font = CreatePDF.HELVETICA_BOLD_ELEMENTO;
					break;
				case 13:
					color = BaseColor.YELLOW;
					font = CreatePDF.HELVETICA_BOLD_ELEMENTO;
					break;
				default:
					break;
				}

            	CreatePDF.addCell(tableInner, item.getMovementDescription(), Element.ALIGN_LEFT, 1, font, 1, 1, 1, ((countLinesInnerTable == countItems) ? 1 : 0));            	            	
    			CreatePDF.addCellBackgroundColor(tableInner, StringUtils.formatDecimalValue(totalItem), Element.ALIGN_RIGHT, 1, font, 0, 1, 1, ((countLinesInnerTable == countItems) ? 1 : 0), color);
        	}
        	
        	final PdfPCell cellTable = new PdfPCell(tableInner);
        	cellTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        	cellTable.setColspan(1);
        	cellTable.setBorderWidthLeft(0);
        	cellTable.setBorderWidthRight(0);
        	cellTable.setBorderWidthTop(0);
        	cellTable.setBorderWidthBottom(0);
    		table.addCell(cellTable);
    		
    		if(isAcumulated) {
    			
    			for(int i = (countInnerTable + 1); i <= table.getNumberOfColumns(); i++) {
    				final PdfPCell cellSpace = new PdfPCell(new Phrase(" ", CreatePDF.HELVETICA_BOLD_ELEMENTO));
        			cellSpace.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cellSpace.setColspan(1);
        			cellSpace.setBorderWidthLeft(0);
        			cellSpace.setBorderWidthRight(0);
        			cellSpace.setBorderWidthTop(0);
        			cellSpace.setBorderWidthBottom(0);
            		table.addCell(cellSpace); 
    			}
    			table.setComplete(true);
    		}
    		
    		countInnerTable++;
    		
    		if(countInnerTable % 3 == 0) {
    			countInnerTable = 0;
    			
    			countLines++;
    			
    			if(countLines == 1) {
        			final PdfPCell cellSpace = new PdfPCell(new Phrase(" ", CreatePDF.HELVETICA_BOLD_ELEMENTO));
        			cellSpace.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cellSpace.setColspan(5);
        			cellSpace.setBorderWidthLeft(0);
        			cellSpace.setBorderWidthRight(0);
        			cellSpace.setBorderWidthTop(0);
        			cellSpace.setBorderWidthBottom(0);
            		table.addCell(cellSpace); 
    			} else {
    				countLines = 0;
    				document.add(table);
    				document.newPage();
    			            		
    				table = CreatePDF.createTable(widths.length, widths); 
    			}
    		}
        }
        
        document.add(table);
        document.close();        
        return baos.toByteArray();
	}
}