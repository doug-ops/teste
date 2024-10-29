package com.manager.systems.common.service.impl.movement.product;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.manager.systems.common.dao.impl.movement.product.MovementProductDaoImpl;
import com.manager.systems.common.dao.movement.product.MovementProductDao;
import com.manager.systems.common.dto.CompanyLastMovementExecutedDTO;
import com.manager.systems.common.dto.movement.company.MovementCompany;
import com.manager.systems.common.dto.movement.company.PreviewMovementCompanyDTO;
import com.manager.systems.common.dto.movement.company.ReportMovementCompany;
import com.manager.systems.common.dto.movement.company.ReportMovementCompanyFinancialTransfer;
import com.manager.systems.common.dto.movement.company.ReportMovementCompanyGroup;
import com.manager.systems.common.dto.movement.company.ReportMovementCompanyProduct;
import com.manager.systems.common.dto.movement.product.MovementProductDTO;
import com.manager.systems.common.service.movement.product.MovementProductService;
import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.CreatePDF;
import com.manager.systems.common.utils.HeaderFooterPortablePageEvent;
import com.manager.systems.common.utils.StringUtils;

public class MovementProductServiceImpl implements MovementProductService {
	private MovementProductDao movementProductDao;

	public MovementProductServiceImpl(final Connection connection) {
		super();
		this.movementProductDao = new MovementProductDaoImpl(connection);
	}

	@Override
	public void get(MovementProductDTO movementProduct) throws Exception {
		this.movementProductDao.get(movementProduct);
	}

	@Override
	public boolean save(final MovementProductDTO movementProduct) throws Exception {
		return this.movementProductDao.save(movementProduct);
	}

	@Override
	public void getMovementsCompany(final MovementCompany movementCompany) throws Exception {
		this.movementProductDao.getMovementsCompany(movementCompany);
	}

	@Override
	public void previewProcessMovement(final PreviewMovementCompanyDTO movementCompany) throws Exception {
		this.movementProductDao.previewProcessMovement(movementCompany);
	}

	@Override
	public long processMovement(final PreviewMovementCompanyDTO movementCompany) throws Exception {
		return this.movementProductDao.processMovement(movementCompany);
	}

	@Override
	public PreviewMovementCompanyDTO reportCompanyMovement(final long documentParentId, final long companyId)
			throws Exception {
		return this.movementProductDao.reportCompanyMovement(documentParentId, companyId);
	}

