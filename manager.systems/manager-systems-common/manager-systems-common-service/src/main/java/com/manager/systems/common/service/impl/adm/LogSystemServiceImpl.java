/**Create 01/08/2022*/
package com.manager.systems.common.service.impl.adm;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.dao.adm.LogSystemDao;
import com.manager.systems.common.dao.impl.adm.LogSystemDaoImpl;
import com.manager.systems.common.dto.adm.ChangeObjectDTO;
import com.manager.systems.common.dto.adm.ChangeObjectItemDTO;
import com.manager.systems.common.dto.adm.LogSystemDTO;
import com.manager.systems.common.dto.adm.ReportLogSystemDTO;
import com.manager.systems.common.service.adm.LogSystemService;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterLandscapePageEvent;
import com.manager.systems.common.utils.StringUtils;

public class LogSystemServiceImpl implements LogSystemService {
	private LogSystemDao logSystemDao;

	public LogSystemServiceImpl(final Connection connection) {
		super();
		this.logSystemDao = new LogSystemDaoImpl(connection);
	}
	
	@Override
	public List<LogSystemDTO> get(final ReportLogSystemDTO reportLog) throws Exception {
		return this.logSystemDao.get(reportLog);
	}
	
	@Override
	public List<ChangeObjectDTO> getAll(final ReportLogSystemDTO reportLog) throws Exception {
		return this.logSystemDao.getAll(reportLog);
	}
	
	@Override
	public byte[] generatePDFLogSystem(final List<ChangeObjectDTO> chargeObject, boolean analitic) throws Exception {
		


		final Document document = CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		float[] widths = new float[] { 5, 10, 10, 30 };
		PdfPTable tableHeader = CreatePDF.createTable(4, widths);
		tableHeader.setTotalWidth(680);
		tableHeader.setLockedWidth(true);

		/** Title */

		CreatePDF.addCell(tableHeader,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_LOG_SYSTEM_PDF,
				Element.ALIGN_CENTER, 3, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);

		final Date dateTime = Calendar.getInstance().getTime();

		CreatePDF.addCell(tableHeader, StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_DD_MM_YYYY)
				+com.manager.systems.common.utils.ConstantDataManager.SPACE
				+ StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_HH_MM_SS),
		Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
		
		CreatePDF.addCell(tableHeader, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER,
				4, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0, 0, 0);


				final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();

		PdfPTable table = CreatePDF.createTable(4, widths);
		/** Launch title */
		if(!analitic) {
			
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_USER.toUpperCase()),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
	
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
	
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_WINDOW_ALTER),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
	
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_COLUMN_ALTER),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1, BaseColor.LIGHT_GRAY);
			
		}
		if(analitic) {
			 widths = new float[] { 10, 10 };
			tableHeader = CreatePDF.createTable(2, widths);
			 table = CreatePDF.createTable(2, widths);

			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_OLD.toUpperCase()),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 1, 1, BaseColor.LIGHT_GRAY);
	
			CreatePDF.addCellBackgroundColor(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_NEW),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 1, 1, BaseColor.LIGHT_GRAY);
		}
		
		if(!analitic) {
			for (final ChangeObjectDTO report : chargeObject) {
				
				CreatePDF.addCell(table, report.getChangeUserName()
						+ com.manager.systems.common.utils.ConstantDataManager.PARENTESES_LEFT
						+ report.getChangeUser()
						+ com.manager.systems.common.utils.ConstantDataManager.PARENTESES_RIGHT,Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
				
				CreatePDF.addCell(table, report.getChangeDate(),Element.ALIGN_CENTER, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
				
				CreatePDF.addCell(table, report.getChangeObjectName(),Element.ALIGN_LEFT, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
				
				CreatePDF.addCell(table, report.getChangeFields(),Element.ALIGN_LEFT, 1, 
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f);
			}
		}
		if(analitic) {
			for (final ChangeObjectDTO report : chargeObject) {
				for (final Map.Entry<String , ChangeObjectItemDTO> entry : report.getFildes().entrySet()) {
					final String keyName = entry.getKey();
					final String valueOld = entry.getValue().getValueOld(); 
					final String valueNew = entry.getValue().getValueNew();
					final boolean isChange = entry.getValue().isChange();
					
					if(isChange) {
						CreatePDF.addCell(table, keyName
								+ com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
								+ com.manager.systems.common.utils.ConstantDataManager.SPACE
								+ valueOld,Element.ALIGN_CENTER, 1, 
								CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);
						
						CreatePDF.addCell(table, keyName
								+ com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS
								+ com.manager.systems.common.utils.ConstantDataManager.SPACE
								+ valueNew,Element.ALIGN_CENTER, 1, 
								CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f);
					}
					
				}
			}
		}
		
		document.add(table);
		document.close();
		return baos.toByteArray();
	}
}