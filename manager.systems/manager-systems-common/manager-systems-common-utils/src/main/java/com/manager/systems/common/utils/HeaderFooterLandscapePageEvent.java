package com.manager.systems.common.utils;

import java.util.Calendar;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterLandscapePageEvent extends PdfPageEventHelper 
{
	private PdfPTable tableHeader;
	 
	public HeaderFooterLandscapePageEvent(final PdfPTable tableHeader)
	{
		this.tableHeader = tableHeader;
	}
	
    private PdfTemplate t;
    private Image total;

    @Override
	public void onOpenDocument(final PdfWriter writer, final Document document) 
    {
        this.t = writer.getDirectContent().createTemplate(30, 16);
        try {
            this.total = Image.getInstance(this.t);
            this.total.setRole(PdfName.ARTIFACT);
        } 
        catch (final DocumentException de) 
        {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(final PdfWriter writer, final Document document) 
    {
        try
		{
			addHeader(writer);
			addFooter(writer);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
    }

    private void addHeader(final PdfWriter writer) throws Exception
    {
        try {
            // write content
        	PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            tableHeader.writeSelectedRows(0, -1, 80, 590, canvas);
            canvas.endMarkedContentSequence();
            
        	///this.tableHeader.writeSelectedRows(0, -1, 34, 820, writer.getDirectContent());
        } 
        catch(final Exception ex) 
        {
            throw ex;
        }         
    }

    private void addFooter(final PdfWriter writer)
    {
        final PdfPTable footer = new PdfPTable(3);
        try 
        {
            // set defaults
            footer.setWidths(new int[]{22, 3, 2});
            footer.setTotalWidth(800);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(30);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            footer.addCell(new Phrase("\u00A9 Manager Systems - " + StringUtils.formatDate(Calendar.getInstance().getTime(), "dd/MM/yyyy HH:mm:ss"), new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase(String.format("Pagina %d de", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(this.total);
            totalPageCount.setBorder(Rectangle.TOP);
            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 20, 20, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
	public void onCloseDocument(final PdfWriter writer, final Document document) 
    {
        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(this.t, Element.ALIGN_RIGHT, new Phrase(String.valueOf(writer.getPageNumber()-1), new Font(Font.FontFamily.HELVETICA, 8)), totalWidth, 6, 0);
    }
}
