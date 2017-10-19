package com.yzb.lee.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import com.itextpdf.tool.xml.pipeline.html.NoImageProviderException;
import com.yzb.lee.generator.HtmlGenerator;
import com.yzb.lee.service.PdfService;

/**
 * Servlet implementation class PdfDownloadServlet
 */
public class PdfFontServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PdfFontServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding(CHARSET_NAME);
		OutputStream out = response.getOutputStream();
		response.setContentType("application/pdf");
		response.addHeader(
				"Content-Disposition",
				"attachment; filename="
						+ String.format("%s.pdf",
								Long.toString(System.currentTimeMillis())));
		PdfService ps = new PdfService();
		Map<String, Object> content = ps.getContent();
		try {
			String templateHtml = HtmlGenerator.generate("fileTemplate.html",
					content);
			generatePdf(templateHtml, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	private void generatePdf(String htmlStr, OutputStream out)
			throws IOException, DocumentException {
		//final ServletContext servletContext = getServletContext();

		Document document = new Document(PageSize.A4, 30, 30, 30, 30);
		document.setMargins(30, 30, 30, 30);
		PdfWriter writer = PdfWriter.getInstance(document, out);
		document.open();

		// html内容解析
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(
				new CssAppliersImpl(new XMLWorkerFontProvider() {
					@Override
					public Font getFont(String fontname, String encoding,
							float size, final int style) {
						Font font = null;
						if (fontname == null) {
							//字体  
				            String fontCn = getChineseFont();  
				            BaseFont bf;
							try {
								//注意这里有一个,1 
								bf = BaseFont.createFont(fontCn+",1", 
								         BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
								font = new Font(bf, size, style);
							} catch (DocumentException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}  
				            
						}
						return font;
					}
				})) {
			@Override
			public HtmlPipelineContext clone()
					throws CloneNotSupportedException {
				HtmlPipelineContext context = super.clone();
				try {
					ImageProvider imageProvider = this.getImageProvider();
					context.setImageProvider(imageProvider);
				} catch (NoImageProviderException e) {
				}
				return context;
			}
		};

		// 图片解析
		htmlContext.setImageProvider(new AbstractImageProvider() {

			// String rootPath = servletContext.getRealPath("/");

			@Override
			public String getImageRootPath() {
				return "";
			}

			@Override
			public Image retrieve(String src) {
				if (StringUtils.isEmpty(src)) {
					return null;
				}
				try {
					// String imageFilePath = new File(rootPath, src).toURI().toString();
					Image image = Image.getInstance(src);
					image.setAbsolutePosition(400, 400);
					if (image != null) {
						store(src, image);
						return image;
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return super.retrieve(src);
			}
		});
		htmlContext.setAcceptUnknown(true).autoBookmark(true)
				.setTagFactory(Tags.getHtmlTagProcessorFactory());

		// css解析
		CSSResolver cssResolver = XMLWorkerHelper.getInstance()
				.getDefaultCssResolver(true);
		cssResolver.setFileRetrieve(new FileRetrieve() {
			@Override
			public void processFromStream(InputStream in,
					ReadingProcessor processor) throws IOException {
				try (InputStreamReader reader = new InputStreamReader(in,
						CHARSET_NAME)) {
					int i = -1;
					while (-1 != (i = reader.read())) {
						processor.process(i);
					}
				} catch (Throwable e) {
				}
			}

			// 解析href
			@Override
			public void processFromHref(String href, ReadingProcessor processor)
					throws IOException {
				// InputStream is = servletContext.getResourceAsStream(href);
				URL url = new URL(href);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				InputStream is = conn.getInputStream();

				try (InputStreamReader reader = new InputStreamReader(is,
						CHARSET_NAME)) {
					int i = -1;
					while (-1 != (i = reader.read())) {
						processor.process(i);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext,
				new PdfWriterPipeline(document, writer));
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
				htmlPipeline);
		XMLWorker worker = null;
		worker = new XMLWorker(pipeline, true);
		XMLParser parser = new XMLParser(true, worker,
				Charset.forName(CHARSET_NAME));
		try (InputStream inputStream = new ByteArrayInputStream(
				htmlStr.getBytes())) {
			parser.parse(inputStream, Charset.forName(CHARSET_NAME));
		}
		document.close();
	}
	
	/** 
     * 获取中文字体位置 
     * @return 
     */  
    private String getChineseFont() {  
  
		String chineseFont = null;
		chineseFont = this.getClass().getResource("/").getPath() + "font/SIMSUN.TTC";
		if(!new File(chineseFont).exists()){  
            throw new RuntimeException("字体文件不存在,影响导出pdf中文显示！"+chineseFont);  
        }   
        
        return chineseFont;  
    } 
}
