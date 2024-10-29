/**
 * Date create 09/04/2020.
 */
package com.manager.systems.web.movements.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyItemDTO;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterLandscapePageEvent;
import com.manager.systems.common.utils.HeaderFooterPortablePageEvent;
import com.manager.systems.common.utils.StringUtils;
import com.manager.systems.web.movements.dao.DocumentMovementDao;
import com.manager.systems.web.movements.dao.impl.DocumentMovementDaoImpl;
import com.manager.systems.web.movements.dto.DocumentMovementDTO;
import com.manager.systems.web.movements.dto.DocumentMovementGroup;
import com.manager.systems.web.movements.dto.DocumentMovementGroupFilter;
import com.manager.systems.web.movements.dto.DocumentMovementItemDTO;
import com.manager.systems.web.movements.dto.MovementDocumentCompanyDTO;
import com.manager.systems.web.movements.dto.MovementDocumentCompanyItemDTO;
import com.manager.systems.web.movements.dto.MovementWekendDTO;
import com.manager.systems.web.movements.dto.MovementWekendItemDTO;
import com.manager.systems.web.movements.service.DocumentMovementService;

public class DocumentMovementServiceImpl implements DocumentMovementService {
	private DocumentMovementDao documentMovementDao;

	public DocumentMovementServiceImpl(final Connection connection) {
		super();
		this.documentMovementDao = new DocumentMovementDaoImpl(connection);
	}

	@Override
	public void getAllDocumentMovement(final DocumentMovementDTO documentMovement) throws Exception {
		this.documentMovementDao.getAllDocumentMovement(documentMovement);
	}

	@Override
	public void getDocumentMovement(final PreviewMovementCompanyDTO previewMovementCompany) throws Exception {
		this.documentMovementDao.getDocumentMovement(previewMovementCompany);
	}

	@Override
	public byte[] genetarePdfReportFromPreview(final PreviewMovementCompanyDTO preview) throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		final PdfPTable tableHeader = new PdfPTable(1);
		tableHeader.setTotalWidth(680);
		tableHeader.setLockedWidth(true);