	@Override
	public byte[] generatePDFMovementCompany(final PreviewMovementCompanyDTO preview) throws Exception {
		final Document document = CreatePDF.createPDFDocument(PageSize.A4, 5, 5, 30, 5);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PdfWriter writer = PdfWriter.getInstance(document, baos);

		final float[] widths = new float[] { 10, 36, 12, 12, 12, 12, 12, 12, 12 };
		final PdfPTable tableHeader = CreatePDF.createTable(9, widths);
		tableHeader.setTotalWidth(600);
		tableHeader.setLockedWidth(true);

		/** Title */
		CreatePDF.addCell(tableHeader,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_TITLE_REPORT_PDF
						+ com.manager.systems.common.utils.ConstantDataManager.SPACE + preview.getCompnayId()
						+ com.manager.systems.common.utils.ConstantDataManager.SPACE + preview.getCompanyDescription(),
				Element.ALIGN_CENTER, 5, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 1);

		//final Date dateTime = Calendar.getInstance().getTime();
		
		CreatePDF.addCell(tableHeader,  StringUtils.formatDate(preview.getMovementDate(),StringUtils.DATE_PATTERN_DD_MM_YYYY),
			Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
	
		CreatePDF.addCell(tableHeader, StringUtils.formatDate(preview.getMovementDate(), StringUtils.DATE_PATTERN_HH_MM_SS),
			Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		
		CreatePDF.addCell(tableHeader, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER,
				2, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0, 0, 0, 0);

		CreatePDF.addCell(tableHeader, com.manager.systems.common.utils.ConstantDataManager.SPACE, Element.ALIGN_CENTER,
				5, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		CreatePDF.addCell(tableHeader,
				StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_DOCUMENT),
				Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);

		CreatePDF.addCell(tableHeader, String.valueOf(preview.getDocumentParentId()), Element.ALIGN_CENTER, 1,
				CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);
		
		CreatePDF.addCell(tableHeader, preview.getUserName()+ConstantDataManager.PARENTESES_LEFT+String.valueOf(preview.getUserChange())+ConstantDataManager.PARENTESES_RIGHT,				
				Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0, 0, 0, 0);		

		final HeaderFooterPortablePageEvent event = new HeaderFooterPortablePageEvent(tableHeader);
		writer.setPageEvent(event);

		document.open();

		final PdfPTable table = CreatePDF.createTable(9, widths);

		final ReportMovementCompany movementCompany = preview.getMovementCompany();
		for (final ReportMovementCompanyGroup group : movementCompany.getMovementsGroupsList()) {

			if(group.getProductsMovements()!=null && group.getProductsMovements().size()>0) {
				/** Product title */
				CreatePDF.addCellBackgroundColor(table,
						com.manager.systems.common.utils.ConstantDataManager.PRODUCT_CAPITALIZE, Element.ALIGN_CENTER, 9,
						CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0, 0, 0, 1, BaseColor.LIGHT_GRAY);

				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_ID),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 0);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_TYPE),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 0);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_INITIALS),
						Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_FINALS),
						Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_PERIOD),
						Element.ALIGN_CENTER, 2, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_LEFT_OVER),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);

				CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);
				CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_INPUT),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_OUTPUT),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_INPUT),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_OUTPUT),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_INPUT),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);
				CreatePDF.addCell(table,
						StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_OUTPUT),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 0, 0, 1);

				CreatePDF.addCell(table, StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.SPACE),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1, 1, 0, 1);

				/** Product item */
				for (final ReportMovementCompanyProduct product : group.getProductsMovements()) {
					CreatePDF.addCell(table, String.valueOf(product.getProductId()), Element.ALIGN_CENTER, 1,
							CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, product.getProductDescription(), Element.ALIGN_LEFT, 1,
							CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, String.valueOf(product.getCreditInInitial()),
							Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table,
							String.valueOf(product.getCreditOutInitial()+product.getCreditClockInitial()),
							Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);
					
					CreatePDF.addCell(table,
							String.valueOf(product.getCreditInFinal()),
							Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table,
							String.valueOf(product.getCreditOutFinal()+product.getCreditClockFinal()),
							Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, StringUtils.formatDecimalValue(product.getTotalCreditIn()),
							Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, StringUtils.formatDecimalValue(product.getTotalCreditOut()+product.getTotalCreditClock()),
							Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 1f);

					CreatePDF.addCell(table, StringUtils.formatDecimalValue(product.getBalance()), Element.ALIGN_RIGHT, 1,
							CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
				}

				/** Product total */
				CreatePDF.addCellBackgroundColor(table,
						com.manager.systems.common.utils.ConstantDataManager.LABEL_BALANCE_INPUT_OUTPUT, Element.ALIGN_LEFT,
						6, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

				// CreatePDF.addCell(table, " ", Element.ALIGN_RIGHT, 4,
				// CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 0f, 0f, 0f);

				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(group.getTotalCreditIn()),
						Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(group.getTotalCreditOut()),
						Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);
				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(group.getBalanceProductMovement()),
						Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);				
			}

			/** Launch item */
			int count = 0;
			for (final ReportMovementCompanyFinancialTransfer transf : group.getTransferMovements()) {

				if(count == 0) {
					/** Launch title */
					CreatePDF.addCell(table, " ", Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 0f, 0f, 0f);

					CreatePDF.addCellBackgroundColor(table,
							com.manager.systems.common.utils.ConstantDataManager.LABEL_LAUNCH_CAPITALIZE, Element.ALIGN_CENTER,
							9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 1f, BaseColor.LIGHT_GRAY);

					CreatePDF.addCell(table,
							StringUtils.capitalize(
									com.manager.systems.common.utils.ConstantDataManager.LABEL_DATE_LAUNCH_CAPITALIZE),
							Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 1f, 1f, 0f, 1f);
					CreatePDF.addCell(table,
							StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_DESCRIPTION),
							Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0f, 1f, 0f, 1f);
					CreatePDF.addCell(table,
							StringUtils.capitalize(com.manager.systems.common.utils.ConstantDataManager.LABEL_VALUE),
							Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_BOLD_ELEMENTO, 0f, 1f, 0f, 1f);
				}
				CreatePDF.addCell(table, transf.getTransferDate(),
						Element.ALIGN_CENTER, 1, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 1f, 1f, 0f, 1f);
				CreatePDF.addCell(table, transf.getTransferDescription(), Element.ALIGN_LEFT, 7,
						CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
				CreatePDF.addCell(table, StringUtils.formatDecimalValue(transf.getTransferValue()), Element.ALIGN_RIGHT,
						1, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 1f, 0f, 1f);
				
				count++;
			}

			/** Launch total */
			if(count > 0) {
				CreatePDF.addCell(table, " ", Element.ALIGN_LEFT, 7, CreatePDF.HELVETICA_NORMAL_ELEMENTO_SIZE_7, 0f, 0f, 0f, 0f);

				CreatePDF.addCellBackgroundColor(table, com.manager.systems.common.utils.ConstantDataManager.LABEL_DEBTED,
						Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);

				CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(group.getBalanceTransferMovement()),
						Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 1f, 0f, 1f, BaseColor.LIGHT_GRAY);

				CreatePDF.addCell(table, " ", Element.ALIGN_RIGHT, 9, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 0f, 0f, 0f, 0f);				
			}
		}

		/** Final Balance */
		CreatePDF.addCellBackgroundColor(table,
				(preview.getCompnayId() + com.manager.systems.common.utils.ConstantDataManager.SPACE
						+ com.manager.systems.common.utils.ConstantDataManager.TRACO
						+ com.manager.systems.common.utils.ConstantDataManager.SPACE + preview.getCompanyDescription()),
				Element.ALIGN_CENTER, 7, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 0f, 1f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table,
				com.manager.systems.common.utils.ConstantDataManager.LABEL_BILLING, Element.ALIGN_RIGHT, 1,
				CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 0f, 1f, 1f, BaseColor.LIGHT_GRAY);
		CreatePDF.addCellBackgroundColor(table, StringUtils.formatDecimalValue(preview.getBalanceMovement()),
				Element.ALIGN_RIGHT, 1, CreatePDF.HELVETICA_BOLD_TITLE_SIZE_8, 1f, 1f, 1f, 1f, BaseColor.LIGHT_GRAY);

		document.add(table);
		document.close();
		return baos.toByteArray();
	}
	
	@Override
	public boolean saveSynchronize(final MovementProductDTO movementProduct) throws Exception {
		return this.movementProductDao.saveSynchronize(movementProduct);
	}
	
	@Override
	public List<Long> hasNoMovementProduct(final long companyId, final long user) throws Exception {
		return this.movementProductDao.hasNoMovementProduct(companyId, user);
	}

	@Override
	public CompanyLastMovementExecutedDTO companyLastMovementExecuted(final long companyId, final long user) throws Exception {
		return this.movementProductDao.companyLastMovementExecuted(companyId, user);
	}
}