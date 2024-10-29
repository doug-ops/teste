/*
 * Date create 28/06/2023.
 */
package com.manager.systems.web.financial.cash.statement.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.financial.cash.statement.dao.CashStatementDao;
import com.manager.systems.web.financial.cash.statement.dao.impl.CashStatementDaoImpl;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportDTO;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenReportFilterDTO;
import com.manager.systems.web.financial.cash.statement.dto.CashStatemenResumeItemDTO;
import com.manager.systems.web.financial.cash.statement.dto.CashStatementResumeDTO;
import com.manager.systems.web.financial.cash.statement.service.CashStatementService;

public class CashStatementServiceImpl implements CashStatementService {

	private CashStatementDao cashStatementDao;
	
	public CashStatementServiceImpl(final Connection connection) {
		super();
		this.cashStatementDao = new CashStatementDaoImpl(connection);
	}
	
	@Override
	public CashStatemenReportDTO getCashStatement(final CashStatemenReportFilterDTO filter) throws Exception {
		return this.cashStatementDao.getCashStatement(filter);
	}
	
	@Override
	public byte[] processPdfReport(final CashStatemenReportDTO report) throws Exception {
		
		final Workbook workbook = new XSSFWorkbook();
		
		final Sheet sheet = workbook.createSheet("JAN_2024");
		
		final Row header = sheet.createRow(0);
		
		final XSSFFont fontHeader = ((XSSFWorkbook) workbook).createFont();
		fontHeader.setFontName("Arial");
		fontHeader.setFontHeightInPoints((short) 8);
		fontHeader.setBold(true);
		
		final XSSFFont fontNormal = ((XSSFWorkbook) workbook).createFont();
		fontNormal.setFontName("Arial");
		fontNormal.setFontHeightInPoints((short) 8);
		
		final XSSFFont fontDespesaLoja = ((XSSFWorkbook) workbook).createFont();
		fontDespesaLoja.setFontName("Arial");
		fontDespesaLoja.setFontHeightInPoints((short) 8);
		fontDespesaLoja.setColor(IndexedColors.DARK_RED.getIndex());
		
		final CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFont(fontHeader);
		
		final CellStyle creditCellStyle = workbook.createCellStyle();
		creditCellStyle.setAlignment(HorizontalAlignment.CENTER);
		creditCellStyle.setWrapText(true);
		creditCellStyle.setFont(fontNormal);
		
		final CellStyle normalCellStyle = workbook.createCellStyle();
		normalCellStyle.setAlignment(HorizontalAlignment.LEFT);
		normalCellStyle.setWrapText(true);
		normalCellStyle.setFont(fontNormal);
		
		final CellStyle duplicataCellStyle = workbook.createCellStyle();
		duplicataCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		duplicataCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		duplicataCellStyle.setAlignment(HorizontalAlignment.RIGHT);
		duplicataCellStyle.setWrapText(true);
		duplicataCellStyle.setFont(fontNormal);
		
		final CellStyle fiadoCellStyle = workbook.createCellStyle();
		fiadoCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		fiadoCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		fiadoCellStyle.setAlignment(HorizontalAlignment.RIGHT);
		fiadoCellStyle.setWrapText(true);
		fiadoCellStyle.setFont(fontNormal);
		
		final CellStyle despesaLojaCellStyle = workbook.createCellStyle();
		despesaLojaCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		despesaLojaCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		despesaLojaCellStyle.setAlignment(HorizontalAlignment.RIGHT);
		despesaLojaCellStyle.setWrapText(true);
		despesaLojaCellStyle.setFont(fontDespesaLoja);
		
		final CellStyle totalStyle = workbook.createCellStyle();
		totalStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		totalStyle.setAlignment(HorizontalAlignment.LEFT);
		totalStyle.setFont(fontHeader);
		
		final CellStyle totalValueStyle = workbook.createCellStyle();
		totalValueStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		totalValueStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		totalValueStyle.setAlignment(HorizontalAlignment.RIGHT);
		totalValueStyle.setFont(fontHeader);

		final Row creditRow = sheet.createRow(1);
		
		int countCell = 0;
		for (final Map.Entry<Integer, CashStatementResumeDTO> entryWeek : report.getResumeMap().entrySet()) {
						
			final Cell headerCell = header.createCell(countCell);
			headerCell.setCellValue(entryWeek.getValue().getWeekTitleLabel());
			headerCell.setCellStyle(headerStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, countCell, (countCell + 1)));
					
			final Cell cellCredit = creditRow.createCell(countCell);
			cellCredit.setCellValue("Créditos");
			cellCredit.setCellStyle(creditCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, countCell, (countCell + 1)));
			
			for (final Map.Entry<Integer, CashStatemenResumeItemDTO> entryWeekItem : entryWeek.getValue().getResumeMap().entrySet()) {
				
				if(entryWeekItem.getValue().getMovementType() == 1) {
					
					final Row fiadosRow = sheet.createRow(2);
					
					final Cell cellDescription = fiadosRow.createCell(countCell);
					cellDescription.setCellValue(entryWeekItem.getValue().getDescription());
					cellDescription.setCellStyle(normalCellStyle);				
					
					final Cell cellValue = fiadosRow.createCell((countCell + 1));
					cellValue.setCellValue(StringUtils.formatDecimalValue(entryWeekItem.getValue().getValue()));
					cellValue.setCellStyle(fiadoCellStyle);			
					
				} else if(entryWeekItem.getValue().getMovementType() == 2) {
					
					final Row fiadosRow = sheet.createRow(3);
					
					final Cell cellDescription = fiadosRow.createCell(countCell);
					cellDescription.setCellValue(entryWeekItem.getValue().getDescription());
					cellDescription.setCellStyle(normalCellStyle);
					
					final Cell cellValue = fiadosRow.createCell((countCell + 1));
					cellValue.setCellValue(StringUtils.formatDecimalValue(entryWeekItem.getValue().getValue()));
					cellValue.setCellStyle(duplicataCellStyle);					
				} else if(entryWeekItem.getValue().getMovementType() == 3) {
					
					final Row despesasLojaRow = sheet.createRow(4);
					
					final Cell cellDescription = despesasLojaRow.createCell(countCell);
					cellDescription.setCellValue(entryWeekItem.getValue().getDescription());
					cellDescription.setCellStyle(normalCellStyle);
					
					final Cell cellValue = despesasLojaRow.createCell((countCell + 1));
					cellValue.setCellValue(StringUtils.formatDecimalValue(entryWeekItem.getValue().getValue()));
					cellValue.setCellStyle(despesaLojaCellStyle);					
				} 
				
				final Row totalCreditRow = sheet.createRow(5);
				
				final Cell cellDescription = totalCreditRow.createCell(countCell);
				cellDescription.setCellValue("Total de Créditos");
				cellDescription.setCellStyle(totalStyle);
				
				final Cell cellValue = totalCreditRow.createCell((countCell + 1));
				cellValue.setCellValue(StringUtils.formatDecimalValue(entryWeek.getValue().getTotalCredit()));
				cellValue.setCellStyle(totalValueStyle);		
			}
			
			countCell = countCell + 3;
		}
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		workbook.write(baos);
		workbook.close();
		
		return baos.toByteArray();
	}
}