		final StringBuilder pdfTitle = new StringBuilder();
		pdfTitle.append(ConstantDataManager.LABEL_REPORT);
		pdfTitle.append(ConstantDataManager.SPACE);
		pdfTitle.append(ConstantDataManager.LABEL_FROM);
		pdfTitle.append(ConstantDataManager.SPACE);
		pdfTitle.append(ConstantDataManager.LABEL_LAUNCH);
		pdfTitle.append(ConstantDataManager.SPACE);
		pdfTitle.append(ConstantDataManager.LABEL_DATE);
		pdfTitle.append(ConstantDataManager.DOIS_PONTOS);
		pdfTitle.append(ConstantDataManager.SPACE);
		pdfTitle.append(
				StringUtils.formatDate(Calendar.getInstance().getTime(), StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
		pdfTitle.append(ConstantDataManager.SPACE);
		pdfTitle.append(ConstantDataManager.TRACO);
		pdfTitle.append(ConstantDataManager.SPACE);
		pdfTitle.append(preview.getCompanyDescription());

		PdfPCell cell = new PdfPCell(new Phrase(pdfTitle.toString(), CreatePDF.HELVETICA_BOLD_TITLE));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(1);
		cell.setBorderWidth(0);
		tableHeader.addCell(cell);

		final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();
		PdfPTable table = null;

		final float[] widths = new float[] { 15, 15, 15, 15, 15, 15, 15, 15, 15 };
		table = CreatePDF.createTable(9, widths);

		CreatePDF.addCellBackgroundColor(table,
				ConstantDataManager.LABEL_CREDIT + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT
						+ ConstantDataManager.LABEL_INPUT_MOVEMENT + ConstantDataManager.PARENTESES_RIGHT,
				Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1, BaseColor.LIGHT_GRAY);
		CreatePDF.addCell(table,
				org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_INITIAL_DATE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_FINAL_DATE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_DOCUMENT),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table,
				org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_TYPE_OF_WATCH),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_MACHINE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_TYPE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table,
				org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_INITIAL_ENTRY),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_FINAL_ENTRY),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_VALUE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);

		for (final Map.Entry<String, List<PreviewMovementCompanyItemDTO>> entryProductsIn : preview
				.getMovementProductsIn().entrySet()) {
			for (final PreviewMovementCompanyItemDTO item : entryProductsIn.getValue()) {
				CreatePDF.addCell(table, item.getInitialDate(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);
				CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, item.getTypeWatch(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getProductId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, item.getProductDescription(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getInitialEntry()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getFinalEntry()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT,
						1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
			}
			CreatePDF.addCell(table,
					ConstantDataManager.LABEL_TOTAL + ConstantDataManager.SPACE + ConstantDataManager.LABEL_CREDIT
							+ ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT
							+ ConstantDataManager.LABEL_INPUT_MOVEMENT + ConstantDataManager.PARENTESES_RIGHT,
					Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 0, 1);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(preview.getTotalMovementInput()),
					Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 0, 1);
		}
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_NORMAL_ELEMENTO,
				0, 0, 0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_NORMAL_ELEMENTO,
				0, 0, 0, 0);

		CreatePDF.addCellBackgroundColor(table,
				ConstantDataManager.LABEL_DEBIT + ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT
						+ ConstantDataManager.LABEL_OUTPUT_MOVEMENT + ConstantDataManager.PARENTESES_RIGHT,
				Element.ALIGN_LEFT, 9, CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1, BaseColor.LIGHT_GRAY);
		CreatePDF.addCell(table,
				org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_INITIAL_DATE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_FINAL_DATE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_DOCUMENT),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table,
				org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_TYPE_OF_WATCH),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_MACHINE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_TYPE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table,
				org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_INITIAL_ENTRY),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_FINAL_ENTRY),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, org.springframework.util.StringUtils.capitalize(ConstantDataManager.LABEL_VALUE),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);

		for (final Map.Entry<String, List<PreviewMovementCompanyItemDTO>> entryProductsOut : preview
				.getMovementProductsOut().entrySet()) {
			for (final PreviewMovementCompanyItemDTO item : entryProductsOut.getValue()) {
				CreatePDF.addCell(table, item.getInitialDate(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);
				CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, item.getTypeWatch(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getProductId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, item.getProductDescription(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getInitialOutput()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getFinalOutput()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT,
						1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
			}
			CreatePDF.addCell(table,
					ConstantDataManager.LABEL_TOTAL + ConstantDataManager.SPACE + ConstantDataManager.LABEL_DEBIT
							+ ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT
							+ ConstantDataManager.LABEL_OUTPUT_MOVEMENT + ConstantDataManager.PARENTESES_RIGHT,
					Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 0, 1);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(preview.getTotalMovementOutput()),
					Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 0, 1);
		}

		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_NORMAL_ELEMENTO,
				0, 0, 0, 0);

		CreatePDF.addCell(table,
				ConstantDataManager.LABEL_TOTAL_BALANCE + ConstantDataManager.SPACE
						+ ConstantDataManager.PARENTESES_LEFT + ConstantDataManager.LABEL_INPUT_PLUS_OUTPUT_MOVEMENT
						+ ConstantDataManager.PARENTESES_RIGHT,
				Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1);
		CreatePDF.addCell(table, StringUtils.formatDecimalValue(preview.getBalanceMovementInputOutput()),
				Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 1, 1);

		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_NORMAL_ELEMENTO,
				0, 0, 0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_NORMAL_ELEMENTO,
				0, 0, 0, 0);

		CreatePDF.addCell(table, ConstantDataManager.LABEL_TRANSFERS, Element.ALIGN_LEFT, 9,
				CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1);
		CreatePDF.addCell(table, ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
		CreatePDF.addCell(table, ConstantDataManager.LABEL_DOCUMENT, Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, ConstantDataManager.LABEL_SPECIFICATION, Element.ALIGN_CENTER, 6,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		CreatePDF.addCell(table, ConstantDataManager.LABEL_VALUE, Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);

		for (final Map.Entry<String, List<PreviewMovementCompanyItemDTO>> entryTransfers : preview
				.getMovementTransfers().entrySet()) {
			for (final PreviewMovementCompanyItemDTO item : entryTransfers.getValue()) {
				CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1);
				CreatePDF.addCell(table, String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, item.getProviderId() + " - " + item.getProviderDescription(),
						Element.ALIGN_CENTER, 6, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT,
						1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
			}
			CreatePDF.addCell(table,
					ConstantDataManager.LABEL_TOTAL + ConstantDataManager.SPACE + ConstantDataManager.LABEL_DEBIT
							+ ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT
							+ ConstantDataManager.LABEL_COMISSIONS_PLUS_EXPENSES + ConstantDataManager.PARENTESES_RIGHT,
					Element.ALIGN_LEFT, 8, CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 0, 1);
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(preview.getBalanceTransfer()), Element.ALIGN_RIGHT,
					1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 0, 1);
		}
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_NORMAL_ELEMENTO,
				0, 0, 0, 0);

		CreatePDF.addCell(table, ConstantDataManager.LABEL_TOTAL_LAUNCH, Element.ALIGN_LEFT, 8,
				CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1);
		CreatePDF.addCell(table, StringUtils.formatDecimalValue(preview.getBalanceMovement()), Element.ALIGN_RIGHT, 1,
				CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 1, 1);

		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_NORMAL_ELEMENTO,
				0, 0, 0, 0);

		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.LABEL_LANCH_USER, Element.ALIGN_RIGHT, 3,
				CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1);
		CreatePDF.addCell(table, "", Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 1, 1);

		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.LABEL_CLOSE_USER, Element.ALIGN_RIGHT, 3,
				CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1);
		CreatePDF.addCell(table, "", Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 1, 1);

		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.SPACE, Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0,
				0, 0);
		CreatePDF.addCell(table, ConstantDataManager.LABEL_DATE, Element.ALIGN_RIGHT, 3, CreatePDF.HELVETICA_BOLD_TITLE,
				1, 1, 0, 1);
		CreatePDF.addCell(table, "", Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);

		document.add(table);
		document.close();
		return baos.toByteArray();

		/**
		 * final Document document =
		 * CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30); final
		 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); final PdfWriter
		 * writer = PdfWriter.getInstance(document, baos);
		 * 
		 * final PdfPTable tableHeader = new PdfPTable(1);
		 * tableHeader.setTotalWidth(680); tableHeader.setLockedWidth(true);
		 * 
		 * final StringBuilder pdfTitle = new StringBuilder();
		 * pdfTitle.append(ConstantDataManager.LABEL_REPORT);
		 * pdfTitle.append(ConstantDataManager.SPACE);
		 * pdfTitle.append(ConstantDataManager.LABEL_FROM);
		 * pdfTitle.append(ConstantDataManager.SPACE);
		 * pdfTitle.append(ConstantDataManager.LABEL_LAUNCH);
		 * pdfTitle.append(ConstantDataManager.SPACE);
		 * pdfTitle.append(ConstantDataManager.LABEL_DATE);
		 * pdfTitle.append(ConstantDataManager.DOIS_PONTOS);
		 * pdfTitle.append(ConstantDataManager.SPACE);
		 * pdfTitle.append(StringUtils.formatDate(Calendar.getInstance().getTime(),
		 * StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM_SS));
		 * 
		 * PdfPCell cell = new PdfPCell(new Phrase(pdfTitle.toString(),
		 * CreatePDF.HELVETICA_BOLD_TITLE));
		 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setColspan(1);
		 * cell.setBorderWidth(0); tableHeader.addCell(cell);
		 * 
		 * cell = new PdfPCell(new Phrase(ConstantDataManager.LABEL_COMPANY +
		 * ConstantDataManager.SPACE + preview.getCompnayId(),
		 * CreatePDF.HELVETICA_BOLD_TITLE));
		 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setColspan(1);
		 * cell.setBorderWidth(0); tableHeader.addCell(cell);
		 * 
		 * final HeaderFooterLandscapePageEvent event = new
		 * HeaderFooterLandscapePageEvent(tableHeader); writer.setPageEvent(event);
		 * 
		 * document.open(); PdfPTable table = null;
		 * 
		 * final float[] widths = new float[] {15,15,15,40,15}; table =
		 * CreatePDF.createTable(5, widths);
		 * 
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_CREDIT +
		 * ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT +
		 * ConstantDataManager.LABEL_INPUT_MOVEMENT +
		 * ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 6,
		 * CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_DOCUMENT, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_NOTE, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_VALUE, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		 * 
		 * for (final Map.Entry<String, List<PreviewMovementCompanyItemDTO>>
		 * entryProductsIn : preview.getMovementProductsIn().entrySet()) { for (final
		 * PreviewMovementCompanyItemDTO item : entryProductsIn.getValue()) {
		 * CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1); //CreatePDF.addCell(table,
		 * item.getPaymentDate(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * item.getProviderId() + " - " + item.getProviderDescription(),
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
		 * CreatePDF.addCell(table, item.getDocumentNote(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT,
		 * 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); }
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_TOTAL +
		 * ConstantDataManager.SPACE + ConstantDataManager.LABEL_CREDIT+
		 * ConstantDataManager.PARENTESES_LEFT +
		 * ConstantDataManager.LABEL_INPUT_MOVEMENT +
		 * ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 4,
		 * CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 0, 1); CreatePDF.addCell(table,
		 * StringUtils.formatDecimalValue(preview.getTotalMovementInput()),
		 * Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 0, 1); }
		 * CreatePDF.addCell(table, " ", Element.ALIGN_RIGHT, 5,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 0, 0, 0); CreatePDF.addCell(table, "
		 * ", Element.ALIGN_RIGHT, 5, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 0, 0, 0);
		 * 
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_DEBIT +
		 * ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT +
		 * ConstantDataManager.LABEL_OUTPUT_MOVEMENT +
		 * ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 6,
		 * CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1); //CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_DATE, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_DOCUMENT, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_PROVIDER, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_NOTE, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * ConstantDataManager.LABEL_VALUE, Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		 * 
		 * for (final Map.Entry<String, List<PreviewMovementCompanyItemDTO>>
		 * entryProductsOut : preview.getMovementProductsOut().entrySet()) { for (final
		 * PreviewMovementCompanyItemDTO item : entryProductsOut.getValue()) {
		 * CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1); //CreatePDF.addCell(table,
		 * item.getPaymentDate(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * item.getProviderId() + " - " + item.getProviderDescription(),
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
		 * CreatePDF.addCell(table, item.getDocumentNote(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT,
		 * 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); }
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_TOTAL +
		 * ConstantDataManager.SPACE + ConstantDataManager.LABEL_DEBIT+
		 * ConstantDataManager.PARENTESES_LEFT +
		 * ConstantDataManager.LABEL_OUTPUT_MOVEMENT +
		 * ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 4,
		 * CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 0, 1); CreatePDF.addCell(table,
		 * StringUtils.formatDecimalValue(preview.getTotalMovementOutput()),
		 * Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 0, 1); }
		 * 
		 * CreatePDF.addCell(table, " ", Element.ALIGN_RIGHT, 5,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 0, 0, 0);
		 * 
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_TOTAL_BALANCE +
		 * ConstantDataManager.SPACE + ConstantDataManager.PARENTESES_LEFT +
		 * ConstantDataManager.LABEL_INPUT_PLUS_OUTPUT_MOVEMENT +
		 * ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 4,
		 * CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1); CreatePDF.addCell(table,
		 * StringUtils.formatDecimalValue(preview.getBalanceMovementInputOutput()),
		 * Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 1, 1);
		 * 
		 * CreatePDF.addCell(table, " ", Element.ALIGN_RIGHT, 5,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 0, 0, 0); CreatePDF.addCell(table, "
		 * ", Element.ALIGN_RIGHT, 5, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 0, 0, 0);
		 * 
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_DEBIT +
		 * ConstantDataManager.SPACE + ConstantDataManager.LABEL_TRANSFERS,
		 * Element.ALIGN_LEFT, 6, CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1);
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_DATE,
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
		 * //CreatePDF.addCell(table, ConstantDataManager.LABEL_DATE,
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_DOCUMENT,
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_PROVIDER,
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_NOTE,
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_VALUE,
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		 * 
		 * for (final Map.Entry<String, List<PreviewMovementCompanyItemDTO>>
		 * entryTransfers : preview.getMovementTransfers().entrySet()) { for (final
		 * PreviewMovementCompanyItemDTO item : entryTransfers.getValue()) {
		 * CreatePDF.addCell(table, item.getPaymentDate(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1, 1, 0, 1); //CreatePDF.addCell(table,
		 * item.getPaymentDate(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * String.valueOf(item.getDocumentParentId()), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * item.getProviderId() + " - " + item.getProviderDescription(),
		 * Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1);
		 * CreatePDF.addCell(table, item.getDocumentNote(), Element.ALIGN_CENTER, 1,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); CreatePDF.addCell(table,
		 * StringUtils.formatDecimalValue(item.getDocumentValue()), Element.ALIGN_RIGHT,
		 * 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 1, 0, 1); }
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_TOTAL +
		 * ConstantDataManager.SPACE + ConstantDataManager.LABEL_DEBIT +
		 * ConstantDataManager.PARENTESES_LEFT +
		 * ConstantDataManager.LABEL_COMISSIONS_PLUS_EXPENSES +
		 * ConstantDataManager.PARENTESES_RIGHT, Element.ALIGN_LEFT, 4,
		 * CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 0, 1); CreatePDF.addCell(table,
		 * StringUtils.formatDecimalValue(preview.getBalanceTransfer()),
		 * Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 0, 1); }
		 * CreatePDF.addCell(table, " ", Element.ALIGN_RIGHT, 5,
		 * CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0, 0, 0, 0);
		 * 
		 * CreatePDF.addCell(table, ConstantDataManager.LABEL_TOTAL_LAUNCH,
		 * Element.ALIGN_LEFT, 4, CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 1, 1);
		 * CreatePDF.addCell(table,
		 * StringUtils.formatDecimalValue(preview.getBalanceMovement()),
		 * Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 0, 1, 1, 1);
		 * 
		 * document.add(table); document.close(); return baos.toByteArray();
		 */
	}

	@Override
	public void getDocumentMovementItem(final DocumentMovementItemDTO item) throws Exception {
		this.documentMovementDao.getDocumentMovementItem(item);
	}

	@Override
	public void getAllDocumentMovementReport(final DocumentMovementDTO documentMovement) throws Exception {
		this.documentMovementDao.getAllDocumentMovementReport(documentMovement);
	}

	@Override
	public byte[] generatePDFMovementDocuments(final DocumentMovementDTO documents) throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.LETTER.rotate(), 5, 5, 60, 30);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		final float[] widths = new float[] { 6, 15, 15, 15, 15, 15, 15 };
		final PdfPTable tableHeader = CreatePDF.createTable(7, widths);
		tableHeader.setTotalWidth(680);
		tableHeader.setLockedWidth(true);

		/** Title */
		CreatePDF.addCell(tableHeader,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_MOVEMENTS_PDF
						+ com.manager.systems.common.utils.ConstantDataManager.SPACE,
				Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);

		final Date dateTime = Calendar.getInstance().getTime();

		CreatePDF.addCell(tableHeader, StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_DD_MM_YYYY),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		CreatePDF.addCell(tableHeader, StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_HH_MM_SS),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		CreatePDF.addCell(tableHeader, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER,
				2, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0, 0, 0);

		CreatePDF.addCell(tableHeader, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER,
				5, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		CreatePDF.addCell(tableHeader,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_DOCUMENT),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();

		final PdfPTable table = CreatePDF.createTable(7, widths);
		/** Launch title */

		double totalCredit = 0d;
		double totalDebit = 0d;
		double totalSaldo = 0d;

		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH_CAPITALIZE, Element.ALIGN_CENTER, 7,
				CreatePDF.HELVETICA_BOLD_TITLE, 0, 0, 0, 1, BaseColor.LIGHT_GRAY);

		if (!documents.isAnalitic()) {

			/** Header */
			CreatePDF.addCell(table,
					StringUtils.capitalize(
							com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY_CODE.toUpperCase()),
					Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

			CreatePDF.addCell(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY),
					Element.ALIGN_LEFT, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

			CreatePDF.addCell(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_DOCUMENT),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

			CreatePDF.addCell(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_ISSUE_DATE),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

			CreatePDF.addCell(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPIRATION_DATE),
					Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

			CreatePDF.addCell(table,
					StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_VALUE),
					Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);
		}

		long lasDocumentParent = 0;
		for (final Map.Entry<Long, DocumentMovementItemDTO> entryDocument : documents.getItens().entrySet()) {
			final long documentParentId = entryDocument.getKey();
			final DocumentMovementItemDTO documentParent = entryDocument.getValue();

			if (documents.isAnalitic() && (lasDocumentParent == 0 || lasDocumentParent != documentParentId)) {

				/** Header */
				CreatePDF.addCell(table,
						StringUtils.capitalize(
								com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY_CODE.toUpperCase()),
						Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY),
						Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_DOCUMENT),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_ISSUE_DATE),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

				CreatePDF.addCell(table,
						StringUtils
								.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_EXPIRATION_DATE),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_VALUE),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);

				/** Header value */
				CreatePDF.addCell(table, String.valueOf(documentParent.getCompanyId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, documentParent.getCompany(), Element.ALIGN_CENTER, 2,
						CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, String.valueOf(documentParent.getDocumentParentId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, documentParent.getPaymentData(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, documentParent.getPaymentData(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, StringUtils.formatDecimalValue(documentParent.getDocumentValue()),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 0f, 1f);
			}

			if (!documents.isAnalitic()) {
				CreatePDF.addCell(table, String.valueOf(documentParent.getCompanyId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, documentParent.getCompany(), Element.ALIGN_LEFT, 2,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, String.valueOf(documentParent.getDocumentParentId()), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, documentParent.getPaymentData(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, documentParent.getPaymentData(), Element.ALIGN_CENTER, 1,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

				CreatePDF.addCell(table, StringUtils.formatDecimalValue(documentParent.getDocumentValue()),
						Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f);

				if (documentParent.getDocumentValue() < 0) {
					totalDebit += documentParent.getDocumentValue();
				} else {
					totalCredit += documentParent.getDocumentValue();
				}
				totalSaldo = totalCredit - (totalDebit * -1);
			}

			if (documents.isAnalitic()) {
				for (final DocumentMovementItemDTO item : documentParent.getItens()) {
					CreatePDF.addCell(table, String.valueOf(documentParent.getCompanyId()), Element.ALIGN_CENTER, 1,
							CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, item.getDocumentNote(), Element.ALIGN_CENTER, 2,
							CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, String.valueOf(item.getDocumentId()), Element.ALIGN_CENTER, 1,
							CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, item.getPaymentData(), Element.ALIGN_CENTER, 1,
							CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, item.getPaymentData(), Element.ALIGN_CENTER, 1,
							CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, StringUtils.formatDecimalValue(item.getDocumentValue()),
							Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 0f, 1f);
				}
			}
		}

		/** Launch total */
		CreatePDF.addCell(table, " ", Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 0f, 0f, 0f);
		/** Crédito */
		CreatePDF.addCell(table, " ", Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 0f, 0f, 0f);
		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_CREDIT_CAPITALIZE, Element.ALIGN_RIGHT, 1,
				CreatePDF.HELVETICA_BOLD_TITLE, 1f, 0f, 1f, 0f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalCredit), Element.ALIGN_RIGHT, 1,
				CreatePDF.HELVETICA_BOLD_TITLE, 1f, 1f, 1f, 0f, BaseColor.LIGHT_GRAY);

		/** Débito */
		CreatePDF.addCell(table, " ", Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 0f, 0f, 0f);
		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_DEBIT_CAPITALIZE, Element.ALIGN_RIGHT, 1,
				CreatePDF.HELVETICA_BOLD_TITLE, 1f, 0f, 1f, 0f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalDebit), Element.ALIGN_RIGHT, 1,
				CreatePDF.HELVETICA_BOLD_TITLE, 1f, 1f, 1f, 0f, BaseColor.LIGHT_GRAY);
		/** Saldo */
		CreatePDF.addCell(table, " ", Element.ALIGN_LEFT, 5, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 0f, 0f, 0f);
		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL_CAPITALIZE, Element.ALIGN_RIGHT, 1,
				CreatePDF.HELVETICA_BOLD_TITLE, 1f, 0f, 1f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(totalSaldo), Element.ALIGN_RIGHT, 7,
				CreatePDF.HELVETICA_BOLD_TITLE, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);

		document.add(table);
		document.close();
		return baos.toByteArray();
	}

	public byte[] generatePDFMovementDocumentsWeek(final MovementWekendDTO documents) throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.A4.rotate(), 5, 5, 60, 30);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		final int columnSize = documents.getMapDate().size() + 2;

		final float[] widths = new float[columnSize];
		for (int i = 0; i < widths.length; i++) {
			if (i == 0) {
				widths[i] = 12;
			} else {
				widths[i] = 7;
			}
		}

		final PdfPTable tableHeader = CreatePDF.createTable(columnSize, widths);
		tableHeader.setTotalWidth(680);
		tableHeader.setLockedWidth(true);

		/** Title */
		CreatePDF.addCell(tableHeader,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_MOVEMENTS_PDF
						+ com.manager.systems.common.utils.ConstantDataManager.SPACE,
				Element.ALIGN_CENTER, 4, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);

		final Date dateTime = Calendar.getInstance().getTime();

		CreatePDF.addCell(tableHeader,
				StringUtils.formatDate(dateTime,
						StringUtils.DATE_PATTERN_DD_MM_YYYY + com.manager.systems.common.utils.ConstantDataManager.SPACE
								+ com.manager.systems.common.utils.ConstantDataManager.SPACE
								+ StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_HH_MM_SS)),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		CreatePDF.addCell(tableHeader, StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_HH_MM_SS),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		final HeaderFooterLandscapePageEvent event = new HeaderFooterLandscapePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();

		final PdfPTable table = CreatePDF.createTable(columnSize, widths);
		/** Launch title */

		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH_CAPITALIZE, Element.ALIGN_CENTER,
				columnSize, CreatePDF.HELVETICA_BOLD_TITLE, 0, 0, 0, 1, BaseColor.LIGHT_GRAY);

		/** Header */
		CreatePDF.addCell(table,
				StringUtils
						.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY.toUpperCase()),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);

		final Map<Integer, Double> totalWeekMap = new TreeMap<Integer, Double>();
		for (final Map.Entry<Integer, MovementWekendItemDTO> entryWeek : documents.getMapDate().entrySet()) {
			CreatePDF.addCell(table, entryWeek.getValue().getWeekDescription(), Element.ALIGN_CENTER, 1,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);
		}

		CreatePDF.addCell(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL.toUpperCase()),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);

		double totalGeralCompany = 0d;
		for (final Map.Entry<String, Map<Integer, MovementWekendItemDTO>> entryDocument : documents.getOrdernadCompany()
				.entrySet()) {
			int count = 0;
			double totalCompany = 0d;
			for (final Map.Entry<Integer, MovementWekendItemDTO> entryWeek : entryDocument.getValue().entrySet()) {
				if (count == 0) {
					CreatePDF.addCell(table, entryWeek.getValue().getCompanyDescription(), Element.ALIGN_LEFT, 1,
							CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f);
				}

				CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryWeek.getValue().getDocumentValue()),
						Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 1f, 0f, 1f);

				totalCompany += entryWeek.getValue().getDocumentValue().doubleValue();

				Double totalWeek = totalWeekMap.get(entryWeek.getValue().getWeek());
				if (totalWeek == null) {
					totalWeek = 0d;
				}
				totalWeek += entryWeek.getValue().getDocumentValue();
				totalWeekMap.put(entryWeek.getValue().getWeek(), totalWeek);
				count++;
			}
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(totalCompany), Element.ALIGN_RIGHT, 1,
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 1f, 0f, 1f);
			totalGeralCompany += totalCompany;
		}

		int count = 0;
		for (final Map.Entry<Integer, Double> entryWeek : totalWeekMap.entrySet()) {

			if (count == 0) {
				CreatePDF.addCellBackgroundColor(table,
						com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL, Element.ALIGN_LEFT, 1,
						CreatePDF.HELVETICA_BOLD_TITLE, 1, 1, 0, 1, BaseColor.LIGHT_GRAY);
			}
			CreatePDF.addCell(table, StringUtils.formatDecimalValue(entryWeek.getValue()), Element.ALIGN_RIGHT, 1,
					CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);

			count++;
		}
		CreatePDF.addCell(table, StringUtils.formatDecimalValue(totalGeralCompany), Element.ALIGN_RIGHT, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 1, 0, 1);

		document.add(table);
		document.close();
		return baos.toByteArray();
	}

	public byte[] generatePDFMovementDocumentsGroupCompany(final MovementDocumentCompanyDTO documents)
			throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.A4, 10, 10, 40, 40);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		final float[] widths = new float[] { 1, 12, 2 };
		final PdfPTable tableHeader = new PdfPTable(1);
		tableHeader.setTotalWidth(680);
		tableHeader.setLockedWidth(true);

		final Date dateTime = Calendar.getInstance().getTime();
		final Date dateFrom = StringUtils.convertStringDateToDate(documents.getDataFrom(),
				StringUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		final Date dateTo = StringUtils.convertStringDateToDate(documents.getDataTo(),
				StringUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);

		/** Title */
		CreatePDF.addCell(tableHeader, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER,
				3, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
		final StringBuilder pdfTitle = new StringBuilder();

		pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_MOVEMENTS_PDF);
		pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.TRACO);
		pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE);
		pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
		pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfTitle.append(StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_DD_MM_YYYY));
		pdfTitle.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfTitle.append(StringUtils.formatDate(dateTime, StringUtils.DATE_PATTERN_HH_MM_SS));

		PdfPCell cell = new PdfPCell(new Phrase(pdfTitle.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(1);
		cell.setBorderWidth(0);
		tableHeader.addCell(cell);

		final StringBuilder pdfSubtitle2 = new StringBuilder();

		pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfSubtitle2.append(
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_DATA_PESQUISA));
		pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.DOIS_PONTOS);
		pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfSubtitle2.append(StringUtils.formatDate(dateFrom, StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM));
		pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfSubtitle2.append(StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_ATE));
		pdfSubtitle2.append(com.manager.systems.common.utils.ConstantDataManager.SPACE);
		pdfSubtitle2.append(StringUtils.formatDate(dateTo, StringUtils.DATE_PATTERN_DD_MM_YYYY_HH_MM));

		cell = new PdfPCell(new Phrase(pdfSubtitle2.toString(), CreatePDF.HELVETICA_BOLD_ELEMENTO));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(1);
		cell.setBorderWidth(0);
		tableHeader.addCell(cell);

		CreatePDF.addCell(tableHeader, " ", Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 0f, 0f, 0f);
		CreatePDF.addCell(tableHeader, " ", Element.ALIGN_LEFT, 3, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 0f, 0f, 0f);

		final HeaderFooterPortablePageEvent event = new HeaderFooterPortablePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();

		final PdfPTable table = CreatePDF.createTable(3, widths);
		/** Launch title */

		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH_CAPITALIZE, Element.ALIGN_CENTER, 3,
				CreatePDF.HELVETICA_BOLD_TITLE, 0, 0, 0, 1, BaseColor.LIGHT_GRAY);

		/** Header */
		CreatePDF.addCell(table,
				StringUtils.capitalize(
						com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY_CODE.toUpperCase()),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

		CreatePDF.addCell(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_COMPANY),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

		CreatePDF.addCell(table,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);

		for (final MovementDocumentCompanyItemDTO company : documents.getOrdernadItens()) {
//				for (final Map.Entry<Long, MovementDocumentCompanyItemDTO> entryGroupComapny : documents.getItens().entrySet()) {	
//				final MovementDocumentCompanyItemDTO company = entryGroupComapny.getValue();												
			CreatePDF.addCell(table, String.valueOf(company.getCompanyId()), Element.ALIGN_CENTER, 1,
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

			CreatePDF.addCell(table, company.getCompanyDescription(), Element.ALIGN_LEFT, 1,
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 0f, 0f, 1f);

			CreatePDF.addCell(table, StringUtils.formatDecimalValue(company.getTotal()), Element.ALIGN_RIGHT, 1,
					CreatePDF.HELVETICA_NORMAL_ELEMENTO, 1f, 1f, 0f, 1f);

		}
		/** Launch total */
		CreatePDF.addCell(table, " ", Element.ALIGN_LEFT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO, 0f, 0f, 0f, 0f);
		CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_TOTAL,
				Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(documents.getTotal()),
				Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);

		document.add(table);
		document.close();
		return baos.toByteArray();
	}

	@Override
	public DocumentMovementGroup getDocumentMovementGroup(final DocumentMovementGroupFilter filter) throws Exception {
		return this.documentMovementDao.getDocumentMovementGroup(filter);
	}
}