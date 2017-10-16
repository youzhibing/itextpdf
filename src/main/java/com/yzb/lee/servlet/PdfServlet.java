package com.yzb.lee.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.yzb.lee.service.PdfService;

/**
 * Servlet implementation class PdfServlet
 */
public class PdfServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CHARSET_NAME = "UTF-8";
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PdfServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(CHARSET_NAME);
		PrintWriter writer = response.getWriter();
		response.setContentType("application/pdf");
        response.addHeader(
                "Content-Disposition",
                "attachment; filename="
                        + String.format("%s.pdf",
                        Long.toString(System.currentTimeMillis())));
        XMLWorker worker = null;
		
		Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        document.setMargins(30, 30, 30, 30);
        try {
			PdfWriter pw = PdfWriter.getInstance(document, response.getOutputStream());
			PdfWriter.getInstance(document, new FileOutputStream(getClass().getResource("/").getFile().toString() + "/template.pdf"));
			document.open();
		
			
			document.close();
			
			PdfService ps = new PdfService();
			ps.getContent();
			writer.flush();
			writer.close();
        } catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	
}
