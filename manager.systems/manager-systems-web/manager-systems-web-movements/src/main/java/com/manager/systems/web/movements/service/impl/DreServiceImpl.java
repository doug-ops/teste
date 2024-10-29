/**
 * Date create 09/04/2020.
 */
package com.manager.systems.web.movements.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterLandscapePageEvent;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.movements.dao.DreDao;
import com.manager.systems.web.movements.dao.impl.DreDaoImpl;
import com.manager.systems.web.movements.dto.dre.DreReportDTO;
import com.manager.systems.web.movements.dto.dre.DreReportFilterDTO;
import com.manager.systems.web.movements.dto.dre.DreReportGroupTypeDTO;
import com.manager.systems.web.movements.service.DreService;

public class DreServiceImpl implements DreService
{
	private DreDao dreDao;
	
	public DreServiceImpl(final Connection connection) 
	{
		super();
		this.dreDao = new DreDaoImpl(connection);
	}

	@Override
	public DreReportDTO getAllDre(final DreReportFilterDTO dre) throws Exception
	{
 		return this.dreDao.getAllDre(dre);
	}
	
	@Override
	public byte[] generatePDFDre(final DreReportDTO dre) throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		final float[] widths = new float[] { 15, 15, 15 };
		final PdfPTable tableHeader = CreatePDF.createTable(3, widths);
		tableHeader.setTotalWidth(680);
		tableHeader.setLockedWidth(true);

		/** Title */
		CreatePDF.addCell(tableHeader,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_DRE_PDF
						+ com.manager.systems.common.utils.ConstantDataManager.SPACE,
				Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);

		final Date dateTime = Calendar.getInstance().getTime();

		CreatePDF.addCell(tableHeader, StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_DD_MM_YYYY)
				+com.manager.systems.common.utils.ConstantDataManager.SPACE
				+ StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_HH_MM_SS),
		Element.ALIGN_CENTER, 3,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
		
		CreatePDF.addCell(tableHeader, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER,
				3, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0, 0, 0);


				final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();

		final PdfPTable table = CreatePDF.createTable(3, widths);
		/** Launch title */
		CreatePDF.addCellBackgroundColor(table, null,Element.ALIGN_LEFT, 3, 
				null, 0f, 0f, 0f, 1f, null);
		
		for (final Map.Entry<Integer, DreReportGroupTypeDTO> entryDRE : dre.getMapGroupType().entrySet()) {
			final DreReportGroupTypeDTO valueDre = entryDRE.getValue();
			
						
			CreatePDF.addCellBackgroundColor(table, valueDre.getDescriptionGroupType(),Element.ALIGN_LEFT, 1, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
			
			CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(valueDre.getTotal()), Element.ALIGN_RIGHT, 1, 
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

			CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(valueDre.getPercentSales()), Element.ALIGN_RIGHT, 1,
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);
		}
		
		document.add(table);
		document.close();
		return baos.toByteArray();
	}
}