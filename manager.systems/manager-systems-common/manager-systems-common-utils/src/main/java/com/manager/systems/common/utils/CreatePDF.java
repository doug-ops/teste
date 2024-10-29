package com.manager.systems.common.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public final class CreatePDF 
{
	public static final Font TIME_ROMAN = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	public static final Font TIME_ROMAN_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	public static final Font HELVETICA_BOLD_TITLE = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);   
	public static final Font HELVETICA_BOLD_TITLE_SIZE_8 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD); 
	public static final Font HELVETICA_BOLD_ELEMENTO = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	public static final Font HELVETICA_NORMAL_ELEMENTO = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
	public static final Font HELVETICA_NORMAL_ELEMENTO_SIZE_7 = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
	public static final Font HELVETICA_BOLD_TITLE_SIZE_7 = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD); 
	public static final Font HELVETICA_BOLD_TITLE_SIZE_6 = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD); 
	
	public static final BaseColor MOR_LIGHT_GRAY = new BaseColor(248, 246, 246);
	
	
	public CreatePDF()
	{
		super();
	}
	
	public static Document createPDFDocument(final Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom) 
	{
		return new Document(pageSize, marginLeft, marginRight, marginTop, marginBottom);
	}
	
	public static void addTitlePage(final Document document, final String title, final int alignment, final float fontSize, final int fontStyle) throws DocumentException 
	{
		HELVETICA_BOLD_TITLE.setSize(fontSize);
		HELVETICA_BOLD_TITLE.setStyle(fontStyle);
		final Paragraph preface = new Paragraph();
		preface.add(new Paragraph(title, HELVETICA_BOLD_TITLE));
		preface.setAlignment(alignment);
		document.add(preface);
	}
	
	public static void addSubTitlePage(final Document document, final String title, final int alignment, final float fontSize, final int fontStyle) throws DocumentException 
	{
		HELVETICA_BOLD_TITLE.setSize(fontSize);
		HELVETICA_BOLD_TITLE.setStyle(fontStyle);
		final Paragraph preface = new Paragraph();
		preface.add(new Paragraph(title, HELVETICA_BOLD_TITLE));
		preface.setAlignment(alignment);
		document.add(preface);
	}
	
	public static void creteEmptyLine(final Paragraph paragraph, final int number) throws Exception
	{
		for (int i = 0; i < number; i++) 
		{
			paragraph.add(new Paragraph(" "));
		}
	}
	
	public static void creteEmptyLineParagrafo(final Document document, final int number) throws Exception
	{
		final Paragraph paragraph = new Paragraph();
		for (int i = 0; i < number; i++) 
		{
			paragraph.add(new Paragraph(" "));
		}
		document.add(paragraph);
	}
	
	public static PdfPTable createTable(int numeroColunas, final float[] widths) throws Exception 
	{
		final PdfPTable table = new PdfPTable(numeroColunas);
		table.setWidths(widths);
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setHeaderRows(0);
		return table;
	}
	
	public static PdfPTable createTable(int numeroColunas) throws Exception 
	{
		final PdfPTable table = new PdfPTable(numeroColunas);
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setHeaderRows(0);
		return table;
	}
	
	public static void addCell(final PdfPTable table, final String texto, final int alignment, final int colspan, final Font font, final float borderLeft, final float borderRight, final float borderTop, final float borderBottom)
	{
		final PdfPCell cell = new PdfPCell(new Phrase(texto, font));
		cell.setHorizontalAlignment(alignment);
		cell.setColspan(colspan);
		cell.setBorderWidthLeft(borderLeft);
		cell.setBorderWidthRight(borderRight);
		cell.setBorderWidthTop(borderTop);
		cell.setBorderWidthBottom(borderBottom);
		table.addCell(cell);
	}
	
	public static void addCellRowSpan(final PdfPTable table, final String texto, final int alignment, final int colspan, final Font font, final float borderLeft, final float borderRight, final float borderTop, final float borderBottom, final int rowspan)
	{
		final PdfPCell cell = new PdfPCell(new Phrase(texto, font));
		cell.setHorizontalAlignment(alignment);
		cell.setColspan(colspan);
		cell.setBorderWidthLeft(borderLeft);
		cell.setBorderWidthRight(borderRight);
		cell.setBorderWidthTop(borderTop);
		cell.setBorderWidthBottom(borderBottom);
		cell.setRowspan(rowspan);
		table.addCell(cell);
	}
	
	public static void addCellBackgroundColor(final PdfPTable table, final String texto, final int alignment, final int colspan, final Font font, final float borderLeft, final float borderRight, final float borderTop, final float borderBottom, final BaseColor backgroundColor)
	{
		final PdfPCell cell = new PdfPCell(new Phrase(texto, font));
		cell.setHorizontalAlignment(alignment);
		cell.setColspan(colspan);
		cell.setBorderWidthLeft(borderLeft);
		cell.setBorderWidthRight(borderRight);
		cell.setBorderWidthTop(borderTop);
		cell.setBorderWidthBottom(borderBottom);
		cell.setBackgroundColor(backgroundColor);
		table.addCell(cell);
	}
	
	public static void addCellHeight(final PdfPTable table, final String texto, final float height, final int alignment, final int colspan, final Font font, final float borderLeft, final float borderRight, final float borderTop, final float borderBottom)
	{
		final PdfPCell cell = new PdfPCell(new Phrase(texto, font));
		cell.setHorizontalAlignment(alignment);
		cell.setColspan(colspan);
		cell.setBorderWidthLeft(borderLeft);
		cell.setBorderWidthRight(borderRight);
		cell.setBorderWidthTop(borderTop);
		cell.setBorderWidthBottom(borderBottom);
		cell.setFixedHeight(height);
		table.addCell(cell);
	}	
	
	public static void addNestdTableCell(final PdfPTable table, final PdfPTable nestedTable,  final int colspan, final float borderLeft, final float borderRight, final float borderTop, final float borderBottom)
	{
		final PdfPCell cell = new PdfPCell(nestedTable);
        cell.setPadding(0);
        cell.setColspan(colspan);
		cell.setBorderWidthLeft(borderLeft);
		cell.setBorderWidthRight(borderRight);
		cell.setBorderWidthTop(borderTop);
		cell.setBorderWidthBottom(borderBottom);
        table.addCell(cell);
	}	
}